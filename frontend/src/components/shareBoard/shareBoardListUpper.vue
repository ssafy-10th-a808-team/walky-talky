<template>
  <div class="member-container">
    <shareBoardMember :nickname="nickname" :profilePic="profilePic" />
    <div class="info-container">
      <div class="upper-container">
        <p>조회수 {{ hit }}</p>
      </div>
      <div class="lower-container">
        <p>{{ create_at }}</p>
        <p>|</p>
        <p>{{ lastPart }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import shareBoardMember from '@/components/shareBoard/shareBoardMember.vue'

const { nickname, profilePic, create_at, address, hit } = defineProps([
  'nickname',
  'profilePic',
  'create_at',
  'address',
  'hit'
])

const addressParts = address.split(' ')
const lastPart = addressParts[addressParts.length - 1]
</script>

<style scoped>
.rounded-image {
  border-radius: 50%;
  margin-right: 10px;
}
.member-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.upper-container {
  display: flex;
  align-items: center;
  margin-left: auto;
}

.lower-container {
  display: flex;
  align-items: center;
}

.info-container {
  display: flex;
  flex-direction: column;
}
</style>
