<script setup lang="ts">
  import type { Colors } from '@/styles/Colors.ts'

  const tripleConnectorsProps = defineProps<{
    height: number
    color1: Colors
    color2: Colors
    color3: Colors
  }>()
</script>

<template>
  <div
    class="flex flex-row gap-3
           md:gap-4"
  >
    <SingleConnector :color="tripleConnectorsProps.color1" :height="tripleConnectorsProps.height" />
    <SingleConnector :color="tripleConnectorsProps.color2" :height="tripleConnectorsProps.height" />
    <SingleConnector :color="tripleConnectorsProps.color3" :height="tripleConnectorsProps.height" />
  </div>
</template>
