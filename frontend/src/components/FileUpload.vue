<template>
  <v-main>
    <v-container
        class="fill-height"
        fluid
        style="animation: frames(5)"
    >
      <v-row></v-row>

      <v-row dense>
        <v-col cols="12" sm="3"></v-col>
        <v-col cols="12" sm="6">


          <div style="font-size: 20px; color: dodgerblue" class="font-weight-black">Выберите, что вы хотите загрузить</div>
          <div class="pt-3"></div>

          <v-expansion-panels focusable>
            <v-expansion-panel>
              <v-col cols="120" sm="1000">
                <v-expansion-panel-header style="font-size: 16px; color: #666666" class="font-weight-medium">Даг</v-expansion-panel-header>
                <v-expansion-panel-content>
                  <v-file-input
                      clearable
                      v-model="filesOne"
                      accept=".py"
                      label="Выберите файл для сохранения в папку с Dags"
                      color="grey"
                      small-chips
                      show-size
                      truncate-length="15"
                  >
                  </v-file-input>
                  <v-col cols="12" sm="1">
                    <v-btn text class="modal-btn" color="primary" @click="submitFile()"
                           depressed
                           :disabled="fileDisableBtn()"
                    >
                      <v-icon color="primary">mdi-file-upload</v-icon>
                    </v-btn>
                  </v-col>
                </v-expansion-panel-content>
              </v-col>
            </v-expansion-panel>

            <v-expansion-panel>
              <v-col cols="120" sm="1000">
                <v-expansion-panel-header style="font-size: 16px; color: #666666" class="font-weight-medium">Скрипты и другие файлы, содержащие конфеденциальные данные</v-expansion-panel-header>
                <v-expansion-panel-content class="pt-3">
                  <v-text-field
                      autofocus
                      v-model="folder"
                      label="Директория"
                      placeholder="Укажите имя директории для сохранения файлов"
                      outlined
                      clearable
                      counter="100"
                  ></v-text-field>

                  <v-file-input
                      clearable
                      v-model="files"
                      accept=".py, .jar, .zip"
                      label="Несколько файлов для сохранения в папку с scripts"
                      color="grey"
                      multiple="true"
                      small-chips
                      show-size
                      truncate-length="15"
                  >
                  </v-file-input>
                  <v-col cols="12" sm="1">
                    <v-btn text class="modal-btn" color="primary" v-on:click="isHidden = !isHidden" @click="submitFiles()"
                           depressed
                           :disabled="filesDisableBtn()"
                    >

                      <v-icon color="primary">mdi-upload-multiple</v-icon>
                    </v-btn>
                  </v-col>
                </v-expansion-panel-content>
              </v-col>
            </v-expansion-panel>
          </v-expansion-panels>
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
    <v-card v-if="hasAccess">
      <v-card-text>Недостаточно прав</v-card-text>
    </v-card>
  </v-main>
</template>

<script>
import axios from "axios";

export default {
  name: "FileUpload",
  data() {
    return {
      filesOne: {},
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
      isHidden: true,
      hasAccess: false
    }
  },
  components: {},
  mounted() {
    this.getAccessPermisson()
  },
//TODO вынести в store api
  methods: {
    getAccessPermisson() {
      var self = this;
      axios.get('/api/upload/check-access').catch(function (error) {
        self.handleEditError(error.response.data, "black").then(function(){
          self.hasAccess=response;
        })
      });
    },

    fileDisableBtn(){
      if (this.filesOne!=null) {
        // this.fileEmpty = !(this.filesOne.size > 0);
        if (this.filesOne.size > 0) {
          if (this.filesOne.name.split('.').pop()!=='py'){this.fileEmpty=true;}
          else this.fileEmpty=false;
        }
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
        if (fileSize > 157286400) {
          self.handleEditError("Размер файла "+ self.files[i].name + " " + (fileSize/1000000).toFixed(2) + "Мб. Максимум 150Мб", "deep-orange accent-3")
          return;
        }
        else{formData.append('files', self.files[i]);}

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
        self.handleEditError("Файлы загружены", "light-green accent-4")      })
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

      const fileSize = self.filesOne.size;

      if (self.filesOne.name.split('.').pop()!=='py'){
        self.handleEditError("Недопустимое содержимое файла. Выберите файл \"*.py\" и повторите отправку.", "deep-orange accent-3")
        return;
      }
      if (fileSize > 157286400) {
        self.handleEditError("Размер файла "+ self.filesOne.name + " " +  (fileSize/1000000).toFixed(2) + "Мб. Максимум 150Мб", "deep-orange accent-3")
        return;
      }

      formData.append('files', self.filesOne);
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
        self.filesOne = {};
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
