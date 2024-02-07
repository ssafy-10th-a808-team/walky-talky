<template>
    <ClubDetailHeaderNav />
    {{ seq }}
    {{ clubstore.clubDetail }}
    {{ clubstore.clubSeq }}
</template>
  
<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'
import axios from 'axios';
import { useCounterStore } from '@/stores/counter'

const clubstore = useClubStore()

const { seq } = defineProps({
    seq: String
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