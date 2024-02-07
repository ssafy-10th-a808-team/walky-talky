<template>
  <div>
    <nav id="navbar" class="navbar">
      <ul>
        <li>
          <RouterLink :to="{ name: 'club-detail', params: { seq: clubstore.clubSeq } }" class="nav-link scrollto">
            홈
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="{ name: 'club-plan', params: { seq: clubstore.clubSeq } }" class="nav-link scrollto">
            일정
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="{ name: 'club-chat2', params: { seq: clubstore.clubSeq } }" class="nav-link scrollto">
            채팅
          </RouterLink>
        </li>
        <li v-if="clubstore.clubDetail.role === 'owner'">
          <RouterLink :to="{ name: 'club-setting-member', params: { seq: clubstore.clubSeq } }" class="nav-link scrollto">
            설정
          </RouterLink>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { useClubStore } from '@/stores/club'
const clubstore = useClubStore()
</script>

<style scoped>
#navbar {
  background-color: darkgreen;
  /* 상단 바의 배경색 */
  padding: 10px 20px;
  /* 상단 바의 패딩 */
}

.navbar {
  display: flex;
  justify-content: center;
  align-items: center;
}

.navbar ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  gap: 20px;
  /* 메뉴 아이템 사이의 간격 */
}

.nav-link {
  text-decoration: none;
  color: #ffffff;
  /* 링크의 글꼴 색상 */
  font-weight: bold;
  /* 글꼴 두께 */
  transition: color 0.3s ease-in-out;
  /* 호버 효과를 위한 색상 전환 */

  font-size: 20px;
}

.nav-link:hover,
.nav-link:focus {
  color: #ffc107;
  /* 링크에 마우스를 올렸을 때의 색상 */
}

/* 현재 활성화된 링크의 스타일 */
.nav-link.active {
  color: #ffd700;
  border-bottom: 2px solid #ffffff;
  /* 활성 링크 아래에 흰색 테두리 추가 */
}
</style>