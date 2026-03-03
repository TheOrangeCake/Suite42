<template>
  <signupSections
    @signup="onSignup"
    :isLoading="isLoading"
    :errorMessage="errorMessage"
  />
</template>

<script setup lang="ts">
import { ref } from 'vue'
import signupSections from '../components/sections/SignupSections/signupSections.vue'
import { signup } from '../api/auth'
import { useRouter } from 'vue-router'

type SignupPayload = {
  username: string
  email: string
  password: string
  confirmPassword: string
}

const isLoading = ref(false)
const errorMessage = ref('')
const router = useRouter()

function isValidEmail(email: string): boolean {
  return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)
}

async function onSignup(payload: SignupPayload) {
  errorMessage.value = ''


  if (!payload.username || payload.username.length < 3) {
    errorMessage.value = 'Username must be at least 3 characters'
    return
  }

  if (!isValidEmail(payload.email)) {
    errorMessage.value = 'Invalid email format'
    return
  }

  if (payload.password.length < 8) {
    errorMessage.value = 'Password must be at least 8 characters'
    return
  }

  if (payload.password !== payload.confirmPassword) {
    errorMessage.value = 'Passwords do not match'
    return
  }

  isLoading.value = true

  try {
    await signup(payload.username, payload.email, payload.password)
    router.push('/login')
  } catch (error) {
    if (error instanceof Error) {
      errorMessage.value = error.message
    }
  } finally {
    isLoading.value = false
  }
}
</script>
