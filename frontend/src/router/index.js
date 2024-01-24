import { createRouter, createWebHistory } from 'vue-router'

import Signup from '@/components/member/Signup.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',

    },
    {
      path:"/member/local-signup/",
      name:"Signup",
      component:Signup
    }
  ]
})

export default router
