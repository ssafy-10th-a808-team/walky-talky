<template>
  <div>
    <section id="contact" class="contact">
      <div class="container">
        <div class="section-title">
          <h1>로그인</h1>
        </div>
        <div class="col-lg-15 d-flex justify-content-center">
          <div class="col-lg-7 mt-5 mt-lg-0 d-flex align-items-stretch">
            <form method="post" role="form" class="php-email-form">
              <div class="row">
                <!-- 아이디 -->
                <div class="form-group col-md-8">
                  <label>ID</label>
                  <input
                    type="text"
                    name="name"
                    class="form-control"
                    v-model.trim="memberId"
                    required
                  />
                </div>
              </div>
              <!-- 비밀번호 -->
              <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호</label>
                <input
                  type="password"
                  class="form-control"
                  id="inputPassword"
                  v-model="password"
                  required
                />
              </div>
              <RouterLink :to="{ name: 'Signup' }">회원가입</RouterLink>
              <div class="text-center">
                <button type="submit" @click="login">로그인</button>
              </div>
            </form>
            <button type="submit" @click="kakaoLogin">
              <img src="/src/assets/img/KakaoLoginIcon.png" alt="Kakao Login" />
            </button>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useMemberStore } from '@/stores/member'
const memberstore = useMemberStore()

const memberId = ref(null)
const password = ref(null)

const login = (event) => {
  event.preventDefault()
  const payload = {
    memberId: memberId.value,
    password: password.value
  }
  memberstore.login(payload)
}

const kakaoLogin = () => {
  memberstore.kakaoLogin()
}
</script>

<style scoped></style>
