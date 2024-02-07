<template>
  <div>
    <h5>내가 산책했던 코스 보기</h5>
  </div>
  <button class="share-button" @click="shareBoardWrite">내 산책 공유하기</button>
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
        :movable="false"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useShareBoardStore } from '@/stores/shareBoard'
import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'

const shareBoardStore = useShareBoardStore()

const records = ref([])
onMounted(async () => {
  await shareBoardStore.getMyRecord()
  records.value = shareBoardStore.myRecords
})

const selectedRecord = ref(null)

const selectRecord = (seq) => {
  if (selectedRecord.value === seq) {
    selectedRecord.value = null
  } else {
    selectedRecord.value = seq
  }
}
const shareBoardWrite = () => {
  router.push({ name: 'share-board-write' })
}
</script>

<style scoped>
.share-button {
  margin-left: auto;
  display: flex;
}
</style>
