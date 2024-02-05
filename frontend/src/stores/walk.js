import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'
import { useCounterStore } from './counter'

const REST_WALK_API = 'https://i10a808.p.ssafy.io'

export const useWalkStore = defineStore('walk', () => {
  const counterstore = useCounterStore()
  const walkList = ref([])
  const address_name = ref('')
  const address_code = ref('')
  const token = ref(null)
  const nickname = ref('')
  const profileImage = ref('')

  //회원가입
  const createWalk = function (payload) {
    const formData = new FormData()
    if (payload.profileImg) {
      formData.append('multipartFile', payload.profileImg)
    }
    formData.append('id', payload.walkId)
    formData.append('password', payload.password)
    formData.append('birth', payload.birth)
    formData.append('gender', payload.gender)
    formData.append('nickname', payload.nickname)
    formData.append('introduce', payload.introduce)
    formData.append('regionCd', payload.region_cd)

    axios({
      method: 'POST',
      url: `${REST_WALK_API}/api/walk/local-signup`,
      headers: {
        'Content-Type': 'multipart/form-data'
      },
      data: formData
    })
      .then((res) => {
        // console.log(res)
        alert('회원가입을 축하드립니다! 로그인을 해주세요')
        router.push({ name: 'Login' })
      })
      .catch((err) => {
        // console.log(err)
        const errmsg = err.response.data.message
        alert(errmsg)
        console.log(errmsg)
      })
  } // 회원가입 end

  // 아이디 중복 체크
  const checkId = function (walkId) {
    axios({
      method: 'POST',
      url: `${REST_WALK_API}/api/walk/check-id`,
      data: {
        id: walkId
      }
    })
      .then((res) => {
        // console.log(res)
        alert('사용가능한 아이디입니다')
      })
      .catch((err) => {
        // console.log(err)
        const errmsg = err.response.data.message
        console.log(errmsg)
        if (errmsg == 'id is empty') {
          alert('아이디를 입력해주세요')
        } else if (errmsg == '중복된 아이디입니다.') {
          alert('중복된 아이디입니다. 다른 아이디를 입력해주세요')
        }
      })
  } // 아이디 중복 체크 end

  // 닉네임 중복 체크
  const checkNickname = function (nickname) {
    axios({
      method: 'POST',
      url: `${REST_WALK_API}/api/walk/check-nickname`,
      data: {
        nickname: nickname
      }
    })
      .then((res) => {
        console.log(res)
        alert('사용가능한 닉네임입니다')
      })
      .catch((err) => {
        // console.log(err)
        const errmsg = err.response.data.message
        console.log(errmsg)
        if (errmsg == 'nickname is empty') {
          alert('닉네임을 입력해주세요')
        } else if (errmsg == '중복된 닉네임입니다.') {
          alert('중복된 닉네임입니다. 다른 닉네임을 입력해주세요')
        }
      })
  } //닉네임 중복 체크 end

  // const loginWalk = ref([])
  // if (localStorage.getItem('loginWalk') != null) {
  //   loginWalk.value.push(localStorage.getItem('loginWalk'))
  //   //페이지 로딩시 로컬스토리지에 로그인 정보가 남아있으면 바로 로그인 정보를 수토어 유저 정보에 할당
  // }

  // 로그인
  const login = (payload) => {
    axios({
      method: 'post',
      url: `${REST_WALK_API}/api/walk/local-login`,
      data: {
        walkId: payload.walkId,
        password: payload.password
      }
    })
      .then((res) => {
        alert('로그인 성공')
        token.value = res.headers.get('atk')
        counterstore.setCookie('atk', token.value)
        nickname.value = res.data.data.nickname
        profileImage.value = res.data.data.profileImage
        router.push({ name: 'home' })
      })
      .catch((err) => {
        alert('로그인 실패')
        console.log(err)
      })
  }

  // const isLogin = computed(() => {
  //   if (counterstore.getCookie("atk") === null) {
  //     return false
  //   } else {
  //     return true
  //   }
  // })
  const isLogin = computed(() => counterstore.getCookie('atk') !== undefined)

  const logout = () => {
    axios({
      method: 'post',
      url: `${REST_WALK_API}/api/walk/logout`,
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        alert('로그아웃 성공')
        nickname.value = null
        profileImage.value = null
        token.value = null
        counterstore.deleteCookie('atk')
        router.push({ name: 'home' })
      })
      .catch((err) => {
        alert('로그아웃 실패')
        console.log(err)
      })
  }

  // const selectedWalk = ref(null)
  // const clickWalk = function (walk) {
  //   selectedWalk.value = walk
  //   router.push(`/ssafit/walk/${selectedWalk.value.walkId}`)
  // }
  // // 회원정보 수정
  // const updateWalk = function () {
  //   axios.put(REST_WALK_API, loginWalk.value[0]).then(() => {
  //     router.push(`/ssafit/walk/${loginWalk.value[0].walkId}`)
  //   })
  // }

  // 지역코드 및 주소 가져오기
  const getLocationInfo = () => {
    return [address_name.value, address_code.value]
  }

  return {
    walkList,
    // walk,
    // getWalk,

    createWalk,
    // 로그인
    checkId,
    checkNickname,
    login,
    token,
    nickname,
    profileImage,
    // loginWalk,
    isLogin,
    // 로그아웃
    logout,
    // selectedWalk,
    // clickWalk,
    // updateWalk,
    // 지역 가져오기 카카오맵
    address_name,
    address_code,
    getLocationInfo
  }
})
