<template>
  <div class="event-list">
    <div v-for="event in events" :key="event.id" class="event" @click="selectEvent(event.id)">
      {{ event.title }} — {{ event.time }}
    </div>
  </div>
</template>

<script setup>
const { emit, defineProps } = require('vue')

const events = defineProps({
  events: Array
})

const selectEvent = (eventId) => {
  emit('selectEvent', eventId)
}
</script>

<style scoped>
.event-list {
  margin-top: 20px;
}
.event {
  padding: 10px;
  border-bottom: 1px solid #ccc;
  cursor: pointer;
}
.event:hover {
  background-color: #f0f0f0;
}
</style>
