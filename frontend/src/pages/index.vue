<script setup lang="ts">
  definePage({
    meta: { layout: 'default' },
  })
</script>

<template>
  <div
    class="w-full px-6
          md:px-10
          lg:px-14
          xl:px-18
          2xl:px-28"
  >
    <IndexSections />
    <ToolkitSections />
    <SingleConnector color="suite42Red" :height="16" />
    <AboutSections />
    <TechSections />
  </div>
</template>
