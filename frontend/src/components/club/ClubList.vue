<template>
    <div class="portfolio-info">
        <img :src="club.url" alt="프로필"><h3>{{ club.name }}</h3>
        <ul>
        <li><div>{{ club.address }} {{ club.genderType }}</div></li>
        <li>{{ club.introduce }}</li>
        <li>{{ club.oldBirth }}부터 {{ club.youngBirth }}까지</li>
        <li>{{ club.nowCapacity }} / {{ club.maxCapacity }}명</li>
        </ul>
    </div>
</template>

<script setup>
    const { club } = defineProps({
        club: Object,
    })
</script>

<style scoped>

</style>