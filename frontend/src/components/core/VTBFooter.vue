<template>
    <v-footer
            color="secondary"
            app
    >
        <v-spacer/>
        <div class="blue-grey--text">{{localeDate}}</div>

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

       // beforeDestroy() {
       //     if (this.intervalId) clearInterval(this.intervalId)
      //  },
    }
</script>

<style scoped>

</style>