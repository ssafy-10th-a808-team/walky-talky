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

  return {
    data,
    startWalk
  }
})
