<template>
  <v-app class="app-root">
    <router-view/>
  </v-app>
</template>

<style>
.app-root {
  background: white !important;
  color: black;
  min-height: 100vh;
  overflow-x: hidden; /* empêche le scroll horizontal */
}

.v-application__wrap {
  min-height: unset !important;
  padding: 0 !important;
}
</style>

