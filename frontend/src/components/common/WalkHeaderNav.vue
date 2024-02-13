<template>
  <div>
    <ul class="navbar justify-content-center">
      <li class="nav-item">
        <RouterLink :to="{ name: 'DoWalk' }" style="text-decoration-line: none">
          <a class="nav-link" aria-current="page" href="#">산책하기</a>
        </RouterLink>
      </li>
      <li class="nav-item">
        <RouterLink :to="{ name: 'WalkList' }" style="text-decoration-line: none">
          <a class="nav-link" href="#">내코스</a>
        </RouterLink>
      </li>
      <li class="nav-item">
        <RouterLink :to="{ name: 'ScrapList' }" style="text-decoration-line: none">
          <a class="nav-link" href="#">스크랩</a>
        </RouterLink>
      </li>
      <li class="nav-item">
        <RouterLink :to="{ name: 'RecommendList' }" style="text-decoration-line: none">
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
}
</style>
