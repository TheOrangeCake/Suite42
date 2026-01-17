import { defineStore } from 'pinia'
import * as authApi from '@/api/auth'
import { UnauthenticatedError } from '@/api/errors'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    user: null as null | authApi.User,
    loading: false,
  }),

  getters: {
    isAuthenticated: (state) => state.user !== null,
  },

  actions: {
    async fetchUser() {
      try {
        this.user = await authApi.me()
      } catch (err) {
        if (err instanceof UnauthenticatedError) {
          this.user = null
        } else {
          throw err
        }
      }
    },

    async login(username: string, password: string) {
      this.loading = true
      await authApi.login(username, password)
      await this.fetchUser()
      this.loading = false
    },

    async logout() {
      await authApi.logout()
      this.user = null
    },
  },
})

