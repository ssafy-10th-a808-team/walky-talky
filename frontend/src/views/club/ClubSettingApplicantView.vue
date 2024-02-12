<template>
  <ClubDetailHeaderNav />
  <ClubSettingHeaderNav />
  <div class="members-list">
    <div v-if="clubstore.clubApplicants.responseClubMemberApplyListDtoMembers?.length > 0">
      <div
        v-for="clubmember in clubstore.clubApplicants.responseClubMemberApplyListDtoMembers"
        :key="clubmember.nickname"
        class="member-item"
      >
        <!-- <MemberList :member="clubmember" /> -->
        <div class="member-container">
          <div class="circular-small">
            <img :src="clubmember.url" alt="" />
            <p class="owner-badge" v-if="clubmember.role === 'owner'">OWNER</p>
          </div>
          <div class="member-info">
            <div>{{ clubmember.nickname }}</div>
            <div>{{ clubmember.birth }}</div>
            <div>{{ clubmember.address }}</div>
            <div>{{ clubmember.introduce }}</div>
          </div>

          <!-- 버튼 컨테이너 추가 -->
          <div class="buttons-container">
            <button class="accept-button" @click="clubstore.applyAccept(clubmember)">수락</button>
            <button class="reject-button" @click="clubstore.applyReject(clubmember)">거절</button>
          </div>
        </div>
      </div>
    </div>
    <div v-else>가입 요청이 없습니다</div>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'
import axios from 'axios'
import { useCounterStore } from '@/stores/counter'
import ClubSettingHeaderNav from '@/views/club/ClubSettingHeaderNav.vue'

const clubstore = useClubStore()

const { seq } = defineProps({
  seq: String
})

onMounted(async function () {
  clubstore.applyList()
})
</script>

<style scoped>
.circular {
  width: 200px;
  /* 고정 가로 크기 */
  height: 200px;
  /* 고정 세로 크기 */
  border-radius: 50%;
  /* 원 모양으로 만듦 */
  overflow: hidden;
  display: flex;
  align-items: center;
  /* 이미지를 중앙에 정렬 */
  justify-content: center;
}

.circular img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 이미지 비율 유지하며 컨테이너 채움 */
  border-radius: 50%;
}

.member-container {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.circular-small {
  width: 64px;
  height: 64px;
  border-radius: 50%;
  overflow: hidden;
  margin-right: 16px;
  position: relative;
  /* 'OWNER' 텍스트를 위해 상대 위치 설정 */
}

.circular-small img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.owner-badge {
  position: absolute;
  /* 절대 위치 설정 */
  bottom: -20px;
  /* 하단에서부터의 위치, 텍스트 높이에 따라 조정 */
  left: 50%;
  /* 왼쪽에서부터 50% 위치에 배치 */
  transform: translateX(-50%);
  /* X축 기준 중앙 정렬 */
  background-color: rgba(0, 0, 0, 0.5);
  /* 배경색 추가 */
  color: greenyellow;
  /* 글자색 */
  padding: 2px 10px;
  /* 패딩 */
  border-radius: 10px;
  /* 둥근 모서리 */
  font-size: 12px;
  /* 글자 크기 */
}

/* 추방하기 버튼 스타일 */
button {
  padding: 5px 15px;
  background-color: #e74c3c;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-left: auto;
  /* 왼쪽에 자동 마진을 추가하여 오른쪽 정렬 */
}

/* 버튼 컨테이너 스타일 추가 */
.buttons-container {
  display: flex;
  gap: 10px;
  /* 버튼 사이의 간격을 설정합니다. */
}

button:hover {
  background-color: #c0392b;
}

/* 수락 버튼 스타일 */
.accept-button {
  padding: 5px 15px;
  background-color: #3498db;
  /* 파란색 배경 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  /* 추가 스타일이 필요하다면 여기에 추가하세요. */
}

.accept-button:hover {
  background-color: #2980b9;
  /* 호버 시 더 어두운 파란색 */
}

/* 거절 버튼 스타일 */
.reject-button {
  padding: 5px 15px;
  background-color: #e74c3c;
  /* 빨간색 배경 */
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  /* 추가 스타일이 필요하다면 여기에 추가하세요. */
}

.reject-button:hover {
  background-color: #c0392b;
  /* 호버 시 더 어두운 빨간색 */
}
</style>
