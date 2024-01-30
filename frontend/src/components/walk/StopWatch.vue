<template>
  <div>
    <div>
      {{ count }}
      <button @click="play">P</button>
      <button @click="stop">S</button>
      <button @click="reset">R</button>
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
