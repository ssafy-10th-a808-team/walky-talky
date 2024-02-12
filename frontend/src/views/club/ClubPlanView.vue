<template>
  <ClubDetailHeaderNav />

  <!-- {{ clubstore.planList }} -->

  <vue-cal
    @cell-click="handleDayClick"
    :events="formattedPlans"
    :available-views="['month']"
    :disable-views="['years', 'year', 'day', 'week']"
    active-view="month"
    :hide-view-selector="true"
  />

  <div v-if="selectedDate" style="font-size: 24px">
    {{ selectedDate }}
  </div>

  <div v-if="selectedDayPlans.length">
    <!-- 여기에 선택된 날짜에 해당하는 일정 목록을 렌더링합니다. -->
    <ul>
      <li v-for="plan in selectedDayPlans" :key="plan.seq" class="plan-item">
        <div class="plan-title">{{ plan.title }}</div>
        <div class="plan-time">
          시작 시간:
          {{
            new Date(plan.startTime).toLocaleTimeString('ko-KR', {
              hour: '2-digit',
              minute: '2-digit'
            })
          }}
        </div>
        <div class="plan-capacity">참여 인원: {{ plan.nowCapacity }} / {{ plan.maxCapacity }}</div>
        <div class="plan-location">위치: {{ plan.location }}</div>
        <button @click="goToDetail(plan.seq)" class="detail-button">상세보기</button>
      </li>
    </ul>
  </div>
  <div v-else>해당 날짜는 일정이 없습니다</div>

  <!-- 소모임 만들기 버튼 -->
  <div class="fixed-button-container">
    <RouterLink :to="{ name: 'club-plan-regist', params: { seq: clubstore.clubSeq } }">
      <button>+ 일정 생성</button>
    </RouterLink>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import { useRouter } from 'vue-router'

import VueCal from 'vue-cal'
import '@/../node_modules/vue-cal/dist/vuecal.css'

const clubstore = useClubStore()
const router = useRouter()

const formattedPlans = ref([])
const selectedDayPlans = ref([]) // 선택된 날짜의 일정 목록을 위한 반응형 변수
const selectedDate = ref()

const { seq } = defineProps({
  seq: String
})

onMounted(async function () {
  await clubstore.getPlanList()
  // 추가
  formatPlans()

  selectedDate.value = new Date().toLocaleString('ko-KR', {
    month: 'short', // 'long'으로 변경하면 전체 월 이름이 출력됩니다.
    day: '2-digit'
  })
  handleDayClick(new Date())
})

function formatPlans() {
  formattedPlans.value = clubstore.planList.plans.map((plan) => {
    return {
      start: new Date(plan.startTime),
      end: new Date(new Date(plan.startTime).getTime() + plan.duration * 60000), // 가정: duration이 분 단위
      title: plan.title,
      class: plan.seq === new Date().getDate() ? 'today' : ''
    }
  })
}

const handleDayClick = function (date) {
  // Date 객체 생성
  selectedDate.value = new Date(date).toLocaleString('ko-KR', {
    month: 'short', // 'long'으로 변경하면 전체 월 이름이 출력됩니다.
    day: '2-digit'
  })

  // 해당 날짜에 맞는 일정 목록을 필터링하여 selectedDayPlans에 저장합니다.
  selectedDayPlans.value = clubstore.planList.plans.filter(
    (plan) => new Date(plan.startTime).toDateString() === date.toDateString()
  )
}

const goToDetail = (seq) => {
  router.push({ name: 'club-plan-detail', params: { clubSeq: clubstore.clubSeq, planSeq: seq } })
}
</script>
<style scoped>
.plan-item {
  /* 여기에 스타일을 정의하세요 */
  background-color: #f5f5f5;
  border-radius: 15px;
  padding: 20px;
  margin-bottom: 10px;
}

.plan-title {
  /* 여기에 제목 스타일을 정의하세요 */
  font-size: 20px;
  font-weight: bold;
}

.plan-time,
.plan-capacity,
.plan-location {
  /* 여기에 시간, 참여 인원, 위치에 대한 스타일을 정의하세요 */
  font-size: 16px;
  margin: 5px 0;
}

/* 상세보기 버튼 스타일 */
.detail-button {
  background-color: #3498db; /* 파란색 배경 */
  color: white; /* 흰색 텍스트 */
  border: none; /* 테두리 제거 */
  padding: 10px 15px; /* 패딩 설정 */
  border-radius: 5px; /* 모서리 둥글게 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
  transition: background-color 0.3s; /* 배경 색 변경 시 애니메이션 효과 */
}

/* 상세보기 버튼 호버 스타일 */
.detail-button:hover {
  background-color: #2980b9; /* 호버 시 더 어두운 파란색 배경 */
}

.fixed-button-container {
  position: fixed;
  /* 뷰포트에 대해 고정된 위치 */
  bottom: 20px;
  /* 아래쪽에서 20px 떨어진 위치 */
  left: 20px;
  /* 오른쪽에서 20px 떨어진 위치 */
  z-index: 1000;
  /* 다른 요소들 위에 표시되도록 z-index 설정 */
  box-shadow:
    0 4px 6px rgba(50, 50, 93, 0.11),
    0 1px 3px rgba(0, 0, 0, 0.08);
  /* 버튼에 그림자를 추가합니다 */
}

.fixed-button-container button {
  /* 반응형 디자인을 위해 패딩을 vw 단위로 설정할 수 있습니다 */
  padding: 1.25vw;
  /* 뷰포트 너비의 1% */

  background-color: darkgreen;
  /* 버튼 배경색 */
  color: white;
  /* 버튼 글자색 */
  border-radius: 5vw;
  /* 버튼의 border-radius도 뷰포트에 따라 조절합니다 */
  /* 원형 버튼을 원하는 경우 */
  cursor: pointer;
  /* 마우스 오버시 커서 변경 */

  font-size: 2.5vw;
  /* 글자 크기도 뷰포트 너비에 따라 조절합니다 */

  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
  /* 버튼에 그림자 효과 추가 */
  /* 버튼에 대한 추가 스타일링 */

  min-width: 150px;
  /* 버튼의 최소 너비를 설정합니다. */
  min-height: 50px;
  /* 버튼의 최소 높이를 설정합니다. */
}

/* 버튼 호버 효과를 더 눈에 띄게 합니다 */
.fixed-button-container button:hover {
  background-color: #34c759;
  transform: translateY(-3px);
  /* 버튼이 약간 위로 올라가도록 합니다 */
  box-shadow:
    0 7px 14px rgba(50, 50, 93, 0.1),
    0 3px 6px rgba(0, 0, 0, 0.08);
  /* 그림자를 강조합니다 */
}

/* 작은 화면 (모바일) 대응을 위한 미디어 쿼리 */
@media (max-width: 600px) {
  .fixed-button-container button {
    padding: 3vw;
    /* 모바일에서 버튼의 패딩을 늘립니다. */
    font-size: 5vw;
    /* 모바일에서 버튼의 글자 크기를 늘립니다. */
    min-width: 120px;
    /* 모바일에서 버튼의 최소 너비를 조정합니다. */
    min-height: 40px;
    /* 모바일에서 버튼의 최소 높이를 조정합니다. */
  }
}

/* 중간 크기의 화면 (태블릿) 대응을 위한 미디어 쿼리 */
@media (min-width: 601px) and (max-width: 1024px) {
  .fixed-button-container button {
    padding: 2vw;
    /* 태블릿에서 버튼의 패딩을 조금 줄입니다. */
    font-size: 4vw;
    /* 태블릿에서 버튼의 글자 크기를 조금 줄입니다. */
    min-width: 140px;
    /* 태블릿에서 버튼의 최소 너비를 조정합니다. */
    min-height: 45px;
    /* 태블릿에서 버튼의 최소 높이를 조정합니다. */
  }
}

/* 큰 화면 (데스크탑) 대응을 위한 미디어 쿼리 */
@media (min-width: 1025px) {
  .fixed-button-container button {
    padding: 20px;
    /* 데스크탑에서 고정된 패딩을 사용합니다. */
    font-size: 18px;
    /* 데스크탑에서 고정된 글자 크기를 사용합니다. */
    min-width: 160px;
    /* 데스크탑에서 버튼의 최소 너비를 설정합니다. */
    min-height: 50px;
    /* 데스크탑에서 버튼의 최소 높이를 설정합니다. */
  }
}
</style>
