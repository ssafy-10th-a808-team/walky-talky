<template>
    <div>
        <h1>소모임 세부 페이지</h1>
        <p>Club Seq : {{ seq }}</p>
        <p>{{  }}</p>

    </div>
</template>

<script setup>
import { onMounted, ref, toRefs } from 'vue'
import { useClubStore } from '@/stores/club';
const clubstore = useClubStore()

const { seq } = defineProps({
    seq: String,
})
onMounted(async () => {
        await clubstore.findClub(seq) 
        const { club } = toRefs(clubstore.clubDetail)
        console.log('현재 페이지에서 클럽')
        console.log(club.value)
    })


</script>

<style scoped>

</style>