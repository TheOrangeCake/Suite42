<script>
  import Corner from '../../ui/Corner.vue'
  </script>

  <template>
    <section class="container">
      <div class="first">
        <div class="column">
          <Corner :vSize="60" :hSize="25" color="var(--color-turquoise)" :thickness="4"/>
          <Corner :vSize="360" :hSize="25" color="var(--color-green)" :thickness="4"/>
        </div>
        <div class="section1">
          <p> A practical Company for 42 journey ! </p>
          <div class="section2">
            <Corner :vSize="35" :hSize="25" color="var(--color-turquoise)" :thickness="4"/>
            <p class="text"> An unofficial collection of tools for learning and collaboration dedicated to 42 students</p>
          </div>

          <div class="buttonWrap">
            <Corner :vSize="95" :hSize="25" color="var(--color-turquoise)" :thickness="4"/>
            <button> Start your 42 journey here </button>
          </div>

            <img class= "heroImg" src="/design/assets/images/homepage_illustration.png">
        </div>

      </div>

    </section>
  </template>
<style>

.heroImg {
  position: absolute;
  bottom: 0;
  right: 0;
  width: clamp(240px, 30vw, 520px);
  height: auto;
  margin-top: 20px;

}
.buttonWrap {
  display: flex;
  align-items: end;   /* aligne Corner + bouton sur la même ligne */
  gap: 12px;             /* espace entre Corner et bouton */
  width: 100%;

}

button {
  padding: 10px 10px;
  background-color: var(--color-red);
  font-family: monda;
  color: var(--color-white);
}
.text {
  padding-top: 20px;
  font-size: 15px;
  font-weight: normal;
  padding-left: 10px;
}
.section2 {
  display: flex;
}
.section1 {
  position: relative;
  display: flex;
  flex-direction: column;
  padding-left: 20px;
}
.column {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.first {
  display: flex;
}
p {
  display: inline-block;
  max-width: 100%;
  color: #202020;
  font-family: monda;
  font-size: clamp(24px, 5vw, 50px);
  font-weight: bold;
}
.container {
  padding-left: 5%;
  padding-top: 3%;
}
</style>
