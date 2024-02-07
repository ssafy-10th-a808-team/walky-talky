// import { defineStore } from 'pinia'
// import axios from 'axios'
// import { useCounterStore } from './counter'

// export const useClubStore = defineStore('club', () => {
//   const counterstore = useCounterStore()

//   const clubSeq = ref()
//   const clubDetail = ref({})

//   async function findClub(seq) {
//     try {
//       const response = await axios({
//         method: 'get',
//         url: `https://i10a808.p.ssafy.io/api/club/detail?clubSeq=${seq}`,
//         headers: {
//           Authorization: `Bearer ${counterstore.getCookie('atk')}`
//         }
//       })
//       console.log(response)
//       return response.data // 응답 데이터로 상태 업데이트
//     } catch (err) {
//       console.error(err)
//       alert('소모임 정보를 가져오는데 실패했습니다.')
//     }
//   }

//   async function apply(seq) {
//     console.log('seq = ' + seq)
//     try {
//       const response = await axios({
//         method: 'post',
//         url: `https://i10a808.p.ssafy.io/api/club-member/apply`,
//         headers: {
//           Authorization: `Bearer ${counterstore.getCookie('atk')}`
//         },
//         data: {
//           clubSeq: seq
//         }
//       })
//       console.log(response)
//       window.location.reload()
//     } catch (error) {
//       console.log(error)
//     }
//   }

//   async function applyCancel(seq) {
//     console.log('seq = ' + seq)
//     try {
//       const response = await axios({
//         method: 'post',
//         url: `https://i10a808.p.ssafy.io/api/club-member/apply-cancel`,
//         headers: {
//           Authorization: `Bearer ${counterstore.getCookie('atk')}`
//         },
//         data: {
//           clubSeq: seq
//         }
//       })
//       console.log(response)
//       window.location.reload()
//     } catch (error) {
//       console.log(error)
//     }
//   }

//   async function withdraw(seq) {
//     console.log('seq = ' + seq)
//     try {
//       const response = await axios({
//         method: 'post',
//         url: `https://i10a808.p.ssafy.io/api/club-member/withdraw`,
//         headers: {
//           Authorization: `Bearer ${counterstore.getCookie('atk')}`
//         },
//         data: {
//           clubSeq: seq
//         }
//       })
//       console.log(response)
//       window.location.reload()
//     } catch (error) {
//       console.log(error)
//     }
//   }
// })

import { defineStore } from 'pinia'
import axios from 'axios'
import { useCounterStore } from './counter'

export const useClubStore = defineStore('club', {
  // 상태 정의
  state: () => ({
    clubSeq: null, // 초기 상태값 설정
    clubDetail: {}, // 초기 상태값 설정
    clubApplicants: {}
  }),
  // 행동(액션) 정의
  actions: {
    async findClub(seq) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'get',
          url: `https://i10a808.p.ssafy.io/api/club/detail?clubSeq=${seq}`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        })
        console.log(response)
        this.clubDetail = response.data // 상태 업데이트
        return response.data
      } catch (err) {
        console.error(err)
        alert('소모임 정보를 가져오는데 실패했습니다.')
      }
    },
    async apply(seq) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club-member/apply`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: seq
          }
        })
        console.log(response)
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
      }
    },
    async applyCancel(seq) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club-member/apply-cancel`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: seq
          }
        })
        console.log(response)
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
      }
    },
    async withdraw(seq) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club-member/withdraw`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: seq
          }
        })
        console.log(response)
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
      }
    },
    async applyList() {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'get',
          url: `https://i10a808.p.ssafy.io/api/club-member/apply/list?clubSeq=${this.clubSeq}`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        })
        console.log(response)
        this.clubApplicants = response.data
      } catch (error) {
        console.log(error)
      }
    },
    async exclude(clubmember) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club-member/exclude`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: this.clubSeq,
            memberSeq: clubmember.seq
          }
        })
        console.log(response)
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
      }
    },
    async applyAccept(clubmember) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club-member/apply/accept`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: this.clubSeq,
            memberSeq: clubmember.memberSeq
          }
        })
        console.log(response)
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
      }
    },
    async applyReject(clubmember) {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club-member/apply/reject`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: this.clubSeq,
            memberSeq: clubmember.memberSeq
          }
        })
        console.log(response)
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
      }
    },
    async delete() {
      const counterstore = useCounterStore()

      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/club/delete`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: this.clubSeq
          }
        })
        console.log(response)
      } catch (error) {
        console.log(error)
      }
    }
  },
  // 지속성 설정
  persist: {
    enabled: true,
    strategies: [
      {
        key: 'myClubState',
        storage: window.localStorage // localStorage를 사용하여 상태 저장
      }
    ]
  }
})
