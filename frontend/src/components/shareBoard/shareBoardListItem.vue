<template>
  <div>
    <shareBoardListUpper
      :nickname="content.member.nickname"
      :profilePic="content.member.profilePic"
      :create_at="content.create_at"
      :address="record.address"
      :hit="content.hit"
    />
    <shareBoardTitle :title="content.title" :commentCount="content.commentCount" />
    <shareBoardRecord
      :duration="record.duration"
      :distance="record.distance"
      :points="record.points"
      :address="record.address"
      :title="record.title"
      :seq="record.recordSeq"
      :movable="false"
      :isList="false"
    />
    <div class="like-scrap-container">
      <shareBoardLike :likeCount="like.likeCount" :liked="like.liked" />
      <shareBoardScrap :scrapCount="scrap.scrapCount" :scraped="scrap.scraped" />
    </div>
  </div>
</template>

<script setup>
import shareBoardListUpper from '@/components/shareBoard/shareBoardListUpper.vue'
import shareBoardTitle from '@/components/shareBoard/shareBoardTitle.vue'
import shareBoardLike from '@/components/shareBoard/shareBoardLike.vue'
import shareBoardScrap from '@/components/shareBoard/shareBoardScrap.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'

const { content, record, like, scrap } = defineProps({
  content: Object,
  record: Object,
  like: Object,
  scrap: Object
})
</script>

<style scoped>
.like-scrap-container {
  display: flex;
  align-items: center;
  justify-content: space-around;
}
</style>
