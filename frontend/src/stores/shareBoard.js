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
  const shareContent = ref([])
  const getContent = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/content`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareContent.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareRecord = ref([])
  const getRecord = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/record`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareRecord.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareLike = ref([])
  const getLike = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/like`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareLike.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareScrap = ref([])
  const getScrap = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/list/scrap`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareScrap.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  return {
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
