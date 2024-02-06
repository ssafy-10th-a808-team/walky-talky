<template>
  <ClubDetailHeaderNav />
  <h2>소모임 정보</h2>
  <div v-if="clubDetail.responseClubDetailDtoClub">
    <div class="circular">
      <img :src="clubDetail.responseClubDetailDtoClub.url" />
    </div>

    <p>
      {{ clubDetail.responseClubDetailDtoClub.name }}
    </p>

    <p>
      {{ clubstore.clubDetail.responseClubDetailDtoClub.introduce }}
    </p>

    <p>
      {{ clubstore.clubDetail.responseClubDetailDtoClub.oldBirth }}년생 ~ {{
        clubstore.clubDetail.responseClubDetailDtoClub.youngBirth }}년생
    </p>
    <div>
      <p v-if="clubstore.clubDetail.responseClubDetailDtoClub.genderType === 'A'">
        남녀무관
      </p>
      <p v-else-if="clubstore.clubDetail.responseClubDetailDtoClub.genderType === 'M'">
        남자만
      </p>
      <p v-else>
        여자만
      </p>
    </div>
    <p>
      {{ clubstore.clubDetail.responseClubDetailDtoClub.nowCapacity }} / {{
        clubstore.clubDetail.responseClubDetailDtoClub.maxCapacity }} 명
    </p>
    <div>
      <p v-if="clubstore.clubDetail.responseClubDetailDtoClub.autoRecruit">
        즉시 가입
      </p>
      <p v-else>
        승인 후 가입
      </p>
    </div>
    <div>
      <p v-if="clubstore.clubDetail.responseClubDetailDtoClub.openRecruit">
        모집 열려 있음
      </p>
      <p v-else>
        모집 닫혀 있음
      </p>
    </div>
  </div>
  <div v-if="clubDetail.responseClubDetailDtoClub">
    <h2>소모임원</h2>
    <div v-for="clubmember in clubstore.clubDetail.responseClubDetailDtoMembers" :key="clubmember.nickname">
      <MemberList :member="clubmember" />
    </div>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'
import axios from 'axios';
import { useCounterStore } from '@/stores/counter'

const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api/club'

const counterstore = useCounterStore()
const clubstore = useClubStore()
const clubDetail = ref({})

const { seq } = defineProps({
  seq: String
})

const findClub = (seq) => {
  axios({
    method: 'get',
    url: `${REST_CLUB_API}/detail?clubSeq=${seq}`,
    headers: {
      Authorization: `Bearer ${counterstore.getCookie('atk')}`
    }
  }).then(res => {
    clubDetail.value = res.data // 응답 데이터로 상태 업데이트
  }).catch(err => {
    console.error(err)
    alert('소모임 정보를 가져오는데 실패했습니다.')
  });
}

onMounted(() => {
  findClub(seq); // 컴포넌트가 마운트될 때 소모임 데이터를 가져옵니다.
})

</script>

<style scoped>
.circular {
  width: 200px;
  /* 고정 가로 크기 */
  height: 200px;
  /* 고정 세로 크기 */
  border-radius: 50%;
  /* 원 모양으로 만듦 */
  overflow: hidden;
  display: flex;
  align-items: center;
  /* 이미지를 중앙에 정렬 */
  justify-content: center;
}

.circular img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 이미지 비율 유지하며 컨테이너 채움 */
  border-radius: 50%;
}
</style>