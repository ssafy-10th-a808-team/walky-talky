import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'

const REST_MEMBER_API = `http://localhost:8080/member`

export const useMemberStore = defineStore('member', () => {
  const memberList = ref([])
  const address_name = ref('')
  const address_code = ref('')

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
  const createMember = function (member) {
    axios({
      url: REST_MEMBER_API + '/local-signup',
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
        console.log(member.id)
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

  // 지역코드 및 주소 가져오기
  const getLocationInfo = () => {
    return [address_name.value, address_code.value]
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
    updateMember,
    // 지역 가져오기 카카오맵
    address_name,
    address_code,
    getLocationInfo,

  }
})
