<template>
  <div class="chat-window">
    <MessageList :messages="messages" />
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
import { onUpdated, ref, watch, onMounted } from 'vue'
import { useChatStore } from '@/stores/chat'
import MessageList from '@/components/chat/MessageList.vue'

const messages = ref([])
const newMessage = ref('')

const chatStroe = useChatStore()

onMounted(() => {
  chatStroe.getConnection(1)
})

// 메시지 리스트의 변화를 감지
watch(messages, () => {
  // 다음 틱에서 스크롤을 조정
  nextTick(() => {
    const messageListElement = document.querySelector('.message-list')
    messageListElement.scrollTop = messageListElement.scrollHeight
  })
})

const sendMessage = () => {
  // 새 메시지 객체를 만듭니다
  const newMsg = {
    /* 메시지 데이터 */
  }
  // 메시지 리스트에 추가
  messages.value.push(newMsg)
  // 입력 필드를 비웁니다
  newMessage.value = ''
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
