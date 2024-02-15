<template>
  <ClubDetailHeaderNav />
  <div class="chat-window">
    <div class="message-list" ref="messageListElement">
      <MessageList :messages="chatStore.messages" :clubSeq="clubSeq" />
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
import { onUnmounted, onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import { useChatStore } from '@/stores/chat'
import { useCounterStore } from '@/stores/counter'
import MessageList from '@/components/chat/MessageList.vue'

const clubstore = useClubStore()
const newMessage = ref('')
const clubSeq = clubstore.clubSeq
const chatStore = useChatStore()
const counterstore = useCounterStore()

const props = defineProps({
  seq: Number
})

const sendMessage = () => {
  if (newMessage.value.trim() !== '') {
    chatStore.sendMessage(
      {
        clubSeq: clubSeq,
        sender: counterstore.getCookie('nickname'),
        content: newMessage.value,
        timestamp: new Date().toISOString(),
        type: 'TALK'
      },
      clubSeq
    )
    // 입력 필드를 비우기
    newMessage.value = ''
  }
}

onMounted(async () => {
  chatStore.resetMessages() // 채팅 목록 초기화
  await chatStore.getConnection(clubSeq)
  await chatStore.loadMessage(clubSeq, 0)
})
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
  background-color: #5cb874; /* 핑크색 배경 */
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
    /* 모바일 화면에서도 여전히 오른쪽 여백을 유지 */
    margin-bottom: 0; /* 버튼과의 간격 제거 */
  }

  .send-button {
    /* 모바일 화면에서도 버튼의 너비를 자동으로 조정 */
    width: auto; /* 버튼 너비를 100%에서 자동으로 변경 */
  }
}
</style>
