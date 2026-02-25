<script setup lang="ts">
import Corner from '../../ui/Corner.vue'
</script>

<template>
  <section class="container">
    <div class="title">
      <div class="titleCorner" aria-hidden="true">
        <Corner :vSize="60" :hSize="30" :thickness="5" color="var(--color-turquoise)" />

        <Corner :vSize="160" :hSize="3" :thickness="5" color="var(--color-green)" />
      </div>

      <h1 class="headline">
        A practical companion<br />
        for the 42 journey !
      </h1>
    </div>
    <div class="sndcontainer">

        <Corner :vSize="640" :hSize="20" :thickness="5" color="var(--color-green)" />
     <div class="box">
      <div class="boxCorner" aria-hidden="true">
        <Corner :vSize="50" :hSize="40" color="var(--color-turquoise)" :thickness="5"/>
        <h3> An unofficial collection of tools for learning <br/> and collaboration dedicated to 42 students</h3>
      </div>
      <div class= "boxCorner2">
         <Corner class="corner" :vSize="90" :hSize="40" :thickness="5" color="var(--color-turquoise)" />
        <button > Start your 42 journey here </button>
      </div>
    </div>

    </div>
      <img
    src="/design/assets/images/homepage_illustration.png"
    alt=""
    class="illustration"
    aria-hidden="true"
  />
  </section>
</template>

<style scoped>


.illustration {
  position: absolute;
  bottom: 0;
  right: 0;

  width: clamp(230px, 43vw, 720px);
  height: auto;

  pointer-events: none;
  z-index: 0;
}

button {

  margin-left: 19px;
  background-color: #FF5959;
  font-size: clamp(0.85rem, 2.2vw, 1.4rem);
  line-height: 1.3; /* virgule -> point */
  padding: 12px 36px;
  color: white;
  border-radius: 4px;
  cursor: pointer;
}

.boxCorner2 {
  position: absolute;
  top: clamp(68px, 2vw, 100px);
  left: clamp(10px, 2vw, 18px);

  min-height: 180px;
  width: calc(100% - 2 * clamp(10px, 2vw, 18px));

  display: flex;
  align-items: center;
  gap: clamp(8px, 1.5vw, 14px);

}

.corner {
  align-self: flex-start;
}

.boxCorner2 { pointer-events: auto; }

.corner { pointer-events: none; }

.cta {
}

h3 {
  color: black;
  align-self: end;
    font-size: clamp(0.85rem, 2.2vw, 1.4rem);

}
.sndcontainer {
  display: flex;
  gap: clamp(12px, 2vw, 24px);
  position: relative;
  align-items: flex-start;

}
.container {
  padding: clamp(16px, 5vw, 48px);
  width: 100%;
  min-height: 100vh;
  box-sizing: border-box;
   position: relative;
  overflow: hidden;
}

.container > * {
}

.title {
  display: flex;
  flex-direction: row;
  flex-wrap: nowrap;
  align-items: flex-start;
  gap: clamp(10px, 2vw, 20px);
  width: 100%;
  max-width: var(--Text-text-hero-width-max, 1900px);
  min-width: 0;

}

.titleCorner {
  display: flex;
  flex-direction: column;
  flex: 0 0 auto;
  align-items: flex-start;
  gap: 10px;
}

.headline {
  margin: 0;
  flex: 1 1 auto;
  min-width: 0;
  color: var(--Black, #202020);
  font-family: Monda, system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
  font-style: normal;
  font-weight: 700;
  font-size: clamp(1.6rem, 7vw, var(--Text-hero, 96px));
  line-height: 1.05;
  letter-spacing: -0.04em;
}

.decor {
  margin-top: clamp(8px, 2vw, 20px);
}

.box {
  width: 85%;
  max-width: 85%;
  margin-left: auto;
  margin-right: auto;
  min-height: clamp(140px, 35vh, 420px);
  position: relative;
}

.boxCorner {
    position: absolute;
  top: clamp(10px, 2vw, 18px);
  left: clamp(10px, 2vw, 18px);

  display: flex;
  gap: clamp(8px, 1.5vw, 14px);

  width: calc(100% - 2 * clamp(10px, 2vw, 18px));
  max-width: 100%;

  pointer-events: none;
}
</style>
