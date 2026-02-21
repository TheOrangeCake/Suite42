<template>
  <section class="hero">

    <p class="text">
      Let's get productive !
    </p>

    <Corner
      class="corner"
      :vSize="80"
      :hSize="35"
      color="var(--color-red)"
      :thickness="5"
    />

    <Corner
      class="corner"
      :vSize="200"
      :hSize="35"
      color="var(--color-turquoise)"
      :thickness="5"
      :style="{ position: 'absolute', marginTop: '10px' }"
    />

    <p class="text2">42 student login</p>

    <Corner
      class="corner"
      :vSize="80"
      :hSize="35"
      color="var(--color-red)"
      :thickness="4"
      :style="{ marginLeft: '240px' }"
    />

    <v-btn
      class="login42"
      variant="outlined"
      rounded="lg"
      height="44"
    >
      <img
        class="login42__icon"
        src="/design/assets/icons/42_Logo.svg.png"
        alt="42"
      />
      <span class="login42__text">
        Login with 42 account
      </span>
    </v-btn>

    <p class="text2">Public login</p>


  </section>
</template>

<script setup>
import Corner from '@/components/ui/Corner.vue'
</script>

<style>

.border2 {
  padding-top: 15px;
}
.login42 {
  margin-left: 300px;
  position: relative;
  top: -28px;
  border: 1px solid #d9d9d9 !important;
  background: white !important;
  text-transform: none;
  letter-spacing: 0;
  padding: 0 18px;
  min-width: 420px;
  justify-content: center;
  gap: 14px;
}

.login42__icon {
  height: 28px;
  width: auto;
}

.login42__text {
  padding-left: 30px;
  font-weight: 600;
  color: #222;
}

.button {
  padding-left: 280px;
  color: #222222;
}

.corner2 {
  padding-bottom: 15px;
  margin-left: 150px;
}
.corner {
  margin-left: 150px;
}

.hero {
  position: relative;
  padding-top: 50px;
  background: var(--color-white);
}

.text2 {
  font-size: 42px;
  font-weight: 400;
  margin-left: 210px;
  color: #222222;
}

.text {
  font-family: 'Inter', sans-serif;
  font-size: clamp(3rem, 6vw, 5rem);
  font-weight: 700;
  line-height: 1.1;
  letter-spacing: 2px;
  color: #222222;
  margin-left: 130px;
}
</style>

