import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'
// import { useMemberStore } from './member'
import { useCounterStore } from './counter'

const REST_WALK_API = 'https://i10a808.p.ssafy.io'

export const useWalkStore = defineStore('walk', () => {
  //   const router = useRouter()
  const counterstore = useCounterStore()
  // const memberStore = useMemberStore()
  const data = ref(null)
  // 산책시작
  const startWalk = function () {
    axios({
      method: 'POST',
      url: `${REST_WALK_API}/api/walk/start-record`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        data.value = res.data
        // console.log(res.data.data.seq)
        // console.log(data.value.data.seq)
        // console.log(res)
        console.log('산책을 시작합니다')
      })
      .catch((err) => {
        // console.log(err)
        const errmsg = err.response.data.message
        // alert(errmsg)
        console.log(errmsg)
      })
  } // 산책시작

  ///// 내 산책 목록 불러오기/////
  const myRecords = ref([])
  const getMyRecord = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_WALK_API}/api/walk/list`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      myRecords.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 내 스크랩 목록 불러오기/////
  const myScraps = ref([])
  const getMyScrap = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_WALK_API}/api/scrap-record/list`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      myScraps.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 추천 목록 불러오기 - 동네/////
  const myRecomTown = ref([])
  const getMyRecomTown = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_WALK_API}/api/walk/recommend-town`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      myRecomTown.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 추천 목록 불러오기 - 사용자 정보/////
  const myRecomInfo = ref([])
  const getMyRecomInfo = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_WALK_API}/api/walk/recommend-info`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      myRecomInfo.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 싫어요/////
  const dislike = async (seq) => {
    try {
      const res = await axios({
        method: 'post',
        url: `${REST_WALK_API}/api/walk/dislike/${seq}`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
    } catch (err) {
      console.log(err)
    }
  }

  ///// 기록 상세보기/////
  const recordDetail = ref([])
  const getRecordDetail = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_WALK_API}/api/walk/view/${seq}`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      recordDetail.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 스크랩한 기록 상세보기/////
  const scrapDetail = ref([])
  const getScrapDetail = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_WALK_API}/api/scrap-record/view/${seq}`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      scrapDetail.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 기록 삭제/////
  const deleteRecord = async (seq) => {
    try {
      const res = await axios({
        method: 'post',
        url: `${REST_WALK_API}/api/walk/delete/${seq}`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
    } catch (err) {
      console.log(err)
    }
  }

  ///// 기록 별점, 한줄평 수정/////
  const modifyRecord = async (seq, starRating, comment) => {
    try {
      const res = await axios({
        method: 'post',
        url: `${REST_WALK_API}/api/walk/modify/${seq}`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        },
        data: {
          starRating: starRating,
          comment: comment
        }
      })
    } catch (err) {
      console.log(err)
    }
  }

  return {
    data,
    startWalk,
    // 내 산책 불러오기
    myRecords,
    getMyRecord,
    // 내 스크랩 불러오기
    myScraps,
    getMyScrap,
    // 추천 목록 불러오기
    myRecomTown,
    getMyRecomTown,
    myRecomInfo,
    getMyRecomInfo,
    // 추천 싫어요
    dislike,
    // 상세 보기
    recordDetail,
    getRecordDetail,
    // 스크랩 상세 보기
    scrapDetail,
    getScrapDetail,
    // 기록 삭제
    deleteRecord,
    // 기록 수정
    modifyRecord
  }
})
