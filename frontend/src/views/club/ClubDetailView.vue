<template>
    <div>
        <h1>소모임 세부 페이지</h1>
        <div class="circular">
            <img v-if="clubDetail && clubDetail.url" :src="clubDetail.url" />
        </div>
        <p>Club Seq : {{ seq }}</p>
        <!-- <p>Club Name : {{ clubstore.clubDetail.name }}</p>
        <p>Club Profile : {{ clubstore.clubDetail.url }}</p> -->
        <p v-if="clubDetail && clubDetail.name">Club 이름 : {{ clubDetail.name }}</p>
        <p v-if="clubDetail && clubDetail.introduce">Club 소개 : {{ clubDetail.introduce }}</p>
        <p v-if="clubDetail && clubDetail.oldBirth && clubDetail.youngBirth">{{ clubDetail.oldBirth }}년생 ~ {{ clubDetail.youngBirth }}년생</p>
        <p v-if="clubDetail && clubDetail.nowCapacity && clubDetail.maxCapacity">{{ clubDetail.nowCapacity }}/ {{ clubDetail.maxCapacity }} 명</p>
        <!-- <p>Club Detail : {{ clubDetail }}</p> -->
        <!-- <p>Club Member : {{ clubMember }}</p> -->
    </div>
   <h2>클럽원들</h2>
    <div v-if="clubMembers" v-for="clubmember in clubMembers" :key="clubmember.nickname">
        <MemberList
        :member="clubmember"
        />
   </div>
</template>

<script setup>
import { ref, toRef, onMounted } from 'vue'
import { useClubStore } from '@/stores/club';
import MemberList from '@/components/member/MemberListView.vue'
const clubstore = useClubStore()
const { seq } = defineProps({
    seq: String,
})
const clubDetail = ref(null)
const clubMembers = ref(null)

onMounted( async () => {
    const result = await clubstore.findClub(seq)
    clubDetail.value = result[0]
    console.log(clubDetail.value)
    clubMembers.value = result[1]
    console.log(clubMembers.value)
})


</script>

<style scoped>

</style>