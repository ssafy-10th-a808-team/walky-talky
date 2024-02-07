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
    </div>
</template>


<script setup>
defineProps(['member'])
</script>

<style scoped>
.member-container {
    display: flex;
    align-items: center;
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
</style>