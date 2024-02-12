<template>
  <ClubDetailHeaderNav />

  <!-- {{ clubstore.planDetail }} -->

  <div v-if="clubstore.planDetail.responsePlanDetailDtoPlan">
    <div v-if="clubstore.planDetail.responsePlanDetailDtoPlan.recordSeq != null">
      <div
        :id="'map-' + uniqueId"
        class="map"
        :style="{ width: '100%', height: '500px', justifyContent: 'center' }"
        @click="setDraggable(true)"
      ></div>

      <div class="record-container">
        <div class="header">
          <div class="left-section">
            <p v-if="title != '제목'">{{ title }}</p>
          </div>
          <!-- <div class="right-section star-section">
            <StarRating :starRating="parseInt(starRating)" :editable="false" />
          </div> -->
        </div>

        <div class="mid-container">
          <div class="right-section">
            <p>기록 날짜 : {{ startTime }}</p>
            <p>
              소요 시간 : {{ convertTime(parseInt(duration)) }} | 총 거리 :
              {{ Math.round(distance * 10) / 10 }} km
            </p>
          </div>
        </div>

        <div class="comment-section">
          <p v-if="comment != '한줄평'">{{ comment }}</p>
        </div>
      </div>
    </div>
    <div v-else>현재 업데이트 된 기록이 없습니다</div>

    <div class="plan-title">
      일정 제목 : {{ clubstore.planDetail.responsePlanDetailDtoPlan.title }}
    </div>
    <div class="plan-start-time">
      시작 예정 시각 :
      {{
        new Date(clubstore.planDetail.responsePlanDetailDtoPlan.startTime).toLocaleTimeString(
          'ko-KR',
          {
            hour: '2-digit',
            minute: '2-digit'
          }
        )
      }}
    </div>
    <div class="plan-location">
      시작 모임 장소 : {{ clubstore.planDetail.responsePlanDetailDtoPlan.location }}
    </div>
    <div v-if="clubstore.planDetail.responsePlanDetailDtoPlan.content" class="plan-content">
      내용: {{ clubstore.planDetail.responsePlanDetailDtoPlan.content }}
    </div>
    <div class="plan-capacity">
      참여 인원 : {{ clubstore.planDetail.responsePlanDetailDtoPlan.nowCapacity }} /
      {{ clubstore.planDetail.responsePlanDetailDtoPlan.maxCapacity }}
    </div>

    <div v-if="isPlanMember" class="buttons-container">
      <button @click="clubstore.planClubMemberCancel">소모임 일정 참가 취소</button>
      <button @click="goOverwrite">기록 업데이트 하기</button>
      <button @click="clubstore.planCopy">내 기록으로 복사</button>
    </div>
    <div v-else class="buttons-container">
      <button @click="clubstore.planClubMemberApply">소모임 일정 참가</button>
    </div>

    <h2 class="members-title">참가원</h2>
    <div class="members-list">
      <div
        v-for="clubmember in clubstore.planDetail.responsePlanDetailDtoMembers"
        :key="clubmember.nickname"
        class="member-item"
      >
        <div class="member-container">
          <div class="circular-small">
            <img :src="clubmember.url" alt="" />
          </div>
          <div class="member-info">
            <div>
              {{ clubmember.nickname }} {{ clubmember.birth.substring(2, 4) }}년생
              {{ clubmember.gender == 'M' ? '남자' : '여자' }}
            </div>
            <div>
              {{ clubmember.address }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import { useCounterStore } from '@/stores/counter'
import { useWalkStore } from '@/stores/walk'
import { useRouter } from 'vue-router'

const clubstore = useClubStore()
const counterstore = useCounterStore()
const isPlanMember = ref(false)
const router = useRouter()

const walkStore = useWalkStore()

const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
let map = null // map is not defined Reference Error 방지
const uniqueId = ref(Date.now()) // 각 컴포넌트에 고유한 ID를 부여하기 위한 ref

const record = ref(null)
const title = ref(null)
const duration = ref(null)
const distance = ref(null)
const points = ref(null)
const starRating = ref(null)
const comment = ref(null)
const startTime = ref(null)

const { clubSeq, planSeq } = defineProps({
  clubSeq: String,
  planSeq: String
})

function goOverwrite() {
  router.push({ name: 'club-plan-overwrite', params: { clubSeq: clubSeq, planSeq: planSeq } })
}

onMounted(async function () {
  clubstore.planSeq = planSeq
  await clubstore.getPlanDetail()

  const nickname = counterstore.getCookie('nickname')

  // 멤버 배열을 돌면서 닉네임 검사
  isPlanMember.value = clubstore.planDetail.responsePlanDetailDtoMembers.some(
    (member) => member.nickname === nickname
  )

  if (clubstore.planDetail.responsePlanDetailDtoPlan.recordSeq != null) {
    await loadDetail(clubstore.planDetail.responsePlanDetailDtoPlan.recordSeq)

    if (window.kakao && window.kakao.maps) {
      initMap()
    } else {
      const script = document.createElement('script')
      script.onload = () => {
        /* global kakao*/
        kakao.maps.load(initMap)
      }
      script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&libraries=services&autoload=false`
      document.head.appendChild(script)
    }
  }
})

const loadDetail = async (seq) => {
  await walkStore.getRecordDetail(seq)
  record.value = walkStore.recordDetail

  console.log(record.value)

  title.value = record.value.title
  duration.value = record.value.duration
  distance.value = record.value.distance
  points.value = record.value.points
  starRating.value = record.value.starRating
  comment.value = record.value.comment
  startTime.value = record.value.startTime.substr(0, 10)
}

const initMap = () => {
  const container = document.getElementById(`map-${uniqueId.value}`)

  const mid = parseInt(points.value.length / 2)

  if (points.value && points.value.length > 0 && mid >= 0 && mid < points.value.length) {
    const options = {
      center: new kakao.maps.LatLng(points.value[mid].latitude, points.value[mid].longitude),
      level: 5,
      draggable: false
    }

    map = new kakao.maps.Map(container, options)

    ///// 시작, 끝 점 /////
    var startEndPositions = [
      {
        title: '산책 시작',
        img: 'start.png',
        latlng: new kakao.maps.LatLng(points.value[0].latitude, points.value[0].longitude)
      },
      {
        title: '산책 끝',
        img: 'end.png',
        latlng: new kakao.maps.LatLng(
          points.value[points.value.length - 1].latitude,
          points.value[points.value.length - 1].longitude
        )
      }
    ]

    for (var i = 0; i < startEndPositions.length; i++) {
      var imageSize = new kakao.maps.Size(20, 30)

      var markerImage = new kakao.maps.MarkerImage(
        import.meta.env.VITE_MARKER_IMAGE_ROUTE + startEndPositions[i].img,
        imageSize
      )

      var marker = new kakao.maps.Marker({
        map: map,
        position: startEndPositions[i].latlng,
        title: startEndPositions[i].title,
        image: markerImage
      })
    }

    ///// 마커찍기/////
    for (const point of points.value) {
      if (!point.url && !point.pointComment) {
        continue
      }

      const latlng = new kakao.maps.LatLng(point.latitude, point.longitude)

      const marker = new kakao.maps.Marker({
        map: map,
        position: latlng
      })

      let content = `<div class="wrap">` + `    <div class="info">` + `        <div class="body">`

      if (point.url && point.pointComment) {
        content +=
          `            <div class="img">` +
          `                <img src=${point.url} width="100" height="100">` +
          `            </div>` +
          `            <div class="desc">` +
          `                <div class="ellipsis">${point.pointComment}</div>` +
          `            </div>`
      } else if (point.url) {
        content +=
          `            <div class="img">` +
          `                <img src=${point.url} width="100" height="100">` +
          `            </div>`
      } else if (point.pointComment) {
        content +=
          `            <div class="desc">` +
          `                <div class="ellipsis">${point.pointComment}</div>` +
          `            </div>`
      }
      content += `        </div>` + `    </div>` + `</div>`

      const contentElement = document.createElement('div')
      contentElement.innerHTML = content

      const overlay = new kakao.maps.CustomOverlay({
        content: contentElement,
        map: map,
        position: marker.getPosition()
      })

      let isClose = true
      overlay.setMap(null)

      kakao.maps.event.addListener(marker, 'click', function () {
        if (isClose) {
          overlay.setMap(map)
          isClose = false
        } else {
          overlay.setMap(null)
          isClose = true
        }
      })
    }

    // 경로 폴리라인
    var polyline = new kakao.maps.Polyline({
      map: map,
      path: points.value.map((point) => new kakao.maps.LatLng(point.latitude, point.longitude)),
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
  }
}
function setDraggable(draggable) {
  map.setZoomable(draggable)
  map.setDraggable(draggable)
}
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
</script>

<style scoped>
.buttons-container {
  display: flex;
  justify-content: center; /* 가로 방향으로 중앙 정렬 */
  gap: 10px;
}

button {
  padding: 10px 20px; /* 버튼 내부 패딩 조정 */
  border: none;
  border-radius: 5px; /* 버튼 모서리 둥글게 */
  background-color: darkgreen; /* 버튼 배경 색상 */
  color: white; /* 버튼 텍스트 색상 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}

button:hover {
  background-color: yellowgreen; /* 마우스 오버 시 버튼 배경 색상 변경 */
}

.plan-title {
  font-size: 24px; /* 제목의 폰트 크기 */
  font-weight: bold; /* 글자 두께 */
  margin-bottom: 10px; /* 다음 요소와의 여백 */
}

.plan-start-time,
.plan-capacity,
.plan-location,
.plan-content {
  font-size: 18px; /* 정보의 폰트 크기 */
  margin-bottom: 8px; /* 다음 요소와의 여백 */
  padding-left: 10px; /* 텍스트의 왼쪽 패딩 */
  border-left: 3px solid darkgreen; /* 왼쪽에 강조선 추가 */
}

.plan-content {
  padding-bottom: 10px; /* 하단 패딩 */
  border-bottom: 2px solid darkgreen; /* 하단에 경계선 추가 */
}
</style>
