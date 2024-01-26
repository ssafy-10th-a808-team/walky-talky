import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
// member router 멤버
import Signup from '@/views/member/Signup.vue'
import RecordScrapList from '@/views/member/RecordScrapList.vue'
import MyLocationView from '@/views/member/MyLocationView.vue'

// walk router 산책
import DoWalk from '@/views/walk/DoWalk.vue'
import WalkList from '@/views/walk/WalkList.vue'

// group router 소모임
import GroupView from '@/views/group/GroupView.vue'
import GroupMemory from '@/views/group/GroupMemoryView.vue'

// shareboard router 산책공유게시판
import ShareBoardView from '@/views/shareboard/ShareBoardView.vue'
const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView
    },
    // member router  멤버
    {
      path: '/member/local-signup',
      name: 'Signup',
      component: Signup
    },
    {
      path: '/mylocation',
      name: 'mylocation',
      component: MyLocationView
    },
    {
      path: '/record-scrap/list',
      name: 'RecordScrapList',
      component: RecordScrapList
    },

    // walk router 산책
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

    // group router 소모임
    {
      path: '/group',
      name: 'group',
      component: GroupView
    },
    {
      path: '/group/memory',
      name: 'group-memory',
      component: GroupMemory
    },

    // shareboard router 산책공유게시판   
    {
      path: '/share-board',
      name: 'share-board',
      component: ShareBoardView
    },





  ]
})

export default router
