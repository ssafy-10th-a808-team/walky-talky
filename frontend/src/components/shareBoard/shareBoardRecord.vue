<template>
  <div class="map-container">
    <div id="map" style="width: 100%; height: 400px; justify-content: center" />
    <div class="record-container">
      <div>
        <p>소요 시간</p>
        <p>{{ duration }}</p>
      </div>
      <div>
        <p>총 거리</p>
        <p>{{ distance }} km</p>
      </div>
    </div>
  </div>
</template>

<script setup>
const { distance, duration, points, address } = defineProps([
  'distance',
  'duration',
  'points',
  'address'
])
import { onMounted } from 'vue'

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap()
  } else {
    const script = document.createElement('script')
    // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
    /* global kakao */
    script.onload = () => {
      kakao.maps.load(initMap)
    }
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
    //autoload=false를 통해 로딩이 끝나는 시점에 콜백을 통해 객체에 접근
    document.head.appendChild(script)
  }
})

const initMap = () => {
  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(points[0].latitude, points[0].longitude),
    level: 6
  }
  map = new kakao.maps.Map(container, options)

  // 경로 폴리라인
  var polyline = new kakao.maps.Polyline({
    map: map,
    path: points.map((point) => new kakao.maps.LatLng(point.latitude, point.longitude)),
    strokeWeight: 5,
    strokeColor: '#FF0000'
  })
  polyline.setMap(map)
}
</script>

<style>
.map-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.record-container {
  display: flex;
  justify-content: space-around;
  width: 100%;
}
</style>
