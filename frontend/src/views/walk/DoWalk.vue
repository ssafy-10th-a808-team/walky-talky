<template>
  <div>
    <WalkHeaderNav />
    <h1>산책하기</h1>
    <div class="map_wrap" style="position: relative">
      <div id="map" style="width: 100%; height: 350px">
        <!-- 정보 및 버튼 -->
        <div style="text-align: center">
          <div :span="8" class="myRecord">
            <div id="run_desc time">시간</div>
            <span id="time" style="font-weight: 700">{{ clock }}</span>
          </div>
          <div :span="8" class="myRecord">
            <div id="run_desc distance">거리</div>
            <span id="acc_dis" style="font-weight: 700">
              {{ accumulated_distance.toFixed(2) }}km
            </span>
          </div>
        </div>

        <div class="btn_container">
          <!-- 걷지 않을때! -->
          <div v-if="!running">
            <section
              class="bottom-bar"
              style="
                margin-top: 10px;
                margin-bottom: 10px;
                padding-top: 10px;
                margin-bottom: 10px;
                display: flex;
                justify-content: center;
              "
            >
              <!-- 걷지않을때 : 맨 처음 시작할때 -->
              <div v-if="!isPause">
                <button @click="startLocationUpdates">START</button>
              </div>
              <!-- 걷지않을때 : 일시정지를 눌렀을 때 -->
              <div v-if="isPause">
                <button @click="watchLocationUpdates">START</button>
                <button @click="endLocationUpdates">STOP</button>
              </div>
            </section>
          </div>
          <!-- 걷는중일때 띄우는 창 -> pause와 stop만 띄우기 -->
          <div v-if="running">
            <section
              class="bottom-bar"
              style="
                margin-top: 10px;
                margin-bottom: 10px;
                padding-top: 10px;
                margin-bottom: 10px;
                display: flex;
                justify-content: center;
              "
            >
              <button @click="stopLocationUpdates">PAUSE</button>
              <button @click="endLocationUpdates">STOP</button>
            </section>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
import router from '../../router'
import moment from 'moment'
const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
let lat = 0
let lon = 0
// const address = ref('')

const current = ref({ lat: 0, lon: 0 })
const previous = ref({ lat: 0, lon: 0 })
const address = ref('')
const watchPositionId = ref(null)
// const map = ref(null)
const accumulated_distance = ref(0)
const accumulated_time = ref(0)
// const speed = ref(0)
const checkOneKm = ref(0)
const checkSecond = ref(0)
// const avgSpeed = ref(0)
const linePath = ref([])
const poly = ref(null)
// const encoded_polyline = ref('')
const cur_marker = ref(null)
const startTime = ref('')
const endTime = ref('')
// const gugun = ref([])
// const currentCity = ref('')
// const thumbnail = ref('')
const tempRecords = ref([])
const stringTempRecords = ref([])

// 스톱워치
const clock = ref('00:00:00')
const timeBegan = ref(null)
const timeStopped = ref(null)
const stoppedDuration = ref(0)
const started = ref(null)
const running = ref(false)
const isPause = ref(false)

const course = ref(router.currentRoute.value.params.id)

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
    resetLocations()
    accumulated_distance.value = 0
    accumulated_time.value = 0
    checkSecond.value = 0
    checkOneKm.value = 0
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

const startLocationUpdates = function () {
  startTime.value = new Date()
  startTime.value = moment(startTime).format('YYYY-MM-DDTHH:mm:ss')
  watchLocationUpdates()
}

const zeroPrefix = function (num, digit) {
  const zero = ''
  for (var i = 0; i < digit; i++) {
    zero.value += '0'
  }
  return (zero.value + num).slice(-digit)
}

const clockRunning = function () {
  const currentTime = new Date()
  //경과된 시간
  const timeElapsed = new Date(currentTime - timeBegan.value - stoppedDuration.value)

  const hour = timeElapsed.getUTCHours()
  const min = timeElapsed.getUTCMinutes()
  const sec = timeElapsed.getUTCSeconds()

  clock.value = zeroPrefix(hour, 2) + ':' + zeroPrefix(min, 2) + ':' + zeroPrefix(sec, 2)

  const realTime = ((currentTime - timeBegan.value - stoppedDuration.value) / 1000).toFixed(0)
  accumulated_time.value = realTime
  checkSecond.value = realTime
}

const resetLocations = function () {
  endTime.value = ''
  clock.value = '00:00:00'
  timeBegan.value = null
  timeStopped.value = null
  stoppedDuration.value = 0
  started.value = null
  checkSecond.value = 0
  checkOneKm.value = 0
  current.value.lat = 0
  current.value.lng = 0
  previous.value.lat = 0
  previous.value.lng = 0
}

const watchLocationUpdates = function () {
  if (running.value) return

  if (timeBegan.value === null) {
    resetLocations()
    timeBegan.value = new Date()
  }

  if (timeStopped.value !== null) {
    stoppedDuration.value += new Date() - timeStopped.value
  }

  started.value = setInterval(clockRunning, 1000)
  running.value = true
  isPause.value = false

  //Map 시작
  const map = map
  const marker = marker

  watchPositionId.value = navigator.geolocation.watchPosition(
    (position) => {
      current.value.lat = position.coords.latitude
      current.value.lng = position.coords.longitude
      const now = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude)
      // $store.commit('SET_IS_AGREE')
      axios
        .get(
          'https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=' +
            current.value.lng +
            '&y=' +
            current.value.lat,
          {
            headers: {
              Authorization: 'KakaoAK bacd72f58ac01490602415c683ad8c05'
            }
          }
        )
        .then((response) => {
          address.value = response.data.documents[0].address_name
        })
      map.setCenter(now)
      marker.setPosition(now)
      if (previous.value.lat == 0) {
        previous.value.lat = current.value.lat
        previous.value.lng = current.value.lng

        //런닝 시작
        const currentLatLng = new kakao.maps.LatLng(current.value.lat, current.value.lng)
        linePath.value.push(currentLatLng)
      } else {
        const distance = computeDistance(previous, current)
        const threshold = 0.001
        previous.value.lat = current.value.lat
        previous.value.lng = current.value.lng

        if (distance > threshold) {
          // 일정속도 이상
          accumulated_distance.value += distance
          checkOneKm.value += distance

          linePath.value.push(new kakao.maps.LatLng(current.value.lat, current.value.lng))
          // speed.value = (checkOneKm.value * 1000) / checkSecond.value

          drawLines()
        }
        if (checkOneKm.value >= 1) {
          //1km 도달
          savePosition()
          checkOneKm.value -= 1
          checkSecond.value = 0
        }
      }
    },
    () => {
      $store.commit('SET_IS_NOT_AGREE')
      router.push('/index')
    },
    {
      timeout: 5000,
      maximumAge: 0,
      enableHighAccuracy: true,
      distanceFilter: 40
    }
  )
  map = map
  cur_marker.value = marker
}

// 위치 저장하기
const savePosition = function () {
  // const speed = 0
  // if (checkOneKm <= 0 || checkSecond <= 0) {
  //   speed = 0.001
  // } else {
  //   speed = speed + 0.001
  // }

  let tempRecord = {
    accDistance: accumulated_distance.value + 0.001,
    accTime: accumulated_time
    // speed: speed
  }

  tempRecords.value.push(tempRecord)

  let stringTempRecord = {
    accDistance: (accumulated_distance.value + 0.001).toString(),
    accTime: accumulated_time.value.toString()
    // speed: speed.toString()
  }
  stringTempRecords.value.push(stringTempRecord)

  https.post('/main/finishrecord', {
    userId: $store.getters.getLoginUserInfo.userId,
    courseId: course.id,
    distance: accumulated_distance,
    time: accumulated_time,
    calorie: accumulated_time * 0.06
  })
}

// 기록 중지(기록)
const endLocationUpdates = function () {
  stopLocationUpdates()
  alert('산책 기록이 저장되었습니다 📬')

  // speed.value = (accumulated_distance.value * 1000) / accumulated_time.value

  savePosition()
  isPause.value = false
  running.value = false
  stoppedDuration.value = 0
  timeBegan.value = null
  timeStopped.value = null
  clock.value = '00:00:00'
  checkSecond.value = 0
  checkOneKm.value = 0
  endTime.value = new Date()
  endTime.value = moment(endTime).format('YYYY-MM-DDTHH:mm:ss')
  router.push('/main')
}

// 기록 중지
const stopLocationUpdates = function () {
  isPause.value = true
  running.value = false
  timeStopped.value = new Date()
  clearInterval(started)

  navigator.geolocation.clearWatch(watchPositionId)
  drawLines()
}

const computeDistance = function (startCoords, destCoords) {
  var startLatRads = degreesToRadians(startCoords.lat)
  var startLongRads = degreesToRadians(startCoords.lng)
  var destLatRads = degreesToRadians(destCoords.lat)
  var destLongRads = degreesToRadians(destCoords.lng)

  var Radius = 6371 //지구의 반경(km)
  var distance =
    Math.acos(
      Math.sin(startLatRads) * Math.sin(destLatRads) +
        Math.cos(startLatRads) * Math.cos(destLatRads) * Math.cos(startLongRads - destLongRads)
    ) * Radius

  return distance
}
const degreesToRadians = function (degrees) {
  var radians = (degrees * Math.PI) / 180
  return radians
}
// const encode_polyline = function (poly) {
//   var path = poly.getPath()
//   encoded_polyline.value = kakao.maps.geometry.encoding.encodePath(path)
// }

const drawLines = function () {
  poly.value = new kakao.maps.Polyline({
    path: linePath.value,
    geodesic: true,
    strokeColor: '#ff0000',
    strokeOpacity: 1.0,
    strokeWeight: 2,
    map: map.value
  })

  poly.value.setMap(map)
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
