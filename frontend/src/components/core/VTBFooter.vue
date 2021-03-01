<template>
  <v-footer
      color="secondary"
      app
  >
    <v-spacer/>
    <div style="font-size: 16px; color: #666666" class="font-weight-black">{{localeDate}}</div>

    <!--span class="blue-grey--text">ВТБ 2021</span-->
  </v-footer>
</template>

<script>
export default {
  name: "VTBFooter",

  data: () => ({
    date: 1614184739,
  }),

  computed: {
    localeDate() {
      return (new Date(this.date)).toLocaleDateString() + " " + (new Date(this.date)).toLocaleTimeString()
    },
  },

  created() {
    this.intervalId = setInterval(() => this.date = Date.now(), 100);
  },
}
</script>

<style scoped>

</style>