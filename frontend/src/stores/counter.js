// import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCounterStore = defineStore('counter', () => {
  const selectedButton = ref(null)

  const icons = ref([
      { name:'LogoIcon', icon: 'src/assets/img/Logo.png', urlName:'home' },
      { name:'ProfileIcon', icon: 'src/assets/img/ProfileIcon.png', urlName:'mypage' },
      { name:'LocationIcon', icon: 'src/assets/img/LocationIcon.png', urlName:'mylocation' },

  ])

  const selectButton = (name) => {
      const selectedIcon = icons.value.find(icon => icon.name === name)
      if (selectedIcon) {
          selectedButton.value = selectedIcon
      } else {
          console.error(`Icon with name ${name} not found.`)
      }
      
  }

  return { icons, selectButton, selectedButton }
})
