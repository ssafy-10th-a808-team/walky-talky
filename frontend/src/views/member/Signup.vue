<template>
  <div>
    <section id="contact" class="contact">
      <div class="container">
        <div class="section-title">
          <h1>회원가입</h1>

          <div class="col-lg-15 d-flex justify-content-center">
            <div class="col-lg-7 mt-5 mt-lg-0 d-flex align-items-stretch">
              <form action="forms/contact.php" method="post" role="form" class="php-email-form">
                <!-- 아이디 -->
                <div class="row">
                  <div class="form-group col-md-8">
                    <label class="col-form-label">아이디</label>
                    <input
                      type="text"
                      v-model="member.id"
                      class="form-control"
                      id="name"
                      required
                    />
                  </div>
                  <div class="col-md-4 cta-btn-container text-center">
                    <button
                      type="submit"
                      class="cta-btn align-middle"
                      @click="checkDuplicate('id')"
                    >
                      중복확인
                    </button>
                  </div>
                </div>
                <!-- 비밀번호 -->
                <div class="mb-3 row">
                  <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호</label>
                  <div class="col-sm-10">
                    <input
                      type="password"
                      class="form-control"
                      id="inputPassword"
                      v-model="member.password"
                    />
                  </div>
                </div>
                <!-- 비밀번호확인 -->
                <div class="mb-3 row">
                  <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호 확인</label>
                  <div class="col-sm-10">
                    <input type="password" class="form-control" id="confirmPassword" />
                  </div>
                </div>
                <!-- 생년월일 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label class="col-form-label">생년월일</label>
                  </div>
                  <div class="col-auto">
                    <input type="text" class="form-control" v-model="member.birth" />
                  </div>
                  <div class="col-auto">
                    <span class="form-text"> ex: 19910101 </span>
                  </div>
                </div>
                <!-- 성별 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label class="col-form-label">성별</label>
                  </div>
                  <div class="form-check,col-auto" style="text-align: left; margin-left: 50px">
                    <input
                      v-model="member.gender"
                      class="form-check-input"
                      type="radio"
                      value="M"
                      checked
                    />&nbsp;
                    <label class="form-check-label"> 남 </label>
                  </div>
                  <div class="form-check,col-auto" style="text-align: left; margin-left: 50px">
                    <input
                      v-model="member.gender"
                      class="form-check-input"
                      type="radio"
                      value="F"
                    />&nbsp;
                    <label class="form-check-label"> 여 </label>
                  </div>
                </div>
                <!-- 닉네임 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label
                      class="col-form-label"
                      prop="nickname"
                      :rules="[{ required: true, message: '내용을 입력해주세요.' }]"
                      >닉네임</label
                    >
                  </div>
                  <div class="col-auto">
                    <input v-model="member.nickname" type="text" class="form-control" />
                  </div>
                  <div class="col-auto">
                    <button
                      type="button"
                      class="btn btn-primary mb-3"
                      @click="checkDuplicate('nickname')"
                    >
                      중복확인
                    </button>
                  </div>
                </div>
                <!-- 주소 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label prop="address" class="col-form-label">주소</label>
                  </div>
                  <div class="col-auto">
                    <input v-model="member.address" type="text" class="form-control" />
                  </div>
                  <div class="col-auto">
                    <ButtonWithIcon :selectedIcon="locationIcon" />
                  </div>
                </div>
                <!-- 자기소개 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label for="exampleFormControlTextarea1" class="form-label">자기소개</label>
                  </div>
                  <div class="col-auto">
                    <textarea
                      v-model="member.introduce"
                      class="form-control"
                      id="exampleFormControlTextarea1"
                      rows="3"
                      style="width: 18rem"
                    >
안녕하세요. </textarea
                    >
                  </div>
                </div>
                <!-- 취소, 회원가입 버튼 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto text-center">
                    <button type="submit" @click="cancelRegistration">취소</button>
                  </div>
                  <div class="col-auto text-center">
                    <button type="submit" @click="createMember">회원가입</button>
                  </div>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref } from 'vue'

import router from '../../router'
// import VueCookies from 'vue-cookies'

import { useCounterStore } from '@/stores/counter'
import { useMemberStore } from '@/stores/member'

import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'

const store = useCounterStore()
const locationIcon = store.selectButton('LocationIcon')

const member = ref({
  id: '',
  password: '',
  birth: '',
  gender: '',
  profileImg: '',
  nickname: '',
  introduce: '',
  address: ''
  // regionCd: ''
})

const memberStore = useMemberStore()

function createMember() {
  // console.log(`아이디 : ${form.value.id}`)
  memberStore.createMember(member.value)
}

// const memberInfo = ref({
//   id: '',
//   password: '',
//   birth: '',
//   gender: '',
//   profileImg: '',
//   nickname: '',
//   introduce: '',
//   address: '',
//   refreshToken: '',
//   refreshTokenExpire: ''
// })

// methods: {
//   submitForm(formName) {
//     console.log('폼 데이터:', this.form)
//     this.$refs[formName].validate((valid) => {
//       if (valid) {
//         this.register(this.form)
//       } else {
//         // 유효성 검사에 실패했을 때의 처리 (에러 메시지 표시)
//         this.errorMessages = {
//           id: '아이디를 입력해주세요.',
//           password: '비밀번호를 입력해주세요.'
//           // ... 각 필드에 대한 에러 메시지 추가
//         }
//       }
//     })
//   },
// function register(data) {
//   console.log('등록 전 데이터:', data)
//   axios
//     .post('/member/local-signup', data) //?
//     .then((response) => {
//       // 서버 응답에 따라 처리 (예: 성공 메시지 출력, 페이지 이동 등)
//       console.log('회원가입 성공', response.data)
//       VueCookies.set('accessToken', response.data.accessToken, response.data.accessTokenExpire)
//       VueCookies.set('id', response.data.id)
//       this.$store.commit('SET_IS_LOGIN', {
//         isLogin: true,
//         isLogout: false
//       })
//       this.$store.commit('SET_MORE_USER_INFO', {
//         nickname: data.nickname
//       })
//       this.$router.push({ name: 'Main' })
//     })
//     .catch((error) => {
//       // 서버 응답에 따라 처리 (예: 에러 메시지 출력 등)
//       console.error('회원가입 실패', error)
//     })
// }

const cancelRegistration = function () {
  // 취소 버튼 클릭 시의 처리 (로그인 페이지로 이동:지금은 일단 홈으로)
  console.log('회원가입 취소')
  router.push({ name: 'home' })
}

const checkDuplicate = function (field) {
  // 중복 확인 로직 구현
  console.log(`${field} 중복 확인`)
}

// components: { ButtonWithIcon }
</script>

<style scoped></style>
