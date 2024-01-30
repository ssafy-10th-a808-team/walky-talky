<template>
  <div>
    <WalkHeaderNav />
    <h1>산책하기</h1>
    <div id="map" style="width:100%;height:350px;"></div>
</div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import WalkHeaderNav from '@/components/common/WalkHeaderNav.vue'
const API_KEY = import.meta.env.VITE_KAKAO_API_KEY
const script = document.createElement("script");
script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${API_KEY}&autoload=false&libraries=clusterer,services&`;
document.head.appendChild(script);
script.onload = () => {
    kakao.maps.load(() => {
        const node = document.getElementById('map'); // 지도를 표시할 div
        const center = new kakao.maps.LatLng(37.50802, 127.062835);
        const options = {
            center,
            level: 3
        };
        const map = new kakao.maps.Map(node, options);
    });
};

onMounted(() => {
  const mapContainer = document.getElementById('map'); // 지도를 표시할 div

  const mapOption = {
    center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
    level: 3 // 지도의 확대 레벨
  };

  const map = new kakao.maps.Map(mapContainer, mapOption); // 지도를 생성합니다

  const content = `
    <div class="label">
      <span class="left"></span>
      <span class="center">카카오!</span>
      <span class="right"></span>
    </div>
  `

  const position = new kakao.maps.LatLng(33.450701, 126.570667);
  const customOverlay = new kakao.maps.CustomOverlay({
    position: position,
    content: content
  });

  customOverlay.setMap(map);
})
</script>

<style scoped>
.label {margin-bottom: 96px;}
.label * {display: inline-block;vertical-align: top;}
.label .left {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_l.png") no-repeat;display: inline-block;height: 24px;overflow: hidden;vertical-align: top;width: 7px;}
.label .center {background: url(https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_bg.png) repeat-x;display: inline-block;height: 24px;font-size: 12px;line-height: 24px;}
.label .right {background: url("https://t1.daumcdn.net/localimg/localimages/07/2011/map/storeview/tip_r.png") -1px 0  no-repeat;display: inline-block;height: 24px;overflow: hidden;width: 6px;}
</style>
