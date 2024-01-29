<template>
    <div>
    <br />
        <h1>모임 정보</h1>
    <br />
    <fieldset>
      <form>
        <!-- 모임명 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">모임명</label>
          </div>
          <div class="col-auto">
            <input
              v-model.trim="clubname"
              type="text"
              class="form-control"
              
              style="width: 18rem"
            />
          </div>
          <div class="col-auto">
            <button type="button" class="btn btn-primary mb-3" @click="checkDuplicate('id')">
              중복확인
            </button>
          </div>
        </div>
        <!-- 소개글 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">소개글</label>
          </div>
          <div class="col-auto">
            <input
              v-model.trim="introduce"
              type="textarea"
              class="form-control-lg"
              
              style="width: 18rem"
            />
          </div>
        </div>

        <!-- 내 동네 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">내 동네</label>
          </div>
          <div class="col-auto">
            <input
              v-model.trim="region_cd"
              type="text"
              class="form-control"
              
              style="width: 18rem"
            />
          </div>
          <div class="col-auto">
            <ButtonWithIcon 
            :selectedIcon="locationIcon"
            />
          </div>
        </div>

        <!-- 가입년생 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">가입연령</label>
          </div>
          <div class="col-auto">
            <input
              v-model.trim="young_birth"
              type="text"
              class="form-control"
              
              style="width: 10rem"
            />
          </div>
          ~
          <div class="col-auto">
            <input
              v-model.trim="old_birth"
              type="text"
              class="form-control"
              
              style="width: 10rem"
            />
          </div>

        </div>

        <!-- 성별 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">성별</label>
          </div>
          <div class="col-auto">
            <input
              v-model.trim="gender_type"
              type="text"
              class="form-control"
              
              style="width: 18rem"
            />
          </div>
        </div>

        <!-- 최대인원 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">최대인원</label>
          </div>
          <div class="col-auto">
            <input
              v-model.trim="max_capacity"
              type="number"
              class="form-control"
              
              style="width: 18rem"
            />
          </div>
        </div>

        <!-- 즉시 가입 여부 -->
        <div class="row g-3 align-items-center">
          <div class="col-auto">
            <label for="inputPassword6" class="col-form-label">즉시 가입 여부</label>
          </div>
          <div class="col-auto">
            <input
              v-model="is_auto_recruite"
              type="radio"
              class="form-check-input"
              value="true"
              id="auto_recruite"
            /> <label class="form-check-label" for="auto_recruite">즉시가입</label>
            <input
              v-model="is_auto_recruite"
              type="radio"
              class="form-check-input"
              value="false"
              id="no_auto_recruite"
            /> <label class="form-check-label" for="no_auto_recruite">가입승인</label>

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
            <button type="submit" class="btn btn-outline-primary" @click="addClub">모임 생성</button>
          </div>
        </div>

        </form>
    </fieldset>
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