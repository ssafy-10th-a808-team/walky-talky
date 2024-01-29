<template>
    <HeaderNav></HeaderNav>
  
    <h1>소모임 조회 페이지</h1>
    <div>
        <div>멤버</div>
    </div>
</template>

<script setup>
    import HeaderNav from "@/components/common/TheHeaderNav.vue"
    import MemberListView from "@/components/member/MemberListView.vue"
    import { useMemberStore } from '@/stores/member'
    import { ref } from 'vue'
    const memberStore = useMemberStore()
    // const memberList = memberStore.memberList

    
</script>

<style scoped>

</style>