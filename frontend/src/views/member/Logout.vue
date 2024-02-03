<template>
    <div>

    </div>
</template>

<script setup>
import { useMemberStore } from '@/stores/member';
const memberstore = useMemberStore()
memberstore.logout()
</script>

<style scoped>

</style>