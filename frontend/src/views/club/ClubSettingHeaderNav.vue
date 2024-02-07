<template>
  <div class="settings-nav-container">
    <nav class="settings-navbar">
      <ul>
        <li>
          <RouterLink :to="{ name: 'club-setting-member', params: { seq: clubstore.clubSeq } }" class="nav-button">
            가입된 멤버
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="{ name: 'club-setting-applicant', params: { seq: clubstore.clubSeq } }" class="nav-button">
            가입 요청
          </RouterLink>
        </li>
        <li>
          <RouterLink :to="{ name: 'club-setting-club', params: { seq: clubstore.clubSeq } }" class="nav-button">
            모임 설정
          </RouterLink>
        </li>
      </ul>
    </nav>
  </div>
</template>

<script setup>
import { useClubStore } from '@/stores/club'

const clubstore = useClubStore()

const { seq } = defineProps({
  seq: String
})


</script>

<style scoped>
.settings-nav-container {
  display: flex;
  justify-content: center;
  /* 컨테이너를 중앙 정렬 */
  margin-top: 20px;
  /* 상단 마진 추가 */
}

.settings-navbar ul {
  list-style: none;
  padding: 0;
  margin: 0;
  display: flex;
  gap: 10px;
  align-items: center;
  /* 아이템을 중앙 정렬 */
}

.nav-button {
  background-color: #4b8b3b;
  color: #ffffff;
  text-decoration: none;
  padding: 10px 20px;
  border-radius: 5px;
  display: inline-block;
  transition: background-color 0.3s ease;
  font-weight: bold;
  font-size: 16px;
}

.nav-button:hover,
.nav-button:focus {
  background-color: #36732c;
}
</style>
