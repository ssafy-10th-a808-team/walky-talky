<template>
    <div class="card" style="width: 18rem;">
        <div class="card-header">
            <img src="..." alt="프사"> {{ membername }} {{ createdat }} {{ memberlocation }}
        </div>
        <img src="..." class="card-img-top" alt="이미지x, 지도 ">
        <div class="col">
            시간 : {{ coursetime }}
        </div>
        <div class="col">
            거리: {{ coursedistance }}
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