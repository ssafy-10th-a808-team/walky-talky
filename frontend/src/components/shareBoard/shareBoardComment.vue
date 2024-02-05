<template>
  <div class="comment-container">
    <div class="member-container">
      <shareBoardMember :nickname="nickname" :profilePic="profilePic" />
    </div>
    <div class="comment-content-container">
      <h5>{{ content }}</h5>
      <p>{{ create_at }}</p>
    </div>
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'

const { nickname, profilePic, create_at, content } = defineProps([
  'nickname',
  'profilePic',
  'create_at',
  'content'
])
</script>

<style scoped>
.comment-container {
  display: flex;
  align-items: center; /* 세로 가운데 정렬 */

  .member-container {
    margin-right: 10px;
    flex-direction: column;
  }

  .comment-content-container {
    display: flex;
    flex-direction: row;
    justify-content: space-between; /* 양쪽 끝으로 정렬 */
  }
}
</style>
