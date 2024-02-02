import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import 'bootstrap'
import 'bootstrap/dist/css/bootstrap.min.css'
// 1. 전역 컴포넌트 로딩
import StopWatch from './components/walk/StopWatch.vue'
const app = createApp(App)

app.use(createPinia())
app.use(router)

app.component('StopWatch', StopWatch)
app.mount('#app')
