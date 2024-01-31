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
      const formData = new FormData()
      formData.append('multipartFile', payload.profileImg)
      formData.append('name', payload.clubname)
      formData.append('introduce', payload.introduce)
      formData.append('regionCd', payload.region_cd)
      formData.append('young_birth', payload.young_birth)
      formData.append('old_birth', payload.old_birth)
      formData.append('gender_type', payload.gender_type)
      formData.append('max_capacity', payload.max_capacity)
      formData.append('is_auto_recruit', payload.is_auto_recruit)

        axios({
          method : 'post',
          url: `${REST_CLUB_API}/api/club/create`,
          headers: {
            Authorization: `Bearer ${token.value}`,
            'Content-Type': 'multipart/form-data',
          },
          data: formData,

        })
        .then((res) => {
            console.log(res)
            alert('소모임 생성 성공')
            router.push({name : 'home'})
        })
        .catch((err) => {
          for (var pair of formData.entries()) {
            console.log(pair[0] + ', ' + pair[1]);
          }
          console.log(err)
          console.log(err.message)
          
        })
      }
    
  return { createClub }
})
