<template>
  <signupSections @signup="onSignup"/>
</template>

<script setup lang="ts">
import signupSections from '../components/sections/SignupSections/signupSections.vue'
import { signup } from '../api/auth'
import { useRouter } from 'vue-router'

type SignupPayload = {
  username: string
  email: string
  password: string
  confirmPassword: string
}

const router = useRouter()

async function onSignup(payload: SignupPayload) {
  if (payload.password !== payload.confirmPassword) {
    alert('Passwords do not match')
    return
  }

  try {
    await signup(payload.username, payload.email, payload.password)
    router.push('/login')
  } catch (error) {
    console.error('Signup failed:', error)
    alert('Signup failed. Please try again.')
  }
}
</script>
