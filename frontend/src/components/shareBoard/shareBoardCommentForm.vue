<template>
  <div class="comment-form-container">
    <shareBoardMember :nickname="nickname" :profilePic="profilePic" />

    <div class="comment-input-container">
      <textarea v-model="commentInput" placeholder="댓글을 입력하세요..." />
      <button @click="submitComment">댓글 등록</button>
    </div>
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'
import { useShareBoardStore } from '@/stores/shareBoard'
const shareBoardStore = useShareBoardStore()
import { ref } from 'vue'

const { nickname, profilePic, shareBoardSeq, loadComment } = defineProps([
  'nickname',
  'profilePic',
  'shareBoardSeq',
  'loadComment'
])

const commentInput = ref('')

const submitComment = async () => {
  shareBoardStore.commentWrite(shareBoardSeq, commentInput.value)
  await loadComment(shareBoardSeq)
  commentInput.value = ''
}
</script>

<style scoped>
.member-container {
  margin-right: 10px;
  flex-direction: column;
}

.comment-form-container {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}

.comment-input-container {
  flex: 1; /* 남은 공간을 모두 차지하도록 설정 */
  display: flex; /* 자식 요소들을 가로로 정렬하기 위해 추가 */
  margin-left: 16px;
}

.comment-input-container textarea {
  width: 100%; /* 부모 요소에 대해 100% 너비로 설정 */
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  margin-right: 8px;
  font-size: 14px;
  resize: vertical; /* 수직 리사이즈만 허용 */
}

.comment-input-container button {
  padding: 10px;
  cursor: pointer;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 5px;
  font-size: 14px;
  margin-left: auto; /* 맨 오른쪽으로 이동 */
}

.comment-input-container button:hover {
  background-color: #45a049;
}
</style>
