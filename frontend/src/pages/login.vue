<template>
  <LoginSections
    @login="onLogin"
    :isLoading="isLoading"
    :errorMessage="errorMessage"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { signin } from '../api/auth'
import LoginSections from '../components/sections/LoginSections/LoginSections.vue'
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'

type LoginPayload = { username: string; password: string }

const router = useRouter()
const authStore = useAuthStore()

const isLoading = ref(false)
const errorMessage = ref('')

async function onLogin(payload: LoginPayload) {
  isLoading.value = true
  errorMessage.value = ''
  try {
    const user = await signin(payload.username, payload.password)
    console.log(user)
    authStore.setSession(user, authStore.accessToken!)
    router.push('/profile')
  } catch (error) {
    if (error instanceof Error) {
      errorMessage.value = error.message
      console.log(errorMessage.value)
    }
  } finally {
    isLoading.value = false
  }
}
</script>
