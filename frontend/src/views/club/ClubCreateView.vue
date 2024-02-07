<template>
  <div>
    <section id="contact" class="contact">
      <div class="container">
        <div class="section-title">
          <h2>소모임 생성</h2>
        </div>

        <div class="col-lg-15 d-flex align-items-stretch justify-content-center">
          <div class="col-lg-7 mt-5 mt-lg-0 d-flex align-items-stretch">
            <form @submit.prevent="createClub" class="php-email-form">
              <h1>소모임 정보</h1>
              <hr />
              <!-- 내 동네 입력 필드 -->
              <div class="form-group">
                <div>
                  <label for="club-address">내 동네 인증하기 *필수</label>
                </div>
                <!-- <ButtonWithIcon :selectedIcon="locationIcon"> </ButtonWithIcon> -->
                <RouterLink :to="{ name: 'mylocation' }">
                  <img src="@/assets/img/LocationIcon.png" style="width: 100px; height: auto" />
                </RouterLink>
                <div>
                  {{ region_name }}
                </div>
              </div>
              <hr />

              <!-- 이미지 업로드 -->
              <div class="form-group">
                <label for="image-upload">이미지</label>
                <input type="file" class="form-control" id="image-upload" @change="readInputFile" />
                <div id="imageFrame" class="circular">
                  <img :src="profileImg" />
                </div>
              </div>
              <hr />

              <!-- 모임명 입력 필드 -->
              <div class="form-group">
                <label for="club-name">모임명 *필수</label>
                <input
                  type="text"
                  class="form-control"
                  id="club-name"
                  v-model.trim="clubname"
                  required
                />
                <div class="cta-btn-container text-center">
                  <button
                    type="button"
                    class="cta-btn align-middle btn-check-duplicate"
                    @click="checkDuplicate(clubname)"
                  >
                    중복 확인
                  </button>
                </div>
              </div>
              <hr />

              <!-- 소개글 -->
              <div class="form-group">
                <label for="introduction">소개글</label>
                <textarea
                  class="form-control"
                  id="introduction"
                  rows="10"
                  v-model.trim="introduce"
                  required
                ></textarea>
              </div>
              <hr />

              <h1>소모임 설정</h1>
              <hr />

              <!-- 나이 -->
              <div class="form-group">
                <label for="old-age">최대 나이 *필수</label>
                <select class="form-control" id="old-age" v-model="old_birth" required>
                  <option disabled value="">연도 선택</option>
                  <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
                </select>
              </div>
              <div class="form-group">
                <label for="young-age">최소 나이 *필수</label>
                <select class="form-control" id="young-age" v-model="young_birth" required>
                  <option disabled value="">연도 선택</option>
                  <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
                </select>
              </div>
              <hr />

              <!-- 성별 선택 -->
              <div class="form-group">
                <label>성별 *필수</label>
                <div class="portfolio d-flex justify-content-center">
                  <ul id="portfolio-flters">
                    <li
                      :class="{ 'filter-active': gender_type === 'M' }"
                      @click="setGenderType('M')"
                    >
                      남
                    </li>
                    <li
                      :class="{ 'filter-active': gender_type === 'F' }"
                      @click="setGenderType('F')"
                    >
                      여
                    </li>
                    <li
                      :class="{ 'filter-active': gender_type === 'A' }"
                      @click="setGenderType('A')"
                    >
                      무관
                    </li>
                  </ul>
                </div>
              </div>
              <hr />

              <!-- 최대인원 입력 필드 -->
              <div class="form-group">
                <label for="max-capacity">최대 인원 *필수</label>
                <select class="form-control" id="max-capacity" v-model.trim="max_capacity" required>
                  <option value="" disabled selected>선택하세요</option>
                  <option v-for="num in 100" :key="num" :value="num">{{ num }}</option>
                </select>
              </div>
              <hr />

              <!-- 설정 -->
              <div class="form-group col-md-6 mt-3">
                <label for="name">가입 설정 *필수</label>
                <div class="row">
                  <div class="portfolio col-md-8 d-flex justify-content-center">
                    <ul id="portfolio-flters">
                      <li
                        :class="{ 'filter-active': is_auto_recruit === true }"
                        @click="setrecruitType(true)"
                      >
                        즉시 가입 허용
                      </li>
                      <li
                        :class="{ 'filter-active': is_auto_recruit === false }"
                        @click="setrecruitType(false)"
                      >
                        승인 후 가입
                      </li>
                    </ul>
                  </div>
                </div>
              </div>
              <hr />

              <!-- 제출 -->
              <div class="text-center">
                <button
                  type="button"
                  class="cta-btn align-middle btn-check-duplicate"
                  @click="createClub"
                  :disabled="!isNameAvailable || !region_name"
                >
                  모임 생성
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'
import axios from 'axios'
import { useCounterStore } from '@/stores/counter'
import { useMemberStore } from '@/stores/member'
import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'
import { useRouter } from 'vue-router'
// 기본 이미지를 import 합니다.
import defaultLogoPath from '@/assets/img/Logo.png'

const router = useRouter()
const counterstore = useCounterStore()
const memberstore = useMemberStore()
const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api/club'
const locationIcon = counterstore.selectButton('LocationIcon')

const isNameAvailable = ref(false)

const region_name = ref('')
const region_cd = ref('')
region_name.value = memberstore.getLocationInfo()[0]
region_cd.value = memberstore.getLocationInfo()[1]

const profileImg = ref(defaultLogoPath)
const clubname = ref('')
const introduce = ref('')
const old_birth = ref('')
const young_birth = ref('')
const gender_type = ref('')
const max_capacity = ref('')
const is_auto_recruit = ref(false)

///////////////////////////////////////////////////////////////////////////

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

// 소모임 이름 중복 확인
const checkDuplicate = (name) => {
  axios({
    method: 'post',
    url: `${REST_CLUB_API}/check-name`,
    headers: {
      Authorization: `Bearer ${counterstore.getCookie('atk')}`
    },
    data: { name }
  })
    .then(() => {
      alert('사용 가능한 모임명입니다.')
      isNameAvailable.value = true
    })
    .catch(() => {
      alert('사용 불가능한 모임명입니다.')
      isNameAvailable.value = false
    })
}

const years = computed(() => {
  const currentYear = new Date().getFullYear()
  const startYear = 1900
  let years = []
  for (let year = currentYear; year >= startYear; year--) {
    years.push(year)
  }
  return years
})

const setGenderType = (value) => {
  gender_type.value = value
  console.log(`selected gender : ${gender_type.value}`)
}

const setrecruitType = (value) => {
  is_auto_recruit.value = value
  console.log(`selected recruit : ${is_auto_recruit.value}`)
}

const createClub = () => {
  // FormData 인스턴스 생성
  const formData = new FormData()

  console.log('profileImg.value  = ' + profileImg.value)
  if (profileImg.value !== '/src/assets/img/Logo.png') {
    formData.append('multipartFile', profileImg.value)
  }

  formData.append('name', clubname.value)
  if (introduce.value) formData.append('introduce', introduce.value)
  formData.append('regionCd', region_cd.value)
  formData.append('young_birth', young_birth.value.toString())
  formData.append('old_birth', old_birth.value.toString())
  formData.append('gender_type', gender_type.value)
  formData.append('max_capacity', max_capacity.value.toString())
  formData.append('is_auto_recruit', is_auto_recruit.value.toString())

  // Axios를 사용한 API 호출
  axios({
    method: 'post',
    url: `${REST_CLUB_API}/create`,
    headers: {
      Authorization: `Bearer ${counterstore.getCookie('atk')}`,
      'Content-Type': 'multipart/form-data'
    },
    data: formData
  })
    .then((res) => {
      console.log(res)
      alert('소모임 생성 성공')
      // 생성 후에 페이지 이동이나 상태 업데이트 로직
      router.push({ name: 'club' })
    })
    .catch((err) => {
      console.error(err)
      alert(err.response.data.message)
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

.section-title h2 {
  color: #055052;
  font-size: 22px;
  text-align: center;
  font-weight: 700;
  margin-bottom: 20px;
}

form h1 {
  color: #055052;
  font-size: 18px;
  font-weight: 700;
}

form hr {
  margin-top: 10px;
  margin-bottom: 10px;
}

.form-group {
  margin-bottom: 15px;
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

.cta-btn-container button {
  background-color: #34c759;
  color: white;
  border: none;
  padding: 10px 25px;
  margin-top: 10px;
  border-radius: 20px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cta-btn-container button:hover {
  background-color: #28a745;
}

.circular {
  width: 100px;
  /* Adjust as needed */
  height: 100px;
  /* Adjust as needed */
  border-radius: 50%;
  overflow: hidden;
  margin: 20px auto;
  display: block;
}

.circular img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.fixed-button-container {
  /* 이 섹션은 이미지에서 보이지 않으므로 스타일을 유지하거나 적절히 조정합니다. */
}
</style>
