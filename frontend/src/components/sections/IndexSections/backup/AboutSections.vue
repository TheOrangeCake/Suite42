<script setup lang="ts">

import Corner from '../../../components/ui/Corner.vue'

const members = [
  { name: 'name1', image: '/images/member1.png' },
  { name: 'name2', image: '/images/member2.png' },
  { name: 'name3', image: '/images/member3.png' },
  { name: 'name4', image: '/images/member4.png' },
  { name: 'name5', image: '/images/member5.png' },
]

const paragraphs = [
  {
    text: 'Suite42 is a ft_transcendence project by ${name1}, ${name2}, ${name3}, ${name4}, and ${name5}, from the 2023–2024 cohort at 42 Lausanne.',
  },
  {
    text: 'We were not content with simply creating something for the sake of creating; we wanted our project to have a clear purpose and a reason to exist. The idea was first proposed by one of the team members, who had difficulty finding groups for many of his projects at 42. The initial concept was a group finder based on users\' progression in the Common Core. The team immediately supported the idea, as several members had encountered the same problem.\n\nAfter conducting research, we found that several websites already offer "group finder" features, but they are limited to simply displaying information. Our team decided to take this concept further by adding the ability to connect with other students, add friends, communicate directly, and use advanced filtering and sorting. We also implemented a recommendation system based on Common Core progression.',
  },
  {
    text: 'The project later evolved into a multi-purpose suite designed to support 42 students in successfully completing their Common Core.',
  },
]
</script>

<template>
  <section class="about">
    <!-- Title with top-left corner -->
    <div class="about__header">
      <Corner :vSize="32" :hSize="16" color="#e8384f" :thickness="3" />
      <h1 class="about__title">About us</h1>
    </div>

    <div class="about__body">
      <!-- Left: paragraphs with corners -->
      <div class="about__left">
        <div
          v-for="(para, index) in paragraphs"
          :key="index"
          class="about__block"
        >
          <div class="about__block-corner">
            <Corner :vSize="24" :hSize="16" color="#00bcd4" :thickness="3" />
          </div>
          <div class="about__block-bar" />
          <p class="about__text" v-html="para.text.replace(/\n\n/g, '<br><br>')" />
        </div>
      </div>

      <!-- Right: photo grid -->
      <div class="about__grid">
        <div
          v-for="member in members"
          :key="member.name"
          class="about__photo-wrap"
        >
          <img
            :src="member.image"
            :alt="member.name"
            class="about__photo"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
.about {
  background-color: #1a1a1a;
  color: #ffffff;
  min-height: 100vh;
  padding: 48px 64px;
  font-family: 'Share Tech Mono', monospace, sans-serif;
  box-sizing: border-box;
}

/* Header */
.about__header {
  display: flex;
  align-items: flex-end;
  gap: 12px;
  margin-bottom: 48px;
}

.about__title {
  font-size: 3rem;
  font-weight: 800;
  margin: 0;
  line-height: 1;
  letter-spacing: -1px;
}

/* Body: left text + right grid */
.about__body {
  display: flex;
  gap: 64px;
  align-items: flex-start;
}

/* Left column */
.about__left {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 40px;
  /* vertical red line on the left */
  border-left: 3px solid #e8384f;
  padding-left: 0;
}

/* Each paragraph block */
.about__block {
  display: flex;
  align-items: flex-start;
  gap: 0;
  position: relative;
}

.about__block-corner {
  /* pulls the corner out to the left so it sits on the red border */
  margin-left: -9px;
  margin-top: 0;
  flex-shrink: 0;
}

.about__text {
  font-size: 0.95rem;
  line-height: 1.7;
  color: #dddddd;
  margin: 0;
  padding-left: 16px;
}

/* Right: photo grid */
.about__grid {
  display: grid;
  grid-template-columns: repeat(2, 180px);
  grid-template-rows: auto auto auto;
  gap: 16px;
  flex-shrink: 0;
  align-content: start;
}

/* 5th photo centered in last row */
.about__photo-wrap:nth-child(5) {
  grid-column: 1 / 2;
  justify-self: center;
  grid-column: span 2;
  display: flex;
  justify-content: center;
}

.about__photo {
  width: 180px;
  height: 180px;
  object-fit: cover;
  border-radius: 8px;
  display: block;
}

.about__photo-wrap:nth-child(5) .about__photo {
  width: 180px;
  height: 180px;
}
</style>
