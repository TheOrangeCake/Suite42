<template>
    <signupSections @signup="onSignup"/>
  </template>

  <script setup lang="ts">
    import signupSections from '../components/sections/SignupSections/signupSections.vue'
    type SignupPayload = {
      username: string
      email: string
      password: string
      confirmPassword: string
    }

function onSignup(payload: SignupPayload) {
  console.log('Signup reçu du composant enfant:', payload)

}
  </script >
