<template>
  <ClubDetailHeaderNav />
  <div>
    <h5>내가 산책했던 코스 보기</h5>
  </div>
  <div class="record-list-container">
    <!-- <div
      v-for="record in records"
      :key="record.recordSeq"
      :class="{ 'record-list': true, selected: selectedRecord === record.recordSeq }"
      @click="selectRecord(record.recordSeq)"
    > -->
    <div v-for="record in records" :key="record.recordSeq" @click="selectRecord(record.recordSeq)">
      <shareBoardRecord
        class="recommend-list-item"
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
import { useRouter } from 'vue-router'
import { useWalkStore } from '@/stores/walk'
import { useClubStore } from '@/stores/club'

import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'

const walkStore = useWalkStore()
const router = useRouter()
const records = ref([])
const clubstore = useClubStore()

const { clubSeq, planSeq } = defineProps({
  clubSeq: String,
  planSeq: String
})

onMounted(async () => {
  await walkStore.getMyRecord()

  records.value = walkStore.myRecords
  console.log(walkStore.myRecords)
})

// const selectedRecord = ref(null)

// const selectRecord = (seq) => {
//   if (selectedRecord.value === seq) {
//     selectedRecord.value = null
//   } else {
//     selectedRecord.value = seq
//   }
//   console.log(seq)
// }

async function selectRecord(recordSeq) {
  await clubstore.planRecordOverwrite(recordSeq)
  router.push({ name: 'club-plan-detail', params: { clubSeq: clubSeq, planSeq: planSeq } })
}
</script>

<style scoped>
.share-button {
  margin-left: auto;
  display: flex;
}

.recommend-list-item {
  box-sizing: border-box; /* 요소의 padding과 border를 너비에 포함시킴 */
  margin: 20px; /* 각 요소 사이의 간격 설정 */
  padding: 10px;
  border: 1px solid #ccc; /* 테두리 스타일 및 색상 설정 */
  border-radius: 5px; /* 테두리의 둥근 정도 설정 */
}
</style>
