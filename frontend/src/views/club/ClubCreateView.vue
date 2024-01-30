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
            <form action="forms/contact.php" method="post" role="form" class="php-email-form">
                <div class="row">
                  <div class="form-group col-md-8">
                    <label for="name">모임명</label>
                    <input type="text" name="name" class="form-control" id="name" required>
                  </div>
                  <div class="col-md-4 cta-btn-container text-center">
                    <a class="cta-btn align-middle" href="#">중복 확인</a>
                  </div>
                  
                  <div class="form-group mt-3">
                    <label for="name">소개글</label>
                    <textarea class="form-control" name="message" rows="10" required></textarea>
                  </div>
              

                    <div class="form-group col-md-6">
                      <label for="name">내 동네</label>
                      <input type="text" name="name" class="form-control" id="name" required>
                    </div>
                    <div class="col-md-4">
                      
                      <ButtonWithIcon 
                      :selectedIcon="locationIcon"
                      />
                    </div>
                

                  <div class="row">
                    <div class="form-group col-md-5 mt-3 mt-md-0">
                      <label for="name">시작 연령</label>
                      <input type="email" class="form-control" name="email" id="email" required>
                    </div>
                    <div class="form-group col-md-5 mt-3 mt-md-0">
                      <label for="name">늙은 나이 </label>
                      <input type="email" class="form-control" name="email" id="email" required>
                    </div>
                  </div>

              </div>
              <div class="form-group col-md-6 mt-3">
                <label for="name">성별</label>
                <div class="row">
                  <div class="portfolio col-md-6 d-flex justify-content-center">
                    <ul id="portfolio-flters">
                      <li data-filter="*" class="filter-active">남</li>
                      <li data-filter=".filter-app">여</li>
                      <li data-filter=".filter-card">무관</li>
                    </ul>
                  </div>
                </div>
              </div>

              <div class="form-group col-md-8">
                    <label for="name">최대인원</label>
                    <input type="text" name="name" class="form-control" id="name" required>
                  </div>

              <div class="form-group col-md-6 mt-3">
                <label for="name">즉시 가입 여부</label>
                <div class="row">
                  <div class="portfolio col-md-8 d-flex justify-content-center">
                    <ul id="portfolio-flters">
                      <li data-filter="*" class="filter-active">즉시가입</li>
                      <li data-filter=".filter-app">가입승인</li>
                    </ul>
                  </div>
                </div>
                  
              </div>
                  <div class="my-3">
                    <div class="loading">Loading</div>
                    <div class="error-message"></div>
                    <div class="sent-message">Your message has been sent. Thank you!</div>
                  </div>
                  <div class="text-center"><button type="submit">모임 생성</button></div>
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
    import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'
    

    const clubstore = useClubStore()
    const counterstore = useCounterStore()
    
    const locationIcon = counterstore.selectButton('LocationIcon')

    const clubname = ref('')
    const introduce = ref('')
    const region_cd = ref('')
    const young_birth = ref('')
    const old_birth = ref('')
    const gender_type = ref('')
    const max_capacity = ref(0)
    const is_auto_recruite = ref(true)


 
    const addClub = function () {
        const payload = {
            clubname: clubname.value,
            'introduce': introduce.value,
            'region_cd': region_cd.value,
            'young_birth': young_birth.value,
            'old_birth': old_birth.value,
            'gender_type': gender_type.value,
            'max_capacity': max_capacity.value,
            'is_auto_recruite': is_auto_recruite.value,
        }
        clubstore.createClub(payload)
        console.log(payload)
    } 
    
    const checkDuplicate = (field) => {
      // 중복 확인 로직 구현
      console.log(`${field} 중복 확인`)
    }

</script>

<style scoped>

</style>