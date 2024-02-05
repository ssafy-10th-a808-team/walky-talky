<template>
  <div class="containerScrap">
    <img v-if="scraped" src="@/assets/img/scraped.png" alt="scraped" width="20px" height="20px" />
    <img v-else src="@/assets/img/not_scraped.png" alt="Not scraped" width="20px" height="20px" />
    <p>{{ scrapCount }}</p>
  </div>
</template>

<script setup>
const { scrapCount, scraped } = defineProps(['scrapCount', 'scraped'])
</script>

<style>
.containerScrap {
  display: flex;
}

img {
  margin-right: 5px; /* 이미지 간 간격을 설정할 수 있습니다. */
}
</style>
