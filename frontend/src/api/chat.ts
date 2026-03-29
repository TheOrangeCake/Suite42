import { Client, type IMessage, StompHeaders } from '@stomp/stompjs'
import SockJS from 'sockjs-client'
import { http } from './http'
import { useAuthStore } from '../stores/auth'

export interface ChatMessage {
  senderId: string
  recipientId: string
  content: string
  timestamp: number
}

let stompClient: Client | null = null


export function connectChat(
  userId: string,
  onMessage: (msg: ChatMessage) => void,
  onConnectionChange?: (connected: boolean) => void
) {
  const authStore = useAuthStore()

  if (stompClient && stompClient.connected) {
    return
  }

  stompClient = new Client({
    // SockJS retourne un objet WebSocket-like
    // @ts-ignore
    webSocketFactory: () => new SockJS(`/ws-chat`),

    connectHeaders: authStore.accessToken
      ? { Authorization: `Bearer ${authStore.accessToken}` } as StompHeaders
      : {},

    reconnectDelay: 5000, // reconnect automatique

    onConnect: () => {
      onConnectionChange?.(true)

      stompClient?.subscribe(
        `/user/queue/messages`,
        (frame: IMessage) => {
          try {
            const message: ChatMessage = JSON.parse(frame.body)
            onMessage(message)
          } catch (err) {
            console.error('[WS] Failed to parse message:', err)
          }
        }
      )
    },

    onDisconnect: () => {
      onConnectionChange?.(false)
    },

    onStompError: (frame) => {
      onConnectionChange?.(false)
    },

    onWebSocketError: (event: Event) => {
      onConnectionChange?.(false)
    },
  })

  stompClient.activate()
}

export function sendMessage(msg: ChatMessage) {
  if (!stompClient || !stompClient.connected) {
    return
  }

  stompClient.publish({
    destination: '/app/chat',
    body: JSON.stringify(msg),
  })
}

export function disconnectChat() {
  if (stompClient) {
    stompClient.deactivate()
    stompClient = null
  }
}

export function getMessageHistory(senderId: string, recipientId: string) {
  return http.get<ChatMessage[]>(
    `/api/chat/messages/${senderId}/${recipientId}`
  )
}