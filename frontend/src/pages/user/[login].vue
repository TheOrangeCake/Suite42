<template>
  <div class="profile-page">

    <!-- Loading / Error -->
    <div v-if="isLoading" class="loading">Loading...</div>
    <p v-else-if="error" class="errorMsg">{{ error }}</p>

    <template v-else-if="profile">
      <h1 class="greeting">{{ profile.login }} profile</h1>

      <div class="pageBody bodyContent">

          <!-- Hero: avatar + banner + name/email -->
          <div class="hero">
            <img :src="avatarUrl" alt="avatar" class="avatar" />
            <div class="heroRight">
              <img :src="bannerUrl" alt="banner" class="banner" />
              <h2 class="fullname">{{ fullName }}</h2>
              <p class="email">{{ profile.email }}</p>
            </div>
          </div>

          <!-- Info sections -->
          <div class="infoGrid">

            <!-- Personal -->
            <div class="infoSection">
              <Corner :vSize="80" :hSize="16" :thickness="2" color="var(--color-turquoise)" />
              <div class="sectionContent">
                <p class="sectionTitle">Personal</p>
                <div class="infoRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Intra login: {{ profile.login ?? '–' }}</span>
                </div>
                <div class="infoRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Pool: {{ profile.pool_month ?? '?' }} {{ profile.pool_year ?? '?' }}</span>
                </div>
                <div class="infoRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <a :href="profile.intra_url" target="_blank" class="intraLink">42 profile: {{ profile.login }}</a>
                </div>
              </div>
            </div>

            <!-- Common Core -->
            <div class="infoSection">
              <Corner :vSize="100" :hSize="16" :thickness="2" color="var(--color-green)" />
              <div class="sectionContent">
                <p class="sectionTitle">Common Core</p>
                <div class="infoRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Rank: {{ profile.rank }}</span>
                </div>
                <div class="infoRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Current rank progress: {{ profile.rank_progress_percent }}%</span>
                </div>
                <div class="infoRow">
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>Eligible projects:</span>
                </div>
                <div
                  v-for="p in profile.eligible_projects"
                  :key="p"
                  class="infoRow infoRowNested"
                >
                  <Corner :vSize="20" :hSize="16" :thickness="2" color="var(--color-green)" />
                  <span>{{ p }}</span>
                </div>
              </div>
            </div>

          </div>

          <!-- Finished projects -->
          <div v-if="profile.common_core" class="finishedProjects">
            <div class="fpHeader">
              <Corner :vSize="40" :hSize="16" :thickness="2" color="var(--color-turquoise)" />
              <span class="fpTitle">Finished projects</span>
            </div>
            <div class="ranksGrid">
              <div class="rankCol">
                <div
                  v-for="rankGroup in leftRanks"
                  :key="rankGroup.rank"
                  class="rankBlock"
                >
                  <p class="rankLabel">Rank: {{ rankGroup.rank }}</p>
                  <div
                    v-for="proj in rankGroup.projects"
                    :key="proj.slug"
                    class="projRow"
                  >
                    <Corner :vSize="16" :hSize="14" :thickness="2" color="var(--color-coral, #FF5959)" />
                    <span class="projText">{{ proj.name }}: {{ proj.projects_users?.final_mark ?? 0 }} points - {{ proj.projects_users?.occurrence ?? 0 }} retries</span>
                  </div>
                </div>
              </div>
              <div class="rankCol">
                <div
                  v-for="rankGroup in rightRanks"
                  :key="rankGroup.rank"
                  class="rankBlock"
                >
                  <p class="rankLabel">Rank: {{ rankGroup.rank }}</p>
                  <div
                    v-for="proj in rankGroup.projects"
                    :key="proj.slug"
                    class="projRow"
                  >
                    <Corner :vSize="16" :hSize="14" :thickness="2" color="var(--color-coral, #FF5959)" />
                    <span class="projText">{{ proj.name }}: {{ proj.projects_users?.final_mark ?? 0 }} points - {{ proj.projects_users?.occurrence ?? 0 }} retries</span>
                  </div>
                </div>
              </div>
            </div>
          </div>

          <!-- Add friend CTA -->
          <div class="ctaRow">
            <Corner :vSize="40" :hSize="16" :thickness="2" color="var(--color-coral, #FF5959)" />
            <button class="addFriendBtn">Add friend to start chatting!</button>
          </div>

      </div>

    </template>

    <!-- Illustration -->
    <img
      src="/design/assets/images/other_user_page_illustration.png"
      alt=""
      class="illustration"
    />

  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getUserByLogin } from '../../api/api42'
import type { UserDetailed } from '../../api/api42'

definePage({
  meta: {
    requiresAuth: true,
    layout: 'dashboard',
  },
})

const route = useRoute()
const login = computed(() => route.params.login as string)

const profile = ref<UserDetailed | null>(null)
const isLoading = ref(false)
const error = ref('')

const fullName = computed(() => {
  if (!profile.value) return ''
  return `${profile.value.first_name ?? ''} ${profile.value.last_name ?? ''}`.trim() || profile.value.login
})

function validUrl(url: string | null | undefined, fallback: string): string {
  if (!url || url.endsWith('/null')) return fallback
  return url
}

const avatarUrl = computed(() => {
  if (!profile.value) return '/design/assets/images/default_avatar_cropped.jpg'
  return validUrl(profile.value.custom_avatar_url, '')
    || validUrl(profile.value.image?.versions?.medium, '')
    || '/design/assets/images/default_avatar_cropped.jpg'
})

const bannerUrl = computed(() => {
  if (!profile.value) return '/design/assets/images/default_profile_banner.jpg'
  return validUrl(profile.value.custom_banner_url, '/design/assets/images/default_profile_banner.jpg')
})

const leftRanks = computed(() =>
  profile.value?.common_core?.rank?.filter(r => r.rank <= 3) ?? []
)

const rightRanks = computed(() =>
  profile.value?.common_core?.rank?.filter(r => r.rank >= 4) ?? []
)

onMounted(async () => {
  isLoading.value = true
  try {
    profile.value = await getUserByLogin(login.value)
  } catch (e) {
    error.value = e instanceof Error ? e.message : 'User not found'
  } finally {
    isLoading.value = false
  }
})
</script>

<style scoped>
.profile-page {
  padding: 48px 56px;
  min-height: 100vh;
  background: white;
  position: relative;
  overflow-x: hidden;
}

.greeting {
  font-family: Monda, sans-serif;
  font-size: clamp(2rem, 4vw, 3rem);
  font-weight: 700;
  color: #202020;
  margin-bottom: 32px;
}

/* ── Page body ── */
.pageBody {
  position: relative;
  padding-left: 24px;
  margin-bottom: 40px;
}

.pageBody::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 16px;
  border-left: 3px solid var(--color-turquoise);
  border-bottom: 3px solid var(--color-turquoise);
  pointer-events: none;
}

.bodyContent {
  display: flex;
  flex-direction: column;
  gap: 32px;
}

/* ── Hero ── */
.hero {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 0;
}

.avatar {
  width: 190px;
  height: 190px;
  object-fit: cover;
  flex-shrink: 0;
  border-radius: 4px;
}

.heroRight {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 0;
}

.banner {
  width: 100%;
  height: 160px;
  object-fit: cover;
  border-radius: 4px;
  display: block;
}

.fullname {
  font-family: Monda, sans-serif;
  font-size: clamp(1.4rem, 3vw, 2rem);
  font-weight: 700;
  color: #202020;
  margin: 12px 0 4px 0;
}

.email {
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #888;
  margin: 0;
}

/* ── Info sections ── */
.infoGrid {
  display: flex;
  flex-direction: row;
  gap: 48px;
  flex-wrap: wrap;
  align-items: flex-start;
}

.infoSection {
  display: flex;
  flex-direction: row;
  gap: 8px;
  align-items: flex-start;
}

.sectionContent {
  display: flex;
  flex-direction: column;
  gap: 6px;
  padding-top: 4px;
}

.sectionTitle {
  font-family: Monda, sans-serif;
  font-weight: 700;
  font-size: 1rem;
  color: #202020;
  margin: 0 0 6px 0;
}

.infoRow {
  display: flex;
  align-items: center;
  gap: 8px;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  color: #555;
}

.infoRowNested {
  padding-left: 20px;
}

.intraLink {
  color: #555;
  text-decoration: none;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
}

.intraLink:hover { text-decoration: underline; }

/* ── Finished projects ── */
.finishedProjects {
  border: 1.5px solid #d0d0d0;
  border-radius: 10px;
  padding: 24px 28px;
}

.fpHeader {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 10px;
  margin-bottom: 20px;
}

.fpTitle {
  font-family: Monda, sans-serif;
  font-weight: 700;
  font-size: 1.1rem;
  color: #202020;
}

.ranksGrid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 0 48px;
}

.rankCol {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.rankBlock {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.rankLabel {
  font-family: Monda, sans-serif;
  font-weight: 700;
  font-size: 0.85rem;
  color: #202020;
  margin: 0 0 4px 0;
}

.projRow {
  display: flex;
  align-items: center;
  gap: 6px;
}

.projText {
  font-family: Monda, sans-serif;
  font-size: 0.78rem;
  color: #202020;
}

/* ── Add friend CTA ── */
.ctaRow {
  display: flex;
  align-items: center;
  gap: 12px;
}

.addFriendBtn {
  padding: 12px 24px;
  background: #FF5959;
  color: white;
  border: none;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  cursor: pointer;
  transition: opacity 0.2s;
}

.addFriendBtn:hover { opacity: 0.85; }

/* ── Misc ── */
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

.illustration {
  position: absolute;
  bottom: 0;
  right: 0;
  width: clamp(200px, 35vw, 480px);
  pointer-events: none;
}
</style>
