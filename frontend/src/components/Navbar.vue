<script setup lang="ts">
import logo from '/design/assets/logo/logo_full.svg'
</script>

<template>
  <div class="nav">
    <RouterLink to="/" class="logo-link">
      <img :src="logo" class="logo" alt="logo"/>
    </RouterLink>

    <div class="menu">
      <RouterLink to="/campuses" class="campuses">Campuses</RouterLink>
      <RouterLink to="/login" class="signin">Sign in</RouterLink>
    </div>
  </div>
</template>

<style scoped>
.logo {
  color: black;
  height: clamp(36px, 5vw, 64px);
}
.nav {
  display: flex;
  align-items: center;

  padding-left: 5%;
  padding-top: 3%;
  padding-right: 5%;
}


.campuses {
  font-family: monda;
  color: #111;
  font-size: 15px;

}

.menu {
  margin-left: auto;
  display: flex;
  align-items: center;
  gap: 38px;
}
.signin {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 10px 10px;
  background-color: #FF5959;
  border-radius: 3px;
  font-family: monda;
  color: white;
  text-decoration: none;
  font-weight: 500;
}
</style>

