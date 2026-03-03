<template>
  <v-app class="app-root">
    <router-view/>
  </v-app>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { refreshToken } from './api/auth'
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()
onMounted(async () => {
  try {
    authStore.loadFromStorage()
    const user = await refreshToken()
    authStore.setSession(user, authStore.accessToken!)
  } catch {
    authStore.clearSession()
  } finally {
    authStore.setAuthReady()
  }
})
</script>
