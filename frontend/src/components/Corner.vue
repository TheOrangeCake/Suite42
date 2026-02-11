<script setup lang="ts">
import { computed } from 'vue'

const props = defineProps({
  verticalLength: {
    type: Number,
    required: true
  },
  horizontalLength: {
    type: Number,
    required: true
  },
  color: {
    type: String,
    required: true
  },
  thickness: {
    type: Number,
    default: 3
  }
})

const verticalStyle = computed(() => ({
  height: props.verticalLength + 'px',
  width: props.thickness + 'px',
  backgroundColor: props.color
}))

const horizontalStyle = computed(() => ({
  width: props.horizontalLength + 'px',
  height: props.thickness + 'px',
  backgroundColor: props.color,
  top: props.verticalLength - props.thickness + 'px'
}))
</script>

<template>
  <div class="corner">
    <div class="vertical" :style="verticalStyle"></div>
    <div class="horizontal" :style="horizontalStyle"></div>
  </div>
</template>

<style scoped>
.corner {
  position: relative;
}

.vertical {
  position: absolute;
  left: 0;
  top: 0;
}

.horizontal {
  position: absolute;
  left: 0;
}
</style>

