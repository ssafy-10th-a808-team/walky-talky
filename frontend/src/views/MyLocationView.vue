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
    const lat = ref(0)
    const lon = ref(0)
    onMounted(() => {
        if (window.kakao && window.kakao.maps) {
            initMap();
 
        } else {
            const script = document.createElement('script');
            // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
            /* global kakao */
            script.onload = () => kakao.maps.load(initMap);
            script.src = `//dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=${API_KEY}&libraries=services`;
            //autoload=false를 통해 로딩이 끝나는 시점에 콜백을 통해 객체에 접근 
            document.head.appendChild(script);
        }
        if (navigator.geolocation) {
            navigator.geolocation.getCurrentPosition(function( position ){
                lat.value = position.coords.latitude // 위도
                lon.value = position.coords.longitude // 경도
                // geolocation 가능한 경우 내 위치
                // 크롬 브라우저는 https 환경에서만 geolocation이 지원된다고 하네요 local도 되긴 했음
                
            })
        } else {
            lat.value = 37.5014
            lon.value = 127.0395 
            // geolocation 불가능하면 위치를 멀티캠퍼스로
        }


    });


    const initMap = () => {
        const marker = new kakao.maps.Marker({
            position: new kakao.maps.LatLng(lat.value, lon.value)
        })
        const container = document.getElementById('map')
        const options = {
            center: new kakao.maps.LatLng(lat.value, lon.value),
            level: 5,
        }
        map = new kakao.maps.Map(container, options)
        marker.setMap(map)

    }

    // const geocoder =new kakao.maps.services.Geocoder()
    
    // function searchDetailAddrFromCoords(coords, callback) {
    //     // 좌표로 법정동 상세 주소 정보를 요청합니다
    //     geocoder.coord2Address(coords.getLng(), coords.getLat(), callback);
    // }
    // searchDetailAddrFromCoords(map.getCenter(), function(result, status) {
    //     if (status === kakao.services.Status.OK) {
    //         const detailAddr = result[0]
    //         console.log(detailAddr)
    //     }
    // })

</script>

<style scoped>
    #map {
    width: 400px;
    height: 400px;
    }
</style>