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

  return { icons, selectButton }
})
