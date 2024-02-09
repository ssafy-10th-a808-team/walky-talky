import { defineStore } from 'pinia'
import axios from 'axios'
import { ref } from 'vue'
import { Client } from '@stomp/stompjs'
import { useCounterStore } from './counter'

export const useChatStore = defineStore('chat', () => {
  const STOMP = 'wss://i10a808.p.ssafy.io/ws'
  const REST_CHAT_API = 'https://i10a808.p.ssafy.io/api/chat'
  const client = ref(null)
  const messages = ref([])
  const counterstore = useCounterStore()

  const loadMessage = async function (clubSeq, offset) {
    const response = await axios({
      method: 'get',
      url: `${REST_CHAT_API}/${clubSeq}/${offset}`, // REST_CLUB_API는 해당 API 엔드포인트를 가리킵니다.
      headers: {
        Authorization: `Bearer ${counterstore.getCookie('atk')}`
      }
    })

    if (response.data.data.list.length > 0) {
      messages.value = [...response.data.data.list.reverse(), ...messages.value]
    }
  }

  const getConnection = function (clubSeq) {
    client.value = new Client({
      brokerURL: STOMP,
      connectHeaders: {
        atk: `Bearer ${counterstore.getCookie('atk')}`
      },
      onConnect: () => {
        client.value.subscribe(
          `/sub/chat/${clubSeq}`,
          (message) => {
            const receivedMessage = JSON.parse(message.body)
            // 메시지 수신 시 messages 상태 업데이트
            messages.value = [...messages.value, receivedMessage]
          },
          {
            atk: `Bearer ${counterstore.getCookie('atk')}`
          }
        )
      },
      onStompError: () => {
        console.log('STOMP connection error')
      }
    })
    client.value.activate()
  }

  const sendMessage = function (message) {
    client.value.publish({
      destination: '/pub/message',
      body: JSON.stringify(message),
      headers: {
        atk: `Bearer ${counterstore.getCookie('atk')}`
      }
    })
  }

  return {
    client,
    messages, // 메시지 상태를 반환하여 다른 컴포넌트에서 접근 가능하게 함
    loadMessage,
    getConnection,
    sendMessage
  }
})
