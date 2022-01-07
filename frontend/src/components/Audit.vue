<template>
  <v-main>
    <v-card flat>
      <br/>
      <br/>
    <v-btn text color="primary" @click="getUserLogs()" >Загрузить полный лог пользователей</v-btn>
    <v-btn text color="primary" @click="getAuditLogs()" >Загрузить полный лог администратора</v-btn>
    </v-card>
  </v-main>
</template>

<script>
import axios from "axios";
export default {
  name: "Audit",

  methods: {
    getAuditLogs() {
      this.snackbar = true;
      this.snackbarText = "Лог загружен";

      axios.get('/api/users/get-audit-logs', {responseType: 'blob'})
      .then(response => {
           var blob = new Blob([response.data]);
           var link = document.createElement('a');
           link.setAttribute("download", "full_audit_data.log");
           link.href = window.URL.createObjectURL(blob);
           link.click();
      })
      .catch(error => {
          this.handleEditError('Невозможно получить файл' + error, 'red')
      });
    },

    getUserLogs() {
      this.snackbar = true;
      this.snackbarText = "Лог загружен";

      axios.get('/api/users/get-user-logs', {responseType: 'blob'})
      .then(response => {
           var blob = new Blob([response.data]);
           var link = document.createElement('a');
           link.setAttribute("download", "full_user_data.log");
           link.href = window.URL.createObjectURL(blob);
           link.click();
      })
      .catch(error => {
          this.handleEditError('Невозможно получить файл' + error, 'red')
      });
    }
  }
}
</script>

<style>
</style>