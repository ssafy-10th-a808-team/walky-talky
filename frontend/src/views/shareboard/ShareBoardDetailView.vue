<template>
  <shareBoardDetail :content="content" :record="record" :like="like" :scrap="scrap" />
</template>

<script setup>
import { onMounted, ref, onBeforeMount } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import shareBoardDetail from '@/components/shareBoard/shareBoardDetail.vue'
const shareBoardStore = useShareBoardStore()

const router = useRouter()
const route = useRoute()

const content = ref(null)
const record = ref(null)
const like = ref(null)
const scrap = ref(null)

const loadData = async (seq) => {
  await shareBoardStore.getContent(seq)
  await shareBoardStore.getRecord(seq)
  await shareBoardStore.getLike(seq)
  await shareBoardStore.getScrap(seq)

  content.value = shareBoardStore.shareContent
  console.log(content.value)
  record.value = shareBoardStore.shareRecord
  like.value = shareBoardStore.shareLike
  scrap.value = shareBoardStore.shareScrap
}

onMounted(() => {
  loadData(route.params.seq)
})
</script>
