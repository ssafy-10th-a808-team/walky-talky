import { createRouter, createWebHistory } from 'vue-router'

import Signup from '@/components/member/Signup.vue'

import GroupView from '@/views/GroupView.vue'
import WalkView from '@/views/WalkView.vue'
import ShareBoardView from '@/views/ShareBoardView.vue'
import HomeView from '@/views/HomeView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    {
      path: '/group',
      name: 'group',
      component: GroupView
    },
    {
      path: '/walk',
      name: 'walk',
      component: WalkView
    },
    {
      path: '/share-board',
      name: 'share-board',
      component: ShareBoardView
    }
  ]
})

export default router
