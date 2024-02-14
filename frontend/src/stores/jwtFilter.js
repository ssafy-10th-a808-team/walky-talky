import axios from 'axios'
import { useRouter } from 'vue-router'

const router = useRouter()

// 에러 처리 로직과 토큰 재발급 기능을 포함한 함수 정의
async function refreshAndRetry(originalRequest) {
  try {
    // 토큰 재발급 요청
    const reissueResponse = await axios.post(
      'https://i10a808.p.ssafy.io/api/member/reissue',
      null,
      {
        headers: {
          Authorization: `Bearer ${getCookie('rtk')}`
        }
      }
    )
    // 새로운 토큰으로 쿠키 및 헤더 업데이트
    deleteCookie('atk')
    deleteCookie('rtk')
    setCookie('atk', reissueResponse.headers.atk)
    setCookie('rtk', reissueResponse.headers.rtk)

    // 다시 원래 요청을 보내기 전에 헤더 업데이트
    originalRequest.headers.Authorization = `Bearer ${reissueResponse.headers.atk}`

    // 새로운 토큰으로 재시도
    const retryResponse = await axios(originalRequest)

    // 재시도가 성공하면 해당 응답 반환
    return retryResponse
  } catch (err) {
    // 토큰 재발급 실패 시 처리
    deleteCookie('atk')
    deleteCookie('rtk')
    deleteCookie('nickname')
    deleteCookie('profileImage')
    localStorage.clear()

    window.location.href = '/'
    router.push({ name: 'home' })
  }
}

// Axios에 응답 인터셉터 등록
axios.interceptors.response.use(
  (response) => response,
  async (error) => {
    if (error.response) {
      if (error.response.status === 401) {
        const originalRequest = error.config

        try {
          // 토큰 재발급 및 재시도
          const retryResponse = await refreshAndRetry(originalRequest)
          return retryResponse
        } catch (retryError) {
          // 토큰 재발급 및 재시도 실패 시 처리
        }
      } else if (error.response.status === 403) {
        deleteCookie('atk')
        deleteCookie('rtk')
        deleteCookie('nickname')
        deleteCookie('profileImage')
        localStorage.clear()

        window.location.href = '/'
        router.push({ name: 'home' })
      } else {
        // 401 에러가 아닌 다른 에러는 그대로 반환
        console.log('filter')
        console.log(error)
        return Promise.reject(error)
      }
    } else {
      // 401 에러가 아닌 다른 에러는 그대로 반환
      console.log('filter')
      console.log(error)
      return Promise.reject(error)
    }
  }
)

///// token 쿠키 관리/////
const getCookie = (name) => {
  let matches = document.cookie.match(
    new RegExp('(?:^|; )' + name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') + '=([^;]*)')
  )
  return matches ? decodeURIComponent(matches[1]) : undefined
}

const setCookie = (name, value, options = {}) => {
  options = {
    path: '/',
    // 필요한 경우, 옵션 기본값을 설정할 수도 있습니다.
    ...options
  }

  if (options.expires instanceof Date) {
    options.expires = options.expires.toUTCString()
  }

  let updatedCookie = encodeURIComponent(name) + '=' + encodeURIComponent(value)

  for (let optionKey in options) {
    updatedCookie += '; ' + optionKey
    let optionValue = options[optionKey]
    if (optionValue !== true) {
      updatedCookie += '=' + optionValue
    }
  }

  document.cookie = updatedCookie
}

const deleteCookie = (name) => {
  setCookie(name, '', {
    'max-age': -1
  })
}

export { axios }
