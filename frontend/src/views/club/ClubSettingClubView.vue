<template>
    <ClubDetailHeaderNav />
    <ClubSettingHeaderNav />
    <button class="delete-club-button" @click="deleteClub()">소모임 삭제</button>
</template>
  
<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'
import axios from 'axios';
import { useCounterStore } from '@/stores/counter'
import ClubSettingHeaderNav from '@/views/club/ClubSettingHeaderNav.vue'
import { useRouter } from 'vue-router'

const clubstore = useClubStore()
const router = useRouter()

const { seq } = defineProps({
    seq: String
})

async function deleteClub() {
    await clubstore.delete()
    router.push({ name: 'club' })
}

</script>
  
<style scoped>
/* 기존 스타일... */

/* 소모임 삭제 버튼 스타일 */
.delete-club-button {
    padding: 10px 20px;
    background-color: #e74c3c;
    /* 빨간색 배경 */
    color: white;
    /* 흰색 텍스트 */
    border: none;
    /* 테두리 없음 */
    border-radius: 5px;
    /* 약간 둥근 모서리 */
    cursor: pointer;
    /* 클릭 가능한 핸드 아이콘 */
    font-size: 16px;
    /* 글꼴 크기 */
    margin-top: 20px;
    /* 상단 마진 */
    transition: background-color 0.3s ease;
    /* 부드러운 색상 전환 효과 */
}

.delete-club-button:hover {
    background-color: #c0392b;
    /* 버튼을 호버할 때 더 어두운 색상으로 변경 */
}
</style>