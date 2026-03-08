import { defineStore } from 'pinia'

export interface User {
  id: number
  username: string
  email: string
  custom_avatar_url: string
  custom_banner_url: string
  first_name: string | null
  last_name: string | null
}

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as User | null,
    accessToken: null as string | null,
    authReady: false,
  }),

  getters: {
    isLoggedIn: (state) => !!state.user && !!state.accessToken,
  },

  actions: {
    setSession(user: User, token: string) {
      this.user = user
      this.accessToken = token
      sessionStorage.setItem('access_token', token)
    },

  setAuthReady() {
    this.authReady = true
  },
    clearSession() {
      this.user = null
      this.accessToken = null
      sessionStorage.removeItem('access_token')
    },

    loadFromStorage() {
      const token = sessionStorage.getItem('access_token')
      if (token) this.accessToken = token
        console.log('loadFromStorage')
    },
  },
})
