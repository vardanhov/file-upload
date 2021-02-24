<template>
  <v-main>
    <v-container
        class="fill-height"
        fluid
        style="animation: frames(5)"
    >
      <v-row>
        <v-col cols="12" sm="3"></v-col>
        <v-col cols="12" sm="6">
          <v-radio-group v-model="radios">
            <template v-slot:label>
              <div style="font-size: 20px; color: dodgerblue">Выберите, что вы хотите загрузить</div>
            </template>
            <v-divider></v-divider>
            <br/>
            <v-radio value="Dag" @click="files={}">
              <template v-slot:label>
                <div style="font-size: 14px">Даг</div>
                <v-btn text color="green">?</v-btn>
              </template>
            </v-radio>
            <v-radio value="Script" @click="files={}">
              <template v-slot:label>
                <div style="font-size: 14px">
                  Скрипты и другие файлы, <p>содержащие конфеденциальные данные</p></div>
                <v-btn text color="green">?</v-btn>
              </template>
            </v-radio>
          </v-radio-group>
          <v-row>
            <v-file-input
                v-model="files"
                accept=".py"
                label="Выберите файл для сохранения в папку с Dags"
                color="grey"
                v-if="radios=='Dag'"
                small-chips
            >
            </v-file-input>
            <v-col cols="12" sm="1">
              <v-btn text class="modal-btn" color="primary" v-if="radios=='Dag'" @click="submitFiles">
                <v-icon color="primary">mdi-file-upload</v-icon>
              </v-btn>
            </v-col>
          </v-row>
          <br/>
          <v-text-field
              v-model="folder"
              label="Директория"
              placeholder="Укажите имя директории для сохранения файлов"
              outlined
              v-if="radios=='Script'"
              clearable
              counter="100"
          ></v-text-field>
          <v-row>
            <v-file-input
                clearable
                v-model="files"
                accept=".py, .txt, .jar, .war, .zip"
                label="Несколько файлов для сохранения в папку с scripts"
                color="grey"
                v-if="radios=='Script'"
                multiple="true"
                small-chips
            >
            </v-file-input>
            <v-col cols="12" sm="1">
              <v-btn text class="modal-btn" color="primary" v-if="radios=='Script'" @click="submitFiles"
                     depressed
                     :disabled="folder==''">
                <v-icon color="primary">mdi-upload-multiple</v-icon>
              </v-btn>
            </v-col>
          </v-row>

        </v-col>
      </v-row>

    </v-container>
  </v-main>
</template>

<script>
import axios from "axios";

export default {
  name: "FileUpload",
  data() {
    return {
      files: {},
      script: '',
      dialog: false,
      radios: '',
      folder: ''
    }
  },
  components: {},
//TODO вынести в store api
  methods: {
    submitFiles() {
      var self = this;
      let formData = new FormData();
      for (var i in self.files) {
        formData.append('files[' + i + ']', self.files[i]);
      }
      axios.post('/api/upload',
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data'
            }
          }
      ).then(function () {
        console.log('SUCCESS!!');
      })
          .catch(function () {
            console.log('FAILURE!!');
          }).then(function () {
        self.dialog = false;
        self.files = {};
      });
    },
  }
}
</script>
<style>
</style>
