<template>
  <!-- ======= Team Section ======= -->
  <section v-if="mypage !== null" id="team" class="team section-bg">
    <!-- mypage가 render될 때 null임을 방지 (비동기) -->
    <div class="container">
      <div class="section-title">
        <h2>정보수정</h2>
      </div>

      <div class="row">
        <div class="col-lg-4 col-md-6 d-flex align-items-stretch">
          <div class="member">
            <img :src="mypage.profileImage" alt="프사" />
            <div>
              <div v-if="modals.nickname" class="modal">
                <div class="modal-content">
                  <h2>닉네임 수정</h2>
                  <form @submit.prevent="submitForm">
                    <input type="text" v-model="nickname" />
                    <p>{{ nickname }}</p>
                  </form>
                  <button @click="closeModal('nickname')">확인</button>
                </div>
              </div>
              <div>
                <h4>닉네임</h4>
                <p @click="openModal('nickname')">{{ nickname }}</p>
              </div>
            </div>

            <div class="address">
              <h4>생일</h4>
              <p>{{ mypage.birth }}</p>
            </div>
            <div class="address">
              <div v-if="modals.address">
                <MyLocation />
              </div>
              <h4>내 동네</h4>
              <p @click="openModal('address')">
                {{ address }}
              </p>
            </div>

            <div class="email">
              <h4>성별</h4>
              <p>{{ mypage.gender }}</p>
            </div>

            <div>
              <div v-if="modals.introduce" class="modal">
                <div class="modal-content">
                  <h2>소개글 수정</h2>
                  <form @submit.prevent="submitForm">
                    <input type="textarea" v-model="introduce" />
                  </form>
                  <p>{{ introduce }}</p>
                  <button @click="closeModal('introduce')">확인</button>
                </div>
              </div>
              <h4>소개</h4>
              <p @click="openModal('introduce')">{{ introduce }}</p>
            </div>
            <div>
              <RouterLink :to="{ name: 'Mypage' }">
                <button @click="modifyInfo">완료</button>
              </RouterLink>
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
import { useCounterStore } from '@/stores/counter';
import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue';
import MyLocation from './MyLocationView.vue';
defineProps(['closemodal'])
const memberstore = useMemberStore()
const counterstore = useCounterStore()
const mypage = ref(null)

const profileImage = ref(null)
const address = ref('')
const nickname = ref('')
const introduce = ref('')
const regionCd = ref('')
const locationIcon = counterstore.selectButton('LocationIcon')
address.value = memberstore.getLocationInfo()[0]
regionCd.value = memberstore.getLocationInfo()[1]
onMounted(async () => {
  await memberstore.getMypage()
  mypage.value = memberstore.mypage
  console.log(mypage.value)
  profileImage.value = mypage.value.profileImage
  nickname.value = mypage.value.nickname
  introduce.value = mypage.value.introduce
  regionCd.value = mypage.value.regionCd
  console.log(profileImage.value)
  console.log(nickname.value)
  console.log(introduce.value)
  console.log(regionCd.value)

})

// modal창 띄우기 위한 스위치들
const modals = ref({
  nickname: false,
  introduce: false,
  address: false,
})
const openModal = (modalName) => {
  modals.value[modalName] = true
}
const closeModal = (modalName) => {
  modals.value[modalName] = false
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
