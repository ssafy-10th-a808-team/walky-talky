<template>
  <ClubDetailHeaderNav />
  <div class="event-form-container">
    <!-- 일정 상세 폼 -->
    <form class="event-form" @submit.prevent="submitEvent">
      <div class="form-group">
        <label for="title">제목</label>
        <input type="text" id="title" v-model="eventDetails.title" required />
      </div>
      <div class="form-group">
        <label for="date">날짜</label>
        <input type="date" id="date" v-model="eventDetails.date" required />
      </div>
      <div class="form-group">
        <label for="time">시간</label>
        <input type="time" id="time" v-model="eventDetails.time" required />
      </div>
      <div class="form-group">
        <label for="location">장소</label>
        <input type="text" id="location" v-model="eventDetails.location" required />
      </div>
      <div class="form-group">
        <label for="participants">인원</label>
        <input
          type="number"
          id="participants"
          v-model="eventDetails.participants"
          required
          min="1"
        />
      </div>
      <div class="form-group">
        <label for="note">내용</label>
        <textarea id="note" v-model="eventDetails.note"></textarea>
      </div>
      <button type="submit" class="submit-button">일정 등록</button>
    </form>
  </div>
</template>

<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useClubStore } from '@/stores/club'

const clubstore = useClubStore()
const router = useRouter()

const { seq } = defineProps({
  seq: String
})
const eventDetails = ref({
  title: '',
  date: '',
  time: '',
  location: '',
  participants: null,
  note: ''
})

const submitEvent = async function () {
  // 여기에 이벤트 등록 로직을 추가하세요.
  // 예: clubstore.addEvent(eventDetails.value)
  console.log('Submitted:', eventDetails.value)

  // 폼 제출 후에는 다른 페이지로 이동하거나 알림을 표시할 수 있습니다.
  // 예: router.push('/some-path')
  await clubstore.postPlanRegist(eventDetails.value)
  router.push({ name: 'club-plan', params: { seq: seq } })
}
</script>

<style scoped>
.event-form-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 20px;
}
.form-group {
  margin-bottom: 10px;
}
.form-group label {
  display: block;
  margin-bottom: 5px;
}
.form-group input[type='text'],
.form-group input[type='date'],
.form-group input[type='time'],
.form-group input[type='number'],
.form-group textarea {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
.submit-button {
  width: 100%;
  padding: 10px;
  background-color: #4caf50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
.submit-button:hover {
  background-color: #45a049;
}
</style>
