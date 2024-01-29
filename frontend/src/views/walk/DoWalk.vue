<template>
  <div>
    <WalkHeaderNav />
    <h1>산책하기</h1>
    <div class="map_wrap" style="position: relative">
      <div id="map" style="width: 500px; height: 400px"></div>
      <button
        id="my_location_button"
        class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded"
        onclick="getCurrentPosBtn()"
        style="position: absolute; z-index: 2"
      >
        내 위치 찾기
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
// let map = null // map is not defined Reference Error 방지
// let lat = 0
// let lon = 0
// const address = ref('')
// onMounted(() => {
//   if (window.kakao && window.kakao.maps) {
//     initMap()
//     // searchDetailAddrFromCoords(lat, lon, addrCallback)
//   } else {
//     const script = document.createElement('script')
//     // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
//     /* global kakao */
//     script.onload = () => {
//       console.log('카카오맵 api script loaded')
//       kakao.maps.load(initMap)
//     }
//     script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
//     //autoload=false를 통해 로딩이 끝나는 시점에 콜백을 통해 객체에 접근
//     document.head.appendChild(script)
//   }

//   if (navigator.geolocation) {
//     navigator.geolocation.getCurrentPosition(function (position) {
//       lat = position.coords.latitude // 위도
//       lon = position.coords.longitude // 경도
//       // geolocation 가능한 경우 내 위치
//       // 크롬 브라우저는 https 환경에서만 geolocation이 지원된다고 하네요 local도 되긴 했음

//       // lat = 37.2522
//       // lon = 128.9267
//       console.log('내 좌표를 가져왔습니다')
//     })
//   } else {
//     lat = 37.5014
//     lon = 127.0395
//     // geolocation 불가능하면 위치를 멀티캠퍼스로
//     console.log('멀티캠퍼스 좌표를 가져왔습니다')
//   }
// })

// const initMap = () => {
//   console.log('initMap 적용')

//   const marker = new kakao.maps.Marker({
//     position: new kakao.maps.LatLng(lat, lon)
//   })
//   const container = document.getElementById('map')
//   const options = {
//     center: new kakao.maps.LatLng(lat, lon),
//     level: 5
//   }
//   map = new kakao.maps.Map(container, options)

//   marker.setMap(map)

//   const geocoder = new kakao.maps.services.Geocoder()
//   geocoder.coord2Address(lon, lat, addrCallback)
// }

// const searchDetailAddrFromCoords = (lat, lon, callback) => {
//     console.log("좌표 가져오는 코드")
//         this.geocoder.coord2Address(lat, lon, callback);

//     // 좌표로 법정동 상세 주소 정보를 요청합니다
// }

// const addrCallback = (result, status) => {
//   // 법정동 상세 주소를 가져올 때 콜백 함수를 선언한 것입니다
//   if (status === kakao.maps.services.Status.OK) {
//     console.log('주소 가져왔습니다')
//     console.log(result[0].address.address_name)
//     address.value = result[0].address.address_name
//   } else {
//     console.error('Failed to get address info')
//     console.log(kakao.maps.services.Status)
//     console.log(result)
//   }
// }

// 지도 위의 로드뷰 버튼을 눌렀을 때 호출되는 함수입니다
// function setRoadviewRoad() {
//     var control = document.getElementById('roadviewControl');

//     // 버튼이 눌린 상태가 아니면
//     if (control.className.indexOf('active') === -1) {
//         control.className = 'active';

//         // 로드뷰 도로 오버레이가 보이게 합니다
//         toggleOverlay(true);
//     } else {
//         control.className = '';

//         // 로드뷰 도로 오버레이를 제거합니다
//         toggleOverlay(false);
//     }
// }

var mapContainer = document.getElementById('map'), // 지도를 표시할 div
  mapOption = {
    center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
  }

var map = new kakao.maps.Map(mapContainer, mapOption) // 지도를 생성합니다

// 커스텀 오버레이에 표시할 내용입니다
// HTML 문자열 또는 Dom Element 입니다
var content =
  '<div class ="label"><span class="left"></span><span class="center">카카오!</span><span class="right"></span></div>'

// 커스텀 오버레이가 표시될 위치입니다
var position = new kakao.maps.LatLng(lat, lon)

// 커스텀 오버레이를 생성합니다
var customOverlay = new kakao.maps.CustomOverlay({
  position: position,
  content: content
})

// 커스텀 오버레이를 지도에 표시합니다
customOverlay.setMap(map)
</script>

<style scoped>
#map {
  width: 400px;
  height: 400px;
}
.map_wrap {
  position: relative;
  overflow: hidden;
  width: 100%;
  height: 350px;
}
.label {
  margin-bottom: 96px;
}
.label * {
  display: inline-block;
  vertical-align: top;
}
.label .left {
  background: url('https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_l.png')
    no-repeat;
  display: inline-block;
  height: 24px;
  overflow: hidden;
  vertical-align: top;
  width: 7px;
}
.label .center {
  background: url(https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_bg.png)
    repeat-x;
  display: inline-block;
  height: 24px;
  font-size: 12px;
  line-height: 24px;
}
.label .right {
  background: url('https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_r.png') -1px
    0 no-repeat;
  display: inline-block;
  height: 24px;
  overflow: hidden;
  width: 6px;
}
</style>
