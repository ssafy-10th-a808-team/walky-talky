<template>
  <div>
    <WalkHeaderNav />
    <h1>산책하기</h1>
    <div class="map_wrap" style="position: relative">
      <div id="map" style="width: 100%; height: 500px">
        <!-- 산책하기 버튼을 눌렀을 때 스탑워치 실행되게 하기 -->
        <!-- <StopWatch style="position: absolute; z-index: 2; top: 70%; left: 40%" /> -->

        <!-- 정보 및 버튼 -->
        <div
          style="
            text-align: center;
            position: absolute;
            z-index: 2;
            top: 60%;
            left: 50%;
            background-color: yellow;
            display: flex;
            width: 200px;
            margin-left: -100px;
          "
        >
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

        <div
          class="btn_container"
          style="
            text-align: center;
            position: absolute;
            z-index: 2;
            top: 70%;
            left: 50%;
            background-color: yellow;
            width: 200px;
            margin-left: -100px;
          "
        >
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
                <button @click="startWalk">START</button>
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
import { ref, onMounted, watchEffect } from 'vue'
import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
import router from '../../router'
import axios from 'axios'
import moment from 'moment'

import { useWalkStore } from '@/stores/walk'
import { useMemberStore } from '@/stores/member'
import { useCounterStore } from '@/stores/counter'

const walkStore = useWalkStore()
const memberStore = useMemberStore()
const counterstore = useCounterStore()

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
let lat = 0
let lon = 0
const address_name = ref('')
const address_code = ref('')
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
const state = ref({
  positionArr: []
})
// const region_cd = ref('')
// region_cd.value = memberStore.getLocationInfo()[1]

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
    alert('GPS를 사용할 수 없습니다. 위치정보 설정을 확인해주세요.')
    // lat = 37.5014
    // lon = 127.0395
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
  // 라인을 그리기 위해 map 객체를 ref에 저장
  map = new kakao.maps.Map(container, options)

  marker.setMap(map)

  const geocoder = new kakao.maps.services.Geocoder()
  geocoder.coord2Address(lon, lat, addrCallback)
}

const addrCallback = (result, status) => {
  // 법정동 상세 주소를 가져올 때 콜백 함수를 선언한 것입니다
  if (status === kakao.maps.services.Status.OK) {
    console.log('주소 가져왔습니다')
    console.log(result[0])
    if (result[0].region_type === 'B') {
      // 법정동 코드일 경우에만 저장하기, 수정가능성 높음
      address_name.value = result[0].address_name
      address_code.value = result[0].code
      memberStore.address_name = address_name.value
      memberStore.address_code = address_code.value
    }
    address.value = result[0].address.address_name
  } else {
    console.error('Failed to get address info')
    console.log(kakao.maps.services.Status)
    console.log(result)
  }
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
  current.value.lon = 0
  previous.value.lat = 0
  previous.value.lon = 0
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

  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(lat, lon)
  })
  cur_marker.value = marker
  marker.setMap(map)

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

      map.setCenter(now)
      marker.setPosition(now)

      if (previous.value.lat == 0) {
        previous.value.lat = current.value.lat
        previous.value.lon = current.value.lon

        //런닝 시작
        const currentLatLng = new kakao.maps.LatLng(current.value.lat, current.value.lon)
        linePath.value.push(currentLatLng)
        // setLinePathArr 호출 추가
        setLinePathArr(position)
        // makeLine 호출 추가
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
          // drawLines()
          // makeLine 호출 추가
          makeLine(linePath.value)
        }

        if (checkOneKm.value >= 1) {
          savePosition()
          checkOneKm.value -= 1
          checkSecond.value = 0
        }
      }
    },
    () => {
      router.push('/')
    },
    {
      timeout: 5000,
      maximumAge: 0,
      enableHighAccuracy: true,
      distanceFilter: 40
    }
  )
}

const startWalk = function () {
  resetLocations()
  startTime.value = new Date()
  startTime.value = moment(startTime.value).format('YYYY-MM-DDTHH:mm:ss')
  console.log(startTime)
  watchLocationUpdates()
  walkStore.startWalk()
}

const zeroPrefix = function (num, digit) {
  const zero = ref('')
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

const savePosition = async function () {
  console.log(walkStore.data.data.seq)
  console.log(accumulated_time.value)
  console.log(accumulated_distance.value)
  console.log(tempRecords.value.map((record) => [record.lat, record.lon, record.time]))
  try {
    // 아래의 URL은 실제 서버의 엔드포인트로 수정해야 합니다.
    const url = 'https://i10a808.p.ssafy.io/api/walk/regist-record'

    // 서버로 보낼 데이터를 구성합니다.
    const data = {
      seq: walkStore.data.data.seq,
      duration: accumulated_time.value,
      distance: accumulated_distance.value,
      points: tempRecords.value.map((record) => [record.lat, record.lon, record.time]),
      starRating: 1,
      comment: '한줄평',
      title: '제목',
      regionCd: region_cd.value
    }

    // 서버로 보낼 때 헤더에 Bearer 토큰을 추가합니다.
    const accessToken = counterstore.getCookie('atk') // 실제 토큰 값으로 대체해야 합니다.
    const headers = {
      Authorization: `Bearer ${accessToken}`
    }

    // axios를 사용하여 서버로 POST 요청을 보냅니다.
    const response = await axios.post(url, data, { headers })

    // 서버 응답을 처리합니다.
    console.log(response.data) // 서버에서 반환하는 데이터를 확인하거나 필요에 맞게 처리합니다.
  } catch (error) {
    console.error('Error while saving position:', error)
    // 에러 처리를 추가할 수 있습니다.
  }
}

// 산책 끝내기
const endLocationUpdates = function () {
  // alert(walkStore.data)
  // console.log(walkStore.data)
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
  router.push('/') // 어디로 가지? -> 내 코스 기록 페이지로 가자
}

// 일시정지
const stopLocationUpdates = function () {
  isPause.value = true
  running.value = false
  timeStopped.value = new Date()
  clearInterval(started.value)

  navigator.geolocation.clearWatch(watchPositionId.value)
  // drawLines()
  makeLine()
}

const computeDistance = function (startCoords, destCoords) {
  var startLatRads = degreesToRadians(startCoords.lat)
  var startLongRads = degreesToRadians(startCoords.lon)
  var destLatRads = degreesToRadians(destCoords.lat)
  var destLongRads = degreesToRadians(destCoords.lon)

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

// const drawLines = function () {
//   if (poly.value) {
//     poly.value.setMap(null) // Remove existing polyline
//   }
//   poly.value = new kakao.maps.Polyline({
//     path: linePath.value,
//     geodesic: true,
//     strokeColor: '#ff0000',
//     strokeOpacity: 1.0,
//     strokeWeight: 2,
//     map: map
//   })

//   if (map) {
//     poly.value.setMap(map)
//   }
// }

const makeLine = (position) => {
  let linePath = position

  if (poly.value) {
    poly.value.setMap(null) // Remove existing polyline
  }

  poly.value = new kakao.maps.Polyline({
    path: linePath,
    strokeWeight: 5,
    strokeColor: '#FFAE00',
    strokeOpacity: 0.7,
    strokeStyle: 'solid'
  })

  if (map) {
    poly.value.setMap(map)
  }
}

const setLinePathArr = (position) => {
  const moveLatLon = new kakao.maps.LatLng(position.coords.latitude, position.coords.longitude)

  // 초기값이 없다면 빈 배열로 설정
  if (!state.value.positionArr) {
    state.value.positionArr = []
  }

  const newPosition = state.value.positionArr.concat(moveLatLon)
  state.value.positionArr = newPosition.value

  // 라인을 그리는 함수
  makeLine(newPosition.value)
}

watchEffect(() => {
  // watchEffect를 사용하여 map이 변경될 때의 로직을 작성
  if (map) {
    let interval = setInterval(() => {
      navigator.geolocation.getCurrentPosition(setLinePathArr)
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
