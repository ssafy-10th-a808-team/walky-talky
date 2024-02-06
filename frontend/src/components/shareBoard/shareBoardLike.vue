<template>
  <div v-if="loading">
    <p>로딩중</p>
  </div>
  <div v-else class="containerLike">
    <img v-if="liked" src="@/assets/img/liked.png" alt="Liked" width="20px" height="20px" />
    <img v-else src="@/assets/img/not_liked.png" alt="Not Liked" width="20px" height="20px" />
    <p>{{ likeCount }}</p>
  </div>
</template>

<script setup>
const { likeCount, liked, loading } = defineProps(['likeCount', 'liked', 'loading'])
</script>

<style>
.containerLike {
  display: flex;
}

img {
  margin-right: 5px; /* 이미지 간 간격을 설정할 수 있습니다. */
}
</style>
