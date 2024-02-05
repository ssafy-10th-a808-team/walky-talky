<template>
  <shareBoardDetail :content="content" :record="record" :like="like" :scrap="scrap" />
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRouter } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import shareBoardDetail from '@/components/shareBoard/shareBoardDetail.vue'
const shareBoardStore = useShareBoardStore()

const router = useRouter()

const content = ref([])
const record = ref([])
const like = ref([])
const scrap = ref([])

onMounted(async () => {
  const seq = router.params.seq

  await shareBoardStore.getContent(seq)
  await shareBoardStore.getRecord(seq)
  await shareBoardStore.getLike(seq)
  await shareBoardStore.getScrap(seq)

  content.value = shareBoardStore.shareContent
  record.value = shareBoardStore.shareRecord
  like.value = shareBoardStore.shareLike
  scrap.value = shareBoardStore.shareScrap
})
</script>
