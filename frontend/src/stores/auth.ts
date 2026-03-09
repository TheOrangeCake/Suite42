import { defineStore } from 'pinia'

export interface User {
  id: number
  username: string
  email: string
  custom_avatar_url: string
  custom_banner_url: string
  first_name: string | null
  last_name: string | null
  double_authentication: boolean
}

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
  },
})
