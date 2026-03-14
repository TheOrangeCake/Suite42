<script setup lang="ts">
definePage({
  meta: { layout: 'default' },
})
</script>

<template>
  <div class="page">
    <IndexSections />
    <ToolkitSections />
    <div class="dark-bg">
      <AboutSections />
    </div>
    <TechSections />
  </div>
</template>

<style scoped>
.page {
  padding: 30px 0;
  width: 100%;
}

.dark-bg {
  background-color: #1a1a1a;
  width: 100%;
}

@media (max-width: 868px) {
  .page {
    padding: 0;
  }
}
</style>
