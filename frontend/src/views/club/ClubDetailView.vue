<template>
  <ClubDetailHeaderNav />
  <!-- Button added right below the ClubDetailHeaderNav -->
  <!-- role이 'owner'인 경우 -->
  <div v-if="clubDetail.role === 'owner'">
    <div class="action-button-container">
      <button class="action-button" @click="goSetting()">소모임 설정</button>
    </div>
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
      <button class="action-button" @click="clubstore.applyCancel(seq)">가입 신청 취소 하기</button>
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
          {{ clubDetail.responseClubDetailDtoClub.oldBirth }}년생 ~ {{
            clubDetail.responseClubDetailDtoClub.youngBirth }}년생
        </p>
        <p class="club-address">
          {{ clubDetail.responseClubDetailDtoClub.address }}
        </p>
        <div class="club-gender">
          <p v-if="clubDetail.responseClubDetailDtoClub.genderType === 'A'">
            남녀무관
          </p>
          <p v-else-if="clubDetail.responseClubDetailDtoClub.genderType === 'M'">
            남자만
          </p>
          <p v-else>
            여자만
          </p>
        </div>
      </div>
    </div>
    <div class="club-status">
      <p class="club-capacity">
        {{ clubDetail.responseClubDetailDtoClub.nowCapacity }} / {{
          clubDetail.responseClubDetailDtoClub.maxCapacity }} 명
      </p>
      <p class="club-join">
        <span v-if="clubDetail.responseClubDetailDtoClub.autoRecruit">
          즉시 가입
        </span>
        <span v-else>
          승인 후 가입
        </span>
      </p>
      <p class="club-recruitment">
        <span v-if="clubDetail.responseClubDetailDtoClub.openRecruit">
          모집 열려 있음
        </span>
        <span v-else>
          모집 닫혀 있음
        </span>
      </p>
    </div>
    <h2 class="members-title">소모임원</h2>
    <div class="members-list">
      <div v-for="clubmember in clubDetail.responseClubDetailDtoMembers" :key="clubmember.nickname" class="member-item">
        <MemberList :member="clubmember" />
      </div>
    </div>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'
import { useRouter } from 'vue-router';

const clubstore = useClubStore()
const router = useRouter()

const clubDetail = ref({})

const { seq } = defineProps({
  seq: String
})

onMounted(async function () {
  clubDetail.value = await clubstore.findClub(seq); // 컴포넌트가 마운트될 때 소모임 데이터를 가져옵니다.
})

function goSetting() {
  router.push({ name: 'club-setting', params: { seq: seq } })
}


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
  /* Center the button horizontally */
  margin: 20px 0;
  /* Add some space above and below the button */
}

.action-button {
  padding: 10px 20px;
  background-color: darkgreen;
  /* The green background color from the image */
  color: white;
  /* White text color */
  border: none;
  border-radius: 25px;
  /* Rounded corners like in the image */
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s ease;
  /* Smooth transition for hover effect */
}

.action-button:hover {
  background-color: #45A049;
  /* Slightly darker green on hover */
}
</style>