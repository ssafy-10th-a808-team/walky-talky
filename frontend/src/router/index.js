import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '@/views/HomeView.vue'
// member router 멤버
import Signup from '@/views/member/Signup.vue'
import Login from '@/views/member/Login.vue'
import Logout from '@/views/member/Logout.vue'
import RecordScrapList from '@/views/member/RecordScrapList.vue'
import MyLocationView from '@/views/member/MyLocationView.vue'
import Mypage from '@/views/member/Mypage.vue'
import ModifyInfo from '@/views/member/ModifyInfo.vue'

// walk router 산책
import DoWalk from '@/views/walk/DoWalk.vue'
import dowalk2 from '@/views/walk/dowalk2.vue' //임시
import WalkList from '@/views/walk/WalkList.vue'

// club router 소모임
import ClubView from '@/views/club/ClubView.vue'
import ClubMemory from '@/views/club/ClubMemoryView.vue'
import ClubCreate from '@/views/club/ClubCreateView.vue'
import ClubDetail from '@/views/club/ClubDetailView.vue'
import ClubModify from '@/views/club/ClubModifyView.vue'

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
      path: '/member/mypage',
      name: 'Mypage',
      component: Mypage
    },
    {
      path: '/member/modify-info',
      name: 'ModifyInfo',
      component: ModifyInfo
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
      path: '/walk/do-walk2',
      name: 'dowalk2',
      component: dowalk2
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
      path: '/club/modify',
      name: 'ClubModify',
      component: ClubModify,
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
      path: '/shareBoard',
      name: 'share-board',
      component: ShareBoardView
    },
    {
      path: '/shareBoard/view/:seq',
      name: 'share-board-view',
      component: ShareBoardDetailView
    }
  ]
})
import { useMemberStore } from '@/stores/member'
import ModifyInfoVue from '@/views/member/ModifyInfo.vue'
router.beforeEach((to, from) => {
  const memberstore = useMemberStore()
  if (
    to.name !== 'home' &&
    to.name !== 'Login' &&
    to.name !== 'Signup' &&
    to.name !== 'mylocation' &&
    !memberstore.isLogin
  ) {
    window.alert('로그인이 필요합니다')
    return { name: 'home' }
  }
  if ((to.name === 'Signup' || to.name === 'Login') && memberstore.isLogin) {
    window.alert('이미 로그인하셨습니다')
    return { name: 'club' }
  }
})

export default router
