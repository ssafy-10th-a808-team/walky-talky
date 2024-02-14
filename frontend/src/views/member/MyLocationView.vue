<template>
  <div>
    <div id="map" @load="getAddressInfo"></div>
  </div>
  <div>
    내 동네: {{ address_name }}
    <div class="col-lg-3 cta-btn-container text-center">
      <button type="button" @click="reload">다시불러오기</button>
    </div>
  </div>
  <div style="display: none">지역 코드 : {{ address_code }}</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMemberStore } from '@/stores/member'
const API_KEY = import.meta.env.VITE_KAKAO_API_KEY

const memberstore = useMemberStore()
let map = null // map is not defined Reference Error 방지
let lat = 0
let lon = 0
const address_name = ref('')
const address_code = ref('')

// 위치 정보 비허용 등으로 인해 맵이 뜨지 않았을 경우 함수 재실행
const reload = () => {
  getLocation()
  getAddressInfo()
}

// 자식 컴포넌트로서 부모 컴포넌트에 지역 코드와 주소 정보 전달
const emit = defineEmits(['returnAddressName', 'returnAddressCode'])
const getAddressInfo = () => {
  emit('returnAddressName', address_name.value)
  emit('returnAddressCode', address_code.value)
}
// 화면 실행 시
onMounted(async () => {
  setTimeout(() => {
    if (window.kakao && window.kakao.maps) {
      initMap()
      // 카카오 스크립트가 불러와졌다면 카카오 맵 띄우기
    } else {
      const script = document.createElement('script')
      // eslint 사용 시  kakao 변수가 선언되지 않았다고 오류가 나기 때문에 아래줄 추가
      /* global kakao */
      script.onload = () => {
        // 카카오맵 api script 불러오기
        kakao.maps.load(initMap)
        // console.log('카카오맵 api script loaded')
      }
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
      //autoload=false를 통해 로딩이 끝나는 시점에 콜백을 통해 객체에 접근
      document.head.appendChild(script)
    }
  }, 150)
  // 좌표 가져오는 함수
  getLocation()
})

const initMap = async () => {
  // 카카오 맵을 띄우는 함수
  // console.log('initMap 적용')

  // 내 위치에 마커 생성
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

  // 좌표로 지역코드 가져오는 함수, await로 zeroresult 방지
  const geocoder = new kakao.maps.services.Geocoder()
  await geocoder.coord2RegionCode(lon, lat, addrCallback)
}

const addrCallback = (result, status) => {
  // 법정동 상세 주소를 가져올 때 콜백 함수를 선언한 것입니다
  if (status === kakao.maps.services.Status.OK) {
    // console.log('주소 가져왔습니다')
    // console.log(result[0])
    if (result[0].region_type === 'B') {
      // 법정동 코드일 경우에만 저장하기
      address_name.value = result[0].address_name
      address_code.value = result[0].code
      memberstore.address_name = address_name.value
      memberstore.address_code = address_code.value
      getAddressInfo()
    }
  } else {
    console.error('Failed to get address info')
    console.log(kakao.maps.services.Status)
    console.log(result)
  }
}

const getLocation = () => {
  if (navigator.geolocation) {
    // geolocation 라이브러리를 통해 gps로 내 위치 가져오기
    navigator.geolocation.getCurrentPosition(function (position) {
      lat = position.coords.latitude // 위도
      lon = position.coords.longitude // 경도

      // geolocation 가능한 경우 내 위치
      // 크롬 브라우저는 https 환경에서만 geolocation이 지원
      // console.log('내 좌표를 가져왔습니다')
    })
  } else {
    lat = 37.5014
    lon = 127.0395
    // geolocation 불가능하면 위치를 멀티캠퍼스로
    // console.log('멀티캠퍼스 좌표를 가져왔습니다')
  }
  initMap()
}
</script>

<style scoped>
#map {
  max-width: 100%;
  height: 400px;
}
</style>
