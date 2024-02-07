<template>
  <div class="comment-form-container">
    <shareBoardMember :nickname="myNickname" :profilePic="myProfileImage" />

    <div class="comment-input-container" v-if="isAvaliable">
      <textarea v-model="commentInput" placeholder="댓글을 입력하세요..." />
      <button @click="submitComment">댓글 등록</button>
    </div>

    <div class="comment-input-container" v-else>
      <textarea v-model="commentInput" />
      <div class="edit-btn-container">
        <button @click="editComment">수정</button>
        <button @click="cancelEdit">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useCounterStore } from '@/stores/counter'

const shareBoardStore = useShareBoardStore()
const counterStore = useCounterStore()

import { ref, defineEmits } from 'vue'

const { shareBoardSeq, loadComment, content, commentSeq } = defineProps([
  'shareBoardSeq',
  'loadComment',
  'content',
  'commentSeq'
])

const myNickname = ref('')
myNickname.value = counterStore.getCookie('nickname')

const myProfileImage = ref('')
myProfileImage.value = counterStore.getCookie('profileImage')

const commentInput = ref(typeof content === 'string' ? content : '')

const isAvaliable = ref(true)
if (typeof content === 'string') {
  isAvaliable.value = false
}

const submitComment = async () => {
  await shareBoardStore.commentWrite(shareBoardSeq, commentInput.value)
  await loadComment(shareBoardSeq)
  commentInput.value = null
}

const emit = defineEmits() // emit 수정

const editComment = async () => {
  await shareBoardStore.commentEdit(shareBoardSeq, commentSeq, commentInput.value)
  await loadComment(shareBoardSeq)

  emit('updateIsView', true)
  emit('editComment', commentInput.value)
}

const cancelEdit = () => {
  emit('updateIsView', true)
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

.edit-btn-container {
  margin-left: auto;
  display: flex;
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
