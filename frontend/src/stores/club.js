import { defineStore } from 'pinia'
// import axios from 'axios'
import { axios } from '@/stores/jwtFilter'
import { useCounterStore } from './counter'
import { useRouter } from 'vue-router'

export const useClubStore = defineStore('club', {
  // 상태 정의
  state: () => ({
    clubSeq: null, // 초기 상태값 설정
    clubDetail: {}, // 초기 상태값 설정
    clubApplicants: {},
    planList: {},
    planSeq: null,
    planDetail: {}
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
        alert('가입 신청 되었습니다.')
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
        alert('소모임 조건이 맞지 않습니다.')
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
        alert('가입 신청 취소하였습니다.')
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        alert('가입 신청 취소 실패했습니다.')
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
        alert('탈퇴하였습니다.')
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
        alert('탈퇴 실패하였습니다.')
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
        alert('가입 신청 리스트 조회 실패하였습니다.')
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
        alert('추방하였습니다.')
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        alert('추방 실패하였습니다.')
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
        alert('가입 승인하였습니다.')
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        alert('가입 승인 실패하였습니다.')
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
        alert('가입 거절하였습니다.')
        window.location.reload() // 상태 업데이트 방법에 대해 고려 필요
      } catch (error) {
        console.log(error)
        alert('가입 거절 실패하였습니다.')
      }
    },
    async delete() {
      const counterstore = useCounterStore()
      const router = useRouter()
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
        alert('소모임 삭제하였습니다.')
        router.push({ name: 'club' })
        console.log(response)
      } catch (error) {
        console.log(error)
        alert('혼자일때만 삭제 가능합니다.')
      }
    },
    async getPlanList() {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'get',
          url: `https://i10a808.p.ssafy.io/api/plan/${this.clubSeq}/list`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        })
        console.log(response)
        this.planList = response.data
      } catch (error) {
        console.log(error)
        alert('소모임 일정List 가져오기 실패')
      }
    },
    async postPlanRegist(eventDetails) {
      const counterstore = useCounterStore()

      const startTime = `${eventDetails.date}T${eventDetails.time}:00Z`
      console.log('startTime = ' + startTime)

      try {
        const response = await axios({
          method: 'post',
          url: `https://i10a808.p.ssafy.io/api/plan/regist`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          },
          data: {
            clubSeq: this.clubSeq,
            recordSeq: null,
            title: eventDetails.title,
            content: eventDetails.note,
            startTime: startTime,
            duration: null,
            latitude: null,
            longitude: null,
            maxCapacity: eventDetails.participants,
            location: eventDetails.location
          }
        })
        console.log(response)
      } catch (error) {
        console.log(error)
        alert('일정 등록 실패')
      }
    },
    async getPlanDetail() {
      const counterstore = useCounterStore()
      try {
        const response = await axios({
          method: 'get',
          url: `https://i10a808.p.ssafy.io/api/plan/${this.planSeq}/detail`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        })
        console.log(response)
        this.planDetail = response.data
      } catch (error) {
        console.log(error)
        alert('소모임 일정Detail 가져오기 실패')
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
