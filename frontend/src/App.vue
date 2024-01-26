<script setup>
import { RouterLink, RouterView } from 'vue-router'
import HelloWorld from './components/HelloWorld.vue'
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
</script>

<template>
  <header>
    <img alt="Vue logo" class="logo" src="@/assets/logo.svg" width="125" height="125" />

    <div class="wrapper">
      <HelloWorld msg="You did it!" />
      <button type="button" @click="test">나는 버튼</button>

      <nav>
        <RouterLink to="/">Home</RouterLink>
        <RouterLink to="/about">About</RouterLink>
      </nav>
    </div>
  </header>

  <RouterView />
</template>

<script setup>
import TheFooter from './components/common/TheFooter.vue'
import TheHeaderNav from './components/common/TheHeaderNav.vue'


</script>

<style scoped>
/* #app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
} */
/* 반응형 최대크기 */
#app {
  margin: 0 auto;
  width: 750px;
  min-height: 100%;
  padding-top: 71px;
  border-left: 1px #d8d8d8 solid;
  border-right: 1px #d8d8d8 solid;
  position: relative;
  text-align: center;
}

/* 여백제거 + 드래그방지 */
body {
  padding: 0;
  margin: 0;
  -ms-user-select: none;
  -moz-user-select: none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}

/* 반응형 시작 */
@media (max-width: 750px) {
  #app {
    width: 100%;
    border-left: 0;
    border-right: 0;
  }
}
</style>
