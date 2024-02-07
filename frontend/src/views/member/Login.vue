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
                <div class="form-group col-md-8" style="margin: 0 auto">
                  <label>아이디</label>
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
              <div class="row">
                <div class="form-group col-md-8" style="margin: 0 auto">
                  <label>비밀번호</label>
                  <input
                    type="password"
                    class="form-control"
                    id="inputPassword"
                    v-model="password"
                    required
                  />
                </div>
              </div>
              <div
                style="
                  border-radius: 5px;
                  border: 2px solid #5cb874;
                  width: 300px;
                  height: 44px;
                  margin: 0 auto;
                  text-align: center;
                "
              >
                <RouterLink :to="{ name: 'Signup' }" style="display: inline-block; margin-top: 7px"
                  >회원가입</RouterLink
                >
              </div>
              <div class="text-center">
                <button type="submit" @click="login" style="width: 300px; margin-top: 10px">
                  로그인
                </button>
              </div>
              <div>
                <!-- <button @click="kakaoLogin">-->
                <img
                  src="/src/assets/img/KakaoLoginIcon.png"
                  alt="Kakao Login"
                  onclick="javascript:kakaoLogin();"
                  style="margin: auto; display: block; margin-top: 10px"
                />
                <!--</button>-->
              </div>
            </form>
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
