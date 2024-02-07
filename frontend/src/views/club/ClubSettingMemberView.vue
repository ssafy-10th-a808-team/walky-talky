<template>
    <ClubDetailHeaderNav />
    <ClubSettingHeaderNav />
    <div class="members-list">
        <div v-for="clubmember in clubstore.clubDetail.responseClubDetailDtoMembers" :key="clubmember.nickname"
            class="member-item">
            <!-- <MemberList :member="clubmember" /> -->
            <div class="member-container">
                <div class="circular-small">
                    <img :src="clubmember.url" alt="">
                    <p class="owner-badge" v-if="clubmember.role === 'owner'">OWNER</p>
                </div>
                <div class="member-info">
                    <div>{{ clubmember.nickname }}</div>
                    <div>{{ clubmember.address }}</div>
                </div>
                <!-- 추방하기 버튼 추가 -->
                <button v-if="clubmember.role === 'member'" @click="clubstore.exclude(clubmember)">추방하기</button>
            </div>
        </div>
    </div>
    <!-- <div class="members-list">
        <div v-for="clubmember in clubstore.clubDetail.responseClubDetailDtoMembers" :key="clubmember.nickname"
            class="member-item">
            <MemberList :member="clubmember"></MemberList>
        </div>
    </div> -->
</template>

  
<script setup>
import ClubDetailHeaderNav from '@/components/common/ClubDetailHeaderNav.vue'
import { onMounted, ref } from 'vue'
import { useClubStore } from '@/stores/club'
import MemberList from '@/components/member/MemberListView.vue'
import axios from 'axios';
import { useCounterStore } from '@/stores/counter'
import ClubSettingHeaderNav from '@/views/club/ClubSettingHeaderNav.vue'

const clubstore = useClubStore()

const { seq } = defineProps({
    seq: String
})

onMounted(async function () {
    await clubstore.findClub(seq)
}
)

</script>
  
<style scoped>
.club-detail-container {
    max-width: 768px;
    margin: 0 auto;
    padding: 20px;
    background-color: #fff;
    box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
    border-radius: 10px;
}

.club-image-container {
    display: flex;
    align-items: center;
    gap: 20px;
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

.club-info {
    flex-grow: 1;
}

.club-name {
    color: #333;
    font-size: 24px;
    font-weight: bold;
    margin-bottom: 10px;
}

.club-introduce {
    font-size: 16px;
    color: #666;
    margin-bottom: 5px;
}

.club-address,
.club-age-range,
.club-gender,
.club-status,
.club-capacity,
.club-join,
.club-recruitment {
    font-size: 14px;
    color: #888;
}

.members-title {
    margin-top: 30px;
    font-size: 18px;
    font-weight: bold;
    color: #333;
}

.members-list {
    margin-top: 10px;
}

.member-item {
    margin-bottom: 10px;
}

.action-button-container {
    display: flex;
    justify-content: center;
    /* Center the button horizontally */
    margin: 20px 0;
    /* Add some space above and below the button */
}

.action-button {
    padding: 10px 20px;
    background-color: darkgreen;
    /* The green background color from the image */
    color: white;
    /* White text color */
    border: none;
    border-radius: 25px;
    /* Rounded corners like in the image */
    font-size: 16px;
    cursor: pointer;
    transition: background-color 0.3s ease;
    /* Smooth transition for hover effect */
}

.action-button:hover {
    background-color: #45A049;
    /* Slightly darker green on hover */
}

.member-container {
    display: flex;
    align-items: center;
    /* justify-content: space-between; */
    /* 컨테이너 내 아이템들 사이의 공간을 균등하게 설정 */
}

.circular-small {
    width: 64px;
    height: 64px;
    border-radius: 50%;
    overflow: hidden;
    margin-right: 16px;
    position: relative;
    /* 'OWNER' 텍스트를 위해 상대 위치 설정 */
}

.circular-small img {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.owner-badge {
    position: absolute;
    /* 절대 위치 설정 */
    bottom: -20px;
    /* 하단에서부터의 위치, 텍스트 높이에 따라 조정 */
    left: 50%;
    /* 왼쪽에서부터 50% 위치에 배치 */
    transform: translateX(-50%);
    /* X축 기준 중앙 정렬 */
    background-color: rgba(0, 0, 0, 0.5);
    /* 배경색 추가 */
    color: greenyellow;
    /* 글자색 */
    padding: 2px 10px;
    /* 패딩 */
    border-radius: 10px;
    /* 둥근 모서리 */
    font-size: 12px;
    /* 글자 크기 */

}

/* 추방하기 버튼 스타일 */
button {
    padding: 5px 15px;
    background-color: #e74c3c;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    margin-left: auto;
    /* 왼쪽에 자동 마진을 추가하여 오른쪽 정렬 */
}

button:hover {
    background-color: #c0392b;
}
</style>