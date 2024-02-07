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
      shareContent.value = res.data.data
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
      shareRecord.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareLike = ref([])
  const getLike = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/like`,
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
  const getScrap = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/scrap`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareScrap.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  const shareComment = ref([])
  const getComment = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/share-board/view/${seq}/comment`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      shareComment.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 게시글 좋아요, 취소/////
  const like = (shareBoardSeq) => {
    try {
      const res = axios({
        method: 'post',
        url: `${REST_CLUB_API}/share-board/${shareBoardSeq}/like`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      }).catch(function (err) {
        if (err.response.status == 400) {
          alert(err.response.data.message)
          window.location.reload()
        }
      })
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
      } else {
        console.error('좋아요 중 오류 발생:', error)
        window.location.reload()
      }
    }
  }

  const dislike = (shareBoardSeq) => {
    try {
      const res = axios({
        method: 'post',
        url: `${REST_CLUB_API}/share-board/${shareBoardSeq}/like-cancel`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      }).catch(function (err) {
        if (err.response.status == 400) {
          alert(err.response.data.message)
          window.location.reload()
        }
      })
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
        window.location.reload()
      } else {
        console.error('좋아요 취소 중 오류 발생:', error)
      }
    }
  }

  ///// 기록 스크랩, 취소/////
  const scrap = (recordSeq) => {
    try {
      const res = axios({
        method: 'post',
        url: `${REST_CLUB_API}/scrap-record/${recordSeq}/scrap`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      }).catch(function (err) {
        if (err.response.status == 400) {
          alert(err.response.data.message)
          window.location.reload()
        }
      })
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
        window.location.reload()
      } else {
        console.error('기록 스크랩 중 오류 발생:', error)
      }
    }
  }

  const unscrap = (recordSeq) => {
    try {
      const res = axios({
        method: 'post',
        url: `${REST_CLUB_API}/scrap-record/${recordSeq}/cancel`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      }).catch(function (err) {
        if (err.response.status == 400) {
          alert(err.response.data.message)
          window.location.reload()
        }
      })
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
        window.location.reload()
      } else {
        console.error('스크랩 취소 중 오류 발생:', error)
      }
    }
  }

  ///// 댓글 작성/////
  const commentWrite = async (shareBoardSeq, content) => {
    try {
      const response = await axios.post(
        `${REST_CLUB_API}/share-board/${shareBoardSeq}/comment/write`,
        { content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      alert('댓글 작성 성공!')
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
      } else {
        console.error('댓글 작성 중 오류 발생:', error)
      }
    }
  }

  ///// 댓글 수정/////
  const commentEdit = async (shareBoardSeq, commentSeq, content) => {
    try {
      const response = await axios.post(
        `${REST_CLUB_API}/share-board/${shareBoardSeq}/comment/${commentSeq}/modify`,
        { content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      alert('댓글 수정 성공!')
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
      } else {
        console.error('댓글 수정 중 오류 발생:', error)
      }
    }
  }

  ///// 댓글 삭제/////
  const commentDel = async (shareBoardSeq, commentSeq) => {
    try {
      const response = await axios.post(
        `${REST_CLUB_API}/share-board/${shareBoardSeq}/comment/${commentSeq}/delete`,
        {},
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      alert('댓글 삭제 성공!')
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
      } else {
        console.error('댓글 삭제 중 오류 발생:', error)
      }
    }
  }

  ///// 내 산책 목록 불러오기/////
  const myRecords = ref([])
  const getMyRecord = async (seq) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/walk/list`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      myRecords.value = res.data.data
    } catch (err) {
      console.log(err)
    }
  }

  ///// 글쓰기/////
  const write = async (recordSeq, title, content) => {
    try {
      const response = await axios.post(
        `${REST_CLUB_API}/share-board/write`,
        { recordSeq, title, content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      alert('기록 공유 성공!')
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
      } else {
        console.error('글 쓰기 중 오류 발생:', error)
      }
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
    shareScrap,
    getComment,
    shareComment,
    // 좋아요
    like,
    dislike,
    //스크랩
    scrap,
    unscrap,
    // 댓글
    commentWrite,
    commentEdit,
    commentDel,
    // 내 기록
    myRecords,
    getMyRecord,
    // 글쓰기
    write
  }
})
