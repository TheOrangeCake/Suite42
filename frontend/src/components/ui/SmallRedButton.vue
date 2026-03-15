<script setup lang="ts">
  import { type Colors, colors } from '@/styles/Colors.ts'

  const smallRedButtonProps = withDefaults(
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
  <div class="flex justify-center items-center">
    <button
      class="flex px-6 py-2 justify-center items-center rounded border-2 font-medium font-body1-mobile transition-colors duration-200
             md:font-body1-mobile
             lg:font-body1-tablet
             xl:font-body1-desktop"
      :style="{
        borderColor: colors.suite42Red,
        backgroundColor: isHovered ? colors.suite42Red : 'transparent',
        color: isHovered ? colors.suite42White : colors[smallRedButtonProps.hoverTextColor]
      }"
      @click="emit('click')"
      @mouseleave="isHovered = false"
      @mouseover="isHovered = true"
    >
      {{ smallRedButtonProps.text }}
    </button>
  </div>
</template>
