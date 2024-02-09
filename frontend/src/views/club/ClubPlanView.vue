<template>
  <ClubDetailHeaderNav />
  {{ clubstore.planList }}
  <!-- <calendarComponent :events="clubstore.planList" @selectDate="handleDateSelect" /> -->
  <!-- <div v-if="selectedDate">
    <eventList :events="eventsForSelectedDate" @selectEvent="handleEventSelect" />
  </div> -->
  <button @click="createNewEvent">일정 생성</button>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import { useRouter } from 'vue-router'
import calendarComponent from '@/views/club/CalendarComponent.vue'
import eventList from '@/views/club/EventListComponent.vue'

const clubstore = useClubStore()
const selectedDate = ref(null)
const eventsForSelectedDate = ref([])
const router = useRouter()

const { seq } = defineProps({
  seq: String
})

onMounted(async function () {
  await clubstore.getPlanList()
})

// const handleDateSelect = (date) => {
//   selectedDate.value = date
//   eventsForSelectedDate.value = clubstore.planList.filter((event) => event.date === date)
// }

// const handleEventSelect = (eventId) => {
//   // 이벤트 ID를 사용하여 PlanDetail 페이지로 네비게이션 합니다.
// }

const createNewEvent = function () {
  // 일정 생성 로직을 여기에 추가합니다.
  router.push({ name: 'club-plan-regist', params: { seq: clubstore.clubSeq } })
}
</script>

<style scoped></style>
