<template>
  <div>
    <button @click="openLocModal" class="button-style">
      <img src="/src/assets/img/LocationIcon.png" alt="location" class="icon-style" />
    </button>
  </div>
  <div v-if="viewModal" class="modal">
    <div class="modal-content">
      <p>모달</p>
      <MyLocationView />
      <button @click="fnc">확인</button>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import MyLocationView from '@/views/member/MyLocationView.vue'
defineProps(['fnc'])
const viewModal = ref(false)
const openLocModal = () => {
  viewModal.value = true
}
const closeModal = () => {
  viewModal.value = false
}
</script>

<style scoped>
.button-style {
  border: none;
  background-color: transparent;
}
.icon-style {
  width: 30px;
  height: 30px;
}
</style>
