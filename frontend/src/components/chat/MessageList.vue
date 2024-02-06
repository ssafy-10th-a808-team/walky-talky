<template>
  <div ref="messageListElement" class="message-list">
    <Message
      v-for="(message, index) in dummyMessages"
      :key="index"
      :sender="message.sender"
      :content="message.content"
      :timestamp="message.timestamp"
      :isMine="message.isMine"
    />
  </div>
</template>

<script setup>
import { onMounted, onUnmounted, ref } from 'vue'
import Message from './Message.vue'
import { useChatStore } from '@/stores/chat'

const chatSeq = ref('your_chat_seq') // 실제 채팅방 시퀀스 번호로 설정
const offset = ref(0)
const messageListElement = ref(null)

const chatStore = useChatStore()

onMounted(() => {
  // 메시지 리스트 엘리먼트에 스크롤 이벤트 리스너 추가
  messageListElement.value.addEventListener('scroll', handleScroll)
  chatStore.getConnection(chatSeq.value)
})

onUnmounted(() => {
  // 메시지 리스트 엘리먼트에서 스크롤 이벤트 리스너 제거
  messageListElement.value.removeEventListener('scroll', handleScroll)
})

function handleScroll() {
  // 스크롤이 상단에 도달했을 때 메시지를 더 불러옵니다.
  if (messageListElement.value.scrollTop === 0) {
    offset.value += 1 // 다음 페이지의 메시지를 불러옵니다. (예시는 페이지 단위로 처리)
    chatStore.loadMessage(chatSeq.value, offset.value)
  }
}

const dummyMessages = ref([
  {
    sender: 'Alice',
    content: '안녕, 어떻게 지내?',
    timestamp: '10:45 AM',
    isMine: false
  },
  {
    sender: 'You',
    content: '잘 지내, 너는 어때?',
    timestamp: '10:46 AM',
    isMine: true
  },
  {
    sender: 'Alice',
    content: '안녕, 어떻게 지내?',
    timestamp: '10:45 AM',
    isMine: false
  },
  {
    sender: 'You',
    content: '잘 지내, 너는 어때?',
    timestamp: '10:46 AM',
    isMine: true
  },
  {
    sender: 'Alice',
    content: '안녕, 어떻게 지내?',
    timestamp: '10:45 AM',
    isMine: false
  },
  {
    sender: 'You',
    content: '잘 지내, 너는 어때?',
    timestamp: '10:46 AM',
    isMine: true
  }
  // ... 여기에 더 많은 더미 메시지를 추가할 수 있습니다.
])
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
  flex-direction: column-reverse;
  overflow-y: auto; /* 스크롤 가능하게 만듭니다 */
  height: 100%; /* 컨테이너의 높이를 정합니다, 실제 값으로 조정 필요 */
}

/* 상대방이 보낸 메시지 */
.message:not(.mine) {
  margin-right: auto; /* 왼쪽 정렬 */
}

/* 내가 보낸 메시지 */
.mine {
  margin-left: auto; /* 오른쪽 정렬 */
}
</style>
