<template>
  <div class="chat-window">
    <div class="message-list" ref="messageListElement">
      <MessageList :messages="chatStore.messages" />
    </div>
    <div class="input-area">
      <input
        type="text"
        v-model="newMessage"
        @keyup.enter="sendMessage"
        placeholder="메시지를 입력하세요..."
      />
      <button @click="sendMessage" class="send-button">전송</button>
    </div>
  </div>
</template>

<script setup>
import { useRoute } from 'vue-router'
import { ref, onMounted, nextTick } from 'vue'
import { useChatStore } from '@/stores/chat'
import MessageList from '@/components/chat/MessageList.vue'

const newMessage = ref('')
const chatSeq = ref(useRoute().params.seq) // 실제 채팅방 시퀀스 번호로 설정
const chatStore = useChatStore()
const messageListElement = ref(null) // ref를 사용하여 DOM 요소 참조 정의

onMounted(async () => {
  await chatStore.loadMessage(chatSeq.value, 0)
  chatStore.getConnection(chatSeq.value)

  nextTick(() => {
    if (messageListElement) {
      messageListElement.value.scrollTop = messageListElement.value.scrollHeight
    }
  })
})

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    chatStore.sendMessage({
      chatSeq: chatSeq.value,
      sender: 'You',
      content: newMessage.value,
      timestamp: new Date().toISOString(),
      isMine: true
    })
    // 입력 필드를 비웁니다
    newMessage.value = ''
  }
}
</script>

<style>
.chat-window {
  display: flex;
  flex-direction: column;
  max-width: 100%;
  height: 500px;
}

.input-area {
  display: flex;
  margin-top: auto;
}

input[type='text'] {
  flex: 1;
  padding: 10px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 20px;
  margin-right: 8px;
}

.send-button {
  padding: 10px 20px;
  background-color: #ff79c6; /* 귀여운 핑크색 배경 */
  color: white;
  border: none;
  border-radius: 20px;
  cursor: pointer;
  font-size: 16px;
  transition: background-color 0.3s ease;
}

.send-button:hover {
  background-color: #ff61a6; /* 버튼 호버 시 색상 변경 */
}

/* 화면 크기에 따른 반응형 디자인 */
@media (max-width: 768px) {
  .input-area {
    /* flex-direction: column; 이 부분을 주석 처리하거나 제거합니다. */
    align-items: stretch;
  }

  input[type='text'] {
    /* 모바일 화면에서도 여전히 오른쪽 여백을 유지합니다. */
    margin-bottom: 0; /* 버튼과의 간격 제거 */
  }

  .send-button {
    /* 모바일 화면에서도 버튼의 너비를 자동으로 조정합니다. */
    width: auto; /* 버튼 너비를 100%에서 자동으로 변경 */
  }
}
</style>
