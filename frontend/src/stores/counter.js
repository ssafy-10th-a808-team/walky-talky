// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCounterStore = defineStore('counter', () => {
  const icons = ref([
    { name: 'LogoIcon', icon: '/src/assets/img/Logo.png', urlName: 'home' },
    { name: 'ProfileIcon', icon: '/src/assets/img/ProfileIcon.png', urlName: 'home' },
    { name: 'LocationIcon', icon: '/src/assets/img/LocationIcon.png', urlName: 'mylocation' }
  ])

  const selectButton = (alt) => {
    return icons.value.find((icon) => icon.name === alt)
  }

  //// token 쿠키 관리
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

  return {
    icons,
    selectButton,
    getCookie,
    setCookie,
    deleteCookie
  }
})
