<template>
  <div class="friends-page">
    <h1 class="title">Friends</h1>

    <div class="search-section">
      <div class="search-bar">
        <input
          v-model="searchQuery"
          type="text"
          placeholder="Search by login..."
          class="search-input"
          @input="onSearch"
          @keyup.enter="onSearchNow"
        />
        <button class="btn search-btn" @click="onSearchNow">Search</button>
      </div>
      <div v-if="searchResults.length > 0" class="search-results">
        <div v-for="u in searchResults" :key="u.id" class="friend-card">
          <img :src="u.avatar_url || '/default-avatar.png'" alt="" class="avatar" />
          <span class="username">{{ u.login }}</span>
          <button class="btn accept" @click="sendRequest(u.id)">Add</button>
        </div>
      </div>
    </div>

    <div class="tabs">
      <button :class="{ active: tab === 'friends' }" @click="tab = 'friends'">
        Friends ({{ friends.length }})
      </button>
      <button :class="{ active: tab === 'pending' }" @click="tab = 'pending'">
        Pending ({{ pending.length }})
      </button>
      <button :class="{ active: tab === 'sent' }" @click="tab = 'sent'">
        Sent ({{ sent.length }})
      </button>
    </div>

    <div v-if="tab === 'friends'" class="list">
      <p v-if="friends.length === 0" class="empty">No friends yet.</p>
      <div v-for="f in friends" :key="f.id" class="friend-card">
        <div class="avatar-wrapper">
          <img :src="f.friend_avatar_url" alt="" class="avatar" />
          <span class="status-dot" :class="{ online: f.online }"></span>
        </div>
        <span class="username">{{ f.friend_login }}</span>
        <span class="online-label" v-if="f.online">Online</span>
        <button class="btn danger" @click="remove(f.id)">Remove</button>
      </div>
    </div>

    <div v-if="tab === 'pending'" class="list">
      <p v-if="pending.length === 0" class="empty">No pending requests.</p>
      <div v-for="f in pending" :key="f.id" class="friend-card">
        <img :src="f.friend_avatar_url" alt="" class="avatar" />
        <span class="username">{{ f.friend_login }}</span>
        <button class="btn accept" @click="accept(f.id)">Accept</button>
        <button class="btn danger" @click="decline(f.id)">Decline</button>
      </div>
    </div>

    <div v-if="tab === 'sent'" class="list">
      <p v-if="sent.length === 0" class="empty">No sent requests.</p>
      <div v-for="f in sent" :key="f.id" class="friend-card">
        <img :src="f.friend_avatar_url" alt="" class="avatar" />
        <span class="username">{{ f.friend_login }}</span>
        <span class="status">Pending...</span>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue'
import {
  getFriends,
  getPendingRequests,
  getSentRequests,
  acceptFriendRequest,
  declineFriendRequest,
  removeFriend,
  sendFriendRequest,
  searchUsers,
  sendHeartbeat,
  type FriendshipDto,
  type UserSearchResult,
} from '../api/friends'

definePage({
  meta: {
    requiresAuth: true,
    layout: 'dashboard',
  },
})

const tab = ref<'friends' | 'pending' | 'sent'>('friends')
const friends = ref<FriendshipDto[]>([])
const pending = ref<FriendshipDto[]>([])
const sent = ref<FriendshipDto[]>([])

const searchQuery = ref('')
const searchResults = ref<UserSearchResult[]>([])
let searchTimeout: ReturnType<typeof setTimeout> | null = null

function onSearch() {
  if (searchTimeout) clearTimeout(searchTimeout)
  const query = searchQuery.value.trim()
  if (query.length < 2) {
    searchResults.value = []
    return
  }
  searchTimeout = setTimeout(async () => {
    searchResults.value = await searchUsers(query)
  }, 300)
}

async function onSearchNow() {
  if (searchTimeout) clearTimeout(searchTimeout)
  const query = searchQuery.value.trim()
  if (query.length < 2) {
    searchResults.value = []
    return
  }
  searchResults.value = await searchUsers(query)
}

async function sendRequest(userId: number) {
  await sendFriendRequest(userId)
  searchQuery.value = ''
  searchResults.value = []
  await loadAll()
}

async function loadAll() {
  try {
    const [f, p, s] = await Promise.all([
      getFriends(),
      getPendingRequests(),
      getSentRequests(),
    ])
    friends.value = f
    pending.value = p
    sent.value = s
  } catch (e) {
    console.error('Failed to load friends data:', e)
  }
}

async function accept(id: number) {
  await acceptFriendRequest(id)
  await loadAll()
}

async function decline(id: number) {
  await declineFriendRequest(id)
  await loadAll()
}

async function remove(id: number) {
  await removeFriend(id)
  await loadAll()
}

let heartbeatInterval: ReturnType<typeof setInterval> | null = null

onMounted(() => {
  loadAll()
  sendHeartbeat().catch(() => {})
  heartbeatInterval = setInterval(() => {
    sendHeartbeat().catch(() => {})
    loadAll()
  }, 30000)
})

onUnmounted(() => {
  if (heartbeatInterval) clearInterval(heartbeatInterval)
})
</script>

<style scoped>
.friends-page {
  padding: 48px 56px;
  min-height: 100vh;
  background: white;
}

.title {
  font-family: Monda, sans-serif;
  font-size: clamp(1.8rem, 4vw, 3rem);
  font-weight: 700;
  color: #202020;
  margin-bottom: 32px;
}

.search-section {
  margin-bottom: 24px;
  position: relative;
}

.search-bar {
  display: flex;
  gap: 8px;
  max-width: 500px;
}

.search-input {
  flex: 1;
  padding: 10px 16px;
  border: 2px solid #ddd;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.95rem;
  outline: none;
}

.search-input:focus {
  border-color: #00babc;
}

.search-btn {
  background: #00babc;
  color: white;
  padding: 10px 20px;
  font-size: 0.95rem;
}

.search-results {
  margin-top: 8px;
  max-width: 500px;
  border: 1px solid #eee;
  border-radius: 8px;
  overflow: hidden;
}

.tabs {
  display: flex;
  gap: 8px;
  margin-bottom: 24px;
}

.tabs button {
  padding: 10px 20px;
  border: 2px solid #ddd;
  background: white;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.95rem;
  cursor: pointer;
}

.tabs button.active {
  border-color: #00babc;
  color: #00babc;
  font-weight: 600;
}

.list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.friend-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 12px 16px;
  border: 1px solid #eee;
  border-radius: 8px;
}

.avatar-wrapper {
  position: relative;
  width: 40px;
  height: 40px;
  flex-shrink: 0;
}

.avatar {
  width: 40px;
  height: 40px;
  border-radius: 50%;
  object-fit: cover;
}

.status-dot {
  position: absolute;
  bottom: 0;
  right: 0;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #ccc;
  border: 2px solid white;
}

.status-dot.online {
  background: #22c55e;
}

.online-label {
  font-family: Monda, sans-serif;
  font-size: 0.8rem;
  color: #22c55e;
  font-weight: 600;
}

.username {
  font-family: Monda, sans-serif;
  font-weight: 600;
  flex: 1;
}

.status {
  font-family: Monda, sans-serif;
  color: #999;
  font-size: 0.9rem;
}

.btn {
  padding: 6px 16px;
  border: none;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.85rem;
  cursor: pointer;
  font-weight: 600;
}

.btn.accept {
  background: #00babc;
  color: white;
}

.btn.danger {
  background: #ff5959;
  color: white;
}

.btn:hover {
  opacity: 0.85;
}

.empty {
  font-family: Monda, sans-serif;
  color: #999;
}
</style>
