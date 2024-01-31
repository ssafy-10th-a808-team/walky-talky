<template>
    <div class="portfolio-info">
        <div class="circular">
            <img :src="club.url" alt="프로필"><h3>{{ club.name }}</h3>
        </div>
        <ul>
        <li><div>{{ club.address }} {{ club.genderType }}</div></li>
        <li>{{ club.introduce }}</li>
        <li>{{ club.oldBirth }}부터 {{ club.youngBirth }}까지</li>
        <li>{{ club.nowCapacity }} / {{ club.maxCapacity }}명</li>
        </ul>
    </div>
</template>

<script setup>
    const { club } = defineProps({
        club: Object,
    })
</script>

<style scoped>
.circular {
  width: 200px; /* 원하는 크기로 조정 */
  height: 200px;
  border-radius: 50%; /* 50%로 설정하여 원 모양으로 만듭니다 */
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}
.circular img {
  width: 100%; /* 부모 요소(.circular-image)에 대한 상대적인 크기로 설정 */
  height: 100%; /* 가로 세로 비율을 유지하도록 설정 */
  object-fit: cover; /* 이미지 간격 제거를 위해 인라인 요소 대신 블록 요소로 설정 */
  border-radius: 50%;
}
</style>