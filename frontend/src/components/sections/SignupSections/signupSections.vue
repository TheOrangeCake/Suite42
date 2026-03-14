<script setup lang="ts">
import Corner from '../../ui/Corner.vue'
import { ref } from 'vue'
const emit = defineEmits(['signup'])

const username = ref('')
const email = ref('')
const password = ref('')
const confirmPassword = ref('')
const agreeToTerms = ref(false)

const props = defineProps<{
  isLoading?: boolean
  errorMessage?: string
}>()

function handleSignup() {
  emit('signup', {
    username: username.value,
    email: email.value,
    password: password.value,
    confirmPassword: confirmPassword.value,
  })
}
</script>

<template>
  <section class="container2">

    <h1 class="headline">Sign up</h1>

    <div class="body">

      <div class="leftCol">

        <div class="blueSection">
          <Corner :vSize="380" :hSize="16" :thickness="5" color="var(--color-turquoise)" class="blueLine" />

          <div class="sections">

            <div class="loginBlock">
              <div class="blockTitle">
                <Corner :vSize="44" :hSize="16" :thickness="5" color="var(--color-turquoise)" />
                <h2 class="blockName">Credentials</h2>
              </div>

              <div class="blockContent">
                <Corner :vSize="280" :hSize="16" :thickness="5" color="var(--color-turquoise)" />

                <div class="formFields">
                  <div class="field">
                    <label class="fieldLabel">USERNAME</label>
                    <input v-model="username" type="text" class="fieldInput" placeholder="Pookie42" />
                  </div>

                  <div class="field">
                    <label class="fieldLabel">EMAIL</label>
                    <input v-model="email" type="email" class="fieldInput" placeholder="pookie42@proton.me" />
                  </div>

                  <div class="field">
                    <label class="fieldLabel">PASSWORD</label>
                    <input v-model="password" type="password" class="fieldInput" />
                  </div>

                  <div class="field">
                    <label class="fieldLabel">CONFIRM PASSWORD</label>
                    <input v-model="confirmPassword" type="password" class="fieldInput" />
                  </div>

                  <label class="checkboxRow">
                    <input v-model="agreeToTerms" type="checkbox" class="checkbox" />
                    <span class="checkboxLabel">Agree to Terms of Service</span>
                  </label>
                </div>
              </div>
            </div>

          </div>
        </div>

        <div class="signupRow">
          <Corner :vSize="36" :hSize="16" :thickness="5" color="var(--color-turquoise)" />
         <p v-if="errorMessage" class="errorMsg">{{ errorMessage }}</p>

          <button
            class="btnSignup"
            @click="handleSignup"
            :disabled="isLoading" >{{ isLoading ? 'Signing up...' : 'Sign up'}}</button>
        </div>

      </div>

      <div class="rightCol">
        <img
          src="/design/assets/images/sign_up_illustration.png"
          alt="Astronaut with jetpack"
          class="illustration"
        />
      </div>

    </div>
  </section>
</template>

<style scoped>
.errorMsg {
  color: #FF5959;
  font-size: 0.8rem;
  font-family: Monda, system-ui, sans-serif;
  margin: 0;
}

* { box-sizing: border-box; }

.container2 {
  width: 100%;
  padding: 4% 5%;
  background-color: #f0f0f0;
}

.headline {
  color: #202020;
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: clamp(1.6rem, 3.5vw, 44px);
  letter-spacing: -0.02em;
  margin-bottom: clamp(16px, 3vw, 28px);
}

.body {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 0;
}

.leftCol {
  flex: 0 0 42%;
  max-width: 42%;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.blueSection {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 12px;
}

.blueLine {
  flex-shrink: 0;
  margin-top: 4px;
}

.sections {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 28px;
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
  font-size: clamp(0.95rem, 1.8vw, 1.25rem);
  color: #202020;
  margin: 0;
  line-height: 1;
}

.blockContent {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 10px;
  padding-left: 8px;
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

.checkboxRow {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  margin-top: 4px;
}

.checkbox {
  width: 14px;
  height: 14px;
  cursor: pointer;
  accent-color: var(--color-turquoise, #00C9C8);
}

.checkboxLabel {
  font-size: 0.78rem;
  color: #555;
  font-family: Monda, system-ui, sans-serif;
}

.signupRow {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 10px;
  margin-top: 4px;
}

.btnSignup {
  padding: 9px 24px;
  background: var(--color-coral, #FF5959);
  border: none;
  border-radius: 6px;
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: 0.95rem;
  color: white;
  cursor: pointer;
  transition: opacity 0.2s;
}

.btnSignup:hover { opacity: 0.85; }

/* ── Colonne droite ─────────────────────────────── */
.rightCol {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.illustration {
  width: 90%;
  max-width: 420px;
  object-fit: contain;
}

/* ── Responsive ─────────────────────────────────── */
@media (max-width: 768px) {
  .body { flex-direction: column; }
  .leftCol { flex: none; max-width: 100%; width: 100%; }
}
</style>
