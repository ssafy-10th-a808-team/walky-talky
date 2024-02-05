import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'
// import { useMemberStore } from './member'
// import { useCounterStore } from './counter'

const REST_WALK_API = 'https://i10a808.p.ssafy.io'

export const useWalkStore = defineStore('walk', () => {
  //   const router = useRouter()
  //   const counterstore = useCounterStore()
  //   const memberstore = useMemberStore()
  const token = ref(
    'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXEiOjI5LCJpYXQiOjE3MDY1MTcwNjIsImlzcyI6IndhbGt5dGFsa3kiLCJleHAiOjE3MDcxMjE4NjJ9.08uxxYF6uVN8I86ptutE_gAzyIesKxyh2dodYfVxEFs'
  )

  // 산책시작
  const startWalk = function () {
    axios({
      method: 'POST',
      url: `${REST_WALK_API}/api/walk/start-record`,
      headers: {
        Authorization: `Bearer ${token.value}`
      }
    })
      .then((res) => {
        console.log(res)
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
    startWalk
  }
})
