<template></template>
<script setup>
const watchLocationUpdates = function () {
  let intervalId // intervalId 변수를 함수 외부에 선언

  // 기존 코드...

  started.value = setInterval(clockRunning, 1000)
  running.value = true
  isPause.value = false

  const marker = new kakao.maps.Marker({
    position: new kakao.maps.LatLng(lat, lon)
  })

  marker.setMap(state.value.map)

  watchPositionId.value = navigator.geolocation.watchPosition(
    (position) => {
      // 기존 코드...

      if (previous.value.lat === 0) {
        // 기존 코드...

        intervalId = setInterval(() => {
          recordsForPost.value.push({
            lat: current.value.lat,
            lon: current.value.lon,
            time: new Date()
          })
        }, 60000)
      }
    },
    () => {
      router.push('/walk/do-walk')
      console.log('위치 정보를 가져오는 도중 오류가 발생했습니다')
    },
    {
      timeout: 5000,
      maximumAge: 0,
      enableHighAccuracy: true,
      distanceFilter: 40
    }
  )

  return () => {
    clearInterval(started.value)
    clearInterval(intervalId) // intervalId를 함수 외부에서 참조할 수 있도록 변경
  }
}
</script>

<style scoped></style>
