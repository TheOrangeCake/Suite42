<script>
export default {
  data() {
    return {
      msg: 'Hello World!'
    }
  }
}
</script>

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
}
</style>

