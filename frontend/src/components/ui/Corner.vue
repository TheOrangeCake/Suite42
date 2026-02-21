<script setup lang="ts">
const props = withDefaults(
  defineProps<{
    vSize: number
   hSize: number
    color: string
    thickness?: number
  }>(),
  { thickness: 3 }
)
</script>

<template>
  <svg
    :width="hSize"
    :height="vSize"
    :viewBox="`0 0 ${hSize} ${vSize}`"
    fill="none"
    class="cornerSetup"
    aria-hidden="true"
  >
    <path
      :d="`M 0 0 V ${vSize} H ${hSize}`"
      :stroke="color"
      :stroke-width="thickness"
      stroke-linecap="square"
      stroke-linejoin="miter"
    />
  </svg>
</template>

<style scoped>
.cornerSetup {
  flex-shrink: 0;
  display: block;
}
</style>

