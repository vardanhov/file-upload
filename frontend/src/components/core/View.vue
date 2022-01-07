<template>
  <v-main>
    <br/>
    <br/>
    <bar/>
    <router-view/>
    <v-t-b-footer/>
  </v-main>
</template>

<script>
import Bar from "./AppBar.vue";
import VTBFooter from "./VTBFooter.vue";


export default {
  name: 'CoreView',
  components: {
    VTBFooter,
    Bar: Bar,
  },
}
</script>
<style></style>