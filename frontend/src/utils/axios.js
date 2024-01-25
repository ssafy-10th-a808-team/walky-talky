import axios from 'axios'

// axios 객체 생성
export default axios.create({
  //   baseURL: 'https://i10a808.p.ssafy.io/api',
  baseURL: 'http://localhost:5173',
  headers: {
    'Content-type': 'application/json'
  },
  withCredentials: true
})
