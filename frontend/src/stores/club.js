// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const REST_CLUB_API = `https://i10a808.p.ssafy.io/api/club`

export const useClubStore = defineStore('club', () => {
    const router = useRouter()
  

    const createClub = function (payload) {
        axios({
          method : 'post',
          url: `${REST_CLUB_API}/create`,
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
            router.push({name : 'ClubView'})
        })
        .catch((err) => {
          console.log(err)
        })
      }
    
  return { createClub }
})
