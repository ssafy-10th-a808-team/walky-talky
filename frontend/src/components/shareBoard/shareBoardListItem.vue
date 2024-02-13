<template>
  <div>
    <shareBoardListUpper
      :nickname="content.member.nickname"
      :profilePic="content.member.profilePic"
      :create_at="content.create_at"
      :address="record.address"
      :hit="content.hit"
    />
    <div class="middle-info-container">
      <shareBoardTitle :title="content.title" :commentCount="content.commentCount" />
      <div class="like-scrap-container">
        <shareBoardLike class="info" :likeCount="like.likeCount" :liked="like.liked" />
        <shareBoardScrap class="info" :scrapCount="scrap.scrapCount" :scraped="scrap.scraped" />
      </div>
    </div>
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
}

.middle-info-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.info {
  margin: 0px 5px 0px 5px;
}
</style>
