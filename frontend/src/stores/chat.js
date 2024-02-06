import { defineStore } from 'pinia'
import webstomp from 'webstomp-client'
import axios from 'axios'
import { ref } from 'vue'

export const useChatStore = defineStore('chat', () => {
  const STOMP = 'wss://i10a808.p.ssafy.io:8082/ws'
  const REST_CHAT_API = 'https://i10a808.p.ssafy.io/api/chat'
  const client = ref(null)

  const loadMessage = function (chatSeq, offset) {
    axios({
      method: 'get',
      url: `${REST_CHAT_API}/${chatSeq}/${offset}`, // REST_CLUB_API는 해당 API 엔드포인트를 가리킵니다.
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
      .then((res) => {
        console.log(res)
      })
      .catch((err) => {
        console.error(err)
      })
  }

  const getConnection = function (chatSeq) {
    client.value = webstomp.over(new WebSocket(STOMP))

    // 웹소켓 연결 및 STOMP 세션 시작
    client.value.connect(
      {},
      function (frame) {
        // 연결 성공
        console.log('Connected: ' + frame)

        // 특정 목적지를 구독
        client.value.subscribe(`/sub/message/${chatSeq}`, function (message) {
          // 받은 메시지를 처리
          console.log(JSON.parse(message.body).content)
        })
      },
      function (error) {
        // 연결 실패 또는 에러 처리
        console.error('STOMP error:', error)
      }
    )
  }

  return {
    loadMessage,
    getConnection,
    client
  }
})
