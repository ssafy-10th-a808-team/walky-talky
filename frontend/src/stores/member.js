import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'

const REST_MEMBER_API = 'https://i10a808.p.ssafy.io'

export const useMemberStore = defineStore('member', () => {
  const memberList = ref([])
  const token = ref(
    'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzZXEiOjI4LCJpYXQiOjE3MDY2NjI3NTksImlzcyI6IndhbGt5dGFsa3kiLCJleHAiOjE3MDcyNjc1NTl9.wRThHDgFntrbykO0b9tFSyIHTZSZPpleibJb20grlo4'
  )

  //유저 리스트 가져오기
  const getMemberList = function () {
    axios
      .get(REST_MEMBER_API)
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
    axios.get(`${REST_MEMBER_API}/${memberId}`).then((response) => {
      member.value = response.data
    })
  }

  //회원가입
  const createMember = function (payload) {
    const formData = new FormData()
    formData.append('multipartFile', payload.profileImg)
    formData.append('id', payload.memberId)
    formData.append('password', payload.password)
    formData.append('birth', payload.birth)
    formData.append('gender', payload.gender)
    formData.append('nickname', payload.nickname)
    formData.append('introduce', payload.introduce)
    formData.append('regionCd', payload.region_cd)

    axios({
      method: 'post',
      url: `${REST_MEMBER_API}/api/member/local-signup`,
      headers: {
        Authorization: `Bearer ${token.value}`,
        'Content-Type': 'multipart/form-data'
      },
      data: formData
    })
      .then((res) => {
        console.log(res)
        alert('회원가입을 축하드립니다! 로그인을 해주세요')
        router.push({ name: 'Login' })
      })
      .catch((err) => {
        console.log(err)
        console.log(err.message)
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
    axios.post('http://localhost:8080/login', member.value).then((response) => {
      alert(`${response.data.memberName}님 반갑습니다!!`)
      localStorage.setItem('loginMember', response.data)
      loginMember.value.push(response.data)
      console.log(loginMember.value)
      router.push(`/ssafit/`)
    })
  }

  const logout = function () {
    axios.get('http://localhost:8080/logout').then((response) => {
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
    axios.put(REST_MEMBER_API, loginMember.value[0]).then(() => {
      router.push(`/ssafit/member/${loginMember.value[0].memberId}`)
    })
  }

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
    updateMember
  }
})
