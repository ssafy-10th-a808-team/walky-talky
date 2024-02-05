<template>
  <div>
    <shareBoardTitle v-if="content" :title="content.title" :commentCount="content.commentCount" />
    <shareBoardListUpper
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
        @click="pushLike(like.liked, content.shareBoardSeq)"
      />
      <shareBoardScrap
        :scrapCount="scrap.scrapCount"
        :scraped="scrap.scraped"
        @click="pushScrap(scrap.scraped, content.recordSeq)"
      />
    </div>
    <div v-if="content && comments">
      <div v-for="comment in comments">
        <shareBoardComment
          :nickname="comment.member.nickname"
          :profilePic="comment.member.profilePic"
          :content="comment.content"
          :create_at="comment.create_at"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import shareBoardListUpper from '@/components/shareBoard/shareBoardListUpper.vue'
import shareBoardTitle from '@/components/shareBoard/shareBoardTitle.vue'
import shareBoardLike from '@/components/shareBoard/shareBoardLike.vue'
import shareBoardScrap from '@/components/shareBoard/shareBoardScrap.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'
import shareBoardComment from '@/components/shareBoard/shareBoardComment.vue'
import { useShareBoardStore } from '@/stores/shareBoard'
const shareBoardStore = useShareBoardStore()

const { content, record, like, scrap, comments } = defineProps({
  content: Object,
  record: Object,
  like: Object,
  scrap: Object,
  comments: Object
})

const pushLike = (liked, shareBoardSeq) => {
  if (liked) {
    shareBoardStore.dislike(shareBoardSeq)
  } else {
    shareBoardStore.like(shareBoardSeq)
  }
}

const pushScrap = (scraped, recordSeq) => {
  if (scraped) {
    shareBoardStore.unscrap(recordSeq)
  } else {
    shareBoardStore.scrap(recordSeq)
  }
}
</script>

<style scoped>
.like-scrap-container {
  display: flex;
  align-items: center;
  justify-content: space-around;
}
</style>
