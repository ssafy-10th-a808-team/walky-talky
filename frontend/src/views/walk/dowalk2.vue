<template>
  <div>
    <h1>산책하기</h1>
    <div class="map_wrap" style="position: relative">
      <div id="map" style="width: 100%; height: 500px"></div>
      <div class="controls">
        <button @click="startWalk">START</button>
        <button @click="pauseWalk">PAUSE</button>
        <button @click="stopWalk">STOP</button>
      </div>
      <div class="info">
        <div class="myRecord">
          <div id="run_desc time">시간</div>
          <span id="time" style="font-weight: 700; width: 100px; float: left">{{ clock }}</span>
        </div>
        <div class="myRecord">
          <div id="run_desc distance">거리</div>
          <span id="acc_dis" style="font-weight: 700; width: 100px; float: right"
            >{{ walkData.accumulated_distance.toFixed(2) }}km</span
          >
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, reactive, watchEffect } from 'vue'
import axios from 'axios'
import moment from 'moment'

const state = reactive({
  map: null,
  positionArr: [],
  kakaoLoaded: false
})

const walkData = reactive({
  startTime: null,
  endTime: null,
  accumulated_distance: 0,
  accumulated_time: 0
})

const successHandler = (position) => {
  const mapContainer = document.getElementById('map')
  const mapOption = {
    center: new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude),
    level: 3
  }

  state.map = new kakao.maps.Map(mapContainer, mapOption)

  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude)
  })
  marker.setMap(state.map)
}

const loadKakaoMap = () => {
  return new Promise((resolve) => {
    if (window.kakao && window.kakao.maps) {
      resolve()
    } else {
      const script = document.createElement('script')
      script.onload = resolve
      script.src = 'https://dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_APP_KEY&autoload=false'
      document.head.appendChild(script)
    }
  })
}

const errorHandler = (error) => {
  alert('GPS를 사용할 수 없습니다. 위치정보 설정을 확인해주세요.')
}

onMounted(async () => {
  await loadKakaoMap()
  state.kakaoLoaded = true
  const getCurLocation = () => {
    if (navigator.geolocation) {
      navigator.geolocation.getCurrentPosition(successHandler, errorHandler)
    } else {
      alert('GPS를 사용할 수 없습니다. 위치정보 설정을 확인해주세요.')
    }
  }
  getCurLocation()
})

const initMap = () => {
  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(0, 0)
  })
  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(0, 0),
    level: 3
  }

  state.map = new kakao.maps.Map(container, options)
  marker.setMap(state.map)
}

const resetWalkData = () => {
  walkData.startTime = null
  walkData.endTime = null
  walkData.accumulated_distance = 0
  walkData.accumulated_time = 0
}

const startWalk = () => {
  resetWalkData()
  walkData.startTime = new Date()
  walkData.startTime = moment(walkData.startTime).format('YYYY-MM-DDTHH:mm:ss')
  successHandler({
    coords: {
      latitude: 0, // replace with actual latitude
      longitude: 0 // replace with actual longitude
    }
  })
  watchPosition()
}

const pauseWalk = () => {
  stopWatch()
  stopPositionUpdates()
}

const stopWalk = () => {
  stopWatch()
  stopPositionUpdates()
  saveWalkData()
}

const watchPosition = () => {
  let watchPositionId = navigator.geolocation.watchPosition(
    (position) => {
      const currentLatLon = new kakao.maps.LatLng(
        position.coords.latitude,
        position.coords.longitude
      )

      if (state.positionArr.length === 0) {
        state.positionArr.push(currentLatLon)
      } else {
        const lastPosition = state.positionArr[state.positionArr.length - 1]
        const distance = kakao.maps.geometry.distance(lastPosition, currentLatLon)
        walkData.accumulated_distance += distance
        state.positionArr.push(currentLatLon)
      }

      makeLine()
    },
    (error) => {
      console.error('Error getting location:', error)
    },
    {
      enableHighAccuracy: true,
      timeout: 5000,
      maximumAge: 0
    }
  )

  onBeforeUnmount(() => {
    navigator.geolocation.clearWatch(watchPositionId)
  })
}

const makeLine = () => {
  if (state.map && state.positionArr.length > 1) {
    const polyline = new kakao.maps.Polyline({
      path: state.positionArr,
      strokeWeight: 5,
      strokeColor: '#FFAE00',
      strokeOpacity: 0.7,
      strokeStyle: 'solid'
    })

    polyline.setMap(state.map)
  }
}

const stopPositionUpdates = () => {
  if (state.map) {
    state.map = null // remove the map reference
    state.positionArr = [] // clear the position array
    initMap() // reinitialize the map
  }
}

const stopWatch = () => {
  // Implement logic to pause the timer
  // You can use the walkData.startTime and current time to calculate the paused duration
}

const saveWalkData = async () => {
  try {
    const url = 'https://your-api-endpoint' // Replace with your actual API endpoint
    const data = {
      startTime: walkData.startTime,
      endTime: moment().format('YYYY-MM-DDTHH:mm:ss'),
      duration: walkData.accumulated_time,
      distance: walkData.accumulated_distance.toFixed(2)
      // Add other relevant data
    }

    const response = await axios.post(url, data)
    console.log(response.data)
  } catch (error) {
    console.error('Error while saving walk data:', error)
  }
}

watchEffect(() => {
  // Additional logic if needed
})
</script>

<style scoped>
.map_wrap {
  position: relative;
}

.controls {
  margin-top: 10px;
}

.controls button {
  margin-right: 5px;
}
</style>
