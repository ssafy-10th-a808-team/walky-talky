import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
// import axios from 'axios'
import { axios } from '@/stores/jwtFilter'
// import { useMemberStore } from './member'
import { useCounterStore } from './counter'

const REST_WALK_API = 'https://i10a808.p.ssafy.io'

const errorHandle = (err) => {
  if (err.response && err.response.data.message) {
    alert(err.response.data.message)
    if (err.response.status == 400) {
      window.location.reload()
    } else if (err.response.status == 401) {
      router.push({ name: 'home' })
    }
  } else {
    alert('죄송합니다. 현재 오류가 발생했습니다. 불편을 드려 죄송합니다.')
    router.push({ name: 'home' })
  }
}

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
    const res = await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/walk/list`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        myRecords.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 내 스크랩 목록 불러오기/////
  const myScraps = ref([])
  const getMyScrap = async () => {
    await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/scrap-record/list`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        myScraps.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 추천 목록 불러오기 - 동네/////
  const myRecomTown = ref([])
  const getMyRecomTown = async () => {
    await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/walk/recommend-town`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        myRecomTown.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 추천 목록 불러오기 - 사용자 정보/////
  const myRecomInfo = ref([])
  const getMyRecomInfo = async () => {
    await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/walk/recommend-info`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        myRecomInfo.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 싫어요/////
  const dislike = async (seq) => {
    await axios({
      method: 'post',
      url: `${REST_WALK_API}/api/walk/dislike/${seq}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    }).catch((err) => {
      errorHandle(err)
    })
  }

  ///// 기록 상세보기/////
  const recordDetail = ref([])
  const getRecordDetail = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/walk/view/${seq}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        recordDetail.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 스크랩한 기록 상세보기/////
  const scrapDetail = ref([])
  const getScrapDetail = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/scrap-record/view/${seq}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        scrapDetail.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 기록 삭제/////
  const deleteRecord = async (seq) => {
    await axios({
      method: 'post',
      url: `${REST_WALK_API}/api/walk/delete/${seq}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    }).catch((err) => {
      errorHandle(err)
    })
  }

  ///// 기록 별점, 한줄평 수정/////
  const modifyRecord = async (seq, starRating, comment) => {
    await axios({
      method: 'post',
      url: `${REST_WALK_API}/api/walk/modify/${seq}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      },
      data: {
        starRating: starRating,
        comment: comment
      }
    }).catch((err) => {
      errorHandle(err)
    })
  }

  ///// 따라뛰기/////
  const cloneRecord = ref([])
  const getCloneRecord = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_WALK_API}/api/walk/clone/${seq}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        cloneRecord.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
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
    modifyRecord,
    // 따라 뛰기
    cloneRecord,
    getCloneRecord
  }
})
