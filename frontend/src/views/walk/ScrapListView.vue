<template>
  <WalkHeaderNav />
  <div class="scrap-title-container">
    <h2>내가 스크랩한 코스 보기</h2>
  </div>
  <div class="record-list-container">
    <div v-if="records.length == 0" class="nothing-container">
      <h4>아직 스크랩을 하지 않았어요</h4>
      <p @click="moveShareBoard">다른 사람이 공유한 기록 보러가기</p>
    </div>

    <div v-else v-for="record in records" :key="record.recordSeq" :class="{ 'record-list': true }">
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
  await walkStore.getMyScrap()

  records.value = walkStore.myScraps
})

const moveDetail = (seq) => {
  router.push({ name: 'ScrapDetailView', params: { seq } })
}

const moveShareBoard = () => {
  router.push({ name: 'share-board' })
}
</script>

<style scoped>
.scrap-title-container h2 {
  color: white;
  /* 제목 색상을 테마에 맞춥니다 */
  font-size: 1.5rem;
  /* 제목 글꼴 크기를 조정합니다 */
  padding: 0.5rem 0;
  /* 상하 패딩을 추가합니다 */
  margin: 0;
  /* 기본 마진을 제거합니다 */
  text-align: center;
  /* 제목을 중앙 정렬합니다 */
  background-color: darkgreen;
  /* 제목 배경 색상을 추가합니다 */
  border-radius: 10px;
  /* 배경의 모서리를 둥글게 합니다 */
  box-shadow:
    0 4px 6px rgba(50, 50, 93, 0.11),
    0 1px 3px rgba(0, 0, 0, 0.08);
  /* 그림자를 추가합니다 */
}

.recommend-list-item {
  box-sizing: border-box; /* 요소의 padding과 border를 너비에 포함시킴 */
  margin: 20px; /* 각 요소 사이의 간격 설정 */
  padding: 10px;
  border: 1px solid #ccc; /* 테두리 스타일 및 색상 설정 */
  border-radius: 5px; /* 테두리의 둥근 정도 설정 */
}

.nothing-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin: 20px;
}
</style>
