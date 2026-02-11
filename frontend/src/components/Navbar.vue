<script setup lang="ts">
import logo from '/design/assets/logo/logo_icon.png'
</script>


<template>
  <v-app-bar
    app
    flat
    color="#f3f3f3"
    height="96"
    class="px-10"
  >
    <div class="brand">
      <img  src="/design/assets/logo/logo_full.png"
      class="logo"
      />
    </div>

    <v-spacer />

    <v-btn variant="text" class="nav-link">
      Campuses
    </v-btn>

    <v-btn
      color="#ff5a5a"
      class="signin-btn"
      rounded="lg"
    >
      Sign in
    </v-btn>

  </v-app-bar>
</template>

<style scoped>
.brand {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo {
  margin-left: 40px;
  opacity: 0.8;
  height: 40px;
}

.nav-link {
  font-size: 18px;
  margin-right: 24px;
  text-transform: none;
}

.signin-btn {
  font-size: 18px;
  padding: 12px 22px;
  text-transform: none;
  color: white;
}
</style>

