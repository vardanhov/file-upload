<template>
    <v-main>
      <v-container
          class="fill-height"
          fluid
          style="animation: frames(5)"
      >
        <v-col></v-col>
        <v-col>
          <v-file-input
              v-model="file"
              autofocus
              accept=".py"
              label="Выберите файл с расширением .py"
              color="grey"
          >
          </v-file-input>
        </v-col>
        <v-col>
          <v-layout row justify-left>
            <v-dialog v-model="dialog" persistent max-width="500">
              <template v-slot:activator="{ on }">
                <v-btn text class="modal-btn" color="primary" v-on="on">
                  <v-icon color="primary">mdi-file-upload</v-icon>
                </v-btn>
              </template>
              <v-card>
                <v-container>
                <v-row>
                <v-card-title class="headline" style="color: gray">Отправка файла</v-card-title>
                <v-spacer></v-spacer>
                  <v-btn text @click="dialog = false">
                  <v-icon color="red">mdi-window-close</v-icon>
                </v-btn>
                </v-row>
                  </v-container>
                <v-card-text>
                  <p>Содержит ли Ваш файл конфиденциальную информацию?</p>
                  <v-divider></v-divider>
                  <p style="font-size: 12px">Файлы с конфиденциальной информацией будут сохранены в папке <b>scripts</b> и будут
                    <span style="color: red"><b>не доступны</b></span> для запуска как dag airflow</p>
                </v-card-text>
                <v-card-actions>
                  <v-container>
                    <v-layout row justify-center>
                      <v-btn left color="accent" flat @click="submitFile(false)">Не содержит</v-btn>
                      &nbsp;
                      &nbsp;
                      &nbsp;
                      <v-btn color="accent" flat @click="submitFile(true)">Содержит</v-btn>
                    </v-layout>
                  </v-container>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </v-layout>
        </v-col>
      </v-container>
    </v-main>
</template>

<script>
import axios from "axios";

export default {
  name: "FileUpload",
  data() {
    return {
      file: '',
      dialog: false,
    }
  },
  components: {},
//TODO вынести в store api
  methods: {
    submitFile(isConfidential) {
      var self = this;
      let formData = new FormData();
      formData.append('file', this.file);
      formData.append('confidential', isConfidential);
      axios.post('/api/upload',
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }
      ).then(function (response) {
        console.log('SUCCESS!!');
      })
          .catch(function () {
            console.log('FAILURE!!');
          }).then(function (){
            self.dialog=false;
      });
    }
  }
}
</script>
<style>
</style>
