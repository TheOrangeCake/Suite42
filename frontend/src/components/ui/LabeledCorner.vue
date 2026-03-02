<script setup lang="ts">
import Corner from '@/components/ui/Corner.vue'

const props = defineProps({
  text: { type: String, required: true },

  // style du texte
  variant: { type: String, default: 'title' }, // 'title' | 'subtitle'

  // corner
  vLen: { type: Number, default: 80 },
  hLen: { type: Number, default: 35 },
  thickness: { type: Number, default: 3 },
  color: { type: String, default: 'var(--color-red)' },

  // placement
  cornerMarginLeft: { type: Number, default: 150 },
  textMarginLeft: { type: Number, default: 130 }
})
</script>

<template>
  <div class="row">
    <Corner
      :verticalLength="vLen"
      :horizontalLength="hLen"
      :thickness="thickness"
      :color="color"
      :style="{ marginLeft: cornerMarginLeft + 'px' }"
    />

    <p :class="['text', variant]" :style="{ marginLeft: textMarginLeft + 'px' }">
      {{ text }}
    </p>
  </div>
</template>

<style scoped>
.row {
  display: flex;
  align-items: flex-start;
  gap: 16px;
}

/* base */
.text {
  font-family: 'Inter', sans-serif;
  color: #222222;
  margin: 0;
}

/* variantes */
.title {
  font-size: clamp(3rem, 6vw, 5rem);
  font-weight: 700;
  line-height: 1.1;
  letter-spacing: 2px;
}

.subtitle {
  font-size: 42px;
  font-weight: 400;
}
</style>

