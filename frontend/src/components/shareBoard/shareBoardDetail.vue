<template>
  <div>
    <shareBoardTitle v-if="content" :title="content.title" :commentCount="content.commentCount" />
    <shareBoardMember
      v-if="content && record"
      :nickname="content.member.nickname"
      :profilePic="content.member.profilePic"
      :create_at="content.create_at"
      :address="record.address"
      :hit="content.hit"
    />
    <shareBoardRecord
      v-if="record"
      :duration="record.duration"
      :distance="record.distance"
      :points="record.points"
      :address="record.address"
    />
    <div v-if="content">{{ content.content }}</div>
    <div v-if="like && scrap" class="like-scrap-container">
      <shareBoardLike
        :likeCount="like.likeCount"
        :liked="like.liked"
        @click="pushLike(content.shareBoardSeq)"
      />
      <shareBoardScrap
        :scrapCount="scrap.scrapCount"
        :scraped="scrap.scraped"
        @click="pushScrap(content.recordSeq)"
      />
    </div>
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'
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
