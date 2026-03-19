<script setup lang="ts">
  import { ref } from 'vue'
  import { useRouter } from 'vue-router'
  import { signin, verifyOtp } from '../api/auth'
  import { useAuthStore } from '../stores/auth'

  type LoginPayload = { username: string, password: string }

  const router = useRouter()
  const authStore = useAuthStore()

  const isLoading = ref(false)
  const errorMessage = ref('')

  const showOtp = ref(false)
  const pendingEmail = ref('')
  const emailInput = ref('')
  const otpInput = ref('')
  const otpError = ref('')

  async function onLogin (payload: LoginPayload) {
    errorMessage.value = ''
    if (!payload.username.trim()) {
      errorMessage.value = 'Username is required.'
      return
    }
    if (!payload.password) {
      errorMessage.value = 'Password is required.'
      return
    }
    isLoading.value = true
    try {
      const result = await signin(payload.username, payload.password)
      if (result.twoFa) {
        pendingEmail.value = result.email
        emailInput.value = result.email
        otpInput.value = ''
        otpError.value = ''
        showOtp.value = true
      } else {
        const token = authStore.accessToken
        if (!token) {
          errorMessage.value = 'Authentication failed: no token received.'
          return
        }
        authStore.setSession(result.user, token)
        router.push('/profile')
      }
    } catch (error) {
      if (error instanceof Error) errorMessage.value = error.message
    } finally {
      isLoading.value = false
    }
  }

  function onLogin42 () {
    window.location.href = `${import.meta.env.VITE_API_URL}/api/auth/oauth2/authorization/42`
  }

  async function onVerifyOtp () {
    const email = pendingEmail.value || emailInput.value
    const otp = Number.parseInt(otpInput.value)

    if (!email || Number.isNaN(otp) || otpInput.value.length !== 6) {
      otpError.value = 'Please enter a valid 6-digit code.'
      return
    }

    isLoading.value = true
    otpError.value = ''
    try {
      const user = await verifyOtp(email, otp)
      const token = authStore.accessToken
      if (!token) {
        otpError.value = 'Authentication failed: no token received.'
        return
      }
      authStore.setSession(user, token)
      router.push('/profile')
    } catch (error) {
      if (error instanceof Error) otpError.value = error.message
    } finally {
      isLoading.value = false
    }
  }
</script>

<template>
  <div>
    <LoginSections
      :error-message="errorMessage"
      :is-loading="isLoading"
      @login="onLogin"
      @login42="onLogin42"
    />

    <div v-if="showOtp" class="otp-overlay">
      <div class="otp-modal">
        <h2>Two-Factor Authentication</h2>
        <p>A 6-digit code has been sent to your email.</p>

        <div v-if="!pendingEmail" class="field">
          <label>Email</label>
          <input v-model="emailInput" placeholder="your@email.com" type="email">
        </div>

        <div class="field">
          <label>Code</label>
          <input
            v-model="otpInput"
            inputmode="numeric"
            maxlength="6"
            placeholder="123456"
            type="text"
            @keyup.enter="onVerifyOtp"
          >
        </div>

        <p v-if="otpError" class="otp-error">{{ otpError }}</p>

        <div class="otp-actions">
          <button class="btn-confirm" :disabled="isLoading" @click="onVerifyOtp">
            {{ isLoading ? 'Verifying...' : 'Confirm' }}
          </button>
          <button class="btn-cancel" @click="showOtp = false">Cancel</button>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.otp-overlay {
  position: fixed;
  inset: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 100;
}

.otp-modal {
  background: white;
  border-radius: 8px;
  padding: 32px;
  width: 100%;
  max-width: 380px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.otp-modal h2 {
  font-family: Monda, sans-serif;
  font-size: 1.3rem;
  font-weight: 700;
  color: #202020;
  margin: 0;
}

.otp-modal p {
  font-family: Monda, sans-serif;
  font-size: 0.9rem;
  color: #555;
  margin: 0;
}

.field {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.field label {
  font-family: Monda, sans-serif;
  font-size: 0.8rem;
  font-weight: 600;
  color: #202020;
}

.field input {
  padding: 10px 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 1rem;
  outline: none;
}

.field input:focus {
  border-color: #202020;
}

.otp-error {
  color: #FF5959;
  font-size: 0.85rem;
  margin: 0;
}

.otp-actions {
  display: flex;
  gap: 8px;
}

.btn-confirm {
  flex: 1;
  padding: 12px;
  background: #202020;
  color: white;
  border: none;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.95rem;
  font-weight: 600;
  cursor: pointer;
}

.btn-confirm:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

.btn-cancel {
  padding: 12px 20px;
  background: transparent;
  color: #555;
  border: 1px solid #ccc;
  border-radius: 4px;
  font-family: Monda, sans-serif;
  font-size: 0.95rem;
  cursor: pointer;
}
</style>
