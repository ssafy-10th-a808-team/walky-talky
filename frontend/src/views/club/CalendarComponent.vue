<template>
  <div class="calendar">
    <!-- 달력 헤더: 요일 표시 -->
    <div class="calendar-header">
      <div v-for="day in ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']" :key="day" class="day">
        {{ day }}
      </div>
    </div>
    <!-- 달력 본문: 날짜 표시 -->
    <div class="calendar-body">
      <div
        v-for="(day, index) in daysInMonth"
        :key="index"
        class="day"
        :class="{ today: isToday(day), 'has-event': hasEvent(day) }"
        @click="selectDay(day)"
      >
        {{ day.getDate() }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const events = defineProps({
  events: Array
})

const selectedDay = ref(null)

const today = new Date()
const startDay = new Date(today.getFullYear(), today.getMonth(), 1)
const endDay = new Date(today.getFullYear(), today.getMonth() + 1, 0)
const daysInMonth = computed(() => {
  const days = []
  for (let day = startDay; day <= endDay; day.setDate(day.getDate() + 1)) {
    days.push(new Date(day))
  }
  return days
})

const isToday = (day) => {
  return day.toDateString() === today.toDateString()
}

const hasEvent = (day) => {
  return events.value.some((event) => new Date(event.date).toDateString() === day.toDateString())
}

const selectDay = (day) => {
  selectedDay.value = day
  emit('selectDate', day)
}
</script>

<style scoped>
.calendar {
  display: flex;
  flex-direction: column;
}
.calendar-header {
  display: flex;
}
.calendar-body {
  display: flex;
  flex-wrap: wrap;
}
.day {
  width: 14.28%;
  text-align: center;
}
.today {
  background-color: yellow;
}
.has-event {
  border: 1px solid blue;
}
</style>
