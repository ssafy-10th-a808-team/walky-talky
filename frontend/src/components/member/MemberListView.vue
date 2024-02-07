<template>
    <div class="member-container">
        <div class="circular-small">
            <img :src="member.url" alt="">
            <p class="owner-badge" v-if="member.role === 'owner'">OWNER</p>
        </div>
        <div class="member-info">
            <div>{{ member.nickname }}</div>
            <div>{{ member.address }}</div>
        </div>
        <!-- 추방하기 버튼 추가 -->
        <!-- <button v-if="member.role === 'member'" @click="clubstore.exclude(member)">추방하기</button> -->
    </div>
</template>


<script setup>
import { useClubStore } from '@/stores/club'

const clubstore = useClubStore()

defineProps(['member'])



</script>

<style scoped>
.member-container {
    display: flex;
    align-items: center;
    /* justify-content: space-between; */
    /* 컨테이너 내 아이템들 사이의 공간을 균등하게 설정 */
}

.circular-small {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 16px;
    position: relative;
    /* 'OWNER' 텍스트를 위해 상대 위치 설정 */
}

.circular-small img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.owner-badge {
    position: absolute;
    /* 절대 위치 설정 */
    bottom: -20px;
    /* 하단에서부터의 위치, 텍스트 높이에 따라 조정 */
    left: 50%;
    /* 왼쪽에서부터 50% 위치에 배치 */
    transform: translateX(-50%);
    /* X축 기준 중앙 정렬 */
    background-color: rgba(0, 0, 0, 0.5);
    /* 배경색 추가 */
    color: greenyellow;
    /* 글자색 */
    padding: 2px 10px;
    /* 패딩 */
    border-radius: 10px;
    /* 둥근 모서리 */
    font-size: 12px;
    /* 글자 크기 */

}

/* 추방하기 버튼 스타일 */
button {
    padding: 5px 15px;
    background-color: #e74c3c;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-left: auto;
    /* 왼쪽에 자동 마진을 추가하여 오른쪽 정렬 */
}

button:hover {
    background-color: #c0392b;
}
</style>