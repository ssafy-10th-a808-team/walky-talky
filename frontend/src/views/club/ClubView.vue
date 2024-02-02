<template>
  
    <h1>소모임 조회 페이지</h1>
    <div class="section-title">
        <h2>MyClubs</h2>
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
    <div class="section-title">
        <h2>OtherClubs</h2>
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
    <div class="section-title">
        <h2>recommendedClubs</h2>
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
    <RouterLink :to="{ name: 'club-create' }">
        <button class="fixed-btn">소모임생성</button>
    </RouterLink>
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
        router.push({ name : 'club-detail', params: { seq:seq }})
}

</script>

<style scoped>
.fixed-btn {
  position: fixed;
  right: 100px;
  bottom: 500px;
  z-index: 10;
  background: #5cb874;
  width: 100px;
  height: 40px;
  border-radius: 4px;
  font-size: 16px;
  color: #fff;
  line-height: 0;
}

.fixed-btn:hover {
  background: #78c48c;
  color: #fff;
}
</style>