<template>
  <v-container class="login-page" fluid>
    <v-row align="center" justify="center" class="fill-height">

      <v-col cols="12" md="6">
        <h1 class="login-title">Login</h1>
      </v-col>

      <v-col cols="12" md="6">
        <v-card class="login-card" elevation="0">

          <v-text-field label="Username" variant="outlined" />
          <v-text-field label="Password" type="password" variant="outlined" />

          <v-btn color="primary" block class="mb-4">
            Login
          </v-btn>

          <v-divider class="my-6" />

          <v-btn variant="outlined" block class="mb-2">
            Login with 42 Account
          </v-btn>

          <v-btn variant="outlined" block>
            Login with Github Account
          </v-btn>

        </v-card>
      </v-col>

    </v-row>
  </v-container>
</template>

<style scoped>
.login-page {
  min-height: calc(100vh - 64px);
}

.login-title {
  font-size: 4rem;
  font-weight: 700;
}

.login-card {
  border: 1px solid #f0b8a0;
  padding: 32px;
}
</style>

