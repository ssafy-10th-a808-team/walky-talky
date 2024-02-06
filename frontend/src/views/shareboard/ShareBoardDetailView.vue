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
        @click="pushScrap(scrap.scraped, content.recordSeq, content.shareBoardSeq)"
      />
    </div>

    <div v-if="content">
      <shareBoardCommentFormVue
        :shareBoardSeq="content.shareBoardSeq"
        :nickname="myNickname"
        :profilePic="myProfileImage"
        :loadComment="loadComment"
      />
    </div>

    <div v-if="comments">
      <div v-for="comment in comments" :key="comment.commentSeq">
        <shareBoardComment
          :nickname="comment.member.nickname"
          :profilePic="comment.member.profilePic"
          :content="comment.content"
          :created_at="comment.created_at"
          :shareBoardSeq="content.shareBoardSeq"
          :commentSeq="comment.commentSeq"
          :loadComment="loadComment"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useMemberStore } from '@/stores/member'

import shareBoardListUpper from '@/components/shareBoard/shareBoardListUpper.vue'
import shareBoardTitle from '@/components/shareBoard/shareBoardTitle.vue'
import shareBoardLike from '@/components/shareBoard/shareBoardLike.vue'
import shareBoardScrap from '@/components/shareBoard/shareBoardScrap.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'
import shareBoardComment from '@/components/shareBoard/shareBoardComment.vue'
import shareBoardCommentFormVue from '@/components/shareBoard/shareBoardCommentForm.vue'

const shareBoardStore = useShareBoardStore()
const memberStore = useMemberStore()

const route = useRoute()

const content = ref(null)
const record = ref(null)
const like = ref(null)
const scrap = ref(null)
const comments = ref(null)

const loadData = async (seq) => {
  await shareBoardStore.getContent(seq)
  await shareBoardStore.getRecord(seq)
  await shareBoardStore.getLike(seq)
  await shareBoardStore.getScrap(seq)
  await shareBoardStore.getComment(seq)

  content.value = shareBoardStore.shareContent
  record.value = shareBoardStore.shareRecord
  like.value = shareBoardStore.shareLike
  scrap.value = shareBoardStore.shareScrap
  comments.value = shareBoardStore.shareComment
}

onMounted(() => {
  loadData(route.params.seq)
})

///////////////////////
const myNickname = ref('')
myNickname.value = memberStore.getNickname()

const myProfileImage = ref('')
myProfileImage.value = memberStore.getProfileImage()

console.log(myNickname.value)
console.log(myProfileImage.value)
///////////////////////

const loadComment = async (seq) => {
  await shareBoardStore.getComment(seq)
  comments.value = shareBoardStore.shareComment
}

const loadLike = async (seq) => {
  await shareBoardStore.getLike(seq)
  console.log('like')
  like.value = shareBoardStore.shareLike
}

const loadScrap = async (seq) => {
  await shareBoardStore.getScrap(seq)
  scrap.value = shareBoardStore.shareScrap
}

const pushLike = (liked, shareBoardSeq) => {
  if (liked) {
    like.value.liked = false
    like.value.likeCount -= 1
    shareBoardStore.dislike(shareBoardSeq)
  } else {
    like.value.liked = true
    like.value.likeCount += 1
    shareBoardStore.like(shareBoardSeq)
  }
}

const pushScrap = (scraped, recordSeq, shareBoardSeq) => {
  if (scraped) {
    scrap.value.scraped = false
    scrap.value.scrapCount -= 1
    shareBoardStore.unscrap(recordSeq)
  } else {
    scrap.value.scraped = true
    scrap.value.scrapCount += 1
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
