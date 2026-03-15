<script setup lang="ts">
  import { viewportValue } from '@/composables/viewportsValue'

  const connectorHeight1 = viewportValue({
    mobile: 2,
    tablet: 2.2,
    laptop: 2,
    desktop: 3.5,
  })
  const connectorHeight2 = viewportValue({
    mobile: 1,
    tablet: 1.1,
    laptop: 1,
    desktop: 1.1,
  })
</script>

<template>
  <section class="flex flex-col">
    <SingleConnector color="suite42Blue" :height="4" />
    <div class="flex flex-row">
      <ConnectConnector color1="suite42Blue" color2="suite42Green" :height="connectorHeight1" />
      <h1
        class="font-bold leading-15 font-hero-mobile
                md:font-hero-tablet md:leading-20
                lg:font-hero-laptop lg:leading-25
                xl:font-hero-desktop xl:leading-30 xl:w-3/4"
      >A practical companion for the 42 journey</h1>
    </div>
    <DoubleConnectors color1="suite42Green" color2="suite42Blue" :height="3" />
    <div class="flex flex-row items-stretch">
      <SingleConnectionConnector color1="suite42Green" color2="suite42Blue" color3="suite42Blue" :height="connectorHeight2" />
      <h4
        class="font-regular font-h5-mobile
                md:font-h5-tablet
                lg:font-h5-laptop
                xl:font-h5-desktop"
      >An unofficial collection of tools for learning and collaboration dedicated to 42 students.</h4>
    </div>
    <div
      class="flex flex-row gap-3
             md:gap-4"
    >
      <SingleConnector color="suite42Green" />
      <div class="flex flex-row flex-wrap w-full">
        <EndConnector color="suite42Blue" :height="9.5" />
        <div class="pt-32">
          <BigRedButton path="/login" text="Start your 42 journey here" />
        </div>
        <div class="flex grow justify-end">
          <img alt="" class="w-75 md:w-100 lg:w-130 xl:w-200 transform scale-x-[-1]" src="/design/assets/images/homepage_illustration.png">
        </div>
      </div>
    </div>
  </section>
</template>
