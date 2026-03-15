<script setup lang="ts">
  import { useRouter } from 'vue-router'
  import { colors } from '@/styles/Colors.ts'

  const router = useRouter()

  const bigRedButtonProps = defineProps<{
    path: string
    text: string
  }>()

  const isHovered = ref(false)
</script>

<template>
  <div class="flex justify-center items-center">
    <button
      class="flex px-5 py-2.5 justify-center items-center rounded border-2 font-medium font-h4-mobile transition-colors duration-200
             md:font-h4-mobile
             lg:font-h4-tablet
             xl:font-h4-desktop"
      :style="{
        borderColor: colors.suite42Red,
        backgroundColor: isHovered ? undefined : colors.suite42Red,
        color: isHovered ? colors.suite42Black : colors.suite42White
      }"
      @click="router.push(bigRedButtonProps.path)"
      @mouseleave="isHovered = false"
      @mouseover="isHovered = true"
    >
      {{ bigRedButtonProps.text }}
    </button>
  </div>
</template>
