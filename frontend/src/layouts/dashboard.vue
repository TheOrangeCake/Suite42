<template>
  <div class="dashboard">

    <aside class="sidebar">
      <RouterLink to="/" class="logo-link">
        <img src="/design/assets/logo/logo_icon.svg" alt="S42" class="logo" />
      </RouterLink>

      <nav class="nav">
        <RouterLink to="/profile" class="nav-item">
          <img src="/design/assets/images/default_avatar_cropped.jpg" alt="" class="nav-icon" />
          <span class="nav-label">Profile</span>
        </RouterLink>

        <RouterLink to="/finder" class="nav-item">
          <img src="/design/assets/icons/group.png" alt="" class="nav-icon" />
          <span class="nav-label">Finder</span>
        </RouterLink>

        <RouterLink to="/projects" class="nav-item">
          <img src="/design/assets/icons/project.png" alt="" class="nav-icon" />
          <span class="nav-label">Projects</span>
        </RouterLink>

        <RouterLink to="/tasks" class="nav-item">
          <img src="/design/assets/icons/to-do-list.png" alt="" class="nav-icon" />
          <span class="nav-label">Tasks</span>
        </RouterLink>

        <div class="divider" />

        <RouterLink to="/home" class="nav-item">
          <img src="/design/assets/icons/home.png" alt="" class="nav-icon" />
          <span class="nav-label">Home</span>
        </RouterLink>

        <RouterLink to="/chat" class="nav-item">
          <img src="/design/assets/icons/chat.png" alt="" class="nav-icon" />
          <span class="nav-label">Chat</span>
        </RouterLink>
      </nav>

      <button class="logout-btn" @click="handleLogout">
        <span class="nav-label">Logout</span>
      </button>
    </aside>

    <main class="content">
      <router-view />
    </main>

  </div>
</template>

<script setup lang="ts">
import { useAuthStore } from '../stores/auth'
import { useRouter } from 'vue-router'
import { signout } from '../api/auth'

const authStore = useAuthStore()
const router = useRouter()

async function handleLogout() {
  try {
    await signout()
  } catch {}
  authStore.clearSession()
  router.push('/login')
}
</script>

<style scoped>
.dashboard {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 88px;
  background-color: #1a1a1a;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 24px 0;
  gap: 8px;
  position: fixed;
  top: 0;
  left: 0;
  height: 100vh;
}

.logo {
  width: 44px;
  height: 44px;
  object-fit: contain;
  margin-bottom: 16px;
}

.nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  width: 100%;
}

.nav-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 4px;
  padding: 10px 0;
  width: 100%;
  text-decoration: none;
  color: #888;
  transition: color 0.2s;
}

.nav-item:hover,
.nav-item.router-link-active {
  color: white;
}

/* Quand la route est active, on met en avant l'item */
.nav-item.router-link-active .nav-icon {
  filter: brightness(0) invert(1);
}

.nav-icon {
  width: 24px;
  height: 24px;
  object-fit: contain;
  filter: brightness(0) invert(0.5);
  transition: filter 0.2s;
}

.nav-item:hover .nav-icon {
  filter: brightness(0) invert(1);
}

.nav-label {
  font-family: Monda, sans-serif;
  font-size: 0.65rem;
  letter-spacing: 0.02em;
}

.divider {
  width: 40px;
  height: 1px;
  background-color: #333;
  margin: 8px 0;
}

.logout-btn {
  background: none;
  border: none;
  color: #888;
  cursor: pointer;
  font-family: Monda, sans-serif;
  font-size: 0.65rem;
  padding: 10px 0;
  transition: color 0.2s;
}

.logout-btn:hover {
  color: white;
}

.content {
  margin-left: 88px;
  flex: 1;
  min-height: 100vh;
}
</style>
