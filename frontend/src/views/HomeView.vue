<template>
    <div>
        HelloWorld It's TestPage.
    </div>
    <div>

        <ButtonWithIcon
        :name="locationIcon.name"
        :icon="locationIcon.icon"
        :urlName="locationIcon.urlName"
        />


    </div>
 
    <div>
        <button type="button" @click="test" class="btn btn-primary">나는 버튼</button>
    </div>
</template>

<script setup>
import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'
import { useCounterStore } from '@/stores/counter'
import axios from 'axios'
const test = () => {
  axios({
    method: 'post',
    url: 'https://i10a808.p.ssafy.io/api/member/check-id',
    data: {
      'memberId' : 'minhou'
    }
  })
}
    const store = useCounterStore()
    const locationIcon = store.selectButton('LocationIcon')
    
    
</script>

<style scoped></style>
