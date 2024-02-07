<template>
  <div>
    <section id="contact" class="contact">
      <div class="container">
        <div class="section-title">
          <h1>회원가입</h1>
        </div>

        <div class="col-lg-15 d-flex align-items-stretch justify-content-center">
          <div class="col-lg-7 mt-5 mt-lg-0 d-flex align-items-stretch">
            <form method="post" role="form" class="php-email-form">
              <!-- 주소 -->
              <div class="form-group">
                <div>
                  <label for="club-address">내 동네 인증하기 *필수</label>
                </div>
                <ButtonWithIcon :selectedIcon="locationIcon" />
                <div>
                  {{ region_name }}
                </div>
              </div>
              <hr />

              <!-- 아이디 -->
              <div v-if="!isOauth" class="form-group">
                <label>아이디 *필수</label>
                <input
                  type="text"
                  name="name"
                  class="form-control"
                  v-model.trim="memberId"
                  required
                />
              </div>
              <div v-if="!isOauth" class="col-md-4 cta-btn-container text-center col-auto">
                <button type="button" class="cta-btn align-middle" @click="checkId()">
                  중복확인
                </button>
                <!-- <input type="hidden" name="idDuplication" value="idUncheck"/> -->
              </div>
              <hr />

              <!-- 비밀번호 -->
              <div v-if="!isOauth" class="form-group">
                <label for="inputPassword" class="col-sm-10 col-form-label">비밀번호 *필수</label>
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
              <div v-if="!isOauth" class="form-group">
                <label for="inputPassword" class="col-sm-10 col-form-label"
                  >비밀번호 확인 *필수</label
                >
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
              <hr />

              <!-- 이미지 -->
              <div class="form-group">
                <label>이미지</label>
                <input type="file" name="name" class="form-control" @change="readInputFile" />
                <div id="imageFrame" class="circular">
                  <img alt="" />
                </div>
              </div>
              <hr />

              <!-- 닉네임 -->
              <div class="form-group">
                <div class="col-auto">
                  <label class="col-form-label" prop="nickname">닉네임 *필수</label>
                </div>
                <div class="col-auto">
                  <input v-model="nickname" type="text" class="form-control" />
                </div>
              </div>
              <!-- 닉네임 중복확인 버튼 -->
              <div class="col-md-4 cta-btn-container text-center col-auto">
                <button type="button" class="cta-btn align-middle" @click="checkNickname()">
                  중복확인
                </button>
              </div>
              <hr />

              <!-- 생년월일 -->
              <div class="form-group">
                <div class="col-auto">
                  <label class="col-form-label">생년월일 *필수</label>
                </div>
                <div class="col-auto">
                  <input type="text" class="form-control" v-model="birth" @input="validateBirth" />
                </div>
                <div class="col-auto">
                  <span class="form-text"> ex: 19910101 </span>
                </div>
              </div>
              <hr />

              <!-- 성별 -->
              <div class="form-group col-md-6 mt-3">
                <label>성별 *필수</label>
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
              <hr />

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
              <hr />
              <!-- 취소, 회원가입 버튼 -->

              <div class="text-center">
                <button type="submit" @click="createMember">회원가입</button>
              </div>
              <div class="col-auto text-center">
                <button type="submit" @click="cancelRegistration">취소</button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
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
const introduce = ref('')
const region_cd = ref('')
const region_name = ref('')
const isOauth = ref(false)

region_name.value = memberStore.getLocationInfo()[0]
region_cd.value = memberStore.getLocationInfo()[1]

onMounted(() => {
  const code = new URL(window.location.href).searchParams.get('code')
  if (code != null) {
    isMember(code)
  }
})

const isMember = async (code) => {
  await memberStore.isMember(code)
  memberId.value = memberStore.getMemberId()
  nickname.value = memberStore.getNickname()
  profileImg.value = memberStore.getProfileImage()
  isOauth.value = memberStore.getIsOauth()
  loadImage()
}

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

const loadImage = () => {
  document.getElementById('imageFrame').innerHTML = ''
  const img = document.createElement('img')
  img.src = profileImg.value

  document.getElementById('imageFrame').appendChild(img)
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
.contact {
  background: #f8f9fa;
  padding: 40px;
  border-radius: 10px;
  box-shadow: 0px 0px 30px rgba(127, 137, 161, 0.25);
}

.section-title h1 {
  color: #055052;
  font-size: 22px;
  text-align: center;
  font-weight: 700;
  margin-bottom: 20px;
}

.php-email-form {
  background: #ffffff;
  padding: 25px;
  border-radius: 10px;
  box-shadow: 0px 0px 30px rgba(127, 137, 161, 0.3);
}

.php-email-form h1 {
  color: #055052;
  font-size: 18px;
  font-weight: 700;
  margin-bottom: 20px;
}

.php-email-form hr {
  margin-top: 10px;
  margin-bottom: 10px;
}

.form-group label {
  color: #6c757d;
  font-size: 14px;
  font-weight: 500;
}

.form-control {
  border-radius: 5px;
  border: 1px solid #ced4da;
  padding: 0.375rem 0.75rem;
  font-size: 1rem;
}

.form-group .cta-btn {
  background-color: #34c759;
  color: white;
  border: none;
  padding: 10px 25px;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.form-group .cta-btn:hover {
  background-color: #28a745;
}

.portfolio ul {
  padding: 0;
  list-style: none;
  display: flex;
  justify-content: center;
  gap: 10px;
}

.portfolio li {
  cursor: pointer;
  padding: 5px 10px;
  border-radius: 5px;
  background: #e9ecef;
  color: #055052;
  font-size: 14px;
  font-weight: 500;
}

.portfolio li.filter-active,
.portfolio li:hover {
  background: #055052;
  color: #ffffff;
}

#imageFrame {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  overflow: hidden;
  margin: 20px auto;
  display: block;
}

#imageFrame img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

/* 추가적인 스타일링이 필요하다면 여기에 추가합니다. */

/* 버튼을 감싸는 div에 대한 스타일 */
.text-center {
  margin-bottom: 20px; /* 하단 버튼과의 간격 */
}

/* 마지막 버튼에 대한 스타일 (취소 버튼) */
.col-auto.text-center {
  margin-top: 20px; /* 상단 버튼과의 간격 */
}
</style>
