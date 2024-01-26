<template>
    <RouterLink :to="{ name : `${urlName}` }">
        <button class="button-style">
            <img :src="icon" :alt="name" class="icon-style">
        </button>
    </RouterLink>
</template>

<script setup>

    defineProps(['name', 'icon', 'urlName'])

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