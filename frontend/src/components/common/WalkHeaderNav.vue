<template>
  <div>
    <ul class="nav justify-content-center">
      <li class="nav-item">
        <RouterLink :to="{ name: 'DoWalk' }">
          <a class="nav-link" href="#">산책하기</a>
        </RouterLink>
      </li>
      <li class="nav-item">
        <RouterLink :to="{ name: 'WalkList' }">
          <a class="nav-link" href="#">내코스</a>
        </RouterLink>
      </li>
      <li class="nav-item">
        <RouterLink :to="{ name: 'ScrapList' }">
          <a class="nav-link" href="#">스크랩</a>
        </RouterLink>
      </li>
      <li class="nav-item">
        <RouterLink :to="{ name: 'RecommendList' }">
          <a class="nav-link" href="#">산책추천</a>
        </RouterLink>
      </li>
    </ul>
  </div>
</template>

<script setup></script>

<style scoped>
li {
  list-style-type: none;
  list-style-position: inside;
}
a {
  color: black;
}
</style>
