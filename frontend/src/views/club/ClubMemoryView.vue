<template>
    <div>
        <CourseFeed
            :coursetime="coursetime"
            :content="content"
        />
    </div>
</template>

<script setup>
    import { ref } from 'vue'
    import CourseFeed from '@/components/common/CourseFeed.vue'

    const coursetime = ref(1555)
    const content = ref('작동 테스트') 
</script>

<style scoped>

</style>