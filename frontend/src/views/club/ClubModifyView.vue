<template>
    <head>
    </head>
      <div>
        <!-- ======= Contact Section ======= -->
        <section id="contact" class="contact">
          <div class="container">
  
            <div class="section-title">
              <h2>소모임 생성</h2>
            </div>
            <div class="col-lg-15 d-flex align-items-stretch justify-content-center">
            <div class="col-lg-7 mt-5 mt-lg-0 d-flex align-items-stretch">
              <form  method="post" role="form" class="php-email-form">
                  <div class="row">
                    <!-- 이미지 -->
                    <div class="form-group col-md-8">
                      <label for="name">이미지</label>
                      <input type="file" name="name" class="form-control" id="name" @change="readInputFile"/>
                      <div id="imageFrame" class="circular">
                        <img id="img" height="200" alt="이미지미리보기"/>
                      </div>
                    </div>
                    <!-- 이미지 end -->
                    <div class="form-group col-md-8">
                      <label for="name">모임명</label>
                      <input type="text" name="name" class="form-control" id="name" v-model.trim="clubname" required>
                    </div>
                    <div class="col-md-4 cta-btn-container text-center" @click="checkDuplicate(clubname)">
                      <a class="cta-btn align-middle" href="#">중복 확인</a>
                    </div>
                    
                    <div class="form-group mt-3">
                      <label for="name">소개글</label>
                      <textarea class="form-control" name="message" rows="10" v-model.trim="introduce" required></textarea>
                    </div>
                
  
                    <div class="row">
                      <div class="form-group col-md-5 mt-3 mt-md-0">
                        <label for="name">늙은 나이 </label>
                        <input type="name" class="form-control" name="name" id="name" v-model.trim="old_birth" required>
                      </div>
                      <div class="form-group col-md-5 mt-3 mt-md-0">
                        <label for="name">어린 나이</label>
                        <input type="name" class="form-control" name="name" id="name" v-model.trim="young_birth" required>
                      </div>
                    </div>
  
                </div>
                <div class="form-group col-md-6 mt-3">
                  <label for="name">성별</label>
                  <div class="row">
                    <div class="portfolio col-md-6 d-flex justify-content-center">
                      <ul id="portfolio-flters">
                        <li :class="{ 'filter-active': gender_type === 'M' }" @click="setGenderType('M')">남</li>
                        <li :class="{ 'filter-active': gender_type === 'F' }" @click="setGenderType('F')">여</li>
                        <li :class="{ 'filter-active': gender_type === 'A' }" @click="setGenderType('A')">무관</li>
                      </ul>
                    </div>
                  </div>
                </div>
  
                <div class="form-group col-md-8">
                      <label for="name">최대인원</label>
                      <input type="text" name="name" class="form-control" id="name" v-model.trim="max_capacity" required>
                    </div>
  
                <div class="form-group col-md-6 mt-3">
                  <label for="name">즉시 가입 여부</label>
                  <div class="row">
                    <div class="portfolio col-md-8 d-flex justify-content-center">
                      <ul id="portfolio-flters">
                        <li :class="{ 'filter-active': is_auto_recruit === true }" @click="setrecruitType(true)">즉시가입</li>
                        <li :class="{ 'filter-active': is_auto_recruit === false }" @click="setrecruitType(false)">가입승인</li>
                      </ul>
                    </div>
                  </div>
                    
                  <div class="my-3">
                    <div class="loading">Loading</div>
                    <div class="error-message"></div>
                    <div class="sent-message">Your message has been sent. Thank you!</div>
                  </div>
                </div>
                <div class="text-center"><button @click="createClub" type="submit">모임 생성</button></div>
              </form>
            </div>
          </div>
            
          
          
        </div>
      </section><!-- End Contact Section -->
   
      </div>
  
  </template>
  
  <script setup>
      import { ref } from 'vue'
      import { useClubStore } from '@/stores/club'
      import { useCounterStore } from '@/stores/counter';
      import { useMemberStore } from '@/stores/member';
      import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'
      
  
      const clubstore = useClubStore()
      const counterstore = useCounterStore()
      const memberstore = useMemberStore()
      
      const locationIcon = counterstore.selectButton('LocationIcon')
    
      const profileImg = ref(null)
      const clubname = ref('테스트클럽이름')
      const introduce = ref('소모임생성테스트')
      const region_cd = ref('1121510300')
      const young_birth = ref('2000')
      const old_birth = ref('1996')
      const gender_type = ref('A')
      const max_capacity = ref(10)
      const is_auto_recruit = ref(true)
      const is_open_recruit = ref(true)
      const region_name = ref('')
      const seq = ref('')
      region_name.value = memberstore.getLocationInfo()[0]
      region_cd.value = memberstore.getLocationInfo()[1]
      const setrecruitType = (value) => {
        is_auto_recruit.value = value
        console.log(`selected recruit : ${is_auto_recruit.value}`)
      }
      const setGenderType = (value) => {
        gender_type.value = value
        console.log(`selected gender : ${gender_type.value}`)
      }
  
      const modifyClub = function () {
          const payload = {
              profileImg: profileImg.value,
              clubSeq: seq.value,
              clubname: clubname.value,
              introduce: introduce.value,
              region_cd: region_cd.value,
              young_birth: young_birth.value,
              old_birth: old_birth.value,
              gender_type: gender_type.value,
              max_capacity: max_capacity.value,
              is_auto_recruit: is_auto_recruit.value,
              is_open_recruit: is_open_recruit.value,
          }
          console.log(payload)
          clubstore.modifyClub(payload)
      } 
      
      const checkDuplicate = (name) => {
        clubstore.checkDuplicate(name)
      }
      const readInputFile = (e) => {
        document.getElementById('imageFrame').innerHTML = '';
        const files = e.target.files;
        const fileArr = Array.from(files);
        console.log(fileArr[0]);
        profileImg.value = fileArr[0]
        console.log(`현재 저장된 프로필 이미지 : ${profileImg.value}`)
        fileArr.forEach(function(f) {
          if (!f.type.match("image/.*")) {
            alert("이미지 확장자만 업로드 가능합니다.");
            return;
          }
          const reader = new FileReader();
          reader.onload = function(e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            
            document.getElementById('imageFrame').appendChild(img);
  
  
          };
          reader.readAsDataURL(f);
        });
      }
      
    
  </script>
  
  <style scoped>
  .circular {
    width: 200px; /* 원하는 크기로 조정 */
    height: 200px;
    border-radius: 50%; /* 50%로 설정하여 원 모양으로 만듭니다 */
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
  }
  .circular img {
    width: 100%; /* 부모 요소(.circular-image)에 대한 상대적인 크기로 설정 */
    height: 100%; /* 가로 세로 비율을 유지하도록 설정 */
    object-fit: cover; /* 이미지 간격 제거를 위해 인라인 요소 대신 블록 요소로 설정 */
    border-radius: 50%;
  }
  </style>