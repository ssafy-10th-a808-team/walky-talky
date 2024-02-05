// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { ref, toRef } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'
import { useMemberStore } from './member'
import { useCounterStore } from './counter'
const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api'

export const useShareBoardStore = defineStore('shareBoard', () => {
  const router = useRouter()
  const counterstore = useCounterStore()
  const memberstore = useMemberStore()
  const token = memberstore.token

  // 기록 공유 게시판 목록 보기
  const shareContentList = ref([])
  const getContentList = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/content`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareContentList.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareRecordList = ref([])
  const getRecordList = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/record`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareRecordList.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareLikeList = ref([])
  const getLikeList = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/like`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareLikeList.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareScrapList = ref([])
  const getScrapList = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/scrap`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareScrapList.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 기록 공유 게시판 상세 보기/////
  const shareContent = ref([])
  const getContent = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/content`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareContent.value = res.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareRecord = ref([])
  const getRecord = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/record`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareRecord.value = res.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareLike = ref([])
  const getLike = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/like`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareLike.value = res.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareScrap = ref([])
  const getScrap = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/scrap`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareScrap.value = res.data
    } catch (err) {
      console.log(err)
    }
  }

  return {
    // 목록 조회
    getContentList,
    shareContentList,
    getRecordList,
    shareRecordList,
    getLikeList,
    shareLikeList,
    getScrapList,
    shareScrapList,
    // 상세 조회
    getContent,
    shareContent,
    getRecord,
    shareRecord,
    getLike,
    shareLike,
    getScrap,
    shareScrap
  }
})
