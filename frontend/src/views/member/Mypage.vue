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
              <p>{{ year }}년 {{ month }}월 {{ day }}일</p>
            </div>
            <div class="address">
              <h4>내 동네</h4>
              <p>{{ mypage.address }}</p>
            </div>

            <div class="email">
              <h4>성별</h4>
              <p>{{ showGender }}</p>
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
import { ref, onMounted, computed } from 'vue'
import { useMemberStore } from '@/stores/member'
const memberstore = useMemberStore()
const mypage = ref(null)
const year = ref('')
const month = ref('')
const day = ref('')
onMounted(async () => {
  await memberstore.getMypage()
  mypage.value = memberstore.mypage
  console.log(mypage.value)
  console.log(mypage.value.birth)
  year.value = mypage.value.birth.substring(0, 4)
  month.value = mypage.value.birth.substring(4, 6)
  day.value = mypage.value.birth.substring(6, 8)
})
const showGender = computed(() => {
  return mypage.value.gender === 'F' ? '여성' : '남성'
})
// const year = mypage.value.birth.value.substring(0, 4)
// const month = mypage.value.birth.value.substring(4, 6) - 1 // 월은 0부터 시작하므로 1을 빼줍니다.
// const day = mypage.value.birth.value.substring(6, 8)
</script>

<style scoped></style>
