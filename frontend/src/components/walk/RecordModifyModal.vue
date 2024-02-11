<template>
  <div class="modal-container">
    <div class="modal-item">
      <div class="modal-header">
        <div class="close-container">
          <span @click="closeModal" class="close">&times;</span>
        </div>
        <br />
        <div class="modal-title">
          <h2>내 기록 수정하기</h2>
        </div>
      </div>
      <br />
      <div class="modal-body">
        <label for="starRating">별점</label>
        <StarRating
          :starRating="parseInt(editedStarRating)"
          :editable="true"
          @modifyStarRating="modifyStarRating"
        />
        <label for="comment">한줄평</label>
        <br />
        <textarea class="modal-edit-text" v-model="editedComment" id="comment"></textarea>
        <br />
        <button class="modal-save-btn" @click="saveChanges">저장</button>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, defineEmits } from 'vue'
import StarRating from '@/components/walk/StarRating.vue'

const props = defineProps(['initialStarRating', 'initialComment'])
const emits = defineEmits()

const editedStarRating = ref(props.initialStarRating)
const editedComment = ref(props.initialComment)

const modifyStarRating = (rating) => {
  editedStarRating.value = rating
}

const closeModal = () => {
  emits('closeEditModal')
}

const saveChanges = () => {
  emits('saveModal', editedStarRating.value, editedComment.value)
}
</script>

<style scoped>
.close-container {
  margin-left: auto;
}
.star-container {
  justify-content: center;
}

.modal-edit-text {
  resize: none;
  width: 100%;
}

.modal-container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 9;
}

.modal-item {
  background-color: white;
  padding: 20px;
  border-radius: 10px;
  width: 500px;
}

.modal-header {
  display: flex;
  justify-content: space-between;
  flex-direction: column;
}

.close {
  cursor: pointer;
}
</style>
