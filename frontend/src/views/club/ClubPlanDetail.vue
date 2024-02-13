<template>
  <ClubDetailHeaderNav />
  <div v-if="clubstore.planDetail.responsePlanDetailDtoPlan">
    <div v-if="record">
      <shareBoardRecord
        class="detail-record-container"
        :duration="record.duration"
        :distance="record.distance"
        :points="record.points"
        :address="record.address"
        :movable="true"
      />
    </div>
    <div v-else>현재 기록 없음</div>

    <div class="plan-title">{{ clubstore.planDetail.responsePlanDetailDtoPlan.title }}</div>
    <div class="plan-start-time">
      시작 시간:
      {{
        new Date(clubstore.planDetail.responsePlanDetailDtoPlan.startTime).toLocaleTimeString(
          'ko-KR',
          {
            hour: '2-digit',
            minute: '2-digit'
          }
        )
      }}
    </div>
    <div class="plan-capacity">
      참여 인원: {{ clubstore.planDetail.responsePlanDetailDtoPlan.nowCapacity }} /
      {{ clubstore.planDetail.responsePlanDetailDtoPlan.maxCapacity }}
    </div>
    <div class="plan-location">
      위치: {{ clubstore.planDetail.responsePlanDetailDtoPlan.location }}
    </div>
    <div v-if="clubstore.planDetail.responsePlanDetailDtoPlan.content" class="plan-content">
      내용: {{ clubstore.planDetail.responsePlanDetailDtoPlan.content }}
    </div>

    <div v-if="isPlanMember" class="buttons-container">
      <button @click="clubstore.planClubMemberCancel">소모임 일정 참가 취소</button>
      <button @click="goOverwrite">기록 업데이트 하기</button>
      <button @click="clubstore.planCopy">내 기록으로 복사</button>
    </div>
    <div v-else class="buttons-container">
      <button @click="clubstore.planClubMemberApply">소모임 일정 참가</button>
    </div>

    <h2 class="members-title">참가원</h2>
    <div class="members-list">
      <div
        v-for="clubmember in clubstore.planDetail.responsePlanDetailDtoMembers"
        :key="clubmember.nickname"
        class="member-item"
      >
        <div class="member-container">
          <div class="circular-small">
            <img :src="clubmember.url" alt="" />
          </div>
          <div class="member-info">
            <div>
              {{ clubmember.nickname }} {{ clubmember.birth.substring(2, 4) }}년생
              {{ clubmember.gender == 'M' ? '남자' : '여자' }}
            </div>
            <div>
              {{ clubmember.address }}
            </div>
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
import { useCounterStore } from '@/stores/counter'
import shareBoardRecord from '@/components/shareBoard/shareBoardRecord.vue'
import { useShareBoardStore } from '@/stores/shareBoard'
import { useRouter } from 'vue-router'

const clubstore = useClubStore()
const counterstore = useCounterStore()
const isPlanMember = ref(false)
const shareBoardStore = useShareBoardStore()
const record = ref(null)
const router = useRouter()

const { clubSeq, planSeq } = defineProps({
  clubSeq: String,
  planSeq: String
})

function goOverwrite() {
  router.push({ name: 'club-plan-overwrite', params: { clubSeq: clubSeq, planSeq: planSeq } })
}

onMounted(async function () {
  clubstore.planSeq = planSeq
  await clubstore.getPlanDetail()

  const nickname = counterstore.getCookie('nickname')

  // 멤버 배열을 돌면서 닉네임 검사
  isPlanMember.value = clubstore.planDetail.responsePlanDetailDtoMembers.some(
    (member) => member.nickname === nickname
  )

  record.value = await shareBoardStore.getRecord(clubstore.planDetail.record.seq)
})
</script>

<style scoped>
.buttons-container {
  display: flex;
  justify-content: center; /* 가로 방향으로 중앙 정렬 */
  gap: 10px;
}

button {
  padding: 10px 20px; /* 버튼 내부 패딩 조정 */
  border: none;
  border-radius: 5px; /* 버튼 모서리 둥글게 */
  background-color: darkgreen; /* 버튼 배경 색상 */
  color: white; /* 버튼 텍스트 색상 */
  cursor: pointer; /* 마우스 오버 시 커서 변경 */
}

button:hover {
  background-color: yellowgreen; /* 마우스 오버 시 버튼 배경 색상 변경 */
}

.plan-title {
  font-size: 24px; /* 제목의 폰트 크기 */
  font-weight: bold; /* 글자 두께 */
  margin-bottom: 10px; /* 다음 요소와의 여백 */
}

.plan-start-time,
.plan-capacity,
.plan-location,
.plan-content {
  font-size: 18px; /* 정보의 폰트 크기 */
  margin-bottom: 8px; /* 다음 요소와의 여백 */
  padding-left: 10px; /* 텍스트의 왼쪽 패딩 */
  border-left: 3px solid darkgreen; /* 왼쪽에 강조선 추가 */
}

.plan-content {
  padding-bottom: 10px; /* 하단 패딩 */
  border-bottom: 2px solid darkgreen; /* 하단에 경계선 추가 */
}
</style>
