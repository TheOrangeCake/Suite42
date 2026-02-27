<script setup lang="ts">
import Corner from '../../ui/Corner.vue'

const teamMembers = [
  { id: 1, name: 'Alex',   role: 'Frontend',  img: '' },
  { id: 2, name: 'Vincent',     role: 'Backend',    img: '' },
  { id: 3, name: 'Dan', role: 'Backend',     img: '' },
  { id: 4, name: 'Dimitri',   role: 'Backend',      img: '' },
  { id: 5, name: 'Nguyen',     role: 'Full-stack',  img: '' },
]
</script>

<template>
  <section class="container">

    <div class="titleRow">
      <div class="titleCorners" aria-hidden="true">
        <Corner :vSize="100" :hSize="20" :thickness="5" color="var(--color-coral, #FF5959)" />
      </div>
      <h1 class="headline">About us</h1>
    </div>

    <div class="body">

      <div class="greenRail" aria-hidden="true">

        <Corner :vSize="800" :hSize="3" :thickness="5" color="var(--color-green)" />
      </div>

      <div class="content">

        <div class="leftCol">
          <div class="textItem">
            <div class="itemCorner" aria-hidden="true">
              <Corner :vSize="60" :hSize="20" :thickness="5" color="var(--color-turquoise)" />
            </div>
            <div class="textBody">
              <p>
                Suite42 is a ft_transcendence project by ${name1}, ${name2},
                ${name3}, ${name4}, and ${name5}, from the 2023–2024 cohort at
                42 Lausanne.
              </p>
            </div>
          </div>

          <div class="textItem">
            <div class="itemCorner" aria-hidden="true">
              <Corner :vSize="350" :hSize="20" :thickness="5" color="var(--color-turquoise)" />
            </div>
            <div class="textBody">
              <p>
                We were not content with simply creating something for the sake of
                creating; we wanted our project to have a clear purpose and a reason
                to exist. The idea was first proposed by one of the team members, who
                had difficulty finding groups for many of his projects at 42. The initial
                concept was a group finder based on users' progression in the
                Common Core. The team immediately supported the idea, as several
                members had encountered the same problem.
              </p>
              <p class="indent">
                After conducting research, we found that several websites already
                offer "group finder" features, but they are limited to simply displaying
                information. Our team decided to take this concept further by adding
                the ability to connect with other students, add friends, communicate
                directly, and use advanced filtering and sorting. We also implemented
                a recommendation system based on Common Core progression.
              </p>
            </div>
          </div>

          <div class="textItem">
            <div class="itemCorner" aria-hidden="true">
              <Corner :vSize="50" :hSize="20" :thickness="5" color="var(--color-turquoise)" />
            </div>
            <div class="textBody">
              <p>
                The project later evolved into a multi-purpose suite designed to
                support 42 students in successfully completing their Common Core.
              </p>
            </div>
          </div>
        </div>

        <div class="rightCol">
          <div class="photoGrid">
            <div
              v-for="member in teamMembers"
              :key="member.id"
              class="photoCard"
            >
              <img
                v-if="member.img"
                :src="member.img"
                :alt="member.name"
                class="photo"
              />
              <div v-else class="photoPlaceholder">
                <span class="initials">{{ member.name[0] }}</span>
              </div>
            </div>
          </div>
        </div>

      </div>
    </div>

  </section>
</template>

<style scoped>
* { box-sizing: border-box; }
p { margin: 0; }

.container {

  padding-left: 5%;
  padding-right: 5%;
  width: 100%;
}

.titleRow {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 16px;
  margin-bottom: clamp(24px, 4vw, 48px);
}

.titleCorners {
  flex: 0 0 auto;

}

.headline {
  margin: 0;
  color: #ffffff;
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: clamp(1.8rem, 5vw, 64px);
  line-height: 1.05;
  letter-spacing: -0.03em;
}

.body {
  display: flex;
  flex-direction: row;
  align-items: stretch;
  gap: 0;
}

.greenRail {
  flex: 0 0 auto;
  display: flex;
  align-self: stretch;
  margin-right: clamp(16px, 2.5vw, 32px);
}

.rail {
  width: 5px;
  background-color: var(--color-green, #4CAF81);
  border-radius: 3px;
  align-self: stretch;
}

.content {
  padding-left: 2%;
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: clamp(32px, 5vw, 80px);
}

.leftCol {
  flex: 1 1 55%;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: clamp(14px, 3vw, 28px);
}

.textItem {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: clamp(12px, 2vw, 20px);
}

.itemCorner {
  flex: 0 0 auto;
}

.textBody {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.textBody p {
  color: #aaaaaa;
  font-size: clamp(0.82rem, 1.4vw, 1rem);
  line-height: 1.7;
}

.textBody p.indent {
  padding-left: 16px;
}

.rightCol {
  flex: 0 0 38%;
  max-width: 38%;
}
.photoGrid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: clamp(10px, 2vw, 20px);
}

.photoCard:nth-child(5) {
  grid-column: 1 / 3;
  max-width: 50%;
  margin: 0 auto;
  width: 100%;
}

.photoCard {
  display: flex;
  flex-direction: column;
  align-items: stretch;
}

.photo {
  width: 100%;
  aspect-ratio: 1 / 1;
  object-fit: cover;
  border-radius: 8px;
  display: block;
}

.photoPlaceholder {
  width: 100%;
  aspect-ratio: 1 / 1;
  background-color: #2a2a2a;
  border: 2px dashed #3a3a3a;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.initials {
  color: #555;
  font-size: clamp(1.4rem, 3vw, 2.2rem);
  font-weight: 700;
  font-family: Monda, system-ui, sans-serif;
}

@media (max-width: 900px) {
  .content {
    flex-direction: column;
  }
  .rightCol {
    flex: none;
    max-width: 100%;
    width: 100%;
  }
  .photoGrid {
    grid-template-columns: repeat(3, 1fr);
  }
  .photoCard:nth-child(5) {
    grid-column: unset;
    max-width: 100%;
    margin: 0;
  }
}

@media (max-width: 520px) {
  .photoGrid {
    grid-template-columns: repeat(2, 1fr);
  }
  .photoCard:nth-child(5) {
    grid-column: 1 / 3;
    max-width: 50%;
    margin: 0 auto;
  }
}
</style>
