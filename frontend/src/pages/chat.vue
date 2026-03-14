<template>
  <div class="chat-page">

    <!-- Colonne gauche : contacts -->
    <aside class="contacts-panel">
      <h2 class="panel-title">
        Messages
        <span class="ws-dot" :class="connected ? 'ws-on' : 'ws-off'" :title="connected ? 'Connexion active' : 'Déconnecté'"></span>
      </h2>

      <div class="search-box">
        <input
          v-model="searchInput"
          class="contact-input"
          placeholder="Login 42..."
          @keydown.enter="openConversation"
        />
        <button class="open-btn" @click="openConversation">Go</button>
      </div>

      <div
        v-for="login in recentContacts"
        :key="login"
        class="contact-item"
        :class="{ active: recipientId === login }"
        @click="selectContact(login)"
      >
        <span class="contact-login">{{ login }}</span>
      </div>

      <p v-if="recentContacts.length === 0" class="empty-hint">
        Entre un login pour démarrer une conversation.
      </p>
    </aside>

    <!-- Colonne droite : conversation -->
    <main class="conversation-panel">
      <template v-if="recipientId">
        <div class="conv-header">
          <span class="conv-title">{{ recipientId }}</span>
        </div>

        <div ref="messagesEl" class="messages">
          <div
            v-for="(msg, i) in messages"
            :key="i"
            class="bubble-row"
            :class="msg.senderId === myId ? 'mine' : 'theirs'"
          >
            <div class="bubble">
              <p class="bubble-text">{{ msg.content }}</p>
              <span class="bubble-time">{{ formatTime(msg.timestamp) }}</span>
            </div>
          </div>
        </div>

        <form class="input-bar" @submit.prevent="send">
          <input
            v-model="inputText"
            class="message-input"
            placeholder="Écris un message..."
            autocomplete="off"
          />
          <button type="submit" class="send-btn" :disabled="!inputText.trim()">
            Envoyer
          </button>
        </form>
      </template>

      <div v-else class="empty-conv">
        <p>Sélectionne un contact pour commencer à chatter.</p>
      </div>
    </main>

  </div>
</template>

<script setup lang="ts">
import { ref, watch, nextTick, onUnmounted } from 'vue'
import { useAuthStore } from '../stores/auth'
import {
  connectChat,
  disconnectChat,
  sendMessage,
  getMessageHistory,
  type ChatMessage,
} from '../api/chat'

definePage({
  meta: {
    requiresAuth: true,
    layout: 'dashboard',
  },
})

const authStore = useAuthStore()
const myId = authStore.user42?.login ?? authStore.user?.username ?? ''

const recipientId = ref('')
const searchInput = ref('')
const messages = ref<ChatMessage[]>([])
const inputText = ref('')
const connected = ref(false)
const recentContacts = ref<string[]>([])
const messagesEl = ref<HTMLElement | null>(null)

function openConversation() {
  const login = searchInput.value.trim()
  if (!login || login === myId) return
  searchInput.value = ''
  selectContact(login)
}

async function selectContact(login: string) {
  if (!recentContacts.value.includes(login)) {
    recentContacts.value.unshift(login)
  }
  recipientId.value = login
  messages.value = []

  try {
    messages.value = await getMessageHistory(myId, login)
    await scrollToBottom()
  } catch {
    // historique vide ou service indisponible
  }
}

function send() {
  const text = inputText.value.trim()
  if (!text || !recipientId.value) return

  const msg: ChatMessage = {
    senderId: myId,
    recipientId: recipientId.value,
    content: text,
    timestamp: Date.now(),
  }

  sendMessage(msg)
  messages.value.push(msg)
  inputText.value = ''
  scrollToBottom()
}

function formatTime(ts: number) {
  return new Date(ts).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })
}

async function scrollToBottom() {
  await nextTick()
  if (messagesEl.value) {
    messagesEl.value.scrollTop = messagesEl.value.scrollHeight
  }
}

// Connexion WebSocket au montage
connectChat(myId, (msg) => {
  // N'ajoute le message que si on est dans la bonne conversation
  if (
    (msg.senderId === recipientId.value && msg.recipientId === myId) ||
    (msg.senderId === myId && msg.recipientId === recipientId.value)
  ) {
    messages.value.push(msg)
    scrollToBottom()
  }

  // Ajoute l'expéditeur aux contacts récents si pas déjà présent
  const contact = msg.senderId === myId ? msg.recipientId : msg.senderId
  if (!recentContacts.value.includes(contact)) {
    recentContacts.value.unshift(contact)
  }
}, (isConnected) => {
  connected.value = isConnected
})

onUnmounted(() => {
  disconnectChat()
  connected.value = false
})

watch(recipientId, scrollToBottom)
</script>

<style scoped>
.chat-page {
  display: flex;
  height: 100vh;
  font-family: Monda, sans-serif;
  background: white;
}

/* ── Contacts ── */
.contacts-panel {
  width: 260px;
  min-width: 200px;
  border-right: 1px solid #e0e0e0;
  display: flex;
  flex-direction: column;
  padding: 24px 16px;
  gap: 12px;
  overflow-y: auto;
}

.panel-title {
  font-size: 1.2rem;
  font-weight: 700;
  color: #202020;
  margin: 0 0 8px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.ws-dot {
  width: 8px;
  height: 8px;
  border-radius: 50%;
  display: inline-block;
}

.ws-dot.ws-on  { background: var(--color-green); }
.ws-dot.ws-off { background: #ccc; }

.search-box {
  display: flex;
  gap: 8px;
}

.contact-input {
  flex: 1;
  padding: 8px 10px;
  border: 1.5px solid #e0e0e0;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.85rem;
  outline: none;
  transition: border-color 0.15s;
}

.contact-input:focus {
  border-color: var(--color-turquoise);
}

.open-btn {
  padding: 8px 12px;
  background: var(--color-turquoise);
  color: white;
  border: none;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
}

.open-btn:hover { opacity: 0.85; }

.contact-item {
  padding: 10px 12px;
  border-radius: 6px;
  cursor: pointer;
  transition: background 0.12s;
}

.contact-item:hover { background: #f4f4f4; }
.contact-item.active { background: #e8fdfe; }

.contact-login {
  font-size: 0.9rem;
  color: #202020;
  font-weight: 600;
}

.empty-hint {
  font-size: 0.8rem;
  color: #aaa;
  text-align: center;
  margin-top: 16px;
}

/* ── Conversation ── */
.conversation-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.conv-header {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 20px 28px;
  border-bottom: 1px solid #e0e0e0;
}

.conv-title {
  font-size: 1rem;
  font-weight: 700;
  color: #202020;
}

.conn-status {
  font-size: 0.75rem;
  padding: 2px 8px;
  border-radius: 12px;
  font-weight: 600;
}

.conn-status.online  { background: #e6fce7; color: #2d8c31; }
.conn-status.offline { background: #ffe8e8; color: #c0392b; }

.messages {
  flex: 1;
  overflow-y: auto;
  padding: 24px 28px;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.bubble-row {
  display: flex;
}

.bubble-row.mine   { justify-content: flex-end; }
.bubble-row.theirs { justify-content: flex-start; }

.bubble {
  max-width: 60%;
  padding: 10px 14px;
  border-radius: 12px;
}

.bubble-row.mine   .bubble { background: var(--color-turquoise); color: white; border-bottom-right-radius: 2px; }
.bubble-row.theirs .bubble { background: #f0f0f0; color: #202020; border-bottom-left-radius: 2px; }

.bubble-text {
  margin: 0 0 4px;
  font-size: 0.9rem;
  line-height: 1.4;
}

.bubble-time {
  font-size: 0.7rem;
  opacity: 0.7;
  display: block;
  text-align: right;
}

.input-bar {
  display: flex;
  gap: 10px;
  padding: 16px 28px;
  border-top: 1px solid #e0e0e0;
}

.message-input {
  flex: 1;
  padding: 10px 14px;
  border: 1.5px solid #e0e0e0;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  outline: none;
  transition: border-color 0.15s;
}

.message-input:focus { border-color: var(--color-turquoise); }

.send-btn {
  padding: 10px 20px;
  background: #FF5959;
  color: white;
  border: none;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.15s;
}

.send-btn:hover:not(:disabled) { opacity: 0.85; }
.send-btn:disabled { opacity: 0.4; cursor: default; }

.empty-conv {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #aaa;
  font-size: 0.95rem;
}
</style>
