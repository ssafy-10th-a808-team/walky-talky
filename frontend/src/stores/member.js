import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import router from '@/router'
import axios from 'axios'
import { useCounterStore } from './counter'

const REST_MEMBER_API = 'https://i10a808.p.ssafy.io'

export const useMemberStore = defineStore(
  'member',
  () => {
    const counterstore = useCounterStore()
    const memberList = ref([])
    const address_name = ref('')
    const address_code = ref('')
    const token = ref(null)
    const memberId = ref('')
    const nickname = ref('')
    const profileImage = ref('')
    const imgUrl = ref('')
    const isOauth = ref(false)

    //회원가입
    const createMember = function (payload) {
      const formData = new FormData()
      if (payload.profileImg) {
        formData.append('multipartFile', payload.profileImg)
      }
      formData.append('id', payload.memberId)
      formData.append('password', payload.password)
      formData.append('birth', payload.birth)
      formData.append('gender', payload.gender)
      formData.append('nickname', payload.nickname)
      formData.append('introduce', payload.introduce)
      formData.append('regionCd', payload.region_cd)
      formData.append('imgUrl', payload.imgUrl)

      axios({
        method: 'POST',
        url: `${REST_MEMBER_API}/api/member/local-signup`,
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
    const checkId = function (memberId) {
      axios({
        method: 'POST',
        url: `${REST_MEMBER_API}/api/member/check-id`,
        data: {
          id: memberId
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
        url: `${REST_MEMBER_API}/api/member/check-nickname`,
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

    // const loginMember = ref([])
    // if (localStorage.getItem('loginMember') != null) {
    //   loginMember.value.push(localStorage.getItem('loginMember'))
    //   //페이지 로딩시 로컬스토리지에 로그인 정보가 남아있으면 바로 로그인 정보를 수토어 유저 정보에 할당
    // }

    // 로그인
    const login = (payload) => {
      axios({
        method: 'post',
        url: `${REST_MEMBER_API}/api/member/local-login`,
        data: {
          memberId: payload.memberId,
          password: payload.password
        }
      })
        .then((res) => {
          alert('로그인 성공')
          token.value = res.headers.get('atk')

          counterstore.setCookie('atk', token.value)
          counterstore.setCookie('nickname', res.data.data.nickname)
          counterstore.setCookie('profileImage', res.data.data.profileImage)
          nickname.value = res.data.data.nickname
          profileImage.value = res.data.data.profileImage

          //router.push({ name: 'home' })
          console.log(nickname.value)
          window.location.href = '/'
        })
        .catch((err) => {
          alert('로그인 실패')
          console.log(err)
        })
    }

    const kakaoLogin = () => {
      const clientId = import.meta.env.VITE_KAKAO_CLIENT_Id
      const redirectUri = import.meta.env.VITE_KAKAO_REDIRECT_URI
      const url = `https://kauth.kakao.com/oauth/authorize?client_id=${clientId}&redirect_uri=${redirectUri}&response_type=code`
      window.location.href = url
    }

    const isMember = async (code) => {
      await axios({
        method: 'get',
        url: `${REST_MEMBER_API}/api/oauth/kakao?code=${code}`
      }).then((res) => {
        if (res.status === 201) {
          memberId.value = res.data.id
          nickname.value = res.data.nickname
          profileImage.value = res.data.profileImage
          imgUrl.value = res.data.profileImage
          isOauth.value = true
          router.push({ name: 'Signup' })
        } else if (res.status === 200) {
          token.value = res.headers.get('atk')
          counterstore.setCookie('atk', token.value)
          nickname.value = res.data.data.nickname
          profileImage.value = res.data.profileImage
          router.push({ name: 'home' })
        }
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

    const getIsOauth = () => {
      return profileImage.value
    }

    // 스토어의 상태를 초기화하는 함수
    const resetStore = () => {
      memberList.value = []
      address_name.value = ''
      address_code.value = ''
      token.value = null
      memberId.value = ''
      nickname.value = ''
      profileImage.value = ''
      isOauth.value = false
      // 여기에 더 많은 상태를 초기화하십시오.
    }

    const logout = () => {
      axios({
        method: 'post',
        url: `${REST_MEMBER_API}/api/member/logout`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        }
      })
        .then((res) => {
          alert('로그아웃 성공')

          // 스토어 상태 초기화
          // resetStore()
          nickname.value = null
          profileImage.value = null
          token.value = null
          counterstore.deleteCookie('atk')
          counterstore.deleteCookie('nickname')
          counterstore.deleteCookie('profileImage')
          localStorage.clear()

          window.location.href = '/'
          router.push({ name: 'home' })
        })
        .catch((err) => {
          alert('로그아웃 실패')
          console.log(err)
        })
    }

    // 내 정보
    const mypage = ref([])
    const getMypage = async () => {
      try {
        const res = await axios({
          method: 'get',
          url: `${REST_MEMBER_API}/api/member/mypage`,
          headers: {
            Authorization: `Bearer ${counterstore.getCookie('atk')}`
          }
        })
        console.log(res)
        mypage.value = res.data.data
        nickname.value = res.data.data.nickname
        profileImage.value = res.data.data.profileImage
        address_code.value = res.data.data.regionCd
        address_name.value = res.data.data.address
      } catch (err) {
        console.log(err)
      }
    }

    const modifyInfo = (payload) => {
      const formData = new FormData()
      if (payload.profileImage) {
        formData.append('multipartFile', payload.profileImage)
      }
      formData.append('id', payload.memberId)
      formData.append('nickname', payload.nickname)
      formData.append('introduce', payload.introduce)
      formData.append('regionCd', payload.regionCd)
      axios({
        method: 'post',
        url: `${REST_MEMBER_API}/api/member/modify-info`,
        headers: {
          Authorization: `Bearer ${counterstore.getCookie('atk')}`
        },
        data: formData
      })
        .then((res) => {
          console.log(res)
          alert('정보 변경 성공')
          router.push({ name: 'Mypage' })
        })
        .catch((err) => {
          console.log(err)
          alert('정보 변경 실패')
        })
    }
    // const selectedMember = ref(null)
    // const clickMember = function (member) {
    //   selectedMember.value = member
    //   router.push(`/ssafit/member/${selectedMember.value.memberId}`)
    // }
    // // 회원정보 수정
    // const updateMember = function () {
    //   axios.put(REST_MEMBER_API, loginMember.value[0]).then(() => {
    //     router.push(`/ssafit/member/${loginMember.value[0].memberId}`)
    //   })
    // }

    // 지역코드 및 주소 가져오기
    const getLocationInfo = () => {
      return [address_name.value, address_code.value]
    }

    const getMemberId = () => {
      return memberId.value
    }

    const getNickname = () => {
      return nickname.value
    }

    const getProfileImage = () => {
      return profileImage.value
    }

    const getImgUrl = () => {
      return imgUrl.value
    }

    return {
      memberList,
      // member,
      // getMember,

      createMember,
      // 로그인
      checkId,
      checkNickname,
      login,
      kakaoLogin,
      isMember,
      token,
      nickname,
      profileImage,
      imgUrl,
      // loginMember,
      isLogin,
      // 로그아웃
      logout,
      // 마이페이지
      mypage,
      getMypage,
      modifyInfo,
      // selectedMember,
      // clickMember,
      // updateMember,
      // 지역 가져오기 카카오맵
      address_name,
      address_code,
      getLocationInfo,
      getMemberId,
      getNickname,
      getProfileImage,
      getIsOauth,
      getImgUrl
    }
  },
  { persist: true }
)
