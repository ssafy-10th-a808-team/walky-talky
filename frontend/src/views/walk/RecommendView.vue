<template>
  <WalkHeaderNav />
  <div class="record-list-container">
    <div>
      <h2>나와 같은 동네 사람들은 이 경로로 산책했어요!</h2>
      <img src="@/assets/img/reload.png" height="45px" @click="loadRecomTown" />
      <div v-if="recordsTown.length == 0" class="nothing-container">
        <h4>같은 동네 사람들이 아직 산책을 하지 않았어요</h4>
        <p @click="moveClub">우리 동네 사람들과 산책하러 가기</p>
      </div>
      <div v-else class="recom-scroll-container town">
        <div v-for="record in recordsTown" :key="record.recordSeq" :class="{ 'record-list': true }">
          <div v-if="!record.notRecommended" class="recommend-list-item">
            <shareBoardRecord
              :duration="record.duration"
              :distance="record.distance"
              :points="record.points"
              :title="record.title"
              :seq="record.recordSeq"
              :movable="false"
            />
            <div class="action-buttons">
              <p class="not-recommend" @click="notRecommend(record.recordSeq)">
                더 이상 이 코스 추천받지 않기
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div>
      <h2>나와 비슷한 사람들은 이 경로로 산책했어요!</h2>
      <img src="@/assets/img/reload.png" height="45px" @click="loadRecomInfo" />
      <div v-if="recordsInfo.length == 0" class="nothing-container">
        <h4>나와 비슷한 사람들이 아직 산책을 하지 않았어요</h4>
        <p @click="moveShareBoard">다른 사람의 산책 기록 보러 가기</p>
      </div>
      <div v-else class="recom-scroll-container info">
        <div v-for="record in recordsInfo" :key="record.recordSeq" :class="{ 'record-list': true }">
          <div v-if="!record.notRecommended" class="recommend-list-item">
            <shareBoardRecord
              :duration="record.duration"
              :distance="record.distance"
              :points="record.points"
              :title="record.title"
              :seq="record.recordSeq"
              :movable="false"
            />
            <div class="action-buttons">
              <p class="not-recommend" @click="notRecommend(record.recordSeq)">
                더 이상 이 코스 추천받지 않기
              </p>
            </div>
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

const moveClub = () => {
  router.push({ name: 'club' })
}

const moveShareBoard = () => {
  router.push({ name: 'share-board' })
}

const loadRecomTown = async () => {
  await walkStore.getMyRecomTown()
  recordsTown.value = walkStore.myRecomTown
  scrollToLeft('town')
}

const loadRecomInfo = async () => {
  await walkStore.getMyRecomInfo()
  recordsInfo.value = walkStore.myRecomInfo
  scrollToLeft('info')
}

const scrollToLeft = (input) => {
  const scrollContainer = document.querySelector(`.${input}`)
  if (scrollContainer) {
    scrollContainer.scrollLeft = 0
  }
}
</script>

<style scoped>
.recommend-list-item {
  box-sizing: border-box; /* 요소의 padding과 border를 너비에 포함시킴 */
  margin: 10px; /* 각 요소 사이의 간격 설정 */
  padding: 10px;
  border: 1px solid #ccc; /* 테두리 스타일 및 색상 설정 */
  border-radius: 5px; /* 테두리의 둥근 정도 설정 */
  display: flex;
  flex-direction: column;
}

.action-buttons {
  margin-left: auto;
}

.record-list-container h2 {
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

.record-list-container img {
  display: flex;
  margin-left: auto;
  margin-top: 10px;
}

.recom-scroll-container {
  display: flex;
  flex-direction: row;
  overflow-x: auto;
  gap: 10px;
  padding: 1rem 0;
  white-space: nowrap;
  margin: 5px;
}

/* 스크롤바 스타일 */
.recom-scroll-container::-webkit-scrollbar {
  height: 12px;
}

.recom-scroll-container::-webkit-scrollbar-thumb {
  background: darkgreen;
  /* 스크롤바 색상을 테마에 맞춥니다 */
  border-radius: 10px;
}

.recom-scroll-container::-webkit-scrollbar-track {
  background: #e0f2f1;
  border-radius: 10px;
}

.not-recommend {
  font-size: xx-small;
  text-decoration: darkgrey;
}

.nothing-container {
  display: flex;
  flex-direction: column;
  align-items: center;
}
</style>
