import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
// import axios from 'axios'
import { axios } from '@/stores/jwtFilter'
import { useCounterStore } from './counter'

const REST_SHARE_BOARD_API = 'https://i10a808.p.ssafy.io/api'

const router = useRouter()

const errorHandle = (err) => {
  console.log('share-board')
  console.log(err)
  if (err.response && err.response.data.message) {
    alert(err.response.data.message)
    if (err.response.status == 400) {
      window.location.reload()
    } else if (err.response.status == 401) {
      router.push({ name: 'home' })
    }
  }
}

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
        errorHandle(err)
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
        errorHandle(err)
      })
  }

  const shareRecordList = ref([])
  const getRecordList = async (page) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/list/record?page=${page}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareRecordList.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  const shareLikeList = ref([])
  const getLikeList = async (page) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/list/like?page=${page}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareLikeList.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  const shareScrapList = ref([])
  const getScrapList = async (page) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/list/scrap?page=${page}`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareScrapList.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 기록 공유 게시판 상세 보기/////
  const shareContent = ref([])
  const getContent = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/content`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareContent.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  const shareRecord = ref([])
  const getRecord = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/record`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareRecord.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  const shareLike = ref([])
  const getLike = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/like`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareLike.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  const shareScrap = ref([])
  const getScrap = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/scrap`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareScrap.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  const shareComment = ref([])
  const getComment = async (seq) => {
    await axios({
      method: 'get',
      url: `${REST_SHARE_BOARD_API}/share-board/view/${seq}/comment`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        shareComment.value = res.data.data
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 게시글 좋아요, 취소/////
  const like = (shareBoardSeq) => {
    axios({
      method: 'post',
      url: `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/like`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    }).catch(function (err) {
      errorHandle(err)
    })
  }

  const dislike = (shareBoardSeq) => {
    axios({
      method: 'post',
      url: `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/like-cancel`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    }).catch(function (err) {
      errorHandle(err)
    })
  }

  ///// 기록 스크랩, 취소/////
  const scrap = (recordSeq) => {
    axios({
      method: 'post',
      url: `${REST_SHARE_BOARD_API}/scrap-record/${recordSeq}/scrap`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    }).catch(function (err) {
      errorHandle(err)
    })
  }

  const unscrap = (recordSeq) => {
    axios({
      method: 'post',
      url: `${REST_SHARE_BOARD_API}/scrap-record/${recordSeq}/cancel`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    }).catch(function (err) {
      errorHandle(err)
    })
  }

  ///// 댓글 작성/////
  const commentWrite = async (shareBoardSeq, content) => {
    await axios
      .post(
        `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/comment/write`,
        { content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      .then(() => {
        alert('댓글 작성 성공!')
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 댓글 수정/////
  const commentEdit = async (shareBoardSeq, commentSeq, content) => {
    await axios
      .post(
        `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/comment/${commentSeq}/modify`,
        { content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      .then(() => {
        alert('댓글 수정 성공!')
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 댓글 삭제/////
  const commentDel = async (shareBoardSeq, commentSeq) => {
    await axios
      .post(
        `${REST_SHARE_BOARD_API}/share-board/${shareBoardSeq}/comment/${commentSeq}/delete`,
        {},
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      .then(() => {
        alert('댓글 삭제 성공!')
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 글쓰기/////
  const write = async (recordSeq, title, content) => {
    await axios
      .post(
        `${REST_SHARE_BOARD_API}/share-board/write`,
        { recordSeq, title, content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      .then(() => {
        alert('기록 공유 성공!')
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 글 수정/////
  const modify = async (seq, title, content) => {
    await axios
      .post(
        `${REST_SHARE_BOARD_API}/share-board/modify/${seq}`,
        { title, content },
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      .then(() => {
        alert('기록 수정 성공!')
      })
      .catch((err) => {
        errorHandle(err)
      })
  }

  ///// 글 삭제/////
  const deleteShareBoard = async (seq) => {
    await axios
      .post(
        `${REST_SHARE_BOARD_API}/share-board/delete/${seq}`,
        {},
        {
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        }
      )
      .then(() => {
        alert('기록 삭제 성공!')
      })
      .catch((err) => {
        errorHandle(err)
      })
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
