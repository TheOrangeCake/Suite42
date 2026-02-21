<template>
  <section class="block">
    <div class="corner">
      <Corner :vSize="150" :hSize="38" :thickness="5" color="var(--color-turquoise)"/>
      <Corner :vSize="680" :hSize="38" :thickness="5" color="var(--color-green)"/>
    </div>
    <div class="block2">
      <p class="title">A practical compagnon for the 42 journey!</p>
      <div class="block3">
        <Corner class="corner-block" :vSize="50" :hSize="35" :thickness="5" color="var(--color-turquoise)"/>
        <p class="subtitle">An unofficial collection of tools for learning and collaboration dedicated to 42 students</p>
      </div>
      <div class="block-button">
        <Corner class="corner-block" :vSize="130" :hSize="25" :thickness="5" color="var(--color-turquoise)"/>
        <div class="button-and-image">
          <button class="button" type="button" @click="$router.push('/login')">
            Start your journey here
          </button>
          <img class="hero-img" src="../../../../design/assets/images/homepage_illustration.png" alt="illustration">
        </div>
      </div>
    </div>
  </section>
</template>

<script>
import Corner from '../../../components/ui/Corner.vue'
</script>

<style scoped>
.block {
  width: 100%;
  display: grid;
  grid-template-columns: 30px 1fr;
  column-gap: 20px;
  margin-left: 5%;
  align-items: start;
  box-sizing: border-box;
}

.corner {
  display: flex;
  justify-content: flex-start;
  flex-direction: column;
  align-items: center;
}

.block2 {
  display: flex;
  flex-direction: column;
  width: 100%;
  font-family: monda;
  font-weight: bold;
  margin-top: clamp(40px, 8vw, 120px);
  font-size: clamp(32px, 5vw, 74px);
  color: #444444;
}

.title {
  margin: 0 0 16px 0;
  line-height: 1.2;
}

.block3 {
  display: flex;
  width: 100%;
  gap: 16px;
  font-family: monda;
  font-size: clamp(16px, 2.5vw, 34px);
  font-weight: normal;
  color: #444444;
  align-items: flex-start;
}

.subtitle {
  margin: 0;
  line-height: 1.4;
}

.corner-block {
  margin-left: 0;
  flex-shrink: 0;
}

.block-button {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-top: clamp(30px, 5vw, 90px);
}

.button-and-image {
  display: flex;
  flex-wrap: wrap;
  align-items: flex-start;
  gap: 24px;
  width: 100%;
}

.button {
  background-color: #FF5959;
  font-size: clamp(18px, 2.5vw, 40px);
  line-height: 1.3;
  padding: clamp(8px, 1.5vw, 12px) clamp(16px, 3vw, 36px);
  color: white;
  border-radius: 4px;
  border: none;
  cursor: pointer;
  flex-shrink: 0;
  white-space: nowrap;
}

.hero-img {
  width: clamp(180px, 30vw, 420px);
  height: auto;
  pointer-events: none;
  z-index: 0;
  flex-shrink: 1;
}

/* Tablet */
@media (max-width: 768px) {
  .block {
    grid-template-columns: 20px 1fr;
    column-gap: 12px;
    margin-left: 3%;
  }

  .button-and-image {
    flex-direction: column;
    align-items: flex-start;
  }

  .hero-img {
    width: clamp(160px, 60vw, 320px);
  }
}

/* Mobile */
@media (max-width: 480px) {
  .block {
    grid-template-columns: 14px 1fr;
    column-gap: 8px;
    margin-left: 2%;
  }

  .block3 {
    flex-direction: column;
    gap: 8px;
  }

  .block-button {
    flex-direction: column;
  }

  .button {
    white-space: normal;
    text-align: center;
  }

  .hero-img {
    width: 80vw;
    max-width: 300px;
  }
}
</style>
