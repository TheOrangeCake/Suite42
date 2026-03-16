<template>
  <div class="modify-page">
    <h1 class="greeting">Hm, I can be a cat</h1>

    <div v-if="isLoading" class="loading">Loading...</div>
    <p v-else-if="loadError" class="errorMsg">{{ loadError }}</p>

    <!-- 42 user: avatar + banner -->
    <div v-else-if="profile42" class="pageBody bodyContent">

        <!-- Hero -->
        <div class="hero">
          <img :src="avatarPreview || avatarUrl" alt="avatar" class="avatar" />
          <div class="heroRight">
            <img :src="bannerPreview || bannerUrl" alt="banner" class="banner" />
            <h2 class="fullname">{{ fullName42 }}</h2>
            <p class="email">{{ profile42.email }}</p>
          </div>
        </div>

        <!-- Change avatar / Change banner -->
        <div class="formRow">
          <div class="formCol">
            <label class="fieldLabel">Change avatar</label>
            <div class="fileInputRow">
              <span class="filePlaceholder">{{ avatarFile?.name ?? 'png, svg, jpg' }}</span>
              <label class="uploadBtn">
                Upload
                <input
                  type="file"
                  accept=".png,.jpg,.jpeg,.gif,.webp,.svg"
                  @change="onAvatarChange"
                  class="hiddenInput"
                />
              </label>
            </div>
          </div>

          <div class="formCol">
            <label class="fieldLabel">Change banner</label>
            <div class="fileInputRow">
              <span class="filePlaceholder">{{ bannerFile?.name ?? 'png, svg, jpg' }}</span>
              <label class="uploadBtn">
                Upload
                <input
                  type="file"
                  accept=".png,.jpg,.jpeg,.gif,.webp,.svg"
                  @change="onBannerChange"
                  class="hiddenInput"
                />
              </label>
            </div>
          </div>
        </div>

        <p v-if="saveError" class="errorMsg">{{ saveError }}</p>
        <p v-if="saveSuccess" class="successMsg">Profile updated!</p>

        <div class="actionRow">
          <Corner :vSize="60" :hSize="16" :thickness="3" color="var(--color-coral, #FF5959)" />
          <button class="saveBtn" @click="saveChanges42" :disabled="isSaving">
            {{ isSaving ? 'Saving...' : 'Save changes' }}
          </button>
          <RouterLink to="/profile" class="cancelLink">Cancel</RouterLink>
        </div>

    </div>

    <!-- Regular user: 2FA toggle -->
    <div v-else-if="profileRegular" class="pageBody bodyContent">

        <div class="hero">
          <div class="heroRight">
            <h2 class="fullname">{{ profileRegular.username }}</h2>
            <p class="email">{{ profileRegular.email }}</p>
          </div>
        </div>

        <div class="sectionTitle">Security</div>

        <div class="twoFaBlock">
          <label class="twoFaLabel">
            <input type="checkbox" v-model="twoFaEnabled" class="twoFaCheckbox" />
            Enable two-factor authentication (2FA via email)
          </label>
          <p class="twoFaHint">When enabled, a one-time code will be sent to your email at each login.</p>
        </div>

        <div class="formCol">
          <label class="fieldLabel">Confirm password</label>
          <input
            v-model="confirmPassword"
            type="password"
            class="fieldInput"
            placeholder="Your current password"
          />
        </div>

        <p v-if="saveError" class="errorMsg">{{ saveError }}</p>
        <p v-if="saveSuccess" class="successMsg">Settings updated!</p>

        <div class="actionRow">
          <Corner :vSize="60" :hSize="16" :thickness="3" color="var(--color-coral, #FF5959)" />
          <button class="saveBtn" @click="saveChangesRegular" :disabled="isSaving">
            {{ isSaving ? 'Saving...' : 'Save changes' }}
          </button>
          <RouterLink to="/profile" class="cancelLink">Cancel</RouterLink>
        </div>

    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import { getUserProfile } from '../api/api42'
import type { UserDetailed } from '../api/api42'
import { getMyProfile, updateMyProfile } from '../api/auth'
import type { User } from '../api/auth'
import { useAuthStore } from '../stores/auth'

definePage({
  meta: {
    requiresAuth: true,
    layout: 'dashboard',
  },
})

const authStore = useAuthStore()
const profile42 = ref<UserDetailed | null>(null)
const profileRegular = ref<User | null>(null)
const isLoading = ref(false)
const loadError = ref('')
const isSaving = ref(false)
const saveError = ref('')
const saveSuccess = ref(false)

// 42 user fields
const avatarFile = ref<File | null>(null)
const bannerFile = ref<File | null>(null)
const avatarPreview = ref<string | null>(null)
const bannerPreview = ref<string | null>(null)

// Regular user fields
const twoFaEnabled = ref(false)
const confirmPassword = ref('')

const API_BASE_URL = import.meta.env.VITE_API_URL

const fullName42 = computed(() => {
  if (!profile42.value) return ''
  return `${profile42.value.first_name ?? ''} ${profile42.value.last_name ?? ''}`.trim() || profile42.value.login
})

const avatarUrl = computed(() => {
  if (!profile42.value) return '/design/assets/images/default_avatar_cropped.jpg'
  return profile42.value.custom_avatar_url
    || profile42.value.image?.versions?.medium
    || '/design/assets/images/default_avatar_cropped.jpg'
})

const bannerUrl = computed(() => {
  if (!profile42.value) return '/design/assets/images/default_profile_banner.jpg'
  return profile42.value.custom_banner_url || '/design/assets/images/default_profile_banner.jpg'
})

function onAvatarChange(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  if (avatarPreview.value) URL.revokeObjectURL(avatarPreview.value)
  avatarFile.value = file
  avatarPreview.value = URL.createObjectURL(file)
}

function onBannerChange(e: Event) {
  const file = (e.target as HTMLInputElement).files?.[0]
  if (!file) return
  if (bannerPreview.value) URL.revokeObjectURL(bannerPreview.value)
  bannerFile.value = file
  bannerPreview.value = URL.createObjectURL(file)
}

async function uploadFile(file: File, endpoint: string, fieldName: string) {
  const formData = new FormData()
  formData.append(fieldName, file)
  const headers: Record<string, string> = {}
  if (authStore.accessToken) {
    headers['Authorization'] = `Bearer ${authStore.accessToken}`
  }
  const res = await fetch(`${API_BASE_URL}${endpoint}`, {
    method: 'PATCH',
    credentials: 'include',
    headers,
    body: formData,
  })
  if (!res.ok) {
    const data = await res.json().catch(() => ({}))
    throw new Error(data.message || data.error || 'Upload failed')
  }
}

async function saveChanges42() {
  saveError.value = ''
  saveSuccess.value = false
  isSaving.value = true
  try {
    if (avatarFile.value) {
      await uploadFile(avatarFile.value, '/api/api42/v1/42users/avatar', 'avatar')
    }
    if (bannerFile.value) {
      await uploadFile(bannerFile.value, '/api/api42/v1/42users/banner', 'banner')
    }
    saveSuccess.value = true
    avatarFile.value = null
    bannerFile.value = null
    if (avatarPreview.value) { URL.revokeObjectURL(avatarPreview.value); avatarPreview.value = null }
    if (bannerPreview.value) { URL.revokeObjectURL(bannerPreview.value); bannerPreview.value = null }
    profile42.value = await getUserProfile()
  } catch (e) {
    saveError.value = e instanceof Error ? e.message : 'Failed to save changes'
  } finally {
    isSaving.value = false
  }
}

async function saveChangesRegular() {
  if (!profileRegular.value) return
  if (!confirmPassword.value) {
    saveError.value = 'Please confirm your password.'
    return
  }
  saveError.value = ''
  saveSuccess.value = false
  isSaving.value = true
  try {
    const updated = await updateMyProfile({
      email: profileRegular.value.email,
      first_name: profileRegular.value.first_name,
      last_name: profileRegular.value.last_name,
      double_authentication: twoFaEnabled.value,
      confirm_password: confirmPassword.value,
    })
    profileRegular.value = updated
    authStore.user = updated
    confirmPassword.value = ''
    saveSuccess.value = true
  } catch (e) {
    saveError.value = e instanceof Error ? e.message : 'Failed to save changes'
  } finally {
    isSaving.value = false
  }
}

onMounted(async () => {
  isLoading.value = true
  try {
    if (authStore.is42User) {
      profile42.value = await getUserProfile()
    } else {
      const user = await getMyProfile()
      profileRegular.value = user
      twoFaEnabled.value = user.double_authentication
    }
  } catch (e) {
    loadError.value = e instanceof Error ? e.message : 'Failed to load profile'
  } finally {
    isLoading.value = false
  }
})
</script>

<style scoped>
.modify-page {
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

.pageBody {
  position: relative;
  padding-left: 24px;
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
  gap: 28px;
}

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

.sectionTitle {
  font-family: Monda, sans-serif;
  font-size: 1rem;
  font-weight: 700;
  color: #202020;
  letter-spacing: 0.05em;
  text-transform: uppercase;
}

.twoFaBlock {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.twoFaLabel {
  display: flex;
  align-items: center;
  gap: 10px;
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  color: #202020;
  cursor: pointer;
}

.twoFaCheckbox {
  width: 16px;
  height: 16px;
  cursor: pointer;
  accent-color: var(--color-turquoise, #37E3F0);
}

.twoFaHint {
  font-family: Monda, sans-serif;
  font-size: 0.78rem;
  color: #888;
  margin: 0;
  padding-left: 26px;
}

.formRow {
  display: flex;
  flex-direction: row;
  gap: 32px;
  flex-wrap: wrap;
}

.formCol {
  display: flex;
  flex-direction: column;
  gap: 8px;
  min-width: 200px;
}

.fieldLabel {
  font-family: Monda, sans-serif;
  font-size: 0.88rem;
  font-weight: 600;
  color: #202020;
}

.fieldInput {
  background: white;
  border: 1.5px solid #ddd;
  border-radius: 6px;
  padding: 9px 12px;
  font-size: 0.88rem;
  font-family: Monda, sans-serif;
  color: #202020;
  outline: none;
  width: 100%;
  max-width: 280px;
  transition: border-color 0.2s;
}

.fieldInput:focus {
  border-color: var(--color-turquoise, #37E3F0);
}

.fileInputRow {
  display: flex;
  align-items: center;
  border: 1.5px solid #ccc;
  border-radius: 4px;
  overflow: hidden;
}

.filePlaceholder {
  flex: 1;
  padding: 8px 12px;
  font-family: Monda, sans-serif;
  font-size: 0.78rem;
  color: #aaa;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.uploadBtn {
  padding: 8px 14px;
  background: #f0f0f0;
  border-left: 1.5px solid #ccc;
  font-family: Monda, sans-serif;
  font-size: 0.78rem;
  color: #202020;
  cursor: pointer;
  flex-shrink: 0;
  font-weight: 600;
}

.uploadBtn:hover { background: #e0e0e0; }

.hiddenInput {
  display: none;
}

.actionRow {
  display: flex;
  align-items: center;
  gap: 16px;
}

.saveBtn {
  padding: 10px 24px;
  background: white;
  border: 2px solid #FF5959;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  font-weight: 600;
  color: #202020;
  cursor: pointer;
  transition: background 0.2s, color 0.2s;
}

.saveBtn:hover:not(:disabled) {
  background: #FF5959;
  color: white;
}

.saveBtn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.cancelLink {
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  color: #555;
  text-decoration: none;
}

.cancelLink:hover { text-decoration: underline; }

.loading {
  font-family: Monda, sans-serif;
  color: #888;
  text-align: center;
  padding: 60px;
}

.errorMsg {
  color: #FF5959;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  margin: 0;
}

.successMsg {
  color: #4caf50;
  font-family: Monda, sans-serif;
  font-size: 0.82rem;
  margin: 0;
}
</style>
