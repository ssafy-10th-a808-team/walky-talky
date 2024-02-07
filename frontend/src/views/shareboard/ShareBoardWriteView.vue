<template>
  <div class="box">
    <div class="mb-3">
      <label class="form-label">작성자</label>
    </div>

    <div class="mb-3">
      <input id="title" type="text" placeholder="제목을 입력해주세요." class="form-control" />
    </div>

    <div>
      <h5>공유하고 싶은 기록을 선택해주세요.</h5>
    </div>
    <div class="record-list-container">
      <div
        v-for="record in records"
        :key="record.recordSeq"
        :class="{ 'record-list': true, selected: selectedRecord === record.recordSeq }"
        @click="selectRecord(record.recordSeq)"
      >
        <shareBoardRecord
          :duration="record.duration"
          :distance="record.distance"
          :points="record.points"
          :title="record.title"
          :seq="record.recordSeq"
        />
      </div>
    </div>

    <div class="mb-3">
      <textarea
        id="content"
        class="form-control"
        rows="10"
        placeholder="내용을 입력해주세요."
      ></textarea>
    </div>

    <button type="button" class="btn btn-outline-primary btn-sm" @click="write">공유하기</button>
    <button class="btn btn-outline-primary btn-sm" @click="moveList">목록으로</button>
  </div>
</template>
<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'

const router = useRouter()
const shareBoardStore = useShareBoardStore()

const records = ref([])
onMounted(async () => {
  await shareBoardStore.getMyRecord()
  records.value = shareBoardStore.myRecords
  console.log(records.value)
})

const selectedRecord = ref(null)

const selectRecord = (seq) => {
  if (selectedRecord.value === seq) {
    selectedRecord.value = null
  } else {
    selectedRecord.value = seq
  }
}

const write = () => {}

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
