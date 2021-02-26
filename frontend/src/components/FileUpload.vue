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
            <v-radio value="Dag" @click="files={}"

            >
              <template v-slot:label>
                <div style="font-size: 14px">Даг</div>
              </template>
            </v-radio>
            <v-radio value="Script" @click="files={}">
              <template v-slot:label>
                <div style="font-size: 14px">
                  Скрипты и другие файлы, <p>содержащие конфеденциальные данные</p></div>
              </template>
            </v-radio>
          </v-radio-group>
          <v-row>
            <v-file-input
                clearable
                v-model="files"
                :accept=contentTypes
                label="Выберите файл для сохранения в папку с Dags"
                color="grey"
                v-if="radios=='Dag'"
                small-chips
                show-size
                truncate-length="15"
            >
            </v-file-input>
            <v-col cols="12" sm="1">
              <v-btn text class="modal-btn" color="primary" v-if="radios=='Dag'" @click="submitFile()">
                <v-icon color="primary">mdi-file-upload</v-icon>
              </v-btn>
            </v-col>
          </v-row>
          <br/>
          <v-text-field
              autofocus
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
                :accept=contentTypes
                label="Несколько файлов для сохранения в папку с scripts"
                color="grey"
                v-if="radios=='Script'"
                multiple="true"
                small-chips
                small-chips
                show-size
                truncate-length="15"
            >
            </v-file-input>
            <v-col cols="12" sm="1">
              <v-btn text class="modal-btn" color="primary" v-if="radios=='Script'" @click="submitFiles()"
                     depressed
                     :disabled="folder==''"
              >
                <v-icon color="primary">mdi-upload-multiple</v-icon>
              </v-btn>
            </v-col>
          </v-row>

        </v-col>
      </v-row>

    </v-container>
    <template>
      <div class="text-center ma-2">
        <v-snackbar
            :color=color
            v-model="snackbar"
            @mousemove="snackbarText=false"
        >
          {{ snackbarText }}

          <template v-slot:action="{ attrs }">
            <v-btn
                color="red"
                text
                v-bind="attrs"
                @click="snackbar = false"
            >
              <v-icon>mdi-window-close</v-icon>
            </v-btn>
          </template>
        </v-snackbar>
      </div>
    </template>
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
      folder: '',
      snackbar: false,
      snackbarText: '',
      color: '',
      contentTypes: '.py'
    }
  },
  components: {},
  mounted() {
    this.getContentTypes()
  },
//TODO вынести в store api
  methods: {
    getContentTypes() {
      axios.get('/api/upload/contentType').then(function (response) {
        self.contentTypes = response.data.content
      });
    },

    submitFiles() {
      var self = this;
      let formData = new FormData();
      for (var i in self.files) {
        formData.append('files', self.files[i]);
      }

      var headers = {
        'Content-Type': 'multipart/form-data',
      };
      axios.post('/api/upload',
          formData,
          {
            headers: {
              headers
            }
          }
      ).then(function () {
        self.handleEditError("загружен", "green")
      })
          .catch(function (error) {
            self.handleEditError(error.response.data, "black")
          }).then(function () {
        self.dialog = false;
        self.files = {};
      });
    },
    submitFile() {
      var self = this;
      let formData = new FormData();

      formData.append('files', self.files);

      var headers = {
        'Content-Type': 'multipart/form-data',
      };
      axios.post('/api/upload',
          formData,
          {
            headers: {
              headers
            }
          }
      ).then(function () {
        self.handleEditError("Успешно", "green")
      })
          .catch(function (error) {
            self.handleEditError(error.response.data, "black")
          }).then(function () {
        self.dialog = false;
        self.files = {};
      });
    },

    handleEditError(response, color) {
      this.snackbar = true;
      this.snackbarText = response;
      this.color = color;
    }
  }
}
</script>
<style>
</style>
