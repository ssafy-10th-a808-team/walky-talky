<template>
  <WalkHeaderNav />
  <div>
    <h5>내가 산책했던 코스 보기</h5>
  </div>
  <button class="share-button" @click="shareBoardWrite">내 산책 공유하기</button>
  <div class="record-list-container">
    <div v-for="record in records" :key="record.recordSeq" :class="{ 'record-list': true }">
      <shareBoardRecord
        class="recommend-list-item"
        :duration="record.duration"
        :distance="record.distance"
        :points="record.points"
        :title="record.title"
        :seq="record.recordSeq"
        :movable="false"
        @click="moveDetail(record.recordSeq)"
      />
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useWalkStore } from '@/stores/walk'

import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'

const walkStore = useWalkStore()
const router = useRouter()

const records = ref([])

onMounted(async () => {
  await walkStore.getMyRecord()

  records.value = walkStore.myRecords
  console.log(walkStore.myRecords)
})

const moveDetail = (seq) => {
  router.push({ name: 'WalkDetailView', params: { seq } })
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
