import { defineStore } from 'pinia'
import { ref } from 'vue'
import axios from 'axios'
import { useCounterStore } from './counter'

export const useClubStore = defineStore('club', () => {
  const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api/club'
  const counterstore = useCounterStore()

  const clubDetail = ref(null) // 스토어 상태에 추가

  const findClub = async function (seq) {
    try {
      const response = await axios({
        method: 'get',
        url: `${REST_CLUB_API}/detail?clubSeq=${seq}`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
      clubDetail.value = response.data // 응답 데이터로 상태 업데이트
    } catch (err) {
      console.error(err)
      alert('소모임 정보를 가져오는데 실패했습니다.')
    }
  }

  // // 소모임 정보 수정
  // const modifyClub = function (payload) {
  //   const formData = new FormData()
  //   if (payload.profileImg) {
  //     formData.append('multipartFile', payload.profileImg)
  //   }
  //   formData.append('clubSeq', payload.clubSeq)
  //   formData.append('name', payload.clubname)
  //   formData.append('introduce', payload.introduce)
  //   formData.append('regionCd', payload.region_cd)
  //   formData.append('young_birth', payload.young_birth)
  //   formData.append('old_birth', payload.old_birth)
  //   formData.append('gender_type', payload.gender_type)
  //   formData.append('max_capacity', payload.max_capacity)
  //   formData.append('is_auto_recruit', payload.is_auto_recruit)
  //   formData.append('is_open_recruit', payload.is_auto_recruit)
  //     axios({
  //       method: 'post',
  //       url: `${REST_CLUB_API}/club/modify`,
  //       headers: {
  //         Authorization: `Bearer ${counterstore.getCookie("atk")}`,
  //         'Content-Type': 'multipart/form-data',
  //       },
  //       data: formData,

  //     })
  //     .then((res) => {
  //         console.log(res)
  //         alert('소모임 수정 성공')
  //         router.push({name : 'club-detail'})
  //     })
  //     .catch((err) => {
  //       // for (var pair of formData.entries()) {
  //       //   console.log(pair[0] + ', ' + pair[1]);
  //       // }
  //       console.log(err)
  //       console.log(err.message)

  //     })
  //   }

  return {
    clubDetail,
    findClub
  }
})
