<template>
  <div class="map-container">
    <div
      :id="'map-' + uniqueId + seq"
      class="map"
      :style="{ width: containerWidth, height: containerHeight, justifyContent: 'center' }"
      @click="changeMovable"
    />
    <div class="record-container">
      <div v-if="title != undefined" class="text-center">
        <p>산책 제목</p>
        <p>{{ title }}</p>
      </div>
      <div class="text-center">
        <p>소요 시간</p>
        <p>{{ convertTime(parseInt(duration)) }}</p>
      </div>
      <div class="text-center">
        <p>총 거리</p>
        <p>{{ distance }} km</p>
      </div>
    </div>
  </div>
</template>

<script setup>
const { distance, duration, points, title, seq, movable } = defineProps([
  'distance',
  'duration',
  'points',
  'title',
  'seq',
  'movable'
])
import { onMounted, ref } from 'vue'

function convertTime(seconds) {
  if (typeof seconds !== 'number' || seconds < 0) {
    return 'Invalid input'
  }

  const minutes = Math.floor(seconds / 60)
  const remainingSeconds = seconds % 60

  const minutesString = minutes > 0 ? `${minutes}분` : ''
  const secondsString = remainingSeconds > 0 ? `${remainingSeconds}초` : ''

  return `${minutesString} ${secondsString}`.trim() || '0초'
}

const changeMovable = () => {
  if (movable === false) {
    movable = true
  }
}

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
const uniqueId = ref(Date.now()) // 각 컴포넌트에 고유한 ID를 부여하기 위한 ref

const containerWidth = ref('95%')
const containerHeight = ref('300px')

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

  if (container.offsetWidth < container.offsetHeight * 0.6) {
    containerWidth.value = `${container.offsetHeight}px`
  } else {
    containerWidth.value = `${container.offsetWidth}px`
  }
  containerHeight.value = `${container.offsetHeight}px`

  const mid = parseInt(points.length / 2)

  const options = {
    center: new kakao.maps.LatLng(points[mid].latitude, points[mid].longitude),
    level: 5
  }

  map = new kakao.maps.Map(container, options)
  map.setDraggable(movable)
  map.setZoomable(movable)

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
  margin-top: 10px;
}

.text-center {
  text-align: center;
}
</style>
