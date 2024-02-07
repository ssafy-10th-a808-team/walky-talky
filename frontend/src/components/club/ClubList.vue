<template>
    <div v-if="props.templateType === 1">
        <div class=" portfolio-info">
            <div class="circular">
                <!-- <img :src="props.club.url" alt="@/assets/img/Logo.png" /> -->
                <img :src="imageUrl" @error="onImageError" alt="클럽 로고" />
            </div>
            <div class="info-container">
                <h3>{{ props.club.name }}</h3>
            </div>
        </div>
    </div>

    <div v-else-if="props.templateType === 2">
        <div class=" portfolio-info">
            <div class="circular">
                <!-- <img :src="props.club.url" alt="@/assets/img/Logo.png" /> -->
                <img :src="imageUrl" @error="onImageError" alt="클럽 로고" />
            </div>
            <div class="info-container">
                <h3>{{ props.club.name }}</h3>
                <ul class="custom-list">
                    <li>{{
                        club.genderType === 'A' ? '남녀무관'
                        : club.genderType === 'M' ? '남자만'
                            : club.genderType === 'F' ? '여자만'
                                : club.genderType }}</li>
                    <li v-if="club.introduce">
                        {{ club.introduce }}
                    </li>
                    <li>{{ club.oldBirth }}년생 ~ {{ club.youngBirth }}년생</li>
                    <li>{{ club.nowCapacity }}명 / {{ club.maxCapacity }}명</li>
                </ul>
            </div>
        </div>
    </div>
    <div v-else-if="props.templateType === 3">
        <div class=" portfolio-info">
            <div class="circular">
                <!-- <img :src="props.club.url" alt="@/assets/img/Logo.png" /> -->
                <img :src="imageUrl" @error="onImageError" alt="클럽 로고" />
            </div>
            <div class="info-container">
                <h3>{{ props.club.name }}</h3>
                <ul class="custom-list">
                    <li>
                        <div>{{ club.address }}

                        </div>
                    </li>
                    <li>{{
                        club.genderType === 'A' ? '남녀무관'
                        : club.genderType === 'M' ? '남자만'
                            : club.genderType === 'F' ? '여자만'
                                : club.genderType }}</li>
                    <!-- <li v-if="club.introduce">
                        {{ club.introduce }}
                    </li> -->
                    <li>{{ club.oldBirth }}년생 ~ {{ club.youngBirth }}년생</li>
                    <li>{{ club.nowCapacity }} / {{ club.maxCapacity }}명</li>
                </ul>
            </div>
        </div>
    </div>
</template>

<script setup>
import { defineProps, computed } from 'vue'

const props = defineProps({
    club: {
        type: Object,
        required: true
    },
    templateType: {
        type: Number,
        default: 0
    }
})

// 대체 이미지 경로를 정적으로 정의합니다.
// Vue CLI 프로젝트에서 static assets를 import하는 방법입니다.
import defaultImage from '@/assets/img/Logo.png';

// 이미지 URL이 null이 아니면 해당 URL을, null이면 대체 이미지 경로를 사용합니다.
const imageUrl = computed(() => props.club.url || defaultImage);

// 이미지 로드 실패 시 대체 이미지로 교체하는 함수
function onImageError(event) {
    event.target.src = defaultImage;
}

</script>



<style scoped>
.portfolio-info {
    display: flex;
    /* Flex 컨테이너 설정 */
    flex-direction: column;
    /* 자식 요소들을 세로 방향으로 정렬 */
    align-items: center;
    /* 가로 방향에서 중앙 정렬 */
    justify-content: center;
    /* 세로 방향에서 중앙 정렬 */
    text-align: center;
    /* 텍스트 중앙 정렬 */
}

.circular {
    width: 200px;
    /* 고정 가로 크기 */
    height: 200px;
    /* 고정 세로 크기 */
    border-radius: 50%;
    /* 원 모양으로 만듦 */
    overflow: hidden;
    display: flex;
    align-items: center;
    /* 이미지를 중앙에 정렬 */
    justify-content: center;
}

.circular img {
    width: 100%;
    height: 100%;
    object-fit: cover;
    /* 이미지 비율 유지하며 컨테이너 채움 */
    border-radius: 50%;
}

.custom-list {
    list-style-type: none;
    /* 리스트 항목 앞의 점을 제거 */
    padding-left: 0;
    /* 리스트의 왼쪽 패딩 제거 */
}
</style>