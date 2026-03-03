<template>
  <div class="finder-page">

    <!-- Header -->
    <div class="header">
      <h1 class="title">Buddy finder</h1>

      <div class="headerBody">
        <Corner :vSize="180" :hSize="16" :thickness="3" color="var(--color-turquoise)" />
        <div class="headerText">
          <p class="desc">Find a learning buddy or group members.</p>
          <p class="hint">Only active and non alumni students.</p>
          <p class="hint">Rank 7 means student has finished Common Core.</p>
        </div>
      </div>

      <div class="refineRow">
        <Corner :vSize="60" :hSize="16" :thickness="3" color="var(--color-green)" />
        <button class="refineBtn" :class="{ active: panelOpen }" @click="panelOpen = !panelOpen">
          <span class="refineIcon">⚙</span>
          Refine results
        </button>
        <span class="resultCount">{{ totalElements }} results</span>
      </div>
    </div>

    <!-- Filter panel overlay -->
    <div v-if="panelOpen" class="filterPanel">
      <div class="panelSection">
        <div class="panelSectionHeader">
          <Corner :vSize="120" :hSize="16" :thickness="3" color="var(--color-turquoise)" />
          <span class="panelSectionTitle">Sort</span>
        </div>
        <div class="panelFields">
          <div class="panelField">
            <Corner :vSize="30" :hSize="16" :thickness="3" color="var(--color-turquoise)" />
            <select v-model="filters.sort" class="panelSelect">
              <option value="">Default</option>
              <option value="rank,desc">Rank ↓</option>
              <option value="rank,asc">Rank ↑</option>
              <option value="rankProgressPercent,desc">Progress ↓</option>
              <option value="rankProgressPercent,asc">Progress ↑</option>
            </select>
          </div>
        </div>
      </div>

      <div class="panelSection">
        <div class="panelSectionHeader">
          <Corner :vSize="220" :hSize="16" :thickness="3" color="var(--color-green)" />
          <span class="panelSectionTitle">Filter</span>
        </div>
        <div class="panelFields">
          <div class="panelField">
            <Corner :vSize="30" :hSize="16" :thickness="3" color="var(--color-green)" />
            <select v-model="filters.poolMonth" class="panelSelect">
              <option value="">Pool month</option>
              <option v-for="m in poolMonths" :key="m" :value="m">{{ m }}</option>
            </select>
          </div>
          <div class="panelField">
            <Corner :vSize="30" :hSize="16" :thickness="3" color="var(--color-green)" />
            <select v-model="filters.poolYear" class="panelSelect">
              <option value="">Pool year</option>
              <option v-for="y in poolYears" :key="y" :value="y">{{ y }}</option>
            </select>
          </div>
          <div class="panelField">
            <Corner :vSize="30" :hSize="16" :thickness="3" color="var(--color-coral, #FF5959)" />
            <select v-model="filters.rank" class="panelSelect">
              <option :value="undefined">Rank</option>
              <option v-for="r in [0,1,2,3,4,5,6,7]" :key="r" :value="r">Rank {{ r }}</option>
            </select>
          </div>
          <div class="panelField">
            <Corner :vSize="30" :hSize="16" :thickness="3" color="var(--color-coral, #FF5959)" />
            <select v-model="filters.eligibleProject" class="panelSelect">
              <option value="">Eligible project</option>
              <option v-for="p in commonProjects" :key="p" :value="p">{{ p }}</option>
            </select>
          </div>
          <div class="panelField lfgField">
            <Corner :vSize="30" :hSize="16" :thickness="3" color="var(--color-coral, #FF5959)" />
            <label class="lfgLabel">
              <input type="checkbox" v-model="lfgOnly" class="lfgCheck" />
              LFG only
            </label>
          </div>
        </div>
      </div>

      <button class="applyBtn" @click="applyFilters">Apply</button>
    </div>

    <!-- Loading -->
    <div v-if="isLoading" class="loading">Loading...</div>

    <!-- Error -->
    <p v-else-if="error" class="errorMsg">{{ error }}</p>

    <!-- Cards -->
    <div v-else class="cardList">
      <div v-for="user in users" :key="user.id" class="card">

        <img
          :src="user.custom_avatar_url || user.image?.versions?.medium || '/design/assets/placeholder/avatar.jpg'"
          :alt="user.login"
          class="avatar"
        />

        <div class="cardMain">
          <div class="cardTop">
            <h2 class="login">{{ user.login }}</h2>
            <span v-if="user.lfg" class="lfgTag">LFG: {{ user.lfg }}</span>
          </div>

          <div class="cardDetails">
            <div class="detailBlock">
              <Corner :vSize="70" :hSize="16" :thickness="3" color="var(--color-turquoise)" />
              <div class="detailContent">
                <p class="detailTitle">Info:</p>
                <div class="detailRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-turquoise)" />
                  <span>Name: {{ user.first_name }} {{ user.last_name }}</span>
                </div>
                <div class="detailRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-turquoise)" />
                  <span>Pool: {{ user.pool_month }} {{ user.pool_year }}</span>
                </div>
              </div>
            </div>

            <div class="detailBlock">
              <Corner :vSize="70" :hSize="16" :thickness="3" color="var(--color-green)" />
              <div class="detailContent">
                <p class="detailTitle">Common core:</p>
                <div class="detailRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Rank: {{ user.rank }}</span>
                </div>
                <div class="detailRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Current rank progress: {{ user.rank_progress_percent }}%</span>
                </div>
              </div>
            </div>
          </div>

          <button class="profileBtn" @click="goToProfile(user.login)">
            Check {{ user.login }} profile
          </button>
        </div>

      </div>
    </div>

    <!-- Pagination -->
    <div v-if="totalPages > 1" class="pagination">
      <button class="pageBtn" :disabled="filters.page === 0" @click="changePage(filters.page - 1)">←</button>
      <span class="pageInfo">{{ filters.page + 1 }} / {{ totalPages }}</span>
      <button class="pageBtn" :disabled="filters.page >= totalPages - 1" @click="changePage(filters.page + 1)">→</button>
    </div>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { getUsers } from '../api/api42'
import { useRouter } from 'vue-router'

definePage({
  meta: { requiresAuth: true, layout: 'dashboard' },
})

const router = useRouter()
const users = ref<any[]>([])
const isLoading = ref(false)
const error = ref('')
const totalElements = ref(0)
const totalPages = ref(0)
const panelOpen = ref(false)
const lfgOnly = ref(false)

const poolYears = ['2025', '2024', '2023', '2022', '2021', '2020']
const poolMonths = ['january','february','march','april','may','june','july','august','september','october','november','december']
const commonProjects = ['ft_transcendence','webserv','cub3d','minirt','philosophers','cpp-module-09','ft_irc']

const filters = reactive({
  campusName: 'Lausanne',
  page: 0,
  size: 10,
  rank: undefined as number | undefined,
  poolYear: '',
  poolMonth: '',
  eligibleProject: '',
  sort: '',
})

async function fetchUsers() {
  isLoading.value = true
  error.value = ''
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

function applyFilters() {
  filters.page = 0
  panelOpen.value = false
  fetchUsers()
}

async function changePage(newPage: number) {
  filters.page = newPage
  await fetchUsers()
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

function goToProfile(login: string) {
  router.push(`/user/${login}`)
}

onMounted(fetchUsers)
</script>

<style scoped>
.finder-page {
  padding: 40px 48px;
  background: white;
  min-height: 100vh;
  position: relative;
}

/* ── Header ── */
.title {
  font-family: Monda, sans-serif;
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  color: #202020;
  margin: 0 0 24px 0;
}

.headerBody {
  display: flex;
  flex-direction: row;
  gap: 16px;
  align-items: flex-start;
  margin-bottom: 24px;
}

.headerText {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.desc {
  font-family: Monda, sans-serif;
  font-size: clamp(1rem, 2vw, 1.3rem);
  color: #202020;
  margin: 0;
}

.hint {
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #888;
  margin: 0;
}

.refineRow {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 16px;
  margin-bottom: 32px;
}

.refineBtn {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 20px;
  background: white;
  border: 2px solid #FF5959;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  color: #202020;
  cursor: pointer;
  transition: background 0.2s;
}

.refineBtn.active,
.refineBtn:hover {
  background: #FF5959;
  color: white;
}

.resultCount {
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #888;
}

/* ── Filter panel ── */
.filterPanel {
  position: absolute;
  top: 220px;
  left: 48px;
  z-index: 100;
  background: white;
  border: 1.5px solid #eee;
  border-radius: 10px;
  padding: 24px;
  width: 320px;
  box-shadow: 0 8px 32px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.panelSection {
  display: flex;
  flex-direction: row;
  gap: 12px;
  align-items: flex-start;
}

.panelSectionHeader {
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
}

.panelSectionTitle {
  font-family: Monda, sans-serif;
  font-weight: 700;
  font-size: 0.95rem;
  color: #202020;
  padding-top: 2px;
}

.panelFields {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
  padding-top: 24px;
}

.panelField {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 8px;
}

.panelSelect {
  flex: 1;
  padding: 8px 12px;
  border: 1.5px solid #ddd;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #202020;
  background: white;
  cursor: pointer;
  outline: none;
  appearance: none;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='12' height='12' viewBox='0 0 12 12'%3E%3Cpath fill='%23FF5959' d='M6 8L1 3h10z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 10px center;
  padding-right: 28px;
}

.lfgField { align-items: center; }

.lfgLabel {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #202020;
  cursor: pointer;
}

.lfgCheck {
  accent-color: #FF5959;
  width: 15px;
  height: 15px;
}

.applyBtn {
  padding: 10px;
  background: #FF5959;
  color: white;
  border: none;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-weight: 700;
  cursor: pointer;
  transition: opacity 0.2s;
}

.applyBtn:hover { opacity: 0.85; }

/* ── Cards ── */
.cardList {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.card {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 24px;
  border: 1.5px solid #eee;
  border-radius: 10px;
  padding: 20px;
  transition: border-color 0.2s;
}

.card:hover { border-color: var(--color-turquoise); }

.avatar {
  width: 120px;
  height: 120px;
  object-fit: cover;
  border-radius: 8px;
  flex-shrink: 0;
  background: #f0f0f0;
}

.cardMain {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cardTop {
  display: flex;
  align-items: center;
  gap: 16px;
}

.login {
  font-family: Monda, sans-serif;
  font-size: clamp(1.1rem, 2vw, 1.5rem);
  font-weight: 700;
  color: #202020;
  margin: 0;
}

.lfgTag {
  font-family: Monda, sans-serif;
  font-size: 0.8rem;
  color: #888;
}

.cardDetails {
  display: flex;
  flex-direction: row;
  gap: 32px;
}

.detailBlock {
  display: flex;
  flex-direction: row;
  gap: 10px;
  align-items: flex-start;
}

.detailContent {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detailTitle {
  font-family: Monda, sans-serif;
  font-weight: 700;
  font-size: 0.85rem;
  color: #202020;
  margin: 0;
}

.detailRow {
  display: flex;
  align-items: center;
  gap: 8px;
}

.detailRow span {
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #555;
}

.profileBtn {
  align-self: flex-start;
  padding: 8px 20px;
  background: white;
  border: 1.5px solid #202020;
  border-radius: 6px;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #202020;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
  margin-top: 4px;
}

.profileBtn:hover {
  background: #202020;
  color: white;
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
}

.pageBtn:disabled { opacity: 0.3; cursor: not-allowed; }

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

.errorMsg {
  color: #FF5959;
  font-family: Monda, sans-serif;
  text-align: center;
}
</style>
