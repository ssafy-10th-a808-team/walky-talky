<template>
  <div class="comment-container" v-if="isView">
    <shareBoardMember :nickname="nickname" :profilePic="profilePic" />
    <div class="content">{{ content }}</div>
    <div class="comment-info-container">
      <div class="created-at">{{ created_at }}</div>
      <div class="comment-btn-container" v-if="isAvaliable">
        <button class="comment-btn" @click="updateIsView(false)">수정</button>
        <button class="comment-btn" @click="confirmDeleteComment">삭제</button>
      </div>
    </div>
  </div>
  <div v-else>
    <shareBoardCommentFormVue
      :shareBoardSeq="shareBoardSeq"
      :commentSeq="commentSeq"
      :content="contentEdit"
      :loadComment="loadComment"
      @updateIsView="updateIsView"
      @editComment="editComment"
    />
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'
import shareBoardCommentFormVue from '@/components/shareBoard/shareBoardCommentForm.vue'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useCounterStore } from '@/stores/counter'
import { ref } from 'vue'

const shareBoardStore = useShareBoardStore()
const counterStore = useCounterStore()

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

const isAvaliable = ref(counterStore.getCookie('nickname') === nickname ? true : false)

const isView = ref(true)
const contentEdit = ref(content)

const editComment = (newValue) => {
  contentEdit.value = newValue
}

const updateIsView = (newValue) => {
  isView.value = newValue
}

const commentDel = async () => {
  await shareBoardStore.commentDel(shareBoardSeq, commentSeq)
  await loadComment(shareBoardSeq)
}

const confirmDeleteComment = async () => {
  const isConfirmed = window.confirm('댓글을 삭제하시겠습니까?')

  if (isConfirmed) {
    await commentDel()
  }
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
