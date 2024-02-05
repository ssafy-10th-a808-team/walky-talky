<template>
  <h2>소모임 정보</h2>
  <div>
    <div class="circular">
      <img v-if="clubDetail && clubDetail.url" :src="clubDetail.url" />
    </div>
    <p>Club Name : {{ clubstore.clubDetail.name }}</p>
    <p>Club Profile : {{ clubstore.clubDetail.url }}</p>
    <p v-if="clubDetail && clubDetail.name">Club 이름 : {{ clubDetail.name }}</p>
    <p v-if="clubDetail && clubDetail.introduce">Club 소개 : {{ clubDetail.introduce }}</p>
    <p v-if="clubDetail && clubDetail.oldBirth && clubDetail.youngBirth">
      {{ clubDetail.oldBirth }}년생 ~ {{ clubDetail.youngBirth }}년생
    </p>
    <p v-if="clubDetail && clubDetail.nowCapacity && clubDetail.maxCapacity">
      {{ clubDetail.nowCapacity }}/ {{ clubDetail.maxCapacity }} 명
    </p>
    <p>Club Detail : {{ clubDetail }}</p>
    <p>Club Member : {{ clubMember }}</p>
  </div>

  <h2>소모임원</h2>
  <div v-if="clubMembers" v-for="clubmember in clubMembers" :key="clubmember.nickname">
    <MemberList :member="clubmember" />
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'

const clubstore = useClubStore()

const { seq } = defineProps({
  seq: String
})

const clubDetail = ref()
const clubMembers = ref()

onMounted(async () => {
  await clubstore.findClub(seq)
  clubDetail.value = clubstore.clubDetail.responseClubDetailDtoClub
  clubMembers.value = clubstore.clubDetail.responseClubDetailDtoMembers
  console.log(clubstore.clubDetail.responseClubDetailDtoClub)
  console.log(clubstore.clubDetail.responseClubDetailDtoMembers)
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
