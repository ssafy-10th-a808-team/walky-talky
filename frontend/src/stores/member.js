import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'

const REST_USER_API = `http://localhost:8080/ssafit/member`

export const useMemberStore = defineStore('member', () => {
  const memberList = ref([])

  //유저 리스트 가져오기
  const getMemberList = function () {
    axios
      .get(REST_USER_API)
      .then((response) => {
        memberList.value = response.data
      })
      .catch((err) => {
        // 오류나면 처리가능
        console.log(err)
      })
  }
  //회원정보 한개
  const member = ref({})
  const getMember = function (memberId) {
    axios.get(`${REST_USER_API}/${memberId}`).then((response) => {
      member.value = response.data
    })
  }

  //회원가입
  const createMember = function (member) {
    axios({
      url: REST_USER_API,
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      data: member
    })
      .then((res) => {
        alert('회원가입을 축하드립니다! 로그인을 해주세요')
        router.push({ name: 'Login' })
      })
      .catch((err) => {
        console.log(err)
      })
  }

  const loginMember = ref([])
  if (localStorage.getItem('loginMember') != null) {
    loginMember.value.push(localStorage.getItem('loginMember'))
    //페이지 로딩시 로컬스토리지에 로그인 정보가 남아있으면 바로 로그인 정보를 수토어 유저 정보에 할당
  }

  const login = function (member) {
    axios.interceptors.response.use(
      function (response) {
        return response
      },
      function (error) {
        console.log(error)
        alert('아이디나 비밀번호가 틀렸습니다.')
        return Promise.rejecterror
      }
    )
    axios.post('http://localhost:8080/ssafit/login', member.value).then((response) => {
      alert(`${response.data.memberName}님 반갑습니다!!`)
      localStorage.setItem('loginMember', response.data)
      loginMember.value.push(response.data)
      console.log(loginMember.value)
      router.push(`/ssafit/`)
    })
  }

  const logout = function () {
    axios.get('http://localhost:8080/ssafit/logout').then((response) => {
      localStorage.removeItem('loginMember')
      loginMember.value.pop()
      router.push(`/ssafit`)
    })
  }

  const selectedMember = ref(null)
  const clickMember = function (member) {
    selectedMember.value = member
    router.push(`/ssafit/member/${selectedMember.value.memberId}`)
  }
  // 회원정보 수정
  const updateMember = function () {
    axios.put(REST_USER_API, loginMember.value[0]).then(() => {
      router.push(`/ssafit/member/${loginMember.value[0].memberId}`)
    })
  }

  //찜목록
  const likeList = ref([
    {
      id: 'VVZSGp7Zxgt9SRCW',
      src: 'https://www.youtube.com/embed/4EKo44DUvjg?si=VVZSGp7Zxgt9SRCW',
      title: '살이 너무 빠지는 죽음의 타바타 운동',
      channelTitle: '에이핏 afit'
    },
    {
      id: 'HeklMRn5BiB06449',
      src: 'https://www.youtube.com/embed/qaBIL8NMCMo?si=HeklMRn5BiB06449',
      title: '무.조.건! 살빠지는 댄스 다이어트',
      channelTitle: 'Thankyou BUBU'
    },
    {
      id: 'vGlNffmg6CYuHAoA',
      src: 'https://www.youtube.com/embed/2paxL9MmxWM?si=vGlNffmg6CYuHAoA',
      title: '실제 감량 후기 폭발했던 4세대 여돌 플레이리스트로 2주',
      channelTitle: '흥둥이'
    },
    {
      id: 'RdH5sIGogyKOBKBD',
      src: 'https://www.youtube.com/embed/wqIC92AGiLA?si=RdH5sIGogyKOBKBD',
      title: ' 헬스의 신 김종국한테 PT 받은 썰 푼다',
      channelTitle: '디글 :Diggle'
    },
    {
      id: 'e8Jix_CBex71EZPS',
      src: 'https://www.youtube.com/embed/VFyBl2hYKH8?si=e8Jix_CBex71EZPS',
      title: '쉽고 재밌고 확실히 빠진다 - 2주 땀범벅 다이어트 챌린지',
      channelTitle: '빅씨스 Bigsis'
    },
    {
      id: 'DTF2UMS9RKkpRxC_',
      src: 'https://www.youtube.com/embed/zcTIawVsypk?si=DTF2UMS9RKkpRxC_',
      title: '홈점핑 준비운동',
      channelTitle: '집순이점핑'
    },
    {
      id: '3E0QSeX980Kw1sI8',
      src: 'https://www.youtube.com/embed/odc9wVM6o5U?si=3E0QSeX980Kw1sI8',
      title: '전 세계 및 나라별 가장 인기 있는 스포츠 TOP10',
      channelTitle: '박축공 Football Park'
    },
    {
      id: 'CvVMG8FKL5FHcTlL',
      src: 'https://www.youtube.com/embed/efiqud5yKLw?si=CvVMG8FKL5FHcTlL',
      title: '10분 공복 모닝 홈트',
      channelTitle: '빅씨스 Bigsis'
    },
    {
      id: '0OhcnGt4dXEeVzdM',
      src: 'https://www.youtube.com/embed/0iqP6WP2ET4?si=0OhcnGt4dXEeVzdM',
      title: '뱃살빼기! 효과보장!!',
      channelTitle: 'MIZI'
    },
    {
      id: 'GqQqyFoCMb5drqjo',
      src: 'https://www.youtube.com/embed/dZbPtAgofwI?si=GqQqyFoCMb5drqjo',
      title: '줌바댄스 1000칼로리 소비',
      channelTitle: '다이어트의 모든것'
    }
  ])

  return {
    memberList,
    member,
    getMember,
    getMemberList,
    createMember,
    login,
    loginMember,
    logout,
    selectedMember,
    clickMember,
    updateMember,
    likeList
  }
})
