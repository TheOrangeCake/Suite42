<template>
  <v-app class="app-root">
    <Navbar v-if="!isDashboardRoute" />
    <router-view/>
    <AppFooter v-if="!isDashboardRoute" />
  </v-app>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { onMounted } from 'vue'
import { refreshToken, getMe42 } from './api/auth'
import { useAuthStore } from './stores/auth'

const authStore = useAuthStore()
const route = useRoute()
const router = useRouter()

const dashboardRoutes = ['/profile', '/finder', '/home', '/chat', '/tasks', '/projects', '/friends']

const isDashboardRoute = computed(() =>
  dashboardRoutes.includes(route.path)
)

onMounted(async () => {
  try {
    authStore.loadFromStorage()
    const user = await refreshToken()
    authStore.setSession(user, authStore.accessToken!)
  } catch {
    try {
      const me = await getMe42()
      if (me.authenticated) {
        authStore.setSession42({ login: me.sub })
        if (window.location.pathname === '/') {
          router.push('/profile')
        }
      } else {
        authStore.clearSession()
      }
    } catch {
      authStore.clearSession()
    }
  } finally {
    authStore.setAuthReady()
  }
})
</script>
