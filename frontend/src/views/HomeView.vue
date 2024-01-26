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
 
</template>

<script setup>
    import ButtonWithIcon from '@/components/common/ButtonWithIcon.vue';
    import { useCounterStore } from '@/stores/counter'

    const store = useCounterStore()
    const locationIcon = store.selectButton('LocationIcon')
    
    
</script>

<style scoped>

</style>