// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { ref, toRef } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import axios from 'axios'

const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api'

export const useClubStore = defineStore('club', () => {
    const router = useRouter()
    const token = ref('eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXEiOjI5LCJpYXQiOjE3MDY1MTcwNjIsImlzcyI6IndhbGt5dGFsa3kiLCJleHAiOjE3MDcxMjE4NjJ9.08uxxYF6uVN8I86ptutE_gAzyIesKxyh2dodYfVxEFs')


    // 소모임 생성
    const createClub = function (payload) {
      const formData = new FormData()
      if (payload.profileImg) {
        formData.append('multipartFile', payload.profileImg)
      }
      formData.append('name', payload.clubname)
      formData.append('introduce', payload.introduce)
      formData.append('regionCd', payload.region_cd)
      formData.append('young_birth', payload.young_birth)
      formData.append('old_birth', payload.old_birth)
      formData.append('gender_type', payload.gender_type)
      formData.append('max_capacity', payload.max_capacity)
      formData.append('is_auto_recruit', payload.is_auto_recruit)

        axios({
          method: 'post',
          url: `${REST_CLUB_API}/club/create`,
          headers: {
            Authorization: `Bearer ${token.value}`,
            'Content-Type': 'multipart/form-data',
          },
          data: formData,

        })
        .then((res) => {
            console.log(res)
            alert('소모임 생성 성공')
            router.push({name : 'club'})
        })
        .catch((err) => {
          for (var pair of formData.entries()) {
            console.log(pair[0] + ', ' + pair[1]);
          }
          console.log(err)
          console.log(err.message)
          
        })
      }

  // 소모임 이름 중복 확인
  const checkDuplicate = (name) => {
    axios({
      method : 'post',
      url: `${REST_CLUB_API}/club/check-name`,
      headers: {
        Authorization: `Bearer ${token.value}`, 
      },
      data : {
        "name": name
      }
    })
    .then((res) => {
      console.log('사용가능한 소모임명입니다.')
    })
    .catch((err) => {
      console.log(err.response.data.message)
    })
  }
  // 전체 소모임 보기, 전체 소모임 관련
  const clubs = ref([])
  const getClubs = async () => {
    try {
      const res = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/club/list`,
        headers: {
          Authorization: `Bearer ${token.value}`, 
        },
      })
      console.log(res)
      clubs.value = res.data

    } catch (err) {
      console.log(err)
    }
  }
  // 소모임 디테일 관련 함수
  const findClub = async (seq) => {

    try {
      const res = await axios({
        method : 'get',
        url: `${REST_CLUB_API}/club/detail?clubSeq=${seq}`,
        headers: {
          Authorization: `Bearer ${token.value}`, 
        }
      })
      return [res.data.responseClubDetailDtoClub, res.data.responseClubDetailDtoMembers]
      
    } catch(err) {
      console.log(err)
    }
  }

  return { 
    createClub,
    checkDuplicate,
    // 소모임 전체보기
    getClubs,
    clubs,
    // 소모임 디테일보기
    findClub,
  }

})
