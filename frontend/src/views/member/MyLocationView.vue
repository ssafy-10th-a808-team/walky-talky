<template>
    <div>
        <h1>내 위치 확인하는 페이지</h1>
        <div id="map" style="width:500px;height:400px;"></div>
    </div>
</template>


<script setup>
    import { ref, onMounted } from 'vue'
    const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
    let map = null // map is not defined Reference Error 방지
    let lat = 0
    let lon = 0
    onMounted(() => {
        if (window.kakao && window.kakao.maps) {
            initMap();
            searchDetailAddrFromCoords(lat, lon, addrCallback)
        } else {
            
            const script = document.createElement('script');
            // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
            /* global kakao */
            script.onload = () => kakao.maps.load(initMap);
            script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`;
            //autoload=false를 통해 로딩이 끝나는 시점에 콜백을 통해 객체에 접근 
            document.head.appendChild(script);
            
        }

        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function(position){
                lat = position.coords.latitude // 위도
                lon = position.coords.longitude // 경도
                // geolocation 가능한 경우 내 위치
                // 크롬 브라우저는 https 환경에서만 geolocation이 지원된다고 하네요 local도 되긴 했음
                
            })
        } else {
            lat = 37.5014
            lon = 127.0395 
            // geolocation 불가능하면 위치를 멀티캠퍼스로
        }
 
    });


    const initMap = () => {
        const marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat, lon)
        })
        const container = document.getElementById('map')
        const options = {
            center: new kakao.maps.LatLng(lat, lon),
            level: 5,
        }
        map = new kakao.maps.Map(container, options)
        marker.setMap(map)

    }

  
    const searchDetailAddrFromCoords = (lat, lon, callback) => {
        // 좌표로 법정동 상세 주소 정보를 요청합니다
        const geocoder = kakao.maps.services.Geocoder()
        geocoder.coord2Address(lat, lon, callback);
    }

    const addrCallback = (result, status) => {
        // 법정동 상세 주소를 가져올 때 콜백 함수를 선언한 것입니다
        if (status === kakao.services.Status.OK) {
            console.log(result)
        } else {
            console.error()
        }
    }
    



</script>

<style scoped>
    #map {
    width: 400px;
    height: 400px;
    }
</style>