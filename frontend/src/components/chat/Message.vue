<template>
  <div class="message">
    <div class="sender">{{ sender }}</div>
    <div class="content">{{ content }}</div>
    <div class="createdAt">{{ createdAt }}</div>
  </div>
</template>

<script setup>
defineProps({
  sender: String,
  content: String,
  createdAt: String
})
</script>

<style scoped>
/* 기본 메시지 스타일 */
.message {
  display: flex;
  flex-direction: column;
  max-width: 80%;
  padding: 12px 16px;
  margin: 8px 20px;
  border-radius: 18px;
  align-self: flex-end; /* 메시지를 오른쪽으로 정렬 */
  background-color: #00bfa5; /* 채팅 버블의 배경색 */
  color: white; /* 텍스트 색상 */
  font-size: 16px; /* 텍스트 크기 */
  line-height: 1.5; /* 줄 간격 */
  font-family: 'Helvetica Neue', Helvetica, Arial, sans-serif; /* 폰트 */
}

/* 메시지 내용 */
.content {
  white-space: pre-wrap; /* 줄바꿈과 공백을 유지 */
  word-wrap: break-word; /* 긴 단어가 있는 경우 줄바꿈 */
  color: black;
}

/* 메시지 보낸 시간 */
.createdAt {
  color: purple;
  align-self: flex-end; /* 시간을 메시지의 오른쪽 끝으로 정렬 */
  font-size: 12px; /* 시간 텍스트 크기 */
  margin-top: 8px; /* 메시지와 시간 사이의 여백 */
}
</style>
