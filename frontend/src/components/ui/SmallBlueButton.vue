<script setup lang="ts">
  import { type Colors, colors } from '@/styles/Colors.ts'

  const smallBlueButtonProps = withDefaults(
    defineProps<{
      text: string
      hoverTextColor?: Colors
    }>(),
    { hoverTextColor: 'suite42Black' },
  )

  const isHovered = ref(false)
  const emit = defineEmits(['click'])
</script>

<template>
  <div class="flex justify-center items-center cursor-pointer">
    <button
      class="flex px-6 py-2 justify-center items-center rounded border-2 font-bold font-body1-mobile transition-colors duration-200 underline hover:no-underline
             md:font-body1-tablet
             lg:font-body1-laptop
             xl:font-body1-desktop"
      :style="{
        borderColor: isHovered ? colors.suite42Blue : 'transparent',
        backgroundColor: isHovered ? colors.suite42Blue : 'transparent',
        color: isHovered ? colors.suite42Black : colors[smallBlueButtonProps.hoverTextColor]
      }"
      @click="emit('click')"
      @mouseleave="isHovered = false"
      @mouseover="isHovered = true"
    >
      {{ smallBlueButtonProps.text }}
    </button>
  </div>
</template>
