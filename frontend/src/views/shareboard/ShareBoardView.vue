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

  <div class="pagination">
    <button @click="prevPage" :disabled="currentPage === 0" key="prev">이전</button>
    <template v-for="page in totalPage" :key="page">
      <button @click="goToPage(page)" :class="{ active: page === currentPage + 1 }">
        {{ page }}
      </button>
    </template>
    <button @click="nextPage" :disabled="currentPage === totalPage - 1" key="next">다음</button>
  </div>
</template>

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
const totalPage = ref(0)
const currentPage = ref(0)

const loadData = async (page) => {
  await shareBoardStore.getRecordList(page)
  await shareBoardStore.getContentList(page)
  await shareBoardStore.getLikeList(page)
  await shareBoardStore.getScrapList(page)

  contents.value = shareBoardStore.shareContentList
  records.value = shareBoardStore.shareRecordList
  likes.value = shareBoardStore.shareLikeList
  scraps.value = shareBoardStore.shareScrapList
}

onMounted(async () => {
  await shareBoardStore.getTotalCnt()
  await loadData(0)

  totalPage.value = Math.ceil(shareBoardStore.totalCnt / 5)
})

const shareBoardDetail = (seq) => {
  router.push({ name: 'share-board-view', params: { seq } })
}

const shareBoardWrite = () => {
  router.push({ name: 'share-board-write' })
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value -= 1
    console.log(currentPage.value)
    loadData(currentPage.value)
    window.scrollTo(0, 0)
  }
}

const nextPage = () => {
  if (currentPage.value < totalPage.value - 1) {
    currentPage.value += 1
    console.log(currentPage.value)
    loadData(currentPage.value)
    window.scrollTo(0, 0)
  }
}

const goToPage = (page) => {
  currentPage.value = page - 1
  console.log(currentPage.value)
  loadData(currentPage.value)
  window.scrollTo(0, 0)
}
</script>

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

.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
}

.pagination button {
  margin: 0 4px;
  padding: 6px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  cursor: pointer;
}

.pagination button.active {
  background-color: #fff; /* 선택된 페이지의 배경색 */
  color: #000; /* 선택된 페이지의 텍스트 색상 */
}
</style>
