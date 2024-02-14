<template>
  <div class="box">
    <div class="mb-3">
      <label class="form-label">작성자</label>
      <shareBoardMember :nickname="myNickname" :profilePic="myProfileImage" />
    </div>

    <div class="mb-3">
      <input
        v-model="title"
        id="title"
        type="text"
        placeholder="제목을 입력해주세요."
        class="form-control"
      />
    </div>

    <div>
      <h5>내가 공유한 기록</h5>
    </div>
    <shareBoardRecord
      v-if="record"
      :duration="record.duration"
      :distance="record.distance"
      :points="record.points"
      :address="record.address"
      :movable="false"
    />

    <div class="mb-3">
      <textarea
        id="content"
        v-model="content"
        class="form-control"
        rows="10"
        placeholder="내용을 입력해주세요."
      ></textarea>
    </div>

    <button @click="modify">수정하기</button>
    <button @click="moveList">목록으로</button>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useCounterStore } from '@/stores/counter'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'

const route = useRoute()
const router = useRouter()
const shareBoardStore = useShareBoardStore()
const counterStore = useCounterStore()

const record = ref(null)
const defaultContent = ref(null)

const title = ref('')
const content = ref('')

const myNickname = ref('')
const myProfileImage = ref('')

const selectedRecord = ref(null)

onMounted(async () => {
  await shareBoardStore.getContent(route.params.seq)
  defaultContent.value = shareBoardStore.shareContent
  title.value = defaultContent.value.title
  content.value = defaultContent.value.content
  selectedRecord.value = defaultContent.value.recordSeq

  await shareBoardStore.getRecord(route.params.seq)
  record.value = shareBoardStore.shareRecord

  myNickname.value = counterStore.getCookie('nickname')
  myProfileImage.value = counterStore.getCookie('profileImage')
})

const modify = () => {
  if (title.value == '' || content.value == '') {
    alert('제목과 내용을 입력해주세요.')
  } else {
    shareBoardStore.modify(route.params.seq, title.value, content.value)
    router.push({ name: 'share-board' })
    selectedRecord.value = null
    title.value = ''
    content.value = ''
  }
}

const moveList = () => {
  router.push({ name: 'share-board' })
}
</script>

<style scoped>
.record-list-container {
  display: flex;
  overflow-x: auto;
  white-space: nowrap; /* 줄 바꿈 방지 */
}

.record-list {
  box-sizing: border-box; /* 요소의 padding과 border를 너비에 포함시킴 */
  margin: 20px; /* 각 요소 사이의 간격 설정 */
  padding: 10px;
}

.selected {
  background-color: lightgray;
}
</style>
