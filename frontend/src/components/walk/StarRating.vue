<template>
  <div class="star-container">
    <div v-if="editable">
      <span v-for="i in 5" :key="i" @click="selectStar(i)" :class="{ selected: i <= selectedStar }"
        >&#9733;</span
      >
    </div>
    <div v-else>
      <span v-for="i in 5" :key="i" :class="{ selected: i <= starRating }">&#9733;</span>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'

const { starRating, editable } = defineProps(['starRating', 'editable'])
const emits = defineEmits()

const selectedStar = ref(starRating)

const selectStar = (rating) => {
  if (editable) {
    selectedStar.value = rating
    emits('modifyStarRating', rating)
  }
}
</script>

<style scoped>
/* 별 모양 아이콘 스타일링 */
span {
  cursor: pointer;
  font-size: 24px;
  margin-right: 5px;
}

/* 선택된 별을 강조하기 위한 스타일링 */
.selected {
  color: gold;
}

/* 별점 텍스트 스타일링 */
.rating-text {
  margin-top: 10px;
  font-size: 18px;
  font-weight: bold;
}

.star-container {
  display: flex;
  justify-content: flex-end;
}
</style>
