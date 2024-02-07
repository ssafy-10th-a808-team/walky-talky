<template>
  <div>
    <div id="map"></div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount } from 'vue'

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
const state = ref({
  map: null,
  positionArr: []
})

const successHandler = (position) => {
  // 초기 맵 설정
  const mapContainer = document.getElementById('map')
  const mapOption = {
    center: new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude),
    level: 3
  }

  state.map = new kakao.maps.Map(mapContainer, mapOption)

  // 맵에 선 그리기
  makeLine([position])
}

const errorHandler = () => {
  alert('GPS를 사용할 수 없습니다. 위치정보 설정을 확인해주세요.')
}

const getCurLocation = () => {
  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(successHandler, errorHandler)
  } else {
    alert('GPS를 사용할 수 없습니다. 위치정보 설정을 확인해주세요.')
  }
}

const makeLine = (positions) => {
  if (positions.length < 2) {
    return // 선을 그리기 위해서는 최소 2개의 좌표가 필요합니다.
  }
  const linePath = positions.map((pos) => ({
    lat: pos.coords.latitude,
    lng: pos.coords.longitude
  }))

  const polyline = new kakao.maps.Polyline({
    path: linePath,
    strokeWeight: 5,
    strokeColor: '#FFAE00',
    strokeOpacity: 0.7,
    strokeStyle: 'solid'
  })

  polyline.setMap(null)
  // 맵에 선 표시
  polyline.setMap(state.map)
}

const setLinePathArr = (position) => {
  const moveLatLon = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude)
  state.positionArr.push(moveLatLon)

  // 선 그리기
  makeLine(state.positionArr)
}

onMounted(() => {
  if (window.kakao && window.kakao.maps) {
    // 맵이 이미 생성되었으면 현재 위치 가져오기
    if (!state.map) {
      getCurLocation()
    }
  } else {
    const script = document.createElement('script')
    script.onload = () => {
      kakao.maps.load(() => {
        getCurLocation()
      })
    }
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
    document.head.appendChild(script)
  }

  if (state.map) {
    const interval = setInterval(() => {
      navigator.geolocation.getCurrentPosition(setLinePathArr)
    }, 5000)

    onBeforeUnmount(() => {
      clearInterval(interval)
    })
  }
})
</script>

<style>
#map {
  width: 100%;
  height: 400px; /* 필요에 따라 높이 조절 */
}
</style>
