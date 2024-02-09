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
import { defineProps } from 'vue'

const props = defineProps({
  events: Array
})

const today = new Date()
const startDay = new Date(today.getFullYear(), today.getMonth(), 1)
const endDay = new Date(today.getFullYear(), today.getMonth() + 1, 0)
const daysInMonth = computed(() => {
  const days = []
  let day = new Date(startDay)
  while (day <= endDay) {
    days.push(new Date(day))
    day.setDate(day.getDate() + 1)
  }
  return days
})

const isToday = (day) => {
  return day.toDateString() === today.toDateString()
}

const hasEvent = (day) => {
  const dayStr = day.toISOString().split('T')[0]
  return props.events.some((event) => {
    const eventDateStr = event.startTime.split('T')[0]
    return dayStr === eventDateStr
  })
}

// const selectDay = (day) => {
//   emit('selectDate', day.toISOString().split('T')[0])
// }
</script>

<style scoped>
.calendar {
  display: flex;
  flex-direction: column;
  font-family: 'Arial', sans-serif; /* Use a modern font */
  color: #333; /* A darker text color for better readability */
  max-width: 600px; /* Set a max width for the calendar */
  margin: auto; /* Center the calendar */
  user-select: none; /* Prevent text selection */
}

.calendar-header {
  display: flex;
  justify-content: space-between; /* Space out the header items */
  padding: 10px 0; /* Add some vertical padding */
  font-weight: bold; /* Make header bold */
  background-color: #f7f7f7; /* A light background for the header */
  border-bottom: 1px solid #e0e0e0; /* A subtle border under the header */
}

.calendar-body {
  display: flex;
  flex-wrap: wrap;
}

.day {
  width: calc(100% / 7); /* Divide the width by 7 for the days of the week */
  padding: 15px 0; /* Add padding for each day */
  text-align: center;
  cursor: pointer;
  transition: background-color 0.3s; /* Smooth transition for hover effects */
}

/* Style for the current day */
.today {
  background-color: #007bff; /* A blue background for the current day */
  color: #fff; /* White text color for the current day */
  border-radius: 50%; /* Round shape for the current day */
  margin: 5px; /* Space around the current day */
}

/* Hover effect for days */
.day:hover {
  background-color: #f0f0f0; /* A light grey background on hover */
}

/* Style for days with events */
.has-event::after {
  content: '•'; /* Unicode character for a dot */
  color: #ff4500; /* Color of the event dot */
  font-size: 24px; /* Size of the dot */
  line-height: 0; /* Adjust line height to position the dot correctly */
  vertical-align: middle; /* Center the dot vertically */
  margin-left: 5px; /* Space between the day number and the dot */
}

/* Add styles for navigation buttons if you have them */
.nav-button {
  cursor: pointer;
  padding: 5px 10px;
  margin: 0 5px;
  background: #e0e0e0;
  border: none;
  border-radius: 5px;
}

.nav-button:hover {
  background: #d0d0d0;
}

/* Hide the outline that appears when focused on the div for keyboard navigation */
.day:focus {
  outline: none;
}
</style>
