<template>
  <div ref="messageListElement" class="message-list">
    <Message
      v-for="(message, index) in messages"
      :key="index"
      :sender="message.sender"
      :content="message.content"
      :createdAt="message.createdAt"
      :isMine="message.sender === counterstore.getCookie('nickname')"
    />
  </div>
</template>

<script setup>
import { defineProps, ref, onMounted, onUnmounted, watch, nextTick } from 'vue'
import { useChatStore } from '@/stores/chat'
import { useCounterStore } from '@/stores/counter'
import Message from './Message.vue'

const messageListElement = ref(null)
const chatStore = useChatStore()
const counterstore = useCounterStore()
const offset = ref(0) // offset 상태 추가
// 현재 스크롤 높이를 추적하기 위한 ref를 추가
const oldScrollHeight = ref(0)

const props = defineProps({
  messages: Array,
  clubSeq: Number
})

// 메시지 목록의 변경을 감지하여 필요한 UI 업데이트를 수행
const stopWatch = watch(
  () => props.messages,
  async (newMessages, oldMessages) => {
    await nextTick()
    // 새로운 메시지 발생
    if (
      newMessages.length - oldMessages.length === 1 &&
      newMessages[newMessages.length - 1] !== oldMessages[oldMessages.length - 1]
    ) {
      messageListElement.value.scrollTop = messageListElement.value.scrollHeight
    } else {
      const newScrollHeight = messageListElement.value.scrollHeight
      const scrollDiff = newScrollHeight - oldScrollHeight.value
      messageListElement.value.scrollTop += scrollDiff
    }
    // 현재 스크롤 높이를 업데이트
    oldScrollHeight.value = messageListElement.value.scrollHeight
  },
  { deep: true }
)

// 스크롤 이벤트 핸들러
const handleScroll = async () => {
  if (messageListElement.value.scrollTop === 0) {
    // 메시지를 추가로 불러오기 전에 offset 업데이트
    offset.value += 1
    await chatStore.loadMessage(props.clubSeq, offset.value)
  }
}

onMounted(() => {
  messageListElement.value.addEventListener('scroll', handleScroll)
})

onUnmounted(() => {
  if (messageListElement.value) {
    messageListElement.value.removeEventListener('scroll', handleScroll)
  }
  stopWatch()
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
  height: 500px; /* 컨테이너의 높이를 정합니다 */
}
</style>
