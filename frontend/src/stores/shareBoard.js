import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import axios from 'axios'
// import { axios } from '@/stores/jwtFilter'

import { useCounterStore } from './counter'
import { useMemberStore } from './member'
const REST_SHARE_BOARD_API = 'https://i10a808.p.ssafy.io/api'

export const useShareBoardStore = defineStore('shareBoard', () => {
  const counterstore = useCounterStore()

  // 기록 공유 게시판 목록 보기
  const totalCnt = ref(0)
  const getTotalCnt = async () => {
    axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/list/total-count`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        totalCnt.value = res.data.data
      })
      .catch((err) => {
        console.log(err)
      })
  }

  const shareContentList = ref([])
  const getContentList = async (page) => {
    axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/list/content?page=${page}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareContentList.value = res.data.data
      })
      .catch((err) => {
        console.log(err)
      })
  }

  const shareRecordList = ref([])
  const getRecordList = async (page) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_SHARE_BOARD_API}/share-board/list/record?page=${page}`,
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
  const getLikeList = async (page) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_SHARE_BOARD_API}/share-board/list/like?page=${page}`,
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
  const getScrapList = async (page) => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_SHARE_BOARD_API}/share-board/list/scrap?page=${page}`,
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
        url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/content`,
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
        url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/record`,
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
        url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/like`,
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
        url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/scrap`,
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
        url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/comment`,
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
      axios({
        method: 'post',
        url: `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/like`,
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
      axios({
        method: 'post',
        url: `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/like-cancel`,
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
      axios({
        method: 'post',
        url: `${REST_SHARE_BOARD_API}/scrap-record/${recordSeq}/scrap`,
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
      axios({
        method: 'post',
        url: `${REST_SHARE_BOARD_API}/scrap-record/${recordSeq}/cancel`,
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
      await axios.post(
        `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/comment/write`,
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
      await axios.post(
        `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/comment/${commentSeq}/modify`,
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
      await axios.post(
        `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/comment/${commentSeq}/delete`,
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

  ///// 글쓰기/////
  const write = async (recordSeq, title, content) => {
    try {
      await axios.post(
        `${REST_SHARE_BOARD_API}/share-board/write`,
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

  ///// 글 수정/////
  const modify = async (seq, title, content) => {
    try {
      await axios.post(
        `${REST_SHARE_BOARD_API}/share-board/modify/${seq}`,
        { title, content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      alert('기록 수정 성공!')
    } catch (error) {
      if (error.response) {
        alert(error.response.data.message)
      } else {
        console.error('글 쓰기 중 오류 발생:', error)
      }
    }
  }

  ///// 글 삭제/////
  const deleteShareBoard = async (seq) => {
    try {
      await axios.post(
        `${REST_SHARE_BOARD_API}/share-board/delete/${seq}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      alert('기록 삭제 성공!')
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
    totalCnt,
    getTotalCnt,
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
    // 글쓰기
    write,
    // 글 수정
    modify,
    // 글 삭제
    deleteShareBoard
  }
})
