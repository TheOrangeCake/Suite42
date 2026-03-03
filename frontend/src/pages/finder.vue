<template>
  <div class="finder-page">

    <div class="header">
      <h1 class="title">Teammates finder</h1>
      <p class="subtitle">{{ totalElements }} students at 42 Lausanne</p>
    </div>

    <div class="filters">
      <div class="filterGroup">
        <label class="filterLabel">RANK</label>
        <select v-model="filters.rank" class="filterSelect" @change="fetchUsers">
          <option :value="undefined">All</option>
          <option v-for="r in [0,1,2,3,4,5,6,7]" :key="r" :value="r">Rank {{ r }}</option>
        </select>
      </div>

      <div class="filterGroup">
        <label class="filterLabel">POOL YEAR</label>
        <select v-model="filters.poolYear" class="filterSelect" @change="fetchUsers">
          <option value="">All</option>
          <option v-for="y in poolYears" :key="y" :value="y">{{ y }}</option>
        </select>
      </div>

      <div class="filterGroup">
        <label class="filterLabel">SORT</label>
        <select v-model="filters.sort" class="filterSelect" @change="fetchUsers">
          <option value="">Default</option>
          <option value="rank,desc">Rank ↓</option>
          <option value="rank,asc">Rank ↑</option>
          <option value="rankProgressPercent,desc">Progress ↓</option>
        </select>
      </div>

      <div class="filterGroup">
        <label class="filterLabel">LFG ONLY</label>
        <input type="checkbox" v-model="lfgOnly" @change="onLfgChange" class="filterCheckbox" />
      </div>
    </div>

    <div v-if="isLoading" class="loading">Loading...</div>

    <p v-else-if="error" class="error">{{ error }}</p>

    <div v-else class="grid">
      <div
        v-for="user in users"
        :key="user.id"
        class="card"
      >
        <img
          :src="user.custom_avatar_url || user.image.versions.medium || '/design/assets/placeholder/avatar.jpg'"
          :alt="user.login"
          class="avatar"
        />
        <div class="cardBody">
          <p class="login">{{ user.login }}</p>
          <p class="name">{{ user.first_name }} {{ user.last_name }}</p>
          <div class="tags">
            <span class="tag rank">Rank {{ user.rank }}</span>
            <span v-if="user.lfg" class="tag lfg">LFG</span>
            <span v-if="user.pool_year" class="tag pool">{{ user.pool_month }} {{ user.pool_year }}</span>
          </div>
          <div class="progress">
            <div class="progressBar" :style="{ width: user.rank_progress_percent + '%' }" />
          </div>
        </div>
      </div>
    </div>

    <div v-if="totalPages > 1" class="pagination">
      <button
        class="pageBtn"
        :disabled="filters.page === 0"
        @click="changePage(filters.page - 1)"
      >←</button>

      <span class="pageInfo">{{ filters.page + 1 }} / {{ totalPages }}</span>

      <button
        class="pageBtn"
        :disabled="filters.page >= totalPages - 1"
        @click="changePage(filters.page + 1)"
      >→</button>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUsers } from '../api/api42'

definePage({
  meta: { requiresAuth: true, layout: 'dashboard' },
})

const users = ref([])
const isLoading = ref(false)
const error = ref('')
const totalElements = ref(0)
const totalPages = ref(0)
const lfgOnly = ref(false)

const poolYears = ['2025', '2024', '2023', '2022', '2021', '2020']

const filters = reactive({
  campusName: 'Lausanne',
  page: 0,
  size: 24,
  rank: undefined as number | undefined,
  poolYear: '',
  poolMonth: '',
  sort: '',
})

async function fetchUsers() {
  isLoading.value = true
  error.value = ''
  filters.page = 0  // reset à la page 1 quand on change un filtre

  try {
    const res = await getUsers({
      ...filters,
      lfg: lfgOnly.value ? true : undefined,
    })
    users.value = res.content
    totalElements.value = res.page.totalElements
    totalPages.value = res.page.totalPages
  } catch (e) {
    if (e instanceof Error) error.value = e.message
  } finally {
    isLoading.value = false
  }
}

async function changePage(newPage: number) {
  filters.page = newPage
  isLoading.value = true
  try {
    const res = await getUsers({
      ...filters,
      lfg: lfgOnly.value ? true : undefined,
    })
    users.value = res.content
    totalPages.value = res.page.totalPages
  } catch (e) {
    if (e instanceof Error) error.value = e.message
  } finally {
    isLoading.value = false
  }
}

function onLfgChange() {
  fetchUsers()
}

onMounted(fetchUsers)
</script>

<style scoped>
.finder-page {
  padding: 40px 48px;
  background: white;
  min-height: 100vh;
}

.header {
  margin-bottom: 32px;
}

.title {
  font-family: Monda, sans-serif;
  font-size: clamp(1.8rem, 4vw, 3rem);
  font-weight: 700;
  color: #202020;
  margin: 0 0 4px 0;
}

.subtitle {
  font-family: Monda, sans-serif;
  font-size: 0.85rem;
  color: #888;
  margin: 0;
}

/* ── Filtres ── */
.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  margin-bottom: 32px;
  align-items: flex-end;
}

.filterGroup {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.filterLabel {
  font-family: Monda, sans-serif;
  font-size: 0.65rem;
  color: #888;
  letter-spacing: 0.08em;
}

.filterSelect {
  padding: 7px 12px;
  border: 1.5px solid #ddd;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #202020;
  background: white;
  cursor: pointer;
  outline: none;
}

.filterSelect:focus {
  border-color: var(--color-turquoise);
}

.filterCheckbox {
  width: 18px;
  height: 18px;
  accent-color: var(--color-turquoise);
  cursor: pointer;
  margin-top: 4px;
}

/* ── Grid ── */
.grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.card {
  border: 1.5px solid #eee;
  border-radius: 10px;
  overflow: hidden;
  transition: border-color 0.2s, transform 0.15s;
  cursor: pointer;
}

.card:hover {
  border-color: var(--color-turquoise);
  transform: translateY(-2px);
}

.avatar {
  width: 100%;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  display: block;
  background: #f0f0f0;
}

.cardBody {
  padding: 12px;
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.login {
  font-family: Monda, sans-serif;
  font-weight: 700;
  font-size: 0.9rem;
  color: #202020;
  margin: 0;
}

.name {
  font-family: Monda, sans-serif;
  font-size: 0.75rem;
  color: #888;
  margin: 0;
}

.tags {
  display: flex;
  flex-wrap: wrap;
  gap: 4px;
  margin-top: 4px;
}

.tag {
  font-family: Monda, sans-serif;
  font-size: 0.62rem;
  padding: 2px 7px;
  border-radius: 99px;
  font-weight: 600;
}

.rank { background: #f0f0f0; color: #555; }
.lfg  { background: #FF5959; color: white; }
.pool { background: #e8f9f9; color: #37E3F0; }

/* ── Progress bar ── */
.progress {
  height: 3px;
  background: #eee;
  border-radius: 99px;
  margin-top: 8px;
  overflow: hidden;
}

.progressBar {
  height: 100%;
  background: var(--color-turquoise);
  border-radius: 99px;
  transition: width 0.3s;
}

/* ── Pagination ── */
.pagination {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 20px;
  margin-top: 40px;
}

.pageBtn {
  padding: 8px 20px;
  background: #202020;
  color: white;
  border: none;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  cursor: pointer;
  transition: opacity 0.2s;
}

.pageBtn:disabled {
  opacity: 0.3;
  cursor: not-allowed;
}

.pageInfo {
  font-family: Monda, sans-serif;
  font-size: 0.85rem;
  color: #555;
}

.loading {
  font-family: Monda, sans-serif;
  color: #888;
  text-align: center;
  padding: 60px;
}

.error {
  color: #FF5959;
  font-family: Monda, sans-serif;
  text-align: center;
}
</style>
