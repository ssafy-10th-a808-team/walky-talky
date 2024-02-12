<template>
  <!-- ======= Team Section ======= -->
  <section v-if="mypage !== null" id="team" class="team section-bg">
    <!-- mypage가 render될 때 null임을 방지 (비동기) -->
    <div class="container">
      <div class="section-title">
        <h2>정보수정</h2>
      </div>

      <div class="row">
        <div class="col-lg-10 col-md-10 align-items-stretch">
          <div class="member">
            <!-- <div class="circular">
              <img :src="profileImage" alt="프사" @click="openImageUploader" />
            </div>
            <div v-if="showImageUploader">
              <ImageUploader @imageSelected="updateProfileImage" @close="closeImageUploader" />
            </div> -->
            <!-- 이미지 업로드 -->
            <div class="form-group">
              <label for="image-upload">프로필 사진</label>
              <input type="file" class="form-control" id="image-upload" @change="readInputFile" />
              <div class="d-flex justify-content-center align-items-center">
                <div id="imageFrame" class="circular">
                  <img :src="profileImage" />
                </div>
              </div>
            </div>

            <div>
              <div>
                <h4>닉네임</h4>
                <div class="d-flex justify-content-center align-items-center">
                  <p v-if="!modals.nickname">
                    {{ nickname }}
                    <button @click="openModal('nickname')">수정</button>
                  </p>
                  <div v-if="modals.nickname">
                    <div>
                      <form @submit.prevent="submitForm">
                        <input type="text" v-model="nickname" />
                      </form>
                      <button @click="closeModal('nickname')">확인</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>

            <h4>내 동네</h4>
            <div class="address">
              <div class="justify-content-center d-flex">
                <p v-if="!modals.address">
                  {{ address }}
                  <button @click="openModal('address')">수정</button>
                </p>
              </div>

              <div v-if="modals.address">
                <MyLocationView
                  @returnAddressName="updateAddressName"
                  @returnAddressCode="updateAddressCode"
                />
                <button @click="closeModal('address')">확인</button>
              </div>
            </div>

            <div>
              <h4>소개</h4>
              <div class="justify-content-center">
                <p v-if="!modals.introduce">
                  {{ introduce }}
                </p>
                <button @click="openModal('introduce')">수정</button>
                <div v-if="modals.introduce">
                  <div>
                    <form @submit.prevent="submitForm">
                      <input type="textarea" v-model="introduce" />
                    </form>
                    <button @click="closeModal('introduce')">확인</button>
                  </div>
                </div>
              </div>
              <div>
                <div class="d-flex justify-content-center align-items-center">
                  <p v-if="!modals.password">
                    <button @click="openModal('password')">비밀번호 수정</button>
                  </p>
                  <div v-if="modals.password">
                    <div>
                      <form @submit.prevent="submitForm">
                        <!-- 현재 비밀번호 -->
                        <div class="form-group">
                          <label for="inputPassword" class="col-sm-10 col-form-label"
                            >현재 비밀번호</label
                          >
                          <input
                            type="password"
                            class="form-control"
                            id="inputPassword"
                            maxlength="16"
                            v-model="password"
                            required
                          />
                        </div>
                        <!-- 변경할 비밀번호 확인 -->
                        <div class="form-group">
                          <label for="inputNewPassword" class="col-sm-10 col-form-label"
                            >변경할 비밀번호</label
                          >
                          <input
                            type="password"
                            class="form-control"
                            id="inputPassword"
                            maxlength="16"
                            v-model="newPassword"
                            required
                          />
                        </div>
                        <div class="form-group">
                          <label for="inputCheckNewPassword" class="col-sm-10 col-form-label"
                            >비밀번호 확인</label
                          >
                          <input
                            type="password"
                            class="form-control"
                            id="inputPassword"
                            maxlength="16"
                            v-model="checkNewPassword"
                            required
                          />
                        </div>
                      </form>
                      <button @click="changePassword()">비밀번호 변경</button>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div>
              <button @click="modifyInfo">완료</button>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
  <!-- End Team Section -->
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useMemberStore } from '@/stores/member'
import { useCounterStore } from '@/stores/counter'
import MyLocationView from './MyLocationView.vue'
import ImageUploader from '@/components/common/ImageUploader.vue'

const memberstore = useMemberStore()
const counterstore = useCounterStore()
const mypage = ref(null)

const profileImage = ref(null)
const address = ref('')
const nickname = ref('')
const introduce = ref('')
const regionCd = ref('')
const password = ref('')
const newPassword = ref('')
const checkNewPassword = ref('')

const readInputFile = (e) => {
  document.getElementById('imageFrame').innerHTML = ''
  const files = e.target.files
  const fileArr = Array.from(files)
  console.log(fileArr[0])
  profileImage.value = fileArr[0]
  memberstore.profileImage = profileImage.value
  console.log(`현재 저장된 프로필 이미지 : ${profileImage.value}`)
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

const showImageUploader = ref(false)

const openImageUploader = () => {
  showImageUploader.value = true
}

const closeImageUploader = () => {
  showImageUploader.value = false
  memberstore.profileImage = profileImage.value
}

const updateProfileImage = (image) => {
  profileImage.value = image
}

onMounted(async () => {
  await memberstore.getMypage()
  mypage.value = memberstore.mypage
  console.log(mypage.value.introduce)
  profileImage.value = mypage.value.profileImage
  nickname.value = mypage.value.nickname
  introduce.value = mypage.value.introduce
  address.value = mypage.value.address
  regionCd.value = mypage.value.regionCd
})

// modal창 띄우기 위한 스위치들
const modals = ref({
  nickname: false,
  introduce: false,
  address: false,
  password: false
})
const openModal = (modalName) => {
  modals.value[modalName] = true
}
const closeModal = (modalName) => {
  modals.value[modalName] = false
  memberstore.nickname = nickname.value
  memberstore.introduce = introduce.value
}
const updateAddressName = (name) => {
  address.value = name
}
const updateAddressCode = (code) => {
  regionCd.value = code
}
// 비밀번호 변경

const changePassword = () => {
  modals.value[password] = false
  const payload = {
    password: password.value,
    newPassword: newPassword.value,
    checkNewPassword: checkNewPassword.value
  }
  memberstore.modifyPassword(payload)
}
// 수정
const modifyInfo = function (event) {
  event.preventDefault()
  const payload = {
    profileImage: profileImage.value,
    nickname: nickname.value,
    introduce: introduce.value,
    regionCd: regionCd.value
  }
  // console.log(`아이디 : ${form.value.id}`)
  memberstore.modifyInfo(payload)
  // console.log(payload)
}
</script>

<style scoped>
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
}

.modal-content {
  background-color: white;
  padding: 20px;
  border-radius: 5px;
}
</style>
