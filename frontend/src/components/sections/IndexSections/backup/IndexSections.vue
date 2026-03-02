<template>
  <section class="hero">
    <div class="rail">
      <Corner :vSize="150" :hSize="38" :thickness="5" color="var(--color-turquoise)" />
      <Corner :vSize="680" :hSize="38" :thickness="5" color="var(--color-green)" />
    </div>

    <div class="content">
      <p class="title">A practical companion for the 42 journey!</p>

      <div class="subtitle">
        <Corner class="corner-block" :vSize="50" :hSize="35" :thickness="5" color="var(--color-turquoise)" />
        <p class="subtitle-text">
          An unofficial collection of tools for learning and collaboration dedicated to 42 students
        </p>
      </div>

      <div class="actions">
        <div class="cta">
          <Corner class="corner-block" :vSize="130" :hSize="25" :thickness="5" color="var(--color-turquoise)" />
          <button class="button" type="button" @click="$router.push('/login')">
            Start your journey here
          </button>
        </div>

        <img
          class="hero-img"
          src="../../../../design/assets/images/homepage_illustration.png"
          alt="illustration"
        />
      </div>
    </div>
  </section>
</template>

<script> import Corner from '../../../components/ui/Corner.vue' </script>


<style scoped>
/* === Layout global === */
.hero {
  /* container fluide + max width */
  width: min(100% - 2rem, 80rem);
  margin-inline: auto;

  display: grid;
  grid-template-columns: 40px 1fr;
  column-gap: clamp(0.75rem, 2vw, 1.5rem);

  padding-block: clamp(2rem, 6vw, 5rem);
  align-items: start;
}

.rail {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1rem;
}

/* === Contenu === */
.content {
  display: flex;
  flex-direction: column;
  gap: clamp(1rem, 2vw, 1.75rem);
  font-family: monda, system-ui, sans-serif;
  color: #444;
}

.title {
  font-weight: 700;
  font-size: clamp(2rem, 4vw, 4.5rem); /* remplace ton 74px */
  line-height: 1.1;
  margin: 0;
}

/* bloc texte + corner */
.subtitle {
  display: flex;
  align-items: flex-start;
  gap: clamp(0.75rem, 2vw, 1.5rem);
}

.subtitle-text {
  margin: 0;
  font-size: clamp(1.05rem, 1.6vw, 2rem); /* remplace ton 34px */
  line-height: 1.35;
  max-width: 55ch; /* lisibilité */
}

/* === Actions (bouton + image) === */
.actions {
  display: grid;
  grid-template-columns: 1fr auto;
  align-items: center;
  gap: clamp(1rem, 3vw, 2rem);
  margin-top: clamp(0.5rem, 2vw, 1.5rem);
}

/* CTA à gauche */
.cta {
  display: inline-flex;
  align-items: center;
  gap: 0.75rem;
  flex-wrap: wrap; /* si ça manque de place */
}

/* Bouton responsive */
.button {
  background-color: #ff5959;
  color: white;
  border: 0;
  border-radius: 6px;

  font-size: clamp(1rem, 2vw, 2rem); /* remplace ton 40px */
  line-height: 1.2;                  /* corrige 1,3 */
  padding: clamp(0.7rem, 1.2vw, 1rem) clamp(1rem, 2vw, 1.75rem);

  cursor: pointer;
  white-space: nowrap;
}

/* Image responsive */
.hero-img {
  width: clamp(220px, 28vw, 420px);
  height: auto;
  display: block;
  pointer-events: none;
}

/* === Petits écrans === */
@media (max-width: 820px) {
  .hero {
    grid-template-columns: 1fr; /* rail au-dessus */
    row-gap: 1rem;
  }

  .rail {
    flex-direction: row;
    justify-content: flex-start;
    align-items: flex-start;
  }

  .actions {
    grid-template-columns: 1fr; /* image passe dessous */
  }

  .hero-img {
    justify-self: start;
  }

  /* optionnel: éviter le débordement sur très petits écrans */
  .button {
    white-space: normal;
    text-align: left;
  }
}
</style>
