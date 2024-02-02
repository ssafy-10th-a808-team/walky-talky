import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import dotenv from 'dotenv'
import vue from '@vitejs/plugin-vue'

const env = dotenv.config().parsed
// https://vitejs.dev/config/
export default defineConfig({
  'process.env': env,
  plugins: [
    vue(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
})
