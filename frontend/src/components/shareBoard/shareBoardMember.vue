<template>
  <div class="member-container">
    <img :src="profilePic" class="rounded-image" width="50" height="50" />
    <div class="info-container">
      <div class="upper-container">
        <h5>{{ nickname }}</h5>
        <p>조회수 {{ hit }}</p>
      </div>
      <div class="lower-container">
        <p>{{ lastPart }}</p>
        <p>|</p>
        <p>{{ create_at }}</p>
      </div>
    </div>
  </div>
</template>

<script setup>
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
  /* justify-content: space-between; */
  align-items: center;
}

.upper-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
