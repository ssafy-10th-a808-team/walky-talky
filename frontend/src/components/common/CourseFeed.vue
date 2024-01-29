<template>
    <div class="card" style="width: 18rem;">
        <div class="card-header">
        </div>
        <div class="col">
            시간 : {{ coursetime }}
        </div>
        <div class="col">
            거리: 거리
        </div>
        <div class="card-body">
            <p class="card-text"> {{ content }}</p>
        </div>

        <div class="card-footer text-muted">
            좋아요 댓글 공유
        </div>
    </div>
</template>


<script setup>
    
    const { coursetime, content } = defineProps({
    coursetime: Number,
    content: String,
    })

</script>


<style scoped>

</style>