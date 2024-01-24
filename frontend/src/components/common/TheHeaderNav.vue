<template>
<nav class="navbar navbar-expand-lg bg-body-tertiary">
  <div class="container-fluid">
    <a class="navbar-brand">
      <img src="../../assets/img/Logo.png" alt="Logo" width="30" height="30">
    </a>
    <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav" aria-controls="navbarNav" aria-expanded="false" aria-label="Toggle navigation">
      <span class="navbar-toggler-icon"></span>
    </button>
    <div class="collapse navbar-collapse" id="navbarNav">
      <ul class="navbar-nav">
        <li class="nav-item">
          <RouterLink :to="{ name : 'group' }">
            <a class="nav-link" aria-current="page">소모임 찾기</a>
          </RouterLink>
        </li>
        <li class="nav-item">
          <RouterLink :to="{ name: 'walk'}">
            <a class="nav-link">산책하기</a>
          </RouterLink>
        </li>
        <li class="nav-item">
          <RouterLink :to="{ name: 'share-board'}">
            <a class="nav-link">산책 공유 게시판</a>
          </RouterLink>
        </li>

      </ul>
    </div>
  </div>
</nav>
</template>

<script setup>

</script>

<style scoped>

</style>