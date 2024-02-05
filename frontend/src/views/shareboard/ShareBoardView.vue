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
  await shareBoardStore.getContent()
  await shareBoardStore.getRecord()
  await shareBoardStore.getLike()
  await shareBoardStore.getScrap()

  contents.value = shareBoardStore.shareContent
  records.value = shareBoardStore.shareRecord
  likes.value = shareBoardStore.shareLike
  scraps.value = shareBoardStore.shareScrap
})

const shareBoardDetail = (key) => {
  router.push({ name: 'share-board-detail', params: { seq: key } })
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
      @click="shareBoardDetail(key)"
    />
  </div>
</template>

<style lang="scss" scoped></style>
