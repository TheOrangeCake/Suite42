<template>
    <LoginSections @login="onLogin"/>
</template>

  <script setup lang="ts">

    import { login } from '../api/auth'
    import Corner from '../components/ui/Corner.vue'
    import HeroSections from '../components/sections/LoginSections/HeroSections.vue'
    import LoginSections from '../components/sections/LoginSections/LoginSections.vue'
    import { useRouter } from 'vue-router'

    type LoginPayload = {
      username: string
      password: string
    }

    const router = useRouter()

    async function onLogin(payload: LoginPayload) {
      try {
      console.log("ayo");
        await login(payload.username, payload.password);
        router.push('/home')
      } catch(error) {
        console.error('Login failed');
        alert('Login failed')
      }
    }

  </script>

