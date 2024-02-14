<template>
  <div class="title-container">
    <h2>{{ title }}</h2>
    <div class="comment-container">
      <img src="@/assets/img/comment.png" width="20px" height="20px" />
      <p>{{ commentCount }}</p>
    </div>
  </div>
</template>

<script setup>
const { title, commentCount } = defineProps(['title', 'commentCount'])
</script>

<style scoped>
.title-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 10px;
}

.title-container h2 {
  margin-right: 10px;
}

.comment-container {
  display: flex;
  /* align-items: center; */
}

.comment-container img {
  margin-right: 5px; /* 간격 조절 */
}
</style>
