<script setup>
import { onMounted, ref } from 'vue'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useRouter } from 'vue-router'
const shareBoardStore = useShareBoardStore()
const router = useRouter()
import shareBoardListItem from '@/components/shareBoard/shareBoardListItem.vue'

const contents = ref([])
const records = ref([])
const likes = ref([])
const scraps = ref([])

onMounted(async () => {
  await shareBoardStore.getContentList()
  await shareBoardStore.getRecordList()
  await shareBoardStore.getLikeList()
  await shareBoardStore.getScrapList()

  contents.value = shareBoardStore.shareContentList
  records.value = shareBoardStore.shareRecordList
  likes.value = shareBoardStore.shareLikeList
  scraps.value = shareBoardStore.shareScrapList
})

const shareBoardDetail = (seq) => {
  router.push({ name: 'share-board-view', params: { seq } })
}

const shareBoardWrite = () => {
  router.push({ name: 'share-board-write' })
}
</script>

<template>
  <div class="section-title">
    <h2>산책 공유 게시판</h2>
  </div>
  <button class="share-button" @click="shareBoardWrite">내 산책 공유하기</button>
  <div v-for="(content, key) in contents" :key="content.shareBoardSeq">
    <shareBoardListItem
      class="share-board-item"
      :content="content"
      :record="records[key]"
      :like="likes[key]"
      :scrap="scraps[key]"
      @click="shareBoardDetail(content.shareBoardSeq)"
    />
  </div>
</template>

<style lang="scss" scoped>
.share-button {
  margin-left: auto;
  display: flex;
}

.share-board-item {
  box-sizing: border-box; /* 요소의 padding과 border를 너비에 포함시킴 */
  margin: 20px; /* 각 요소 사이의 간격 설정 */
  padding: 10px;
  border: 1px solid #ccc; /* 테두리 스타일 및 색상 설정 */
  border-radius: 5px; /* 테두리의 둥근 정도 설정 */
}
</style>
