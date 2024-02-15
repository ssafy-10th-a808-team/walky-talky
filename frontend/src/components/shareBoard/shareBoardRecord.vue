<template>
  <div class="map-container">
    <div class="scroll-over-container">
      <div
        :id="'map-' + uniqueId + seq"
        class="map"
        :style="{ width: containerWidth, height: containerHeight, justifyContent: 'center' }"
        @click="changeMovable"
      />
    </div>

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
        <p>{{ distance.toFixed(2) }} km</p>
      </div>
    </div>
    <div v-if="isList == undefined" class="record-clone-btn">
      <button @click="moveWalk">따라 산책</button>
    </div>
  </div>
</template>

<script setup>
const { distance, duration, points, title, seq, movable, isList } = defineProps([
  'distance',
  'duration',
  'points',
  'title',
  'seq',
  'movable',
  'isList'
])
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
const router = useRouter()

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

const isMovable = ref(movable)

const changeMovable = () => {
  if (isMovable.value === false) {
    isMovable.value = true
  }
}

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
const uniqueId = ref(Date.now()) // 각 컴포넌트에 고유한 ID를 부여하기 위한 ref

const containerWidth = ref('100%')
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

  const mid = parseInt(points.length / 2)

  if (points && points.length > 0 && mid >= 0 && mid < points.length) {
    const options = {
      center: new kakao.maps.LatLng(points[mid].latitude, points[mid].longitude),
      level: 5
    }

    map = new kakao.maps.Map(container, options)

    var positions = [
      {
        title: '산책 시작',
        img: 'start.png',
        latlng: new kakao.maps.LatLng(points[0].latitude, points[0].longitude)
      },
      {
        title: '산책 끝',
        img: 'end.png',
        latlng: new kakao.maps.LatLng(
          points[points.length - 1].latitude,
          points[points.length - 1].longitude
        )
      }
    ]

    for (var i = 0; i < positions.length; i++) {
      var imageSize = new kakao.maps.Size(20, 30)

      var markerImage = new kakao.maps.MarkerImage(
        import.meta.env.VITE_MARKER_IMAGE_ROUTE + positions[i].img,
        imageSize
      )

      var marker = new kakao.maps.Marker({
        map: map,
        position: positions[i].latlng,
        title: positions[i].title,
        image: markerImage
      })
    }

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
  } else {
    const options = {
      center: new kakao.maps.LatLng(37.501289692413124, 127.03961880220784),
      level: 5
    }

    map = new kakao.maps.Map(container, options)

    map.setDraggable(movable)
    map.setZoomable(movable)
  }
}

const moveWalk = (event) => {
  event.stopPropagation()
  router.push({ name: 'DoWalk', params: { seq } })
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

.record-clone-btn {
  margin: 0px 10px 10px 10px;
}

.text-center {
  text-align: center;
  margin: 20px 20px 0px 20px;
}

.scroll-over-container {
  width: 100%;
  z-index: 9;
}
</style>
