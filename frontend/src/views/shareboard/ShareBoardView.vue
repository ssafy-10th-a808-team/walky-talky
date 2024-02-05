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
</script>

<template>
  <div class="section-title">
    <h2>산책 공유 게시판</h2>
  </div>
  <div v-for="(content, key) in contents" :key="content.shareBoardSeq">
    <shareBoardListItem
      :content="content"
      :record="records[key]"
      :like="likes[key]"
      :scrap="scraps[key]"
      @click="shareBoardDetail(content.shareBoardSeq)"
    />
  </div>
</template>

<style lang="scss" scoped></style>
