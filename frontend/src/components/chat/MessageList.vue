<template>
  <div ref="messageListElement" class="message-list">
    <Message
      v-for="(message, index) in messages"
      :key="index"
      :sender="message.sender"
      :content="message.content"
      :createdAt="message.createdAt"
    />
  </div>
</template>

<script setup>
import { defineProps } from 'vue'
import Message from './Message.vue'

const props = defineProps({
  messages: Array
})
</script>

<style scoped>
.message {
  padding: 10px;
  border-radius: 5px;
  margin-bottom: 10px;
  max-width: 80%;
}

.message-list {
  display: flex;
  flex-direction: column;
  overflow-y: auto; /* 스크롤 가능하게 만듭니다 */
  height: 500px; /* 컨테이너의 높이를 정합니다, 실제 값으로 조정 필요 */
}
</style>
