<template>
    <div>
        <img :src="member.profile" alt=""> {{ member.nickname }} {{ member.region }}
    </div>
</template>

<script setup>
    defineProps(['member'])
</script>

<style scoped>

</style>