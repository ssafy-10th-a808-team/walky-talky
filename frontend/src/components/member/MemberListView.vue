<template>
    <div>
        <div class="circular-small">
            <img :src="member.url" alt="">
        </div> {{ member.nickname }} {{ member.address }}
    </div>
</template>

<script setup>
    defineProps(['member'])
</script>

<style scoped>

</style>