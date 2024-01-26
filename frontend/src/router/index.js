import { createRouter, createWebHistory } from 'vue-router'

import Signup from '@/views/member/Signup.vue'
import RecordScrapList from '@/views/member/RecordScrapList.vue'

import DoWalk from '@/views/walk/DoWalk.vue'
import WalkList from '@/views/walk/WalkList.vue'

import GroupView from '@/views/GroupView.vue'
import WalkView from '@/views/WalkView.vue'
import ShareBoardView from '@/views/ShareBoardView.vue'
import HomeView from '@/views/HomeView.vue'
import MyLocationView from '@/views/MyLocationView.vue'

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
    },
    {
      path: '/mylocation',
      name: 'mylocation',
      component: MyLocationView
    },
    {
      path: '/member/local-signup',
      name: 'Signup',
      component: Signup
    },
    {
      path: '/walk/do-walk',
      name: 'DoWalk',
      component: DoWalk
    },
    {
      path: '/walk/list',
      name: 'WalkList',
      component: WalkList
    },
    {
      path: '/record-scrap/list',
      name: 'RecordScrapList',
      component: RecordScrapList
    }
  ]
})

export default router
