<template>
  <!-- ======= Footer ======= -->
  <footer id="footer">
    <div class="container">
      <h3>WalkyTalky</h3>
      <p>산책해듀오</p>
      <div class="copyright">
        &copy; Copyright <strong><span>WalkyTalky</span></strong
        >. All Rights Reserved
      </div>
    </div>
  </footer>
  <!-- End Footer -->

  <a href="#" class="back-to-top d-flex align-items-center justify-content-center"
    ><i class="bi bi-arrow-up-short"></i
  ></a>
</template>

<script setup></script>

<style scoped>
#footer {
  position: -webkit-sticky; /* Safari 브라우저 지원 */
  position: sticky;
  bottom: 0;
  width: 100%;
  padding: 10px 0;
  /* 푸터의 내부 패딩을 상하 10px, 좌우 0으로 설정 */
  text-align: center;
  /* 텍스트를 중앙에 배치 */
  position: absolute;
  width: 100%;
}

#footer .container {
  max-width: none;
  /* 컨테이너의 최대 너비 설정을 해제 (필요한 경우) */
}

#footer h3 {
  margin-bottom: 10px;
  /* h3 태그 아래 마진 간격 줄임 */
  font-size: 1.5rem;
  /* h3 태그의 폰트 사이즈 조절 (필요한 경우) */
}

#footer p {
  margin-bottom: 10px;
  /* 문단(p) 아래 마진 간격 줄임 */
}

/* 브라우저 기본 스타일에 따라 footer 요소에 기본 마진이 있을 수 있으므로 제거 */
footer {
  margin: 0;
}

/* 저작권 표시 부분의 여백 조절 */
.footer .copyright {
  padding: 10px 0;
  margin: 0;
}
</style>
