<template>
  <div class="map-container">
    <div
      :id="'map-' + uniqueId + seq"
      class="map"
      style="width: 400px; height: 400px; justify-content: center"
    />
    <div class="record-container">
      <div v-if="title != undefined">
        <p>산책 제목</p>
        <p>{{ title }}</p>
      </div>
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
const { distance, duration, points, title, seq } = defineProps([
  'distance',
  'duration',
  'points',
  'title',
  'seq'
])
import { onMounted, ref } from 'vue'

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
const uniqueId = ref(Date.now()) // 각 컴포넌트에 고유한 ID를 부여하기 위한 ref

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    initMap()
  } else {
    const script = document.createElement('script')
    script.onload = () => {
      kakao.maps.load(initMap)
    }
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
    document.head.appendChild(script)
  }
})

const initMap = () => {
  const container = document.getElementById(`map-${uniqueId.value}` + seq)
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
