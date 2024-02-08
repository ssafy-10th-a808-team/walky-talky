<template>
  <ClubDetailHeaderNav />
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
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onUnmounted, onMounted, ref, nextTick } from 'vue'
import { useClubStore } from '@/stores/club'
import { useRoute } from 'vue-router'
import MemberList from '@/components/member/MemberListView.vue'
import axios from 'axios'
import { useCounterStore } from '@/stores/counter'
import { useChatStore } from '@/stores/chat'
import MessageList from '@/components/chat/MessageList.vue'

const clubstore = useClubStore()
const counterstore = useCounterStore()
const newMessage = ref('')
const clubSeq = clubstore.clubSeq
const chatStore = useChatStore()
const messageListElement = ref(null) // ref를 사용하여 DOM 요소 참조 정의
const client = ref(chatStore.client)

const { seq } = defineProps({
  seq: String
})

onMounted(async () => {
  await chatStore.loadMessage(seq, 0)
  chatStore.getConnection(seq)

  //   nextTick(() => {
  //     if (messageListElement) {
  //       messageListElement.value.scrollTop = messageListElement.value.scrollHeight
  //     }
  //   })
})

onUnmounted(() => {
  if (client.value && client.value.connected) {
    client.value.deactivate()
    console.log('WebSocket connection deactivated.')
  }
})

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    chatStore.sendMessage({
      clubSeq: clubSeq,
      sender: counterstore.getCookie('nickname'),
      content: newMessage.value,
      timestamp: new Date().toISOString(),
      type: 'TALK'
    })
    // 입력 필드를 비웁니다
    newMessage.value = ''
  }
}
</script>

<style scoped>
.circular {
  width: 200px;
  /* 고정 가로 크기 */
  height: 200px;
  /* 고정 세로 크기 */
  border-radius: 50%;
  /* 원 모양으로 만듦 */
  overflow: hidden;
  display: flex;
  align-items: center;
  /* 이미지를 중앙에 정렬 */
  justify-content: center;
}

.circular img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 이미지 비율 유지하며 컨테이너 채움 */
  border-radius: 50%;
}
.chat-window {
  display: flex;
  flex-direction: column;
  max-width: 100%;
  height: 600px;
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
