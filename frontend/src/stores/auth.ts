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
  }),

  getters: {
    isLoggedIn: (state) => !!state.user && !!state.accessToken,
  },

  // actions = les fonctions qui modifient le state
  actions: {
    // Appelée après un login ou signup réussi
    // On stocke aussi le token dans sessionStorage pour survivre
    // au refresh de page (sessionStorage est vidé quand on ferme l'onglet)
    setSession(user: User, token: string) {
      this.user = user
      this.accessToken = token
      sessionStorage.setItem('access_token', token)
    },

    // Appelée au logout — remet tout à zéro
    clearSession() {
      this.user = null
      this.accessToken = null
      sessionStorage.removeItem('access_token')
    },

    // Appelée au démarrage de l'app pour récupérer le token
    // si l'utilisateur a déjà été connecté dans cet onglet
    loadFromStorage() {
      const token = sessionStorage.getItem('access_token')
      if (token) this.accessToken = token
        console.log('loadFromStorage')
    },
  },
})
