<script setup lang="ts">
import Corner from '../../ui/Corner.vue'
</script>

<template>
  <section class="container2">

    <div class="title">
      <h1 class="headline">Our toolkit</h1>
    </div>

    <div class="sndcontainer">

      <div class="col col1" aria-hidden="true">
        <Corner :vSize="2150" :hSize="3" :thickness="5" color="var(--color-red)" />
      </div>

      <div class="col col2" aria-hidden="true">
        <div class="sectionCorner">

          <Corner :vSize="55" :hSize="35" :thickness="5" color="var(--color-turquoise)" />

          <Corner :vSize="55" :hSize="35" :thickness="5" color="white" />
          <Corner :vSize="590" :hSize="20" :thickness="5" color="var(--color-green)" />
        </div>
        <div class="sectionCorner">
          <Corner :vSize="690" :hSize="20" :thickness="5" color="var(--color-coral, #FF5959)" />
        </div>
        <div class="sectionCorner">
          <Corner :vSize="690" :hSize="20" :thickness="5" color="var(--Black, #202020)" />
        </div>
      </div>

      <div class="toolsList">

        <div class="toolItem">
          <div class="itemBody">
            <h3>Teammates finder</h3>
            <div class="descRow">
              <Corner :vSize="95" :hSize="35" :thickness="5" color="var(--color-turquoise)" />
              <p>Quickly search for compatible teammates and remove the friction of forming project groups.</p>
            </div>
            <div class="mediaRow">
              <Corner :vSize="300" :hSize="35" :thickness="5" color="var(--color-turquoise)" />
              <img src="/design/assets/placeholder/tool_section.jpg" alt="" class="illustration" aria-hidden="true" />
            </div>
          </div>
        </div>

        <div class="toolItem">
          <div class="itemCornerCol" aria-hidden="true">
          </div>
          <div class="itemBody">
            <h3>AI project helper <span class="tag">(coming soon)</span></h3>
            <div class="descRow">
              <Corner :vSize="95" :hSize="35" :thickness="5" color="var(--color-turquoise)" />
              <p>Get structured guidance and explanations aligned with your current 42 project—without giving direct answers.</p>
            </div>
            <div class="mediaRow">
              <Corner :vSize="300" :hSize="35" :thickness="5" color="var(--color-turquoise)" />
              <img src="/design/assets/placeholder/tool_section.jpg" alt="" class="illustration" aria-hidden="true" />
            </div>
          </div>
        </div>

        <div class="toolItem">
          <div class="itemCornerCol" aria-hidden="true">
          </div>
          <div class="itemBody">
            <h3>To do list <span class="tag">(coming soon)</span></h3>
            <div class="descRow">
              <Corner :vSize="95" :hSize="35" :thickness="5" color="var(--color-coral, #FF5959)" />
              <p>Plan your tasks, follow your progress, and maintain momentum across your 42 projects.</p>
            </div>
            <div class="mediaRow">
              <Corner :vSize="300" :hSize="35" :thickness="5" color="var(--color-coral, #FF5959)" />
              <img src="/design/assets/placeholder/tool_section.jpg" alt="" class="illustration" aria-hidden="true" />
            </div>
          </div>
        </div>

        <div class="ctaRow">
          <div class="itemCornerCol" aria-hidden="true">
          </div>
          <button class="ctaBtn">Let's start working !</button>
        </div>

      </div>

    </div>

  </section>
</template>

<style scoped>
* { box-sizing: border-box; }
h3, p { margin: 0; }

h3 {
  font-size: clamp(1.1rem, 3.5vw, 2.4rem);
  font-weight: 700;
  color: #202020;
  line-height: 1.1;

  font-family: monda;
}
.tag {
  font-weight: 400;
  font-size: 0.8em;
  opacity: 0.6;

  font-family: monda;
}
p {
  font-size: clamp(0.9rem, 1.4vw, 1rem);
  line-height: 1.5;
  color: #333;
  font-family: monda;
}

/* ── Container ──────────────────────────────────── */
.container2 {
  width: 100%;
  position: relative;
  padding-left: 5%;
  padding-right: 5%;
margin-top: -3vw;
}
.container2 > * {

}

.title {
  display: flex;
  align-items: flex-start;
  gap: 20px;
  padding-left: 40px;
}
.titleCorner { flex: 0 0 auto; }
.headline {
  display: flex;
  align-self: start;
  color: #202020;
  font-family: Monda, system-ui, sans-serif;
  font-weight: 700;
  font-size: clamp(1.5rem, 5vw, 66px);
  line-height: 1.05;
  letter-spacing: -0.04em;
}
.sndcontainer {
  display: flex;
  flex-direction: row;
  align-items: stretch;
  gap: 0;


}

.col1 {
  overflow: hidden;

}

.col2 {
  flex: 0 0 auto;
  display: flex;
  flex-direction: column;
  margin-left: 30px;
  gap: 20px;
}
.sectionCorner {
  flex: 0 0 auto;

}

.toolsList {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: clamp(40px, 6vw, 80px);
  padding: 40px 40px 0 40px;
  /* border: 3px solid black; */
}

.toolItem {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 20px;
}
.itemCornerCol {
  flex: 0 0 auto;
  padding-top: 6px;
}
.itemBody {
  flex: 1;
  min-width: 0;
  display: flex;
  flex-direction: column;
  gap: 18px;
}

.descRow {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
  gap: 14px;
  max-width: 46%;
}
.descRow p { flex: 1; min-width: 0; }

.mediaRow {
  display: flex;
  flex-direction: row;
  align-items: flex-start;
  gap: 16px;
}
.illustration {
  flex: 1;
  min-width: 0;
  max-width: 640px;
  width: 100%;
  height: auto;
  display: block;
  object-fit: contain;
  border-radius: 8px;
  margin-left: auto;
}

/* ── CTA ────────────────────────────────────────── */
.ctaRow {
  display: flex;
  flex-direction: row;
  align-items: center;
  gap: 20px;
}
.ctaBtn {
  background-color: #FF5959;
  color: white;
  border: none;
  border-radius: 4px;
  padding: 14px 40px;
  font-size: clamp(0.85rem, 1.8vw, 1.1rem);
  font-weight: 600;
  cursor: pointer;
}
.ctaBtn:hover { opacity: 0.85; }

@media (max-width: 768px) {
  .col2 { display: none; }
  .toolsList { padding: 0 16px 0 20px; }
  .descRow { max-width: 100%; }
  .illustration { max-width: 100%; }
}
</style>
