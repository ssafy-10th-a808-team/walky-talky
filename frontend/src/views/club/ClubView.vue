<template>
  <div class="section-title">
    <h2>내 소모임들</h2>
    <div class="scroll-container">
      <ClubList
        v-for="(club, index) in clubs.myClubs"
        :key="club.seq"
        :index="index"
        :club="club"
        :templateType="1"
        @click="godetail(club.seq)"
        class="club-item"
      />
    </div>
  </div>
  <div class="section-title">
    <h2>소모임 추천</h2>
    <div class="scroll-container">
      <ClubList
        v-for="(club, index) in clubs.recommendClubs"
        :key="club.seq"
        :index="index"
        :club="club"
        :templateType="2"
        @click="godetail(club.seq)"
        class="club-item"
      />
    </div>
  </div>
  <div class="section-title">
    <h2>다른 소모임들</h2>
    <div class="scroll-container">
      <ClubList
        v-for="(club, index) in clubs.otherClubs"
        :key="club.seq"
        :index="index"
        :club="club"
        :templateType="3"
        @click="godetail(club.seq)"
        class="club-item"
      />
    </div>
  </div>

  <!-- 소모임 만들기 버튼 -->
  <div class="fixed-button-container">
    <RouterLink :to="{ name: 'club-create' }">
      <button>+ 소모임 생성</button>
    </RouterLink>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import ClubList from '@/components/club/ClubList.vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
import { useCounterStore } from '@/stores/counter'

const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api/club'

const counterstore = useCounterStore()
const clubstore = useClubStore()
const router = useRouter()

const clubs = ref([])

// 소모임 데이터를 가져오는 함수
const getClubs = () => {
  axios({
    method: 'get',
    url: `${REST_CLUB_API}/list`, // REST_CLUB_API는 해당 API 엔드포인트를 가리킵니다.
    headers: {
      Authorization: `Bearer ${counterstore.getCookie('atk')}`
    }
  })
    .then((res) => {
      clubs.value = res.data // 응답 데이터를 clubs에 할당
      console.log(res)
    })
    .catch((err) => {
      console.error(err)
      alert('소모임들 정보를 가져오는데 실패했습니다.')
    })
}

onMounted(() => {
  getClubs() // 컴포넌트가 마운트될 때 소모임 데이터를 가져옵니다.
})

const godetail = (seq) => {
  clubstore.clubSeq = seq
  router.push({ name: 'club-detail', params: { seq: seq } })
}
</script>

<style scoped>
.section-title h2 {
  color: white;
  /* 제목 색상을 테마에 맞춥니다 */
  font-size: 1.5rem;
  /* 제목 글꼴 크기를 조정합니다 */
  padding: 0.5rem 0;
  /* 상하 패딩을 추가합니다 */
  margin: 0;
  /* 기본 마진을 제거합니다 */
  text-align: center;
  /* 제목을 중앙 정렬합니다 */
  background-color: darkgreen;
  /* 제목 배경 색상을 추가합니다 */
  border-radius: 10px;
  /* 배경의 모서리를 둥글게 합니다 */
  box-shadow:
    0 4px 6px rgba(50, 50, 93, 0.11),
    0 1px 3px rgba(0, 0, 0, 0.08);
  /* 그림자를 추가합니다 */
}

.scroll-container {
  display: flex;
  overflow-x: auto;
  gap: 10px;
  padding: 1rem 0;
  /* 스크롤 컨테이너에 상하 패딩을 추가합니다 */
}

.club-item {
  flex: 0 0 auto;
  /* 기존 스타일 유지 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
  /* 카드에 그림자 효과를 추가합니다 */
  border-radius: 10px;
  /* 카드의 모서리를 둥글게 합니다 */
  overflow: hidden;
  /* 이미지가 모서리를 넘어서지 않도록 합니다 */
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

/* 스크롤바 스타일 */
.scroll-container::-webkit-scrollbar {
  height: 4px;
}

.scroll-container::-webkit-scrollbar-thumb {
  background: darkgreen;
  /* 스크롤바 색상을 테마에 맞춥니다 */
  border-radius: 10px;
}

.scroll-container::-webkit-scrollbar-track {
  background: #e0f2f1;
  border-radius: 10px;
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
