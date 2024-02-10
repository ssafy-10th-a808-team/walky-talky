<template>
  <div>
    <!-- WalkHeaderNav 컴포넌트를 불러와서 사용 -->
    <WalkHeaderNav />

    <!-- 산책하기 제목 -->
    <h1>산책하기</h1>

    <!-- 지도를 표시할 영역 -->
    <div class="map_wrap" style="position: relative">
      <div id="map" style="width: 100%; height: 600px">
        <!-- 정보 및 버튼 표시 영역 -->
        <div
          style="
            text-align: center;
            position: absolute;
            z-index: 2;
            top: 73%;
            left: 50%;
            background-color: rgb(205, 238, 225);
            display: flex;
            width: 200px;
            margin-left: -100px;
            border-radius: 5%;
          "
        >
          <!-- 거리와 시간 정보 -->
          <div class="myRecord">
            <div id="run_desc time">시간</div>
            <span id="time" style="font-weight: 700; width: 100px; float: left">{{ clock }}</span>
          </div>
          <div class="myRecord">
            <div id="run_desc distance">거리</div>
            <span id="acc_dis" style="font-weight: 700; width: 100px; float: right">
              {{ accumulated_distance.toFixed(2) }}km
            </span>
          </div>
        </div>

        <!-- 산책 버튼 및 일시정지, 정지 버튼 표시 영역 -->
        <div
          class="btn_container"
          style="
            text-align: center;
            position: absolute;
            z-index: 2;
            top: 80%;
            left: 50%;
            width: 200px;
            margin-left: -100px;
          "
        >
          <!-- 산책 중이 아닐 때 표시되는 영역 -->
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
              <!-- 산책 시작 버튼 -->
              <div v-if="!isPause">
                <button @click="startWalk">START</button>
              </div>
              <!-- 일시정지일 때 표시되는 버튼 -->
              <div v-if="isPause">
                <button @click="watchLocationUpdates">START</button>
                <button @click="endLocationUpdates">STOP</button>
              </div>
            </section>
          </div>

          <!-- 산책 중일 때 표시되는 영역 -->
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
              <!-- 일시정지 버튼 및 정지 버튼 -->
              <button @click="pauseLocationUpdates">PAUSE</button>
              <button @click="endLocationUpdates">STOP</button>
            </section>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
// Vue 3의 Composition API를 사용하여 코드를 구성

// 필요한 모듈 및 라이브러리 불러오기
import { ref, onMounted, watchEffect, onBeforeUnmount } from 'vue'
import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
import router from '../../router'
import axios from 'axios'
import moment from 'moment'

// VueX에서 사용하는 스토어 불러오기
import { useWalkStore } from '@/stores/walk'
import { useMemberStore } from '@/stores/member'
import { useCounterStore } from '@/stores/counter'

// VueX 스토어 인스턴스 생성
const walkStore = useWalkStore()
const memberStore = useMemberStore()
const counterstore = useCounterStore()

// 카카오 API 키 가져오기
const API_KEY = import.meta.env.VITE_KAKAO_API_KEY

// 위치 및 거리 정보를 저장하는 변수들
let lat = 0
let lon = 0
const address_name = ref('')
const address_code = ref('')
const region_cd = ref('')

// 현재 위치 및 이전 위치 좌표
const current = ref({ lat: 0, lon: 0 })
const previous = ref({ lat: 0, lon: 0 })

// 현재 주소, 위치 감시 ID 등을 저장하는 변수들
const address = ref('')
const watchPositionId = ref(null)

// 누적 거리 및 시간, 체크 변수들
const accumulated_distance = ref(0)
const accumulated_time = ref(0)
const checkOneKm = ref(0)
const checkSecond = ref(0)

// 위치 기록 및 선 경로 정보
const linePath = ref([])
const poly = ref(null)

// 현재 마커, 시작 및 종료 시간 등을 저장하는 변수들
const cur_marker = ref(null)
const startTime = ref('')
const endTime = ref('')

// 임시 위치 기록 및 서버로 전송할 위치 기록 배열
const tempRecords = ref([])
const recordsForPost = ref([])

// 스톱워치 변수 및 상태 변수들
const clock = ref('00:00:00')
const timeBegan = ref(null)
const timeStopped = ref(null)
const stoppedDuration = ref(0)
const started = ref(null)
const running = ref(false)
const isPause = ref(false)

// Vue 3의 Composition API에서는 데이터 및 로직을 setup 안에서 선언
const state = ref({
  map: null,
  positionArr: []
})

// 컴포넌트가 마운트되었을 때 실행되는 로직
onMounted(() => {
  const script = document.createElement('script')
  script.onload = () => {
    kakao.maps.load(initMap)
  }
  script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
  document.head.appendChild(script)

  if (navigator.geolocation) {
    navigator.geolocation.getCurrentPosition(
      function (position) {
        lat = position.coords.latitude
        lon = position.coords.longitude
      },
      function (error) {
        console.error('지오로케이션을 가져오는 중 오류 발생:', error)
      }
    )
  } else {
    alert('GPS를 사용할 수 없습니다. 위치정보 설정을 확인해주세요.')
  }

  if (state.value.map) {
    // 마운트 되었을 때 map이 있다면 interval 을 5초로
    const interval = setInterval(() => {
      navigator.geolocation.getCurrentPosition(setLinePathArr)
    }, 5000)

    onBeforeUnmount(() => {
      // 마운트가 되기 전에 map이 있다면 map 정보 초기화
      if (state.value.map) {
        state.value.map = null
      }
      clearInterval(interval)
    })
  }
})

// 초기 지도 설정 함수
const initMap = () => {
  if (state.value.map) return

  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(lat, lon)
  })

  const container = document.getElementById('map')
  const options = {
    center: new kakao.maps.LatLng(lat, lon),
    level: 4
  }

  state.value.positionArr = []

  state.value.map = new kakao.maps.Map(container, options)

  marker.setMap(state.value.map)

  const geocoder = new kakao.maps.services.Geocoder()
  geocoder.coord2RegionCode(lon, lat, addrCallback)
}

// 주소 변환 콜백 함수
const addrCallback = (result, status) => {
  if (status === kakao.maps.services.Status.OK) {
    if (result[0].region_type === 'B') {
      address_name.value = result[0].address_name
      address_code.value = result[0].code
      memberStore.address_name = address_name.value
      memberStore.address_code = address_code.value
    }
    address.value = result[0].address_name
  } else {
    console.error('Failed to get address info')
    console.log(kakao.maps.services.Status)
    console.log(result)
  }
}

// 위치 초기화 함수
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
  current.value.lon = 0
  previous.value.lat = 0
  previous.value.lon = 0
}

// 위치 정보 감시 함수
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

  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(lat, lon)
  })

  cur_marker.value = marker
  marker.setMap(state.value.map)

  watchPositionId.value = navigator.geolocation.watchPosition(
    (position) => {
      current.value.lat = position.coords.latitude
      current.value.lon = position.coords.longitude
      const now = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude)

      axios
        .get(
          'https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=' +
            current.value.lon +
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

      state.value.map.setCenter(now)
      marker.setPosition(now)

      if (previous.value.lat === 0) {
        previous.value.lat = current.value.lat
        previous.value.lon = current.value.lon

        const currentLatLng = new kakao.maps.LatLng(current.value.lat, current.value.lon)
        linePath.value.push(currentLatLng)
        setLinePathArr(position)

        tempRecords.value.push({ lat: current.value.lat, lon: current.value.lon, time: new Date() })
        makeLine(linePath.value)
      } else {
        const distance = computeDistance(previous, current)
        const threshold = 0.001
        previous.value.lat = current.value.lat
        previous.value.lon = current.value.lon

        if (distance > threshold) {
          accumulated_distance.value += distance
          checkOneKm.value += distance

          linePath.value.push(new kakao.maps.LatLng(current.value.lat, current.value.lon))
          makeLine(linePath.value)
        }
      }

      const locationUpdateInterval = 60000
      watchPositionId.value = setInterval(() => {
        recordsForPost.value.push({
          lat: current.value.lat,
          lon: current.value.lon,
          time: new Date()
        })
      }, locationUpdateInterval)
    },
    () => {
      router.push('/walk/do-walk')
      console.log('위치 정보를 가져오는 도중 오류가 발생했습니다')
    },
    {
      timeout: 5000,
      maximumAge: 0,
      enableHighAccuracy: true,
      distanceFilter: 40
    }
  )
}

// 산책 시작 함수
const startWalk = function () {
  resetLocations()
  startTime.value = new Date()
  startTime.value = moment(startTime.value).format('YYYY-MM-DDTHH:mm:ss')
  region_cd.value = address_code.value
  console.log(startTime)

  // recordsForPost.value.push({
  //   lat: current.value.lat,
  //   lon: current.value.lon,
  //   time: new Date()
  // })
  watchLocationUpdates()
  walkStore.startWalk()
}

// 시간 포맷팅 함수
const zeroPrefix = function (num, digit) {
  const zero = ref('')
  for (var i = 0; i < digit; i++) {
    zero.value += '0'
  }
  return (zero.value + num).slice(-digit)
}

// 시간 업데이트 함수
const clockRunning = function () {
  const currentTime = new Date()
  const timeElapsed = new Date(currentTime - timeBegan.value - stoppedDuration.value)

  const hour = timeElapsed.getUTCHours()
  const min = timeElapsed.getUTCMinutes()
  const sec = timeElapsed.getUTCSeconds()

  clock.value = zeroPrefix(hour, 2) + ':' + zeroPrefix(min, 2) + ':' + zeroPrefix(sec, 2)

  const realTime = ((currentTime - timeBegan.value - stoppedDuration.value) / 1000).toFixed(0)
  accumulated_time.value = realTime
  checkSecond.value = realTime
}

// 위치 저장 함수
const savePosition = async function () {
  console.log(walkStore.data.data.seq)
  console.log(accumulated_time.value)
  console.log(accumulated_distance.value)
  try {
    const url = 'https://i10a808.p.ssafy.io/api/walk/regist-record'
    const accessToken = counterstore.getCookie('atk')
    const headers = {
      Authorization: `Bearer ${accessToken}`
    }

    const data = {
      seq: walkStore.data.data.seq,
      duration: accumulated_time.value,
      distance: accumulated_distance.value,
      points: recordsForPost.value.map((record) => [record.lat, record.lon, record.time]),
      starRating: 1,
      comment: '한줄평',
      title: '제목',
      regionCd: region_cd.value
    }

    const response = await axios.post(url, data, { headers })

    console.log(response.data)
  } catch (error) {
    console.error('Error while saving position:', error)
  }
}

// 산책 종료 함수
const endLocationUpdates = function () {
  pauseLocationUpdates()
  alert('산책 기록이 저장되었습니다 📬')

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
  router.push('/walk/list')
}

// 일시정지 함수
const pauseLocationUpdates = function () {
  isPause.value = true
  running.value = false
  timeStopped.value = new Date()
  clearInterval(started.value)

  navigator.geolocation.clearWatch(watchPositionId.value)
  makeLine()
}

// 거리 비교 및 계산 함수
const computeDistance = function (startCoords, destCoords) {
  var startLatRads = degreesToRadians(startCoords.value.lat)
  var startLongRads = degreesToRadians(startCoords.value.lon)
  var destLatRads = degreesToRadians(destCoords.value.lat)
  var destLongRads = degreesToRadians(destCoords.value.lon)

  var Radius = 6371
  var distance =
    Math.acos(
      Math.sin(startLatRads) * Math.sin(destLatRads) +
        Math.cos(startLatRads) * Math.cos(destLatRads) * Math.cos(startLongRads - destLongRads)
    ) * Radius

  return distance
}

// 각도를 라디안으로 변환 함수
const degreesToRadians = function (degrees) {
  var radians = (degrees * Math.PI) / 180
  return radians
}

// 지도에 선 그리기 함수
const makeLine = () => {
  if (running.value && state.value.positionArr.length >= 2) {
    const linePath = state.value.positionArr

    const polyline = new kakao.maps.Polyline({
      path: linePath,
      strokeWeight: 5,
      strokeColor: '#FFAE00',
      strokeOpacity: 0.7,
      strokeStyle: 'solid'
    })

    if (poly.value) {
      poly.value.setMap(null)
    }

    polyline.setMap(state.value.map)
    poly.value = polyline
  }
}

// 위치 배열 업데이트 함수
const setLinePathArr = (position) => {
  if (position && position.coords) {
    const moveLatLon = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude)

    if (!state.value.positionArr) {
      state.value.positionArr = []
    }
    state.value.positionArr.push(moveLatLon)

    if (running.value) {
      makeLine()
    }
  }
}

// watchEffect에서의 setLinePathArr 호출 부분 제거
watchEffect(() => {
  if (state.value.map && running.value) {
    let interval = setInterval(() => {
      navigator.geolocation.getCurrentPosition((position) => setLinePathArr(position))
    }, 5000)

    return () => {
      clearInterval(interval)
    }
  }
})
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
