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
                <ButtonWithIcon :selectedIcon="locationIcon"> </ButtonWithIcon>
                ← 내 동네 인증하기
                <div>
                  {{ region_name }}
                </div>
              </div>
              <hr />

              <!-- 이미지 업로드 -->
              <div class="form-group">
                <label for="image-upload">이미지</label>
                <input type="file" class="form-control" id="image-upload" @change="readInputFile" />
                <div id="imageFrame" class="circular"></div>
              </div>
              <hr />

              <!-- 모임명 입력 필드 -->
              <div class="form-group">
                <label for="club-name">모임명</label>
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
                <label for="old-age">최대 나이</label>
                <select class="form-control" id="old-age" v-model="old_birth" required>
                  <option disabled value="">연도 선택</option>
                  <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
                </select>
              </div>
              <div class="form-group">
                <label for="young-age">최소 나이</label>
                <select class="form-control" id="young-age" v-model="young_birth" required>
                  <option disabled value="">연도 선택</option>
                  <option v-for="year in years" :key="year" :value="year">{{ year }}</option>
                </select>
              </div>
              <hr />

              <!-- 성별 선택 -->
              <div class="form-group">
                <label>성별</label>
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
                <label for="max-capacity">최대 인원</label>
                <select class="form-control" id="max-capacity" v-model.trim="max_capacity" required>
                  <option value="" disabled selected>선택하세요</option>
                  <option v-for="num in 100" :value="num">{{ num }}</option>
                </select>
              </div>
              <hr />

              <!-- 설정 -->
              <div class="form-group col-md-6 mt-3">
                <label for="name">가입 설정</label>
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
import { useClubStore } from '@/stores/club'
import { useCounterStore } from '@/stores/counter'
import { useMemberStore } from '@/stores/member'
import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const clubstore = useClubStore()
const counterstore = useCounterStore()
const memberstore = useMemberStore()
const REST_CLUB_API = 'https://i10a808.p.ssafy.io/api/club'

const locationIcon = counterstore.selectButton('LocationIcon')

const region_name = ref('')
const region_cd = ref('')
region_name.value = memberstore.getLocationInfo()[0]
region_cd.value = memberstore.getLocationInfo()[1]

const profileImg = ref(null)
const clubname = ref('')
const isNameAvailable = ref(false)
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
    .then((res) => {
      alert('사용 가능한 모임명입니다.')
      isNameAvailable.value = true
    })
    .catch((err) => {
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
  if (profileImg.value) {
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
.circular {
  width: 200px;
  /* 원하는 크기로 조정 */
  height: 200px;
  border-radius: 50%;
  /* 50%로 설정하여 원 모양으로 만듭니다 */
  overflow: hidden;
  display: flex;
  align-items: center;
  justify-content: center;
}

.circular img {
  width: 100%;
  /* 부모 요소(.circular-image)에 대한 상대적인 크기로 설정 */
  height: 100%;
  /* 가로 세로 비율을 유지하도록 설정 */
  object-fit: cover;
  /* 이미지 간격 제거를 위해 인라인 요소 대신 블록 요소로 설정 */
  border-radius: 50%;
}

.cta-btn-container .btn-check-duplicate {
  background-color: #4caf50;
  /* 예시 색상 */
  color: white;
  border: none;
  padding: 10px 20px;
  margin-top: 10px;
  border-radius: 5px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.cta-btn-container .btn-check-duplicate:hover {
  background-color: #45a049;
  /* 마우스 오버 시 색상 */
}
</style>
