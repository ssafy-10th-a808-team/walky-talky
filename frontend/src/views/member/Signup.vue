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
                <!-- 아이디 -->
                <div class="form-group col-md-8">
                  <label>모임명</label>
                  <input
                    type="text"
                    name="name"
                    class="form-control"
                    v-model.trim="memberId"
                    required
                  />
                </div>
                <div class="col-md-4 cta-btn-container text-center col-auto">
                  <button
                    type="submit"
                    class="cta-btn align-middle"
                    @click="checkDuplicate('memberId')"
                  >
                    중복확인
                  </button>
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
              <!-- 비밀번호확인 -->
              <div class="mb-3 row">
                <label for="inputPassword" class="col-sm-2 col-form-label">비밀번호 확인</label>
                <input type="password" class="form-control" id="confirmPassword" />
              </div>
              <!-- 이미지 -->
              <div class="form-group col-md-8">
                <label>이미지</label>
                <input type="file" name="name" class="form-control" @change="readInputFile" />
                <div id="imageFrame">
                  <img id="img" height="200" alt="이미지미리보기" />
                </div>
              </div>
              <!-- 생년월일 -->
              <div class="row g-3 align-items-center">
                <div class="col-auto">
                  <label class="col-form-label">생년월일</label>
                </div>
                <div class="col-auto">
                  <input type="text" class="form-control" v-model="birth" />
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
                    v-model="gender"
                    class="form-check-input"
                    type="radio"
                    value="M"
                    checked
                  />&nbsp;
                  <label class="form-check-label"> 남 </label>
                </div>
                <div class="form-check,col-auto" style="text-align: left; margin-left: 50px">
                  <input v-model="gender" class="form-check-input" type="radio" value="F" />&nbsp;
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
                  <input v-model="nickname" type="text" class="form-control" />
                </div>
                <!-- 닉네임 중복확인 버튼 -->
                <div class="col-md-4 cta-btn-container text-center col-auto">
                  <button
                    type="submit"
                    class="cta-btn align-middle"
                    @click="checkDuplicate('nickname')"
                  >
                    중복확인
                  </button>
                </div>
              </div>
              <!-- 주소 -->
              <div class="form-group col-md-6">
                <label>주소</label>
                <input type="text" name="name" class="form-control" />
              </div>
              <div class="col-auto">
                <ButtonWithIcon :selectedIcon="locationIcon" />
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
const locationIcon = counterstore.selectButton('LocationIcon')

const profileImg = ref(null)
const memberId = ref('')
const password = ref('')
const birth = ref('')
const gender = ref('')
const nickname = ref('')
const introduce = ref('')
// address: ''
const region_cd = ref('')

const memberStore = useMemberStore()

const createMember = function () {
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
  console.log(payload)
}

const cancelRegistration = function () {
  // 취소 버튼 클릭 시의 처리 (로그인 페이지로 이동:지금은 일단 홈으로)
  console.log('회원가입 취소')
  router.push({ name: 'home' })
}

const checkDuplicate = function (field) {
  // 중복 확인 로직 구현
  console.log(`${field} 중복 확인`)
}

const readInputFile = (e) => {
  document.getElementById('imageFrame').innerHTML = ''
  const files = e.target.files
  const fileArr = Array.from(files)
  console.log(fileArr[0])
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

<style scoped></style>
