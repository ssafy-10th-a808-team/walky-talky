<template>
    <div class="section-title">
        <h2>내 소모임들</h2>
        <div class="scroll-container">
            <ClubList v-for="(club, index) in clubs.myClubs" :key="club.seq" :index="index" :club="club" :templateType=1
                @click="godetail(club.seq)" class="club-item" />
        </div>
    </div>
    <div class="section-title">
        <h2>소모임 추천</h2>
        <div class="scroll-container">
            <ClubList v-for="(club, index) in clubs.recommendClubs" :key="club.seq" :index="index" :club="club"
                :templateType=2 @click="godetail(club.seq)" class="club-item" />
        </div>
    </div>
    <div class="section-title">
        <h2>다른 소모임들</h2>
        <div class="scroll-container">
            <ClubList v-for="(club, index) in clubs.otherClubs" :key="club.seq" :index="index" :club="club" :templateType=3
                @click="godetail(club.seq)" class="club-item" />
        </div>
    </div>

    <!-- 소모임 만들기 버튼 -->
    <div class="fixed-button-container">
        <RouterLink :to="{ name: 'club-create' }">
            <button> + 소모임 생성 </button>
        </RouterLink>
    </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useClubStore } from "@/stores/club";
import ClubList from "@/components/club/ClubList.vue"
import { useRouter } from 'vue-router';
import axios from 'axios';
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
        .then(res => {
            clubs.value = res.data; // 응답 데이터를 clubs에 할당
            console.log(res);
        })
        .catch(err => {
            console.error(err);
            alert('소모임 정보를 가져오는데 실패했습니다.');
        });
}

onMounted(() => {
    getClubs(); // 컴포넌트가 마운트될 때 소모임 데이터를 가져옵니다.
})

const godetail = (seq) => {
    router.push({ name: 'club-detail', params: { seq: seq } })
}

</script>

<style scoped>
.scroll-container {
    display: flex;
    overflow-x: auto;
    gap: 10px;
    /* 카드 사이의 간격 조정 */
}

.club-item {
    flex: 0 0 auto;
    /* 아이템이 스크롤 컨테이너의 크기에 맞춰서 줄어들지 않도록 설정 */
}

.fixed-button-container {
    position: fixed;
    /* 뷰포트에 대해 고정된 위치 */
    bottom: 20px;
    /* 아래쪽에서 20px 떨어진 위치 */
    right: 20px;
    /* 오른쪽에서 20px 떨어진 위치 */
    z-index: 1000;
    /* 다른 요소들 위에 표시되도록 z-index 설정 */
}

.fixed-button-container button {
    padding: 10px 20px;
    background-color: #34c759;
    /* 버튼 배경색 */
    color: white;
    /* 버튼 글자색 */
    border: none;
    border-radius: 50px;
    /* 원형 버튼을 원하는 경우 */
    cursor: pointer;
    /* 마우스 오버시 커서 변경 */
    /* font-size: 16px;
     */
    font-size: 40px;
    /* 글자 크기 */
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.2);
    /* 버튼에 그림자 효과 추가 */
    /* 버튼에 대한 추가 스타일링 */
}

.fixed-button-container button:hover {
    background-color: #2ca94a;
    /* 호버 시 버튼 배경색 변경 */
}
</style>