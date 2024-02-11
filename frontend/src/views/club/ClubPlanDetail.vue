<template>
  <ClubDetailHeaderNav />
  <div v-if="clubstore.planDetail">
    {{ clubstore.planDetail.responsePlanDetailDtoPlan }}
    <br />
    {{ clubstore.planDetail.record }}
    <br />
    {{ clubstore.planDetail.recordDetails }}
    <br />
    {{ clubstore.planDetail.responsePlanDetailDtoMembers }}
    <br />
  </div>

  <div>{{ clubstore.planDetail.responsePlanDetailDtoPlan.title }}</div>
  <div v-if="clubstore.planDetail.record">
    <shareBoardRecord
      class="detail-record-container"
      :duration="clubstore.planDetail.record.duration"
      :distance="clubstore.planDetail.record.distance"
      :points="clubstore.planDetail.record.points"
      :address="clubstore.planDetail.record.address"
      :movable="true"
    />
  </div>
  <div>
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
  <div>
    참여 인원: {{ clubstore.planDetail.responsePlanDetailDtoPlan.nowCapacity }} /
    {{ clubstore.planDetail.responsePlanDetailDtoPlan.maxCapacity }}
  </div>
  <div>위치: {{ clubstore.planDetail.responsePlanDetailDtoPlan.location }}</div>

  <div v-if="isPlanMember">
    <button>소모임 일정 참가 취소</button>
  </div>
  <div v-else>
    <button>소모임 일정 참가</button>
  </div>

  <div v-if="isPlanMember">
    <button>기록 업데이트 하기</button>
  </div>

  <div v-if="isPlanMember">
    <button>내 기록으로 복사</button>
  </div>
  <div v-else>
    <button>스크랩</button>
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
            {{ clubmember.nickname }}
          </div>
          <div>{{ clubmember.address }}</div>
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

const clubstore = useClubStore()
const counterstore = useCounterStore()
const isPlanMember = ref(false)

const { clubSeq, planSeq } = defineProps({
  clubSeq: String,
  planSeq: String
})

onMounted(async function () {
  clubstore.planSeq = planSeq
  await clubstore.getPlanDetail()

  const nickname = counterstore.getCookie('nickname')

  // 멤버 배열을 돌면서 닉네임 검사
  isPlanMember.value = clubstore.planDetail.responsePlanDetailDtoMembers.some(
    (member) => member.nickname === nickname
  )
})
</script>

<style scoped></style>
