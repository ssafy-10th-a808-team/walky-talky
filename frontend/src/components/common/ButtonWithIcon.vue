<template>
    
        <ButtonWithIconItem 
        v-if="selectedButton"
        :icon="selectedButton.icon"
        :urlName="selectedButton.urlName">
    
        </ButtonWithIconItem> 
    
</template>

<script setup>
    import ButtonWithIconItem from './ButtonWithIconItem.vue'
   

</script>

<style scoped>

</style>