<script setup lang="ts">

 import Corner from '../../../components/ui/Corner.vue'
const tools = [
  {
    id: 'teammates',
    title: 'Teammates finder',
    description: 'Find the best teammates to match your skills and help you achieve the best project goals.',
    comingSoon: false,

    image: '/design/assets/placeholder/tool_section.jpg',
  },
  {
    id: 'ai-project',
    title: 'AI project helper',
    description: 'Use AI to generate and manage tasks, assign roles to project collaborators and review.',
    comingSoon: true,
    image: '/design/assets/placeholder/tool_section.jpg',
  },
  {
    id: 'todo',
    title: 'To do list',
    description: 'Keep track, follow your progress and maintain stakeholders care for all projects.',
    comingSoon: true,
    image: '/design/assets/placeholder/tool_section.jpg',
  },
]
</script>

<template>
  <section class="toolkit">

  <div class="toolkit__rail">
    <Corner :vSize="800" :hSize="12" color="#6C63FF" :thickness="3" />
  </div>

  <!-- colonne droite : tout ton contenu existant -->
  <div class="toolkit__content">

    <h2 class="toolkit__title">Our toolkit</h2>

    <div class="toolkit__list">
      <div v-for="tool in tools" :key="tool.id" class="toolkit__item">

        <div class="toolkit__info">
          <Corner :vSize="40" :hSize="12" color="#6C63FF" :thickness="3" />
          <div class="toolkit__text">
            <h3 class="toolkit__name">
              {{ tool.title }}
              <span v-if="tool.comingSoon" class="toolkit__soon">(coming soon)</span>
            </h3>
            <p class="toolkit__desc">{{ tool.description }}</p>
          </div>
        </div>

        <div class="toolkit__preview">
          <img :src="tool.image" :alt="tool.title" />
        </div>

      </div>
    </div>

    <div class="toolkit__cta">
      <button class="toolkit__btn">Let's start together!</button>
    </div>

  </div>
  </section>
</template>


<style scoped>


.toolkit__rail{
  display:flex;
  justify-content:center;
  margin-right: 20px;

}
.toolkit {
  display: grid;
  top: 110px;
  grid-template-columns: 30px 1fr;
  gap: 20px;
  margin-left: 5%;
  font-family: sans-serif;
}

.toolkit__title {
  font-size: 1.4rem;
  font-weight: 700;
  margin-bottom: 32px;
  color: #1a1a2e;
}

.toolkit__list {
  display: flex;
  flex-direction: column;
  gap: 40px;
}

.toolkit__item {
  display: flex;
  align-items: center;
  gap: 24px;
}

.toolkit__info {
  display: flex;
  align-items: flex-start;
  gap: 10px;
  flex: 1;
}

.toolkit__text {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.toolkit__name {
  font-size: 0.95rem;
  font-weight: 600;
  color: #1a1a2e;
  margin: 0;
}

.toolkit__soon {
  font-weight: 400;
  color: #888;
  font-size: 0.85rem;
}

.toolkit__desc {
  font-size: 0.8rem;
  color: #666;
  line-height: 1.5;
  margin: 0;
  max-width: 200px;
}

.toolkit__preview {
  flex: 1;
  display: flex;
  justify-content: flex-end;
}

.toolkit__preview img {
  width: 260px;
  border-radius: 12px;
  object-fit: cover;
  margin-right: 300px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.12);
}

.toolkit__cta {
  margin-top: 40px;
  display: flex;
  justify-content: flex-start;
}

.toolkit__btn {
  background-color: #e8384f;
  color: white;
  border: none;
  padding: 10px 24px;
  font-size: 0.85rem;
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s ease;
}

.toolkit__btn:hover {
  background-color: #c9303f;
}
</style>
