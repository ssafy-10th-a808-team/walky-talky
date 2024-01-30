// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const REST_CLUB_API = 'https://i10a808.p.ssafy.io'

export const useClubStore = defineStore('club', () => {
    const router = useRouter()
    const token = ref('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXEiOjI5LCJpYXQiOjE3MDY1MTcwNjIsImlzcyI6IndhbGt5dGFsa3kiLCJleHAiOjE3MDcxMjE4NjJ9.08uxxYF6uVN8I86ptutE_gAzyIesKxyh2dodYfVxEFs')

    const createClub = function (payload) {
        axios({
          method : 'post',
          url: `${REST_CLUB_API}/api/club/create`,
          headers: {
            Authorization: `Bearer ${token.value}`
          },
    
          data : {
            'name': payload.clubname,
            'introduce': payload.introduce,
            'region_cd': payload.region_cd,
            'young_birth': payload.young_birth,
            'old_birth': payload.old_birth,
            'gender_type': payload.gender_type,
            'max_capacity': payload.max_capacity,
            'is_auto_recruite': payload.is_auto_recruite,
          }
        })
        .then((res) => {
            console.log(res)
            alert('소모임 생성 성공')
            router.push({name : 'home'})
        })
        .catch((err) => {
          console.log(err)
        })
      }
    
  return { createClub }
})
