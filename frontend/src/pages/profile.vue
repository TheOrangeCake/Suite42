<template>
  <div class="profile-page">
    <h1 class="greeting">Hello, {{ authStore.displayName }}</h1>

    <div class="body">
      <Corner :vSize="200" :hSize="16" :thickness="5" color="var(--color-turquoise)" />

      <div class="content">
        <div class="block">
          <Corner :vSize="60" :hSize="16" :thickness="5" color="var(--color-turquoise)" />
          <p class="text">You still have projects waiting for you!</p>
        </div>

        <div class="block">
          <Corner :vSize="40" :hSize="16" :thickness="5" color="var(--color-turquoise)" />
          <p class="text">Want to find group members or a learning buddy?</p>
        </div>

        <button class="cta">Look for someone special</button>

        <div class="block" style="margin-top: 48px">
          <Corner :vSize="60" :hSize="16" :thickness="5" color="var(--color-green)" />
          <p class="text">Or maybe want to work on your projects instead?</p>
        </div>

        <div class="block">
          <Corner :vSize="40" :hSize="16" :thickness="5" color="var(--color-green)" />
          <p class="subtext">Let's start studying!</p>
        </div>
      </div>
    </div>

    <img
      src="/design/assets/images/intra_home_illustration.png"
      alt=""
      class="illustration"
    />
  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '../stores/auth'

const authStore = useAuthStore()

definePage({
  meta: {
    requiresAuth: true,
    layout: 'dashboard',
  },
})
</script>

<style scoped>
.profile-page {
  padding: 48px 56px;
  min-height: 100vh;
  background: white;
  position: relative;
  overflow: hidden;
}

.greeting {
  font-family: Monda, sans-serif;
  font-size: clamp(1.8rem, 4vw, 3rem);
  font-weight: 700;
  color: #202020;
  margin-bottom: 40px;
}

.body {
  display: flex;
  flex-direction: row;
  gap: 16px;
  align-items: flex-start;
}

.content {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.block {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 12px;
}

.text {
  font-family: Monda, sans-serif;
  font-size: clamp(1rem, 2vw, 1.3rem);
  color: #202020;
  margin: 0;
}

.subtext {
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  color: #555;
  margin: 0;
}

.cta {
  margin-left: 28px;
  padding: 16px 32px;
  background-color: #FF5959;
  color: white;
  border: none;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 1rem;
  font-weight: 600;
  cursor: pointer;
  align-self: flex-start;
}

.cta:hover { opacity: 0.85; }

.illustration {
  position: absolute;
  bottom: 0;
  right: 0;
  width: clamp(280px, 40vw, 560px);
  pointer-events: none;
}
</style>
