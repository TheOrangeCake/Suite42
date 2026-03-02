<template>
  <div class="page">
    <IndexSections/>
    <ToolkitSections/>
    <div class="dark-bg">
      <AboutSections/>
    </div>

      <TechSections/>
  </div>
</template>

<style scoped>
.dark-bg {
  background-color: #1a1a1a;
  width: 100%;

}
.page {
    padding: 30px 0px; /* ← garde le padding haut/bas, retire gauche/droite */
  width: 100%;
}


@media (max-width: 868px ){
  .page {
      padding: 0px;
  }
}
</style>
  <script>
    import TechSections from '../components/sections/IndexSections/TechSections.vue'
    import IndexSections from '../components/sections/IndexSections/IndexSections.vue'
    import ToolkitSections from '../components/sections/IndexSections/ToolkitSections.vue'
    import AboutSections from '../components/sections/IndexSections/AboutSections.vue'
  </script>

