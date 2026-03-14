import { onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { refreshToken, getMe42 } from '../api/auth'
import { useAuthStore } from '../stores/auth'

/**
 * Handles authentication initialization on app startup.
 * Tries to restore a regular user session via token refresh,
 * then falls back to checking a 42 OAuth session.
 */
export function useAuthInit() {
  const authStore = useAuthStore()
  const router = useRouter()

  onMounted(async () => {
    try {
      authStore.loadFromStorage()
      const user = await refreshToken()
      const token = authStore.accessToken
      if (!token) throw new Error('No access token received after refresh')
      authStore.setSession(user, token)
    } catch {
      await tryRestore42Session(authStore, router)
    } finally {
      authStore.setAuthReady()
    }
  })
}

async function tryRestore42Session(
  authStore: ReturnType<typeof useAuthStore>,
  router: ReturnType<typeof useRouter>,
) {
  try {
    const me = await getMe42()
    if (me.authenticated) {
      authStore.setSession42({ login: me.sub })
      if (router.currentRoute.value.path === '/') {
        router.push('/profile')
      }
    } else {
      authStore.clearSession()
    }
  } catch (e) {
    console.error('[auth] Failed to restore session:', e)
    authStore.clearSession()
  }
}
