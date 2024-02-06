<template>
  <div class="member-container">
    <img :src="profilePic" class="rounded-image" width="50" height="50" />
    <p>{{ nickname }}</p>
  </div>
</template>

<script setup>
const { nickname, profilePic } = defineProps(['nickname', 'profilePic'])
</script>

<style scoped>
.rounded-image {
  border-radius: 50%;
  margin-right: 10px;
}
.member-container {
  display: flex;
  align-items: center;
  margin: 5px;
}
</style>
