<template>
  <WalkHeaderNav />
  <div class="record-list-container">
    <div>
      <h4>같은 동네 사람들은 이 경로로 산책했어요!</h4>
      <div v-for="town in recordsTown" :key="town.recordSeq" :class="{ 'record-list': true }">
        <div v-if="!town.notRecommended" class="recommend-list-item">
          <shareBoardRecord
            :duration="town.duration"
            :distance="town.distance"
            :points="town.points"
            :title="town.title"
            :seq="town.recordSeq"
            :movable="false"
          />
          <div class="action-buttons">
            <p @click="notRecommend(info.recordSeq)">더 이상 이 코스 추천받지 않기</p>
          </div>
        </div>
      </div>
    </div>

    <div>
      <h4>나와 비슷한 사람들은 이 경로로 산책했어요!</h4>
      <div v-for="info in recordsInfo" :key="info.recordSeq" :class="{ 'record-list': true }">
        <div v-if="!info.notRecommended" class="recommend-list-item">
          <shareBoardRecord
            :duration="info.duration"
            :distance="info.distance"
            :points="info.points"
            :title="info.title"
            :seq="info.recordSeq"
            :movable="false"
          />
          <div class="action-buttons">
            <p @click="notRecommend(info.recordSeq)">더 이상 이 코스 추천받지 않기</p>
          </div>
        </div>
      </div>
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

const recordsTown = ref([])
const recordsInfo = ref([])

onMounted(async () => {
  await walkStore.getMyRecomTown()
  await walkStore.getMyRecomInfo()

  recordsTown.value = walkStore.myRecomTown
  recordsInfo.value = walkStore.myRecomInfo
})

const notRecommend = async (seq) => {
  await walkStore.dislike(seq)

  recordsTown.value.find((item) => item.recordSeq === seq).notRecommended = true
  recordsInfo.value.find((item) => item.recordSeq === seq).notRecommended = true
}
</script>

<style scoped>
.recommend-list-item {
  box-sizing: border-box; /* 요소의 padding과 border를 너비에 포함시킴 */
  margin: 20px; /* 각 요소 사이의 간격 설정 */
  padding: 10px;
  border: 1px solid #ccc; /* 테두리 스타일 및 색상 설정 */
  border-radius: 5px; /* 테두리의 둥근 정도 설정 */
  display: flex;
  flex-direction: column;
}

.action-buttons {
  margin-left: auto;
}
</style>
