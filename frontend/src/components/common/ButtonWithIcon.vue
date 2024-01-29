<template>
    <RouterLink :to="{ name : `${selectedIcon.urlName}` }">
        <button class="button-style">
            <img :src="selectedIcon.icon" :alt="selectedIcon.name" class="icon-style">
        </button>
    </RouterLink>
</template>

<script setup>

    defineProps(['selectedIcon'])

</script>

<style scoped>
    .button-style {
        border: none;
    }
    .icon-style {
        width: 30px;
        height: 30px;
    }
</style>