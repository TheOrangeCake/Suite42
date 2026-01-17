<template>
  <v-container fluid class="hero">
    <v-row align="center" class="fill-height">
      <!-- TEXTE -->
      <v-col cols="12" md="6" class="hero-content">
        <h1 class="hero-title">
          PICK THE<br />
          RIGHT<br />
          TEAMMATES
        </h1>

        <p class="hero-subtitle">
          Finding teammates for 42 projects is never easy.<br />
          We are here to help.
        </p>

        <v-btn variant="outlined" color="primary" class="hero-btn" to="/login">
          Login to start looking
        </v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>
.hero {
  min-height: calc(100vh - 64px);
  position: relative;

  /* BACKGROUND IMAGE */
  background-image: url("/hand.jpg");
  background-repeat: no-repeat;

  background-size: cover;
  background-position: right bottom;
}

.hero::before{
  content:"";
  position:absolute;
  inset:0;
  background: linear-gradient(90deg, rgba(0,0,0,.85) 0%, rgba(0,0,0,.35) 60%, rgba(0,0,0,0) 100%);
  pointer-events:none;
}

.hero-content{
  position: relative;
  z-index: 1;
}

.hero-title {
  font-size: 4rem;
  font-weight: 800;
  line-height: 1.1;
  margin-bottom: 24px;
  color: white;
}

.hero-subtitle {
  font-size: 1.1rem;
  margin-bottom: 32px;
  color: rgba(255,255,255,0.7);
}

.hero-btn {
  text-transform: none;
  padding: 12px 24px;
}
</style>

