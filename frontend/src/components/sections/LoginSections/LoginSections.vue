<template>
  <div class="page">
    <div class="card">
      <!-- Décor à gauche : ligne + corners -->
      <div class="decor" aria-hidden="true">
        <div class="rail rail-blue"></div>

        <!-- Wrappers pour “ancrer” la taille des corners -->
        <div class="cornerWrap c1">
          <Corner :verticalLength="80" :horizontalLength="35" :thickness="3" :color="colors.turquoise" />
        </div>

        <div class="cornerWrap c2">
          <Corner :verticalLength="80" :horizontalLength="35" :thickness="3" :color="colors.turquoise" />
        </div>

        <div class="cornerWrap c3">
          <Corner :verticalLength="80" :horizontalLength="35" :thickness="3" :color="colors.turquoise" />
        </div>

        <div class="rail rail-green"></div>
      </div>

      <!-- Contenu -->
      <div class="content">

        <form class="form" @submit.prevent="onSubmit">
          <div class="field">
            <label class="label" for="username">USERNAME</label>
            <input
              id="username"
              v-model="username"
              class="input"
              type="text"
              autocomplete="username"
            />
          </div>

          <div class="field">
            <label class="label" for="password">PASSWORD</label>
            <input
              id="password"
              v-model="password"
              class="input"
              type="password"
              autocomplete="current-password"
            />
          </div>

          <button class="btn" type="submit">Log in</button>

          <p class="footer">
            <span>No account ?</span>
            <a class="link" href="#">Sign up here</a>
          </p>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import Corner from "@/components/ui/Corner.vue";

const username = ref("Pookie42");
const password = ref("****************");

const colors = {
  turquoise: "var(--turquoise)",
  green: "var(--green)",
};

function onSubmit() {
  console.log("login", username.value, password.value);
}
</script>

<style scoped>
/* Variables (scoped-friendly) */
.page {
  --turquoise: #2fd3c8;
  --green: #33c06b;
  --border: #dcdcdc;
  --btn: #ff5a4f;
  --text: #111;
  --muted: #777;

  margin-left: 150px;
  background: #fff;
  padding: 18px 16px;
  font-family: system-ui, -apple-system, Segoe UI, Roboto, Arial, sans-serif;
  color: var(--text);
}

.card {
  width: 320px;
  display: grid;
  grid-template-columns: 90px 1fr; /* <-- CHANGEMENT IMPORTANT */
  column-gap: 10px;
  align-items: start;
}

/* ✅ décor : on CLIPPE vers la droite pour empêcher les coins d’entrer dans le contenu */
.decor {
  position: relative;
  height: 340px;
  overflow: hidden; /* <-- CHANGEMENT IMPORTANT */
}

/* ligne verticale */
.rail {
  position: absolute;
  left: 12px;
  width: 3px;
  border-radius: 2px;
}

.rail-blue {

  height: 235px;
  background: var(--turquoise);
}

.rail-green {
  top: 186px;
  height: 160px;
  background: var(--green);
}

/* ✅ wrapper qui donne une vraie boîte au composant Corner */
.cornerWrap {
  position: absolute;
  left: 12px;         /* aligné avec la ligne */
  width: 38px;        /* ~ horizontalLength + thickness */
  height: 83px;       /* ~ verticalLength + thickness */
  pointer-events: none;
}

/* positions des corners */
.c1 { top: 62px; }   /* username */
.c2 { top: 156px; }  /* password */
.c3 { top: 248px; }  /* bouton */

/* contenu */
.title {
  margin: 0 0 14px 0;
  font-size: 34px;
  font-weight: 800;
  letter-spacing: -0.3px;
}

.form {
  display: grid;
  gap: 16px;
}

.field {
  display: grid;
  gap: 6px;
}

.label {
  font-size: 10px;
  font-weight: 800;
  letter-spacing: 0.8px;
  color: #111;
}

.input {
  height: 34px;
  width: 210px; /* proche modèle */
  border: 1px solid var(--border);
  border-radius: 2px;
  padding: 0 10px;
  font-size: 13px;
  outline: none;
  background: #fff;
}

.input:focus {
  border-color: #bfbfbf;
  box-shadow: 0 0 0 3px rgba(47, 211, 200, 0.18);
}

.btn {
  width: 88px;
  height: 34px;
  border: none;
  border-radius: 2px;
  background: var(--btn);
  color: #fff;
  font-weight: 800;
  font-size: 13px;
  cursor: pointer;
  justify-self: start;
}

.footer {
  margin: 2px 0 0;
  display: flex;
  gap: 6px;
  font-size: 11px;
  color: var(--muted);
}

.link {
  color: #111;
  font-weight: 800;
  text-decoration: none;
}
.link:hover {
  text-decoration: underline;
}
</style>

