<template>
  <ClubDetailHeaderNav />
  <div>
    <!-- Button added right below the ClubDetailHeaderNav -->
    <!-- role이 'owner'인 경우 -->
    <div v-if="clubDetail.role === 'owner'">
      <!-- <div class="action-button-container">
      <button class="action-button" @click="goSetting()">소모임 설정</button>
    </div> -->
    </div>

    <!-- role이 'member'인 경우 -->
    <div v-else-if="clubDetail.role === 'member'">
      <!-- member일 때 보여줄 내용 -->
      <div class="action-button-container">
        <button class="action-button" @click="clubstore.withdraw(seq)">소모임 탈퇴</button>
      </div>
    </div>

    <!-- role이 'applicant'인 경우 -->
    <div v-else-if="clubDetail.role === 'applicant'">
      <!-- applicant일 때 보여줄 내용 -->
      <div class="action-button-container">
        <button class="action-button" @click="clubstore.applyCancel(seq)">
          가입 신청 취소 하기
        </button>
      </div>
    </div>

    <!-- role이 'no'인 경우 -->
    <div v-else-if="clubDetail.role === 'no'">
      <!-- no일 때 보여줄 내용 -->
      <div class="action-button-container">
        <button class="action-button" @click="clubstore.apply(seq)">가입 신청 하기</button>
      </div>
    </div>

    <!-- role이 다른 값인 경우 (예외 처리) -->
    <div v-else>
      <!-- 그 외의 경우에 보여줄 내용 -->
    </div>
  </div>

  <div v-if="clubDetail.responseClubDetailDtoClub" class="club-detail-container">
    <div class="club-image-container">
      <div class="circular">
        <img :src="clubDetail.responseClubDetailDtoClub.url" />
      </div>
      <div class="club-info">
        <h1 class="club-name">
          {{ clubDetail.responseClubDetailDtoClub.name }}
        </h1>
        <p class="club-introduce">
          {{ clubDetail.responseClubDetailDtoClub.introduce }}
        </p>
        <p class="club-age-range">
          {{ clubDetail.responseClubDetailDtoClub.oldBirth }}년생 ~
          {{ clubDetail.responseClubDetailDtoClub.youngBirth }}년생
        </p>
        <p class="club-address">
          {{ clubDetail.responseClubDetailDtoClub.address }}
        </p>
        <div class="club-gender">
          <p v-if="clubDetail.responseClubDetailDtoClub.genderType === 'A'">남녀무관</p>
          <p v-else-if="clubDetail.responseClubDetailDtoClub.genderType === 'M'">남자만</p>
          <p v-else>여자만</p>
        </div>
      </div>
    </div>
    <div class="club-status">
      <p class="club-capacity">
        {{ clubDetail.responseClubDetailDtoClub.nowCapacity }} /
        {{ clubDetail.responseClubDetailDtoClub.maxCapacity }} 명
      </p>
      <p class="club-join">
        <span v-if="clubDetail.responseClubDetailDtoClub.autoRecruit"> 즉시 가입 </span>
        <span v-else> 승인 후 가입 </span>
      </p>
      <p class="club-recruitment">
        <span v-if="clubDetail.responseClubDetailDtoClub.openRecruit"> 모집 열려 있음 </span>
        <span v-else> 모집 닫혀 있음 </span>
      </p>
    </div>
    <h2 class="members-title">소모임원</h2>
    <div class="members-list">
      <div
        v-for="clubmember in clubDetail.responseClubDetailDtoMembers"
        :key="clubmember.nickname"
        class="member-item"
      >
        <!-- <MemberList :member="clubmember" /> -->
        <div class="member-container">
          <div class="circular-small">
            <img :src="clubmember.url" alt="" />
            <!-- <p class="owner-badge" v-if="clubmember.role === 'owner'">OWNER</p> -->
          </div>
          <div class="member-info">
            <div>
              {{ clubmember.nickname }}
              <img
                style="width: 50px; height: auto"
                v-if="clubmember.role === 'owner'"
                src="@/assets/img/king.png"
              />
            </div>
            <div>{{ clubmember.address }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'

const clubstore = useClubStore()

const clubDetail = ref({})

const { seq } = defineProps({
  seq: String
})

onMounted(async function () {
  clubDetail.value = await clubstore.findClub(seq) // 컴포넌트가 마운트될 때 소모임 데이터를 가져옵니다.
})
</script>

<style scoped>
.club-detail-container {
  max-width: 768px;
  margin: 0 auto;
  padding: 20px;
  background-color: #fff;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
  border-radius: 10px;
}

.club-image-container {
  display: flex;
  align-items: center;
  gap: 20px;
}

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

.club-info {
  flex-grow: 1;
}

.club-name {
  color: #333;
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 10px;
}

.club-introduce {
  font-size: 16px;
  color: #666;
  margin-bottom: 5px;
}

.club-address,
.club-age-range,
.club-gender,
.club-status,
.club-capacity,
.club-join,
.club-recruitment {
  font-size: 14px;
  color: #888;
}

.members-title {
  margin-top: 30px;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.members-list {
  margin-top: 10px;
}

.member-item {
  margin-bottom: 10px;
}

.action-button-container {
  display: flex;
  justify-content: center;
  /* 가로축 중앙 정렬 */
  align-items: center;
  /* 세로축 중앙 정렬 (필요한 경우) */
  width: 100%;
  /* 컨테이너가 전체 너비를 차지하도록 설정 */
  margin: 20px 0;
  /* 위아래 여백을 줍니다. */
}

.action-button {
  padding: 10px 20px;
  background-color: darkgreen;
  color: white;
  border: none;
  border-radius: 25px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  /* 마진 관련 스타일을 제거하거나 수정하세요 */
}

.action-button:hover {
  background-color: #45a049;
  /* Slightly darker green on hover */
}

.member-container {
  display: flex;
  align-items: center;
  /* justify-content: space-between; */
  /* 컨테이너 내 아이템들 사이의 공간을 균등하게 설정 */
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

button:hover {
  background-color: #c0392b;
}
</style>
