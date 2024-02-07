<template>
  <!-- ======= Team Section ======= -->
  <section v-if="mypage !== null" id="team" class="team section-bg">
    <!-- mypage가 render될 때 null임을 방지 (비동기) -->
    <div class="container">
      <div class="section-title">
        <h2>마이페이지</h2>
      </div>

      <div class="row">
        <div class="col-lg-10 col-md-10 align-items-stretch justify-content-center">
          <div class="member">
            <div class="d-flex justify-content-center align-items-center">
              <div class="circular">
                <img :src="mypage.profileImage" alt="프사" />
              </div>
            </div>
            <div>
              <h4>닉네임</h4>
              <h3>{{ mypage.nickname }}</h3>
            </div>

            <div class="address">
              <h4>생일</h4>
              <p>{{ mypage.birth }}</p>
            </div>
            <div class="address">
              <h4>내 동네</h4>
              <p>{{ mypage.address }}</p>
            </div>

            <div class="email">
              <h4>성별</h4>
              <p>{{ mypage.gender }}</p>
            </div>

            <div class="phone">
              <h4>소개</h4>
              <p>{{ mypage.introduce }}</p>
            </div>
            <div>
              <RouterLink :to="{ name: 'ModifyInfo' }">
                <button>수정하기</button>
              </RouterLink>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
  <!-- End Team Section -->
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMemberStore } from '@/stores/member'
const memberstore = useMemberStore()
const mypage = ref(null)
onMounted(async () => {
  await memberstore.getMypage()
  mypage.value = memberstore.mypage
  console.log(mypage.value)
})
</script>

<style scoped></style>
