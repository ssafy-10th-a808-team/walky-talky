<template>
  <ClubDetailHeaderNav />
  {{ clubstore.planList }}
  <!-- <div v-if="clubstore.planList">
  <v-date-picker
    v-model="selectedDate"
    :events="eventDates"
    @input="fetchEventsForDate"
  ></v-date-picker>
  <div v-if="eventsForSelectedDate.length">
    <ul>
      <li v-for="event in eventsForSelectedDate" :key="event.seq">
        {{ event.title }} - {{ event.startTime }}
      </li>
    </ul>
  </div> -->
  <button @click="createNewEvent">일정 생성</button>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref, computed } from 'vue'
import { useClubStore } from '@/stores/club'
import { useRouter } from 'vue-router'

const clubstore = useClubStore()
const router = useRouter()
const selectedDate = ref(new Date()) // 현재 날짜를 기본값으로 설정
const eventsForSelectedDate = ref([]) // 선택된 날짜의 이벤트를 담을 ref

const { seq } = defineProps({
  seq: String
})

onMounted(async function () {
  await clubstore.getPlanList()
  fetchEventsForDate(selectedDate.value) // 컴포넌트가 마운트되면 오늘 날짜의 이벤트를 가져옵니다.
})

const eventDates = computed(() => {
  return clubstore.planList.plans.map((plan) => plan.startTime.split('T')[0])
})

const fetchEventsForDate = (date) => {
  eventsForSelectedDate.value = clubstore.planList.plans.filter((plan) => {
    return plan.startTime.startsWith(date.toISOString().split('T')[0])
  })
}

const createNewEvent = function () {
  // 일정 생성 로직을 여기에 추가합니다.
  router.push({ name: 'club-plan-regist', params: { seq: clubstore.clubSeq } })
}
</script>

<style scoped></style>
