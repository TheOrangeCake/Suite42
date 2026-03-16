<script setup lang="ts">
  import { colors } from '@/styles/Colors.ts'

  const props = defineProps<{
    label: string
    modelValue: string
    type?: string
    placeholder?: string
  }>()

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string): void
  }>()
</script>

<template>
  <div class="relative w-full">
    <label
      class="absolute -top-3 left-5 px-3 font-regular font-body2-mobile
                md:font-body2-tablet
                lg:font-body2-laptop
                xl:font-body2-desktop"
      :style="{ backgroundColor: colors.suite42Background, color: colors.suite42Darkgrey }"
    >
      {{ props.label }}
    </label>
    <input
      class="w-full border border-(--border-color-default) rounded px-3 py-3 outline-none focus:border-(--border-color) font-regular font-h5-mobile max-w-md
        md:font-h5-tablet
        lg:font-h5-laptop
        xl:font-h5-desktop"
      :placeholder="props.placeholder"
      :style="{
        backgroundColor: colors.suite42Background,
        color: colors.suite42Black,
        '--border-color-default': colors.suite42Grey,
        '--border-color': colors.suite42Blue
      }"
      :type="props.type ?? 'text'"
      :value="props.modelValue"
      @input="emit('update:modelValue', ($event.target as HTMLInputElement).value)"
    >
  </div>
</template>
