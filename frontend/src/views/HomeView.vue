<template>
  HelloWorld It's TestPage.
  <div>
    <ButtonWithIcon> </ButtonWithIcon>
  </div>
</template>

<script setup>
import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue'
import { useCounterStore } from '@/stores/counter'

const store = useCounterStore()
store.selectButton('LogoIcon')
</script>

<style scoped></style>
