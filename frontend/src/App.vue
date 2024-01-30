<template>
  <div id="app">

    <head>
      <meta charset="utf-8">
      <meta content="width=device-width, initial-scale=1.0" name="viewport">
      
      <title>Green Bootstrap Template - Index</title>
      <meta content="" name="description">
      <meta content="" name="keywords">
      
      <!-- Favicons -->
      <link href="/assets/img/favicon.png" rel="icon">
      <link href="/assets/img/apple-touch-icon.png" rel="apple-touch-icon">
      
      <!-- Google Fonts -->
      <link href="https://fonts.googleapis.com/css?family=Open+Sans:300,300i,400,400i,600,600i,700,700i|Raleway:300,300i,400,400i,500,500i,600,600i,700,700i|Poppins:300,300i,400,400i,500,500i,600,600i,700,700i" rel="stylesheet">
      
      <!-- Vendor CSS Files -->
      <link href="/assets/vendor/animate.css/animate.min.css" rel="stylesheet">
      <link href="/assets/vendor/bootstrap/css/bootstrap.min.css" rel="stylesheet">
      <link href="/assets/vendor/bootstrap-icons/bootstrap-icons.css" rel="stylesheet">
      <link href="/assets/vendor/boxicons/css/boxicons.min.css" rel="stylesheet">
      <link href="/assets/vendor/glightbox/css/glightbox.min.css" rel="stylesheet">
      <link href="/assets/vendor/swiper/swiper-bundle.min.css" rel="stylesheet">
      
      <!-- Template Main CSS File -->
      <link href="/assets/css/style.css" rel="stylesheet">
      
      <!-- =======================================================
        * Template Name: Green
        * Updated: Jan 29 2024 with Bootstrap v5.3.2
        * Template URL: https://bootstrapmade.com/green-free-one-page-bootstrap-template/
        * Author: BootstrapMade.com
        * License: https://bootstrapmade.com/license/
        ======================================================== -->
      </head>
      <body>
        <TheHeaderNav />
        <RouterView />
        <TheFooter />
        
        
        
      </body>
      
    </div>
  </template>

<script setup>
import { onMounted } from 'vue';
import TheFooter from './components/common/TheFooter.vue'
import TheHeaderNav from './components/common/TheHeaderNav.vue'

    onMounted(() => {
      {
      // Vendor JS 파일 로드
      const scripts = [
        "/assets/vendor/bootstrap/js/bootstrap.bundle.min.js",
        "/assets/vendor/glightbox/js/glightbox.min.js",
        "/assets/vendor/isotope-layout/isotope.pkgd.min.js",
        "/assets/vendor/swiper/swiper-bundle.min.js",
        "/assets/vendor/php-email-form/validate.js",
        "/assets/js/main.js"
      ];

      scripts.forEach(script => {
        const scriptElement = document.createElement("script");
        scriptElement.src = script;
        document.body.appendChild(scriptElement);
      });
    }
      
    }) 
  

</script>

<style lang="scss" scoped>

</style>
