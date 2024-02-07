<template>
  <div class="image-uploader">
    <input type="file" @change="selectImage" />
    <!-- 이미지 선택 후 저장 버튼 -->
    <button @click="saveImage">저장</button>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const emit = defineEmits(['imageSelected', 'image', 'close'])
const image = ref(null)

const selectImage = (event) => {
  const file = event.target.files[0]
  const reader = new FileReader()
  reader.onload = () => {
    image.value = reader.result
  }
  reader.readAsDataURL(file)
}

const saveImage = () => {
  // 이미지 저장 로직 수행
  // 저장된 이미지를 부모 컴포넌트로 전달
  emit('imageSelected', image.value)
  emit('close')
}

// const closeUploader = () => {
//   // 이미지 업로더 닫기
//   emit('close')
// }
</script>

<style lang="scss" scoped></style>
