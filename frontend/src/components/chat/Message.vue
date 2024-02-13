<template>
  <div :class="['message', { mine: isMine }, type]">
    <div v-if="type === 'TALK'" class="sender">{{ sender }}</div>
    <div class="content">{{ content }}</div>
    <div v-if="type === 'TALK'" class="createdAt">{{ formatDate(createdAt) }}</div>
  </div>
</template>

<script setup>
defineProps({
  sender: String,
  content: String,
  createdAt: String,
  isMine: Boolean, // 현재 메시지가 사용자 자신에 의해 보내졌는지 여부
  type: String
})

function formatDate(dateString) {
  const date = new Date(dateString)
  const year = date.getFullYear()
  const month = `0${date.getMonth() + 1}`.slice(-2) // 월은 0부터 시작하므로 1을 더해줍니다.
  const day = `0${date.getDate()}`.slice(-2)
  let hours = date.getHours()
  const minutes = `0${date.getMinutes()}`.slice(-2)

  // AM 또는 PM 설정
  const ampm = hours >= 12 ? '오후' : '오전'

  // 24시간 형식 적용
  hours = `0${hours}`.slice(-2)

  return `${year}-${month}-${day} ${hours}:${minutes} ${ampm}`
}
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
  text-align: right;
}

.mine {
  align-self: flex-end;
  background-color: #5cb874;
}

/* 다른 사람이 보낸 메시지의 스타일 */
:not(.mine) {
  align-self: flex-start;
}

.JOIN,
.LEAVE {
  align-self: center; /* 시스템 메시지를 중앙에 정렬 */
  background-color: transparent; /* 배경색 없음 */
  color: #757575; /* 시스템 메시지 색상 */
  font-style: italic; /* 기울임꼴로 표시 */
}
</style>
