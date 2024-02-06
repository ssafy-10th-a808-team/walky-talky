<template>
  <div class="comment-container" v-if="isNotDeleted">
    <shareBoardMember :nickname="nickname" :profilePic="profilePic" />
    <div class="content">{{ content }}</div>
    <div class="comment-info-container">
      <div class="created-at">{{ created_at }}</div>
      <div class="comment-btn-container">
        <button class="comment-btn" @click="commentEdit">수정</button>
        <button class="comment-btn" @click="commentDel">삭제</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'
import { ref } from 'vue'
import { useShareBoardStore } from '@/stores/shareBoard'
const shareBoardStore = useShareBoardStore()

const { nickname, profilePic, created_at, content, shareBoardSeq, commentSeq, loadComment } =
  defineProps([
    'nickname',
    'profilePic',
    'created_at',
    'content',
    'shareBoardSeq',
    'commentSeq',
    'loadComment'
  ])

const commentEdit = () => {
  // 여기서 이제 수정 창을 띄워줘야함
}

const isNotDeleted = ref(true)

const commentDel = async () => {
  shareBoardStore.commentDel(shareBoardSeq, commentSeq)
  await loadComment(shareBoardSeq)
}
</script>

<style scoped>
.member-container {
  margin-right: 10px;
  flex-direction: column;
}

.comment-container {
  display: flex;
  align-items: center; /* 가운데 정렬 */
}

.content {
  margin-left: 10px; /* member와의 간격 조절 */
}
.comment-info-container {
  margin-left: auto;
  display: flex;
  flex-direction: column;
}

.created-at {
  text-align: right;
  align-self: flex-start; /* 위로 정렬 */
}

.comment-btn-container {
  margin-left: auto;
  display: flex;
}

.comment-btn {
  padding: 5px 10px; /* 버튼 내부의 여백 설정 */
  font-size: 12px; /* 폰트 크기 설정 */
}
</style>
