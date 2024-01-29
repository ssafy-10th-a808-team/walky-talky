<template>
    <div>
        <h1>내 위치 확인하는 페이지</h1>
        <div id="map" style="width:500px;height:400px;"></div>
    </div>
    <div>
        님 현재 위치가 {{ address_name }} 맞나요?
    </div>
    <div>
        지역 코드 : {{ address_code }}
    </div>
</template>


<script setup>
    import { ref, onMounted } from 'vue'
    const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
    let map = null // map is not defined Reference Error 방지
    let lat = 0
    let lon = 0
    const address_name = ref("")
    const address_code = ref("")
    onMounted(() => {
        if (window.kakao && window.kakao.maps) {
            initMap();
            // searchDetailAddrFromCoords(lat, lon, addrCallback)
        } else {
            
            const script = document.createElement('script');
            // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
            /* global kakao */
            script.onload = () => {
                console.log('카카오맵 api script loaded')
                kakao.maps.load(initMap);
            }
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
                console.log("내 좌표를 가져왔습니다")
                
            })
        } else {
            lat = 37.5014
            lon = 127.0395 
            // geolocation 불가능하면 위치를 멀티캠퍼스로
            console.log("멀티캠퍼스 좌표를 가져왔습니다")
        }
 
    });


    const initMap = () => {
        console.log("initMap 적용")

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

        const geocoder = new kakao.maps.services.Geocoder()
        geocoder.coord2RegionCode(lon, lat, addrCallback)
    }
 


    const addrCallback = (result, status) => {
        // 법정동 상세 주소를 가져올 때 콜백 함수를 선언한 것입니다
        if (status === kakao.maps.services.Status.OK) {
            console.log('주소 가져왔습니다')
            console.log(result[0])
            if (result[0].region_type === 'B'){
                // 법정동 코드일 경우에만 저장하기, 수정가능성 높음
                address_name.value = result[0].address_name
                address_code.value = result[0].code
            }
        } else {
            console.error("Failed to get address info")
            console.log(kakao.maps.services.Status)
            console.log(result)
        }
    }

    



</script>

<style scoped>
    #map {
    width: 400px;
    height: 400px;
    }
</style>