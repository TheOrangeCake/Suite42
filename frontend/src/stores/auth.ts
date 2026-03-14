import { defineStore } from 'pinia'
import type { User } from '../api/auth'

export type { User }

export interface User42 {
  login: string
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as User | null,
    user42: null as User42 | null,
    accessToken: null as string | null,
    authReady: false,
  }),

  getters: {
    isLoggedIn: (state) => !!state.user || !!state.user42,
    isRegularUser: (state) => !!state.user,
    is42User: (state) => !!state.user42,
    displayName: (state) => state.user?.username ?? state.user42?.login ?? null,
  },

  actions: {
    setSession(user: User, token: string) {
      this.user = user
      this.user42 = null
      this.accessToken = token
      sessionStorage.setItem('access_token', token)
    },

    setSession42(user42: User42) {
      this.user42 = user42
      this.user = null
      this.accessToken = null
    },

    setAuthReady() {
      this.authReady = true
    },

    clearSession() {
      this.user = null
      this.user42 = null
      this.accessToken = null
      sessionStorage.removeItem('access_token')
    },

    loadFromStorage() {
      const token = sessionStorage.getItem('access_token')
      if (token) this.accessToken = token
    },

    async logout() {
      try {
        // Dynamic import breaks the circular dependency:
        // stores/auth.ts ← api/auth.ts (useAuthStore) would create a cycle at module init.
        // With dynamic import, api/auth is only resolved at call time (after all modules load).
        const { signout, logout42 } = await import('../api/auth')
        if (this.user42) {
          await logout42()
        } else {
          await signout()
        }
      } catch (e) {
        console.error('[auth] logout API call failed:', e)
      } finally {
        this.clearSession()
      }
    },
  },
})
