<template>
  <v-app class="app-root">
    <Navbar v-if="!isDashboardRoute" />
    <router-view/>
    <AppFooter v-if="!isDashboardRoute" />
  </v-app>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { onMounted } from 'vue'
import { refreshToken } from './api/auth'
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()
const route = useRoute()

const dashboardRoutes = ['/profile', '/finder', '/home', '/chat', '/tasks', '/projects']

const isDashboardRoute = computed(() =>
  dashboardRoutes.includes(route.path)
)

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
