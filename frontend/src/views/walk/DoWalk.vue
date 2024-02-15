<template>
  <div>
    <WalkHeaderNav />
    <h1>산책하기</h1>
    <div class="map_wrap" style="position: relative">
      <div id="map" style="width: 100%; height: 350px">
        <!-- 산책하기 실행버튼 -->
        <!-- <button
          id="my_location_button"
          class="btn btn-primary"
          style="position: absolute; z-index: 2; top: 80%; left: 44%"
        >
          산책하기
        </button> -->

        <!-- 산책하기 버튼을 눌렀을 때 스탑워치 실행되게 하기 -->
        <StopWatch style="position: absolute; z-index: 2; top: 70%; left: 40%" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
let lat = 0
let lon = 0
const address = ref('')

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap()
    // searchDetailAddrFromCoords(lat, lon, addrCallback)
  } else {
    const script = document.createElement('script')
    // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
    /* global kakao */
    script.onload = () => {
      // console.log('카카오맵 api script loaded')
      kakao.maps.load(initMap)
    }
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
    //autoload=false를 통해 로딩이 끝나는 시점에 콜백을 통해 객체에 접근
    document.head.appendChild(script)
  }

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(function (position) {
      lat = position.coords.latitude // 위도
      lon = position.coords.longitude // 경도
      // geolocation 가능한 경우 내 위치
      // 크롬 브라우저는 https 환경에서만 geolocation이 지원된다고 하네요 local도 되긴 했음

      //   lat = 37.2522
      //   lon = 128.9267
      // console.log('내 좌표를 가져왔습니다')
    })
  } else {
    lat = 37.5014
    lon = 127.0395
    // geolocation 불가능하면 위치를 멀티캠퍼스로
    // console.log('멀티캠퍼스 좌표를 가져왔습니다')
  }
})

const initMap = () => {
  console.log('initMap 적용')

  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(lat, lon)
  })
  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(lat, lon),
    level: 5
  }
  map = new kakao.maps.Map(container, options)

  marker.setMap(map)

  const geocoder = new kakao.maps.services.Geocoder()
  geocoder.coord2Address(lon, lat, addrCallback)

  // 오버레이 (말풍선)
  // const content = `
  //   <div class="label">
  //     <span class="left"></span>
  //     <span class="center">카카오!</span>
  //     <span class="right"></span>
  //   </div>
  // `

  // const position = new kakao.maps.LatLng(lat, lon)
  // const customOverlay = new kakao.maps.CustomOverlay({
  //   position: position,
  //   content: content
  // })

  // console.log(content)

  // customOverlay.setMap(map)
}
const addrCallback = (result, status) => {
  // 법정동 상세 주소를 가져올 때 콜백 함수를 선언한 것입니다
  if (status === kakao.maps.services.Status.OK) {
    console.log('주소 가져왔습니다')
    console.log(result[0].address.address_name)
    address.value = result[0].address.address_name
  } else {
    console.error('Failed to get address info')
    console.log(kakao.maps.services.Status)
    console.log(result)
  }
}
</script>

<style scoped>
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
