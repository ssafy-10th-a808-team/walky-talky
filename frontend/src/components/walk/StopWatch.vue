<template>
  <div>
    <div style="background-color: white">
      {{ count }}
      <div class="text-center">
        <button @click="play" type="submit">산책하기</button>
        <button @click="stop" type="submit">일시정지</button>
        <button @click="reset" type="submit">정지</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
const count = ref(0)
let intervalId
const play = () => {
  intervalId = setInterval(() => {
    count.value++
  }, 1000)
}
const stop = () => clearInterval(intervalId)
const reset = () => {
  stop()
  count.value = 0
}
</script>

<style scoped>
div {
  margin: 10px;
}
</style>
