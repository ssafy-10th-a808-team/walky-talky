<template>
  <div>
    <br />
    <h1>회원가입</h1>
    <br />
    <fieldset>
      <form>
        <!-- 아이디 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">아이디</label>
          </div>
          <div class="col-auto">
            <input
              v-model="form.id"
              type="text"
              class="form-control"
              aria-describedby="passwordHelpInline"
              style="width: 18rem"
            />
          </div>
          <div class="col-auto">
            <button type="button" class="btn btn-primary mb-3" @click="checkDuplicate('id')">
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
              v-model="form.password"
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
            <input
              type="text"
              class="form-control"
              aria-describedby="passwordHelpInline"
              v-model="form.birth"
            />
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
              v-model="form.gender"
              class="form-check-input"
              type="radio"
              name="exampleRadios"
              value="M"
              checked
            />&nbsp;
            <label class="form-check-label" for="exampleRadios1"> 남 </label>
          </div>
          <div class="form-check,col-auto" style="text-align: left; margin-left: 50px">
            <input
              v-model="form.gender"
              class="form-check-input"
              type="radio"
              name="exampleRadios"
              value="F"
            />&nbsp;
            <label class="form-check-label" for="exampleRadios1"> 여 </label>
          </div>
        </div>
        <!-- 닉네임 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label
              for="inputPassword6"
              class="col-form-label"
              prop="nickname"
              :rules="[{ required: true, message: '내용을 입력해주세요.' }]"
            >
              닉네임
            </label>
          </div>
          <div class="col-auto">
            <input
              v-model="form.nickname"
              type="text"
              class="form-control"
              aria-describedby="passwordHelpInline"
            />
          </div>
          <div class="col-auto">
            <button type="button" class="btn btn-primary mb-3" @click="checkDuplicate('nickname')">
              중복확인
            </button>
          </div>
        </div>
        <!-- 주소 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label prop="address" for="inputPassword6" class="col-form-label">주소</label>
          </div>
          <div class="col-auto">
            <input
              v-model="form.address"
              type="text"
              class="form-control"
              aria-describedby="passwordHelpInline"
            />
          </div>
          <div class="col-auto">
            <button type="button" class="btn btn-primary mb-3" @click="searchAddress">
              동네검색
            </button>
          </div>
        </div>
        <!-- 자기소개 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="exampleFormControlTextarea1" class="form-label">자기소개</label>
          </div>
          <div class="col-auto">
            <textarea
              v-model="form.introduce"
              class="form-control"
              id="exampleFormControlTextarea1"
              value="안녕하세요. "
              rows="3"
              style="width: 18rem"
            >
안녕하세요. </textarea
            >
          </div>
        </div>
        <!-- 취소, 회원가입 버튼 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <button type="button" class="btn btn-outline-secondary" @click="cancelRegistration">
              취소
            </button>
          </div>
          <div class="col-auto">
            <button type="submit" class="btn btn-outline-primary">회원가입</button>
          </div>
        </div>
      </form>
    </fieldset>
  </div>
</template>

<script>
import axios from '@/utils/axios.js'
import VueCookies from 'vue-cookies'

export default {
  data() {
    return {
      form: {
        id: '',
        password: '',
        birth: '',
        gender: '',
        profileImg: '',
        nickname: '',
        introduce: '',
        address: ''
      },
      memberInfo: {
        id: '',
        password: '',
        birth: '',
        gender: '',
        profileImg: '',
        nickname: '',
        introduce: '',
        address: '',
        refreshToken: '',
        refreshTokenExpire: ''
      },
      errorMessages: {} // 에러 메시지를 저장하는 객체 추가
    }
  },

  // watch: {
  //   // 아이디의 변경을 감시하고, 변경 시 값을 콘솔에 출력
  //   'form.id': function (newId) {
  //     console.log('아이디:', newId)
  //   },

  //   // 비밀번호의 변경을 감시하고, 변경 시 값을 콘솔에 출력
  //   'form.password': function (newPassword) {
  //     console.log('비밀번호:', newPassword)
  //   },
  //   'form.nickname': function (newNickname) {
  //     console.log('닉네임:', newNickname)
  //   },
  //   'form.gender': function (newGender) {
  //     console.log('성별:', newGender)
  //   },
  //   'form.address': function (newAddress) {
  //     console.log('주소:', newAddress)
  //   },
  //   'form.introduce': function (newIntroduce) {
  //     console.log('자기소개:', newIntroduce)
  //   }
  // },

  methods: {
    submitForm(formName) {
      console.log('폼 데이터:', this.form)

      this.$refs[formName].validate((valid) => {
        if (valid) {
          this.register(this.form)
        } else {
          // 유효성 검사에 실패했을 때의 처리 (에러 메시지 표시)
          this.errorMessages = {
            id: '아이디를 입력해주세요.',
            password: '비밀번호를 입력해주세요.'
            // ... 각 필드에 대한 에러 메시지 추가
          }
        }
      })
    },
    register(data) {
      console.log('등록 전 데이터:', data)
      axios
        .post('/info/register', data)
        .then((response) => {
          // 서버 응답에 따라 처리 (예: 성공 메시지 출력, 페이지 이동 등)
          console.log('회원가입 성공', response.data)

          VueCookies.set('accessToken', response.data.accessToken, response.data.accessTokenExpire)
          VueCookies.set('id', response.data.id)

          this.$store.commit('SET_IS_LOGIN', {
            isLogin: true,
            isLogout: false
          })
          this.$store.commit('SET_MORE_USER_INFO', {
            nickname: data.nickname
          })

          this.$router.push({ name: 'Main' })
        })
        .catch((error) => {
          // 서버 응답에 따라 처리 (예: 에러 메시지 출력 등)
          console.error('회원가입 실패', error)
        })
    },
    cancelRegistration() {
      // 취소 버튼 클릭 시의 처리 (로그인 페이지로 이동:지금은 일단 홈으로)
      console.log('회원가입 취소')
      this.$router.push({ name: 'home' })
    },
    checkDuplicate(field) {
      // 중복 확인 로직 구현
      console.log(`${field} 중복 확인`)
    },
    searchAddress() {
      // 동네 검색 로직 구현
      console.log('동네 검색')
    }
  }
}
</script>

<style scoped></style>
