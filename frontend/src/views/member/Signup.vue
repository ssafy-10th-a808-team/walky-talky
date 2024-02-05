<template>
  <div>
    <section id="contact" class="contact">
      <div class="container">
        <div class="section-title">
          <h1>회원가입</h1>
        </div>
        <div class="col-lg-15 d-flex justify-content-center">
          <div class="col-lg-7 mt-5 mt-lg-0 d-flex align-items-stretch">
            <form method="post" role="form" class="php-email-form">
              <div class="row">
                <!-- 주소 -->
                <div class="form-group col-md-6">
                  <label>* 주소</label>
                  <input type="text" name="name" class="form-control" v-model.trim="region_name" />
                </div>
                <div class="col-auto">
                  <ButtonWithIcon :selectedIcon="locationIcon" />
                </div>
                <!-- 닉네임 -->
                <div class="row g-3 align-items-center">
                  <div class="col-auto">
                    <label class="col-form-label" prop="nickname">* 닉네임</label>
                  </div>
                  <div class="col-auto">
                    <input v-model="nickname" type="text" class="form-control" />
                  </div>
                  <!-- 닉네임 중복확인 버튼 -->
                  <div class="col-md-4 cta-btn-container text-center col-auto">
                    <button type="submit" class="cta-btn align-middle" @click="checkNickname()">
                      중복확인
                    </button>
                  </div>
                </div>
                <!-- 아이디 -->
                <div class="form-group col-md-8">
                  <label>* 아이디</label>
                  <input
                    type="text"
                    name="name"
                    class="form-control"
                    v-model.trim="memberId"
                    required
                  />
                </div>
                <div class="col-md-4 cta-btn-container text-center col-auto">
                  <button type="submit" class="cta-btn align-middle" @click="checkId()">
                    중복확인
                  </button>
                  <!-- <input type="hidden" name="idDuplication" value="idUncheck"/> -->
                </div>
              </div>
              <!-- 비밀번호 -->
              <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-10 col-form-label">* 비밀번호</label>
                <input
                  type="password"
                  class="form-control"
                  id="inputPassword"
                  maxlength="16"
                  v-model="password"
                  required
                />
                <p style="color: grey">
                  ※ 비밀번호는 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용하세요.
                </p>
              </div>
              <!-- 비밀번호확인 -->
              <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-10 col-form-label">* 비밀번호 확인</label>
                <input
                  type="password"
                  class="form-control"
                  id="confirmPassword"
                  maxlength="16"
                  @blur="passwordCheckValid"
                  v-model="repassword"
                />
                <div v-if="!isPasswordMatch">
                  <p style="color: red">비밀번호가 동일하지 않습니다.</p>
                </div>
              </div>
              <!-- 이미지 -->
              <div class="form-group col-md-8">
                <label>이미지</label>
                <input type="file" name="name" class="form-control" @change="readInputFile" />
                <div id="imageFrame">
                  <img width="200" alt="이미지미리보기" />
                </div>
              </div>
              <!-- 생년월일 -->
              <div class="row g-3 align-items-center">
                <div class="col-auto">
                  <label class="col-form-label">* 생년월일</label>
                </div>
                <div class="col-auto">
                  <input type="text" class="form-control" v-model="birth" @input="validateBirth" />
                </div>
                <div class="col-auto">
                  <span class="form-text"> ex: 19910101 </span>
                </div>
              </div>
              <!-- 성별 -->
              <div class="form-group col-md-6 mt-3">
                <label>* 성별</label>
                <div class="row">
                  <div class="portfolio col-md-6 d-flex justify-content-center">
                    <ul id="portfolio-flters">
                      <li :class="{ 'filter-active': gender === 'M' }" @click="setGenderType('M')">
                        남
                      </li>
                      <li :class="{ 'filter-active': gender === 'F' }" @click="setGenderType('F')">
                        여
                      </li>
                    </ul>
                  </div>
                </div>
              </div>

              <!-- 자기소개 -->
              <div class="form-group mt-3">
                <label>자기소개</label>
                <textarea
                  class="form-control"
                  name="message"
                  rows="10"
                  v-model="introduce"
                ></textarea>
              </div>
              <!-- 취소, 회원가입 버튼 -->
              <div class="col-auto text-center">
                <button type="submit" @click="cancelRegistration">취소</button>
              </div>
              <div class="text-center">
                <button type="submit" @click="createMember">회원가입</button>
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
import { useCounterStore } from '@/stores/counter'
import { useMemberStore } from '@/stores/member'

import router from '../../router'
// import VueCookies from 'vue-cookies'
import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'

const counterstore = useCounterStore()
const memberStore = useMemberStore()
const locationIcon = counterstore.selectButton('LocationIcon')

const profileImg = ref(null)
const memberId = ref('')
const password = ref('')
const repassword = ref('')
const birth = ref('')
const gender = ref('M')
const nickname = ref('')
const introduce = ref('안녕하세요! ')
const region_cd = ref('')
const region_name = ref('')

region_name.value = memberStore.getLocationInfo()[0]
region_cd.value = memberStore.getLocationInfo()[1]

// 아이디 중복 체크
const checkId = function () {
  memberStore.checkId(memberId.value)
  console.log(`${memberId.value}`)
}

// 닉네임 중복 체크
const checkNickname = function () {
  memberStore.checkNickname(nickname.value)
  console.log(`${nickname.value}`)
}

// 비밀번호 일치 여부 확인 함수
const isPasswordMatch = ref(true)
const passwordCheckValid = function () {
  isPasswordMatch.value = password.value === repassword.value
}

// 생년월일 유효성 검사
const isBirthValid = ref(true)

const validateBirth = function () {
  const birthRegex = /^[0-9]{8}$/ // 8자리의 숫자만 허용

  if (!birthRegex.test(birth.value)) {
    // 잘못된 형식의 입력이면 상태를 false로 설정
    isBirthValid.value = false
  } else {
    // 올바른 형식의 입력이면 상태를 true로 설정
    const year = birth.value.substring(0, 4)
    const month = birth.value.substring(4, 6) - 1 // 월은 0부터 시작하므로 1을 빼줍니다.
    const day = birth.value.substring(6, 8)

    const parsedDate = new Date(year, month, day)

    // 날짜가 유효한지 확인
    if (
      parsedDate.getFullYear() == year &&
      parsedDate.getMonth() == month &&
      parsedDate.getDate() == day
    ) {
      isBirthValid.value = true
    } else {
      isBirthValid.value = false
    }
  }
}

const setGenderType = (value) => {
  gender.value = value
  console.log(`selected gender : ${gender.value}`)
}

// 회원가입
const createMember = function (e) {
  e.preventDefault()

  if (!isPasswordMatch.value) {
    // 비밀번호가 일치하지 않으면 여기에서 처리 (예: 알림 메시지 등)
    alert('비밀번호가 일치하지 않습니다.')
    return
  } else if (!isBirthValid.value) {
    // 생년월일이 올바르지 않으면 여기에서 처리 (예: 알림 메시지 등)
    alert('생년월일을 올바르게 입력해주세요.')
    return
  }
  const payload = {
    profileImg: profileImg.value,
    memberId: memberId.value,
    password: password.value,
    birth: birth.value,
    gender: gender.value,
    nickname: nickname.value,
    introduce: introduce.value,
    region_cd: region_cd.value
  }
  // console.log(`아이디 : ${form.value.id}`)
  memberStore.createMember(payload)
  // console.log(payload)
}

const cancelRegistration = function () {
  // 취소 버튼 클릭 시의 처리 (로그인 페이지로 이동)
  console.log('회원가입 취소')
  router.push({ name: 'Login' })
}

const readInputFile = (e) => {
  document.getElementById('imageFrame').innerHTML = ''
  const files = e.target.files
  const fileArr = Array.from(files)
  // console.log(fileArr[0])
  profileImg.value = fileArr[0]
  console.log(`현재 저장된 프로필 이미지 : ${profileImg.value}`)
  fileArr.forEach(function (f) {
    if (!f.type.match('image/.*')) {
      alert('이미지 확장자만 업로드 가능합니다.')
      return
    }
    const reader = new FileReader()
    reader.onload = function (e) {
      const img = document.createElement('img')
      img.src = e.target.result

      document.getElementById('imageFrame').appendChild(img)
    }
    reader.readAsDataURL(f)
  })
}
</script>

<style scoped>
#imageFrame {
  max-width: 100%; /* 원하는 가로 크기로 조절 */
  max-height: 300px; /* 원하는 세로 크기로 조절 */
  overflow: auto; /* 크기를 벗어난 이미지를 감출 경우 사용 */
}

#img {
  max-width: 100%; /* 이미지의 최대 가로 크기를 부모 요소의 너비에 맞춤 */
  max-height: 100%; /* 이미지의 최대 세로 크기를 부모 요소의 높이에 맞춤 */
  display: block; /* 인라인 요소 간격 제거 */
  margin: auto; /* 가운데 정렬을 위해 사용 */
}
</style>
