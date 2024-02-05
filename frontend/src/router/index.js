import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
// member router 멤버
import Signup from '@/views/member/Signup.vue'
import Login from '@/views/member/Login.vue'
import Logout from '@/views/member/Logout.vue'
import RecordScrapList from '@/views/member/RecordScrapList.vue'
import MyLocationView from '@/views/member/MyLocationView.vue'

// walk router 산책
import DoWalk from '@/views/walk/DoWalk.vue'
import WalkList from '@/views/walk/WalkList.vue'

// club router 소모임
import ClubView from '@/views/club/ClubView.vue'
import ClubMemory from '@/views/club/ClubMemoryView.vue'
import ClubCreate from '@/views/club/ClubCreateView.vue'
import ClubDetail from '@/views/club/ClubDetailView.vue'

// shareboard router 산책공유게시판
import ShareBoardView from '@/views/shareboard/ShareBoardView.vue'
import ShareBoardDetailView from '@/views/shareboard/ShareBoardDetailView.vue'

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
      path: '/member/local-login',
      name: 'Login',
      component: Login
    },
    {
      path: '/member/logout',
      name: 'Logout',
      component: Logout
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

    // club router 소모임
    {
      path: '/club',
      name: 'club',
      component: ClubView
    },
    {
      path: '/club/detail/:seq',
      name: 'club-detail',
      component: ClubDetail,
      props: true
    },
    {
      path: '/club/memory',
      name: 'club-memory',
      component: ClubMemory
    },
    {
      path: '/club/create',
      name: 'club-create',
      component: ClubCreate
    },

    // shareboard router 산책공유게시판
    {
      path: '/share-board',
      name: 'share-board',
      component: ShareBoardView
    },
    {
      path: '/share-board-detail',
      name: 'share-board',
      component: ShareBoardDetailView
    }
  ]
})
import { useMemberStore } from '@/stores/member'
router.beforeEach((to, from) => {
  const memberstore = useMemberStore()
  if (to.name !== 'home' && to.name !== 'Login' && to.name !== 'Signup' && !memberstore.isLogin) {
    window.alert('로그인이 필요합니다')
    return { name: 'home' }
  }
  if ((to.name === 'Signup' || to.name === 'Login') && memberstore.isLogin) {
    window.alert('이미 로그인하셨습니다')
    return { name: 'club' }
  }
})

export default router
