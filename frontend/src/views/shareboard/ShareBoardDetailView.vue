<template>
  <div>
    <div class="detail-upper-container">
      <button class="upper-list-btn" @click="moveList">목록으로</button>
      <shareBoardTitle v-if="content" :title="content.title" :commentCount="content.commentCount" />
      <shareBoardListUpper
        v-if="content && record"
        :nickname="content.member.nickname"
        :profilePic="content.member.profilePic"
        :create_at="content.create_at"
        :address="record.address"
        :hit="content.hit"
      />
      <div class="content-edit-del-container" v-if="isAvaliable">
        <button class="content-edit-del-btn" @click="moveModify(content.shareBoardSeq)">
          수정
        </button>
        <button class="content-edit-del-btnr" @click="deleteShareBoard(content.shareBoardSeq)">
          삭제
        </button>
      </div>
    </div>

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
        @loadComment="loadComment"
        :shareBoardSeq="content.shareBoardSeq"
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
import { useRoute, useRouter } from 'vue-router'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useCounterStore } from '@/stores/counter'

import shareBoardListUpper from '@/components/shareBoard/shareBoardListUpper.vue'
import shareBoardTitle from '@/components/shareBoard/shareBoardTitle.vue'
import shareBoardLike from '@/components/shareBoard/shareBoardLike.vue'
import shareBoardScrap from '@/components/shareBoard/shareBoardScrap.vue'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'
import shareBoardComment from '@/components/shareBoard/shareBoardComment.vue'
import shareBoardCommentFormVue from '@/components/shareBoard/shareBoardCommentForm.vue'

const shareBoardStore = useShareBoardStore()
const counterStore = useCounterStore()

const route = useRoute()
const router = useRouter()

const content = ref(null)
const record = ref(null)
const like = ref(null)
const scrap = ref(null)
const comments = ref(null)

const myNickname = ref('')
myNickname.value = counterStore.getCookie('nickname')

const myProfileImage = ref('')
myProfileImage.value = counterStore.getCookie('profileImage')

let isAvaliable

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

  isAvaliable = ref(
    counterStore.getCookie('nickname') === content.value.member.nickname ? true : false
  )
}

onMounted(() => {
  loadData(route.params.seq)
})

const loadComment = async (seq) => {
  await shareBoardStore.getComment(seq)
  comments.value = shareBoardStore.shareComment
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

const pushScrap = (scraped, recordSeq) => {
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

const moveModify = (seq) => {
  router.push({ name: 'share-board-modify', seq })
}

const deleteShareBoard = async (seq) => {
  await shareBoardStore.deleteShareBoard(seq)
  router.push({ name: 'share-board' })
}

const moveList = () => {
  router.push({ name: 'share-board' })
}
</script>

<style scoped>
.detail-upper-container {
  padding: 2%;
}
.upper-list-btn {
  display: flex;
  margin-left: auto;
}

.content-edit-del-container {
  display: flex;
}

.content-edit-del-btn {
  display: flex;
  margin-left: auto;
}

.like-scrap-container {
  display: flex;
  align-items: center;
  justify-content: space-around;
}
</style>
