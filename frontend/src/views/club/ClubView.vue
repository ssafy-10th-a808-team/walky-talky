<template>
  
    <h1>소모임 조회 페이지</h1>
    <div>
        <h4>MyClubs</h4>
        <div>
            <ClubList
                v-for="(club, index) in clubs.myClubs"
                    :key="club.seq"
                    :index="index"
                    :club="club"
                    @click="godetail(club.seq)"
            />
        </div>
    </div>
    <div>
        <h4>OtherClubs</h4>
        <div>
            <ClubList
                v-for="(club, index) in clubs.otherClubs"
                    :key="club.seq"
                    :index="index"
                    :club="club"
                    @click="godetail(club.seq)"
            />
        </div>
    </div>
    <div>
        <h4>recommendedClubs</h4>
        <div>
            <ClubList
                v-for="(club, index) in clubs.recommendClubs"
                    :key="club.seq"
                    :index="index"
                    :club="club"
                    @click="godetail(club.seq)"
            />
        </div>
    </div>
</template>

<script setup>
    import { onMounted, ref } from 'vue'
    import { useClubStore } from "@/stores/club";
    import ClubList from "@/components/club/ClubList.vue"
    import { useRouter } from 'vue-router';
    const clubstore = useClubStore()
    const router = useRouter()

    const clubs = ref([])
    onMounted(async () => {
        await clubstore.getClubs() 
        clubs.value = clubstore.clubs
        console.log(`현재 페이지에서 클럽 ${clubs.value}`)
    })

    const godetail = (seq) => {
        router.push({ name : 'ClubDetail', params: { seq:seq }})
}

</script>

<style scoped>

</style>