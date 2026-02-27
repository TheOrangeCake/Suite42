<script setup lang="ts">
import Corner from '../../ui/Corner.vue'
import { ref } from 'vue'

const emit = defineEmits(['login42', 'loginPublic', 'signup'])

const username = ref('')
const password = ref('')

function handlePublicLogin() {
  emit('loginPublic', { username: username.value, password: password.value })
}
</script>

<template>
  <section class="container2">

    <!-- Titre principal -->
    <h1 class="headline">Let's get productive !</h1>

    <div class="body">

      <!-- Colonne gauche : formulaires -->
      <div class="leftCol">

          <div class="sections">

            <!-- 42 Student login -->
            <div class="loginBlock">
              <div class="blockTitle">
                <Corner :vSize="64" :hSize="26" :thickness="5" color="var(--color-coral, #FF5959)" />
                <h2 class="blockName">42 Student login</h2>
              </div>

              <div class="blockContent">
                <Corner :vSize="34" :hSize="36" :thickness="5" color="var(--color-coral, #FF5959)" />
                <button class="btn42" @click="emit('login42')">
                  Login with 42 account
                </button>
              </div>

            <!-- Public login -->
            <div class="loginBlock">
              <div class="blockTitle">

                <Corner :vSize="64" :hSize="26" :thickness="5" color="var(--color-turquoise)" />
                <h2 class="blockName">Public login</h2>
              </div>

              <div class="blockContent">
                <Corner :vSize="160" :hSize="16" :thickness="5" color="var(--color-turquoise)" />

                <div class="formFields">
                  <div class="field">
                    <label class="fieldLabel">USERNAME</label>
                    <input
                      v-model="username"
                      type="text"
                      class="fieldInput"
                      placeholder="Pookie42"
                    />
                  </div>

                  <div class="field">
                    <label class="fieldLabel">PASSWORD</label>
                    <input
                      v-model="password"
                      type="password"
                      class="fieldInput"
                    />
                  </div>

                  <button class="btnLogin" @click="handlePublicLogin">
                    Log in
                  </button>
                </div>
              </div>
            </div>

          </div>
        </div>

        <!-- Sign up en dehors de la ligne rouge -->
        <div class="signupRow">
          <p class="signupText">
            No account ?
            <span class="signupLink" @click="emit('signup')">Sign up here</span>
          </p>
        </div>

      </div>

      <!-- Colonne droite : illustration -->
      <div class="rightCol">
        <img
          src="/design/assets/images/sign_in_illustration.png"
          alt="Astronaut on rocket"
          class="illustration"
        />
      </div>

    </div>
  </section>
</template>

<style scoped>
* { box-sizing: border-box; }

.container2 {
  width: 100%;
  padding: 4% 5%;
  background-color: #f0f0f0;
}

.headline {
  color: #202020;
  font-family: monda;
  font-weight: 700;
  font-size: clamp(3rem, 6vw, 64px);

}

.body {
  display: flex;
  flex-direction: row;
  align-items: center;
}

.leftCol {
  flex: 0 0 42%;
  display: flex;
  flex-direction: column;
}

.redSection {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
}

.redLine {
  flex-shrink: 0;
  margin-top: 4px;
}

.sections {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.loginBlock {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.blockTitle {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 10px;
}

.blockName {
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: clamp(1.2rem, 2.4vw, 1.6rem);
  color: #202020;
  margin: 0;
  line-height: 1;
}

.blockContent {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 10px;
  padding-left: 26px;

}

.formFields {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 3px;
}

.fieldLabel {
  font-size: 0.68rem;
  font-family: Monda, system-ui, sans-serif;
  color: #888;
  letter-spacing: 0.08em;
}

.fieldInput {
  background: white;
  border: 1.5px solid #ddd;
  border-radius: 6px;
  padding: 9px 12px;
  font-size: 0.88rem;
  font-family: Monda, system-ui, sans-serif;
  color: #202020;
  outline: none;
  width: 100%;
  max-width: 240px;
  transition: border-color 0.2s;
}

.fieldInput:focus {
  border-color: var(--color-turquoise, #00C9C8);
}

.btn42 {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 7px 14px;
  background: white;
  border: 1.5px solid #ccc;
  border-radius: 6px;
  font-family: Monda, system-ui, sans-serif;
  font-size: 0.82rem;
  color: #202020;
  cursor: pointer;
  transition: background 0.2s;
}

.btn42:hover { background: #f0f0f0; }

.logo42 {
  height: 16px;
  width: auto;
}

.btnLogin {
  padding: 9px 24px;
  background: var(--color-coral, #FF5959);
  border: none;
  border-radius: 6px;
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: 0.95rem;
  color: white;
  cursor: pointer;
  align-self: flex-start;
  transition: opacity 0.2s;
}

.btnLogin:hover { opacity: 0.85; }

.signupRow {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 10px;
  margin-top: 8px;
}

.signupText {
  font-size: 0.8rem;
  color: #888;
  font-family: Monda, system-ui, sans-serif;
  margin: 0;
}

.signupLink {
  color: #202020;
  font-weight: 700;
  cursor: pointer;
  text-decoration: underline;
}

.signupLink:hover { color: var(--color-green); }

.rightCol {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.illustration {
  width: 90%;
  max-width: 480px;
  object-fit: contain;
}

@media (max-width: 768px) {
  .body { flex-direction: column; }
  .leftCol { flex: none; max-width: 100%; width: 100%; }
}
</style>
