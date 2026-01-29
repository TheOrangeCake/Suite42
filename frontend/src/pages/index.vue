<template>
  <v-container fluid class="index-page">
    <v-row class="fill-height" align="center">
      <!-- LEFT -->
      <v-col cols="12" md="6">
        <h1 class="hero-title">Let’s get productive !</h1>

        <!-- 42 LOGIN -->
        <div class="login-block">
          <h3>42 Student login</h3>

          <v-btn
            variant="outlined"
            class="login-42"
            prepend-icon="mdi-school"
            @click="login42"
          >
            Login with 42 account
          </v-btn>
        </div>

        <!-- PUBLIC LOGIN -->
        <div class="login-block">
          <h3>Public login</h3>

          <v-text-field
            label="Username"
            variant="outlined"
            v-model="username"
          />

          <v-text-field
            label="Password"
            type="password"
            variant="outlined"
            v-model="password"
          />

          <v-btn
            color="primary"
            :loading="auth.loading"
            @click="login"
          >
            Log in
          </v-btn>

          <p class="signup">
            No account ?
            <RouterLink to="/signup">Sign up here</RouterLink>
          </p>
        </div>
      </v-col>

      <!-- RIGHT -->
      <v-col cols="12" md="6" class="illustration">
        <v-img
          src="../../design/assets/images/sign_in_illustration.png"
          max-width="420"
          class="mx-auto"
        />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const auth = useAuthStore()
const router = useRouter()

const username = ref('')
const password = ref('')

async function login() {
  await auth.login(username.value, password.value)
  router.push('/profile')
}

function login42() {
  window.location.href = import.meta.env.VITE_API_URL + '/oauth/42'
}
</script>

<style scoped>
.index-page {
  min-height: calc(100vh - 64px - 40px); /* navbar + footer */
  padding: 4rem;
}

.hero-title {
  font-size: 3rem;
  font-weight: 700;
  margin-bottom: 2rem;
}

.login-block {
  margin-bottom: 3rem;
  max-width: 420px;
}

.login-42 {
  margin-top: 1rem;
}

.signup {
  margin-top: 1rem;
  font-size: 0.9rem;
}

.illustration {
  text-align: center;
}
</style>

