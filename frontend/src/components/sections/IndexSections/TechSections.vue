<script setup lang="ts">
import Corner from '../../ui/Corner.vue'

const categories = [
  {
    name: 'Front-end',
    color: 'var(--color-turquoise)',
    items: ['Vue.js', 'Vuetify'],
  },
  {
    name: 'Design',
    color: 'var(--color-green)',
    items: ['Figma'],
  },
  {
    name: 'Back-end',
    color: 'var(--color-coral, #FF5959)',
    items: ['Java Spring Boot', 'Java Spring Security', 'Java sprind DATA JPA', 'Apache Maven', 'Nginx', 'PostgreSQL'],
  },
  {
    name: 'DevOps',
    color: 'var(--color-green)',
    items: [
      'Docker / Docker Compose',
      'GitHub',
      'Java Spring Cloud Gateway'
    ],
  },
]

const logos = [
  { name: 'Vue.js',  src: '/design/assets/tech_logos/vuejs.png' },
  { name: 'NGINX',   src: '/design/assets/tech_logos/nginx.svg' },
  { name: 'GitHub',  src: '/design/assets/tech_logos/github.svg' },
  { name: 'Maven',   src: '/design/assets/tech_logos/maven.png' },
  { name: 'Spring',  src: '/design/assets/tech_logos/spring.svg' },
]
</script>

<template>
  <section class="container">

    <div class="titleRow">
      <Corner :vSize="90" :hSize="20" :thickness="5" color="var(--color-green)" />
      <h1 class="headline">Tech stack</h1>
    </div>

    <div class="body">

      <div class="leftSection">

        <Corner :vSize="500" :hSize="20" :thickness="4" color="var(--color-coral, #FF5959)" class="redLine" />

        <div class="leftCol">
          <div
            v-for="cat in categories"
            :key="cat.name"
            class="category"
          >
            <Corner :vSize="60" :hSize="20" :thickness="4" :color="cat.color" />

            <div class="catContent">
              <span class="catName">{{ cat.name }}</span>

              <div class="catItems" v-if="cat.items && cat.items.length">
                <div
                  v-for="item in cat.items"
                  :key="item"
                  class="catItem"
                >
                  <Corner :vSize="14" :hSize="12" :thickness="3" :color="cat.color" />
                  <span>{{ item }}</span>
                </div>
              </div>
            </div>

          </div>
        </div>

      </div>

      <div class="rightCol">
        <div class="logoGrid">
          <div
            v-for="logo in logos"
            :key="logo.name"
            class="logoItem"
          >
            <img :src="logo.src" :alt="logo.name" class="logoImg" />
            <span class="logoName">{{ logo.name }}</span>
          </div>
        </div>
      </div>

    </div>
  </section>
</template>

<style scoped>
* { box-sizing: border-box; }

.container {
  width: 100%;
  padding-left: 5%;
  padding-right: 5%;
  background-color: white;
}

.titleRow {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 16px;
  margin-bottom: clamp(14px, 3vw, 28px);
}

.headline {
  color: #202020;
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: clamp(1.8rem, 5vw, 64px);
  line-height: 1.05;
  letter-spacing: -0.03em;
}

.body {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: clamp(32px, 6vw, 80px);
}

.leftSection {
  flex: 0 0 40%;
  max-width: 40%;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 10px;
}

.redLine {
  flex-shrink: 0;
}

.leftCol {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: clamp(20px, 3vw, 36px);
}

.category {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 10px;
}

.catContent {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.catName {
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: clamp(0.85rem, 1.6vw, 1.05rem);
  color: #202020;
  line-height: 1;
}

.catItems {
  display: flex;
  flex-direction: column;
  gap: 4px;
  padding-left: clamp(8px, 1.5vw, 16px);
}

.catItem {
  display: flex;
  align-items: center;
  gap: 8px;
}

.catItem span {
  font-size: clamp(0.72rem, 1.2vw, 0.88rem);
  color: #555;
  line-height: 1.5;
}

.rightCol {
  flex: 1;
  min-width: 0;
}

.logoGrid {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: clamp(32px, 5vw, 60px);
  align-items: center;
  justify-items: center;
}

.logoImg {
  height: clamp(40px, 6vw, 72px);
  width: auto;
  object-fit: contain;
  display: block;
}

.logoItem {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
}

.logoName {
  font-size: clamp(0.65rem, 1vw, 0.8rem);
  color: #888;
  font-family: Monda, system-ui, sans-serif;
  text-align: center;
}
@media (max-width: 768px) {
  .body { flex-direction: column; }
  .leftSection { flex: none; max-width: 100%; width: 100%; }
  .logoGrid { grid-template-columns: repeat(3, auto); }
}

@media (max-width: 480px) {
  .logoGrid { grid-template-columns: repeat(2, auto); }
}
</style>
