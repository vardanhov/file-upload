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
                accept=".py"
                label="Выберите файл для сохранения в папку с Dags"
                color="grey"
                v-if="radios=='Dag'"
                small-chips
                show-size
                truncate-length="15"
            >
            </v-file-input>
            <v-col cols="12" sm="1">
              <v-btn text class="modal-btn" color="primary" v-if="radios=='Dag'" @click="submitFile()"
                     depressed
                     :disabled="fileDisableBtn()"
              >
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
                accept=".py, .jar, .zip"
                label="Несколько файлов для сохранения в папку с scripts"
                color="grey"
                v-if="radios=='Script'"
                multiple="true"
                small-chips
                show-size
                truncate-length="15"
            >
            </v-file-input>
             <v-col cols="12" sm="1">
                       <v-btn text class="modal-btn" color="primary" v-if="radios=='Script'" v-on:click="isHidden = !isHidden" @click="submitFiles()"
                              depressed
                              :disabled="filesDisableBtn()"
                       >

                <v-icon color="primary">mdi-upload-multiple</v-icon>
              </v-btn>
            </v-col>
          </v-row>

        </v-col>
      </v-row>

      <v-row
          class="fill-height"
          align-content="center"
          justify="center"
      >
        <v-col cols="6">
          <v-progress-linear
              color="blue darken-2"
              indeterminate
              rounded
              height="6"
              v-if="!isHidden"
          ></v-progress-linear>
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
      contentTypes: '.py',
      fileEmpty: true,
      filesEmpty: true,
      isHidden: true
    }
  },
  components: {},
  mounted() {
    this.getAccessPermisson()
  },
//TODO вынести в store api
  methods: {
    getAccessPermisson() {
      axios.get('/api/upload/check-access').catch(function (error) {
        self.handleEditError(error.response.data, "black")
      });
    },

    fileDisableBtn(){
      if (this.files!=null) {
        this.fileEmpty = !(this.files.size > 0);
      }
      else {this.fileEmpty=true;}
      console.log("method start");
      console.log(this.fileEmpty)
      return this.fileEmpty;
    },
    filesDisableBtn(){
      this.filesEmpty = !(this.files.length > 0);
      return (this.filesEmpty)||this.folder===''||this.folder==null;
    },
    submitFiles() {
      var self = this;
      let formData = new FormData();

      if (this.folder===''||this.folder==null){
        self.handleEditError("Имя папки не должо быть пустым", "deep-orange accent-3")
        return;
      }

      for (const i in self.files) {
        const fileSize = self.files[i].size;
        if (fileSize > 10485760) {
          self.handleEditError("Превышен допустимый размер файла-" + (fileSize/1000000).toFixed(2) + "Мб. Максимум 10Мб", "deep-orange accent-3")
          return;
        }

        formData.append('files', self.files[i]);
      }
      formData.append('path', self.folder);
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
        self.handleEditError("загружен", "light-green accent-4")      })
          .catch(function (error) {
            self.handleEditError(error.response.data, "black")
          }).then(function () {
        self.dialog = false;
        self.files = {};
        self.folder = '';
      });
    },
    submitFile() {
      var self = this;
      let formData = new FormData();

      const fileSize = self.files.size;

      if (self.files.name.split('.').pop()!=='py'){
        self.handleEditError("Недопустимое содержимое файла. Выберите файл \"*.py\" и повторите отправку.", "deep-orange accent-3")
        return;
      }
      if (fileSize > 10485760) {
        self.handleEditError("Превышен допустимый размер файла - " + (fileSize/1000000).toFixed(2) + "Мб. Максимум 10Мб", "deep-orange accent-3")
        return;
      }

      formData.append('files', self.files);
      formData.append('path', '');

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
        self.handleEditError("Успешно", "light-green accent-4")
      })
          .catch(function (error) {
            self.handleEditError(error.response.data, "black")
          }).then(function () {
        self.dialog = false;
        self.files = {};
      });
    },

    handleEditError(response, color ){
      this.snackbar=true;
      this.snackbarText=response;
      this.color=color;
      this.isHidden=true;
    }
  }
}
</script>
<style>
</style>
