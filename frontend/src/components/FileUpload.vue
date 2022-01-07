<template>
  <v-main style="color:grey">
    <v-container
        class="fill-height"
        fluid
        style="animation: frames(5)"
        v-if="hasAccess"
    >
      <br/>
      <v-row dense>
        <div class="pt-3"></div>
        <v-row>
          <v-col cols="12" sm="3"></v-col>
          <v-col cols="12" sm="6">
            <div style="font-size: 20px; color: dodgerblue">Выберите, что Вы хотите загрузить</div>
            <br/>
            <v-expansion-panels focusable flat>
              <v-expansion-panel id="openDagInput"
                                 v-on:="folderHidden">
                <v-expansion-panel-header style="font-size: 16px; color: #666666" class="font-weight-medium">
                  Даг/родительский скрипт
                </v-expansion-panel-header>
                <br/>
                <v-expansion-panel-content>
                  <v-row>
                    <v-col cols="12" sm="10">
                      <v-file-input
                          id="dagInput"
                          v-model="dagFile"
                          autofocus
                          accept=".py"
                          label="Выберите файл для сохранения в папку с Dags"
                          color="grey"
                          small-chips
                          show-size
                          truncate-length="15"
                          clearable
                      >
                      </v-file-input>
                    </v-col>
                    <v-col cols="12" sm="2">
                      <br/>
                      <v-tooltip top color="#00C4FF">
                        <template v-slot:activator="{ on, attrs }">
                          <v-btn id="uploadFileButton" text class="modal-btn" color="primary"
                                 @click="submitFiles([dagFile], true,'')"
                                 :disabled="!checkFileType(dagFile) || isSubmitting"
                          >
                            <v-icon color="primary" v-on="on">mdi-file-upload</v-icon>
                          </v-btn>
                        </template>
                        <span>Загрузить</span>
                      </v-tooltip>
                    </v-col>
                  </v-row>
                </v-expansion-panel-content>
              </v-expansion-panel>
              <v-expansion-panel id="openScriptInput" @change="folderHidden=true" flat>
                <v-expansion-panel-header style="font-size: 16px; color: #666666" class="font-weight-medium">Скрипты и
                  другие файлы, содержащие конфеденциальные данные
                </v-expansion-panel-header>
                <v-expansion-panel-content class="pt-3">
                  <v-card
                      color="secondary"
                      dark
                      flat
                  >
                    <v-card-subtitle><span style="color: dimgray">Вы можете указать дополнительную поддиректорию для Ваших файлов. <li>По умолчанию файлы будут сохранены в
                      /app/airflow/scripts/"group_name".</li> <li>При указании директории в /app/airflow/scripts/"group_name"/"folder_name".</li>
                      Для указания директории нажмите кнопку "Указать директорию"
                    </span>
                    </v-card-subtitle>
                    <v-card-actions>
                      <v-btn id="setFolder" text v-on:click="folderHidden = !folderHidden"><span style="color:dimgrey">Указать директорию</span>
                      </v-btn>
                      <v-btn text v-on:click="folderHidden = !folderHidden; folder=''" v-if="!folderHidden"><span
                          style="color:dimgrey">Скрыть директорию</span></v-btn>
                    </v-card-actions>
                  </v-card>
                  <v-text-field id="folderNameInput" class="pt-3"
                                autofocus
                                v-model="folder"
                                label="Директория"
                                placeholder="Укажите имя директории для сохранения файлов"
                                outlined
                                clearable
                                counter="100"
                                v-if="!folderHidden"

                  ></v-text-field>

                  <v-row rows="3" sm="2">

                    <v-col cols="12" sm="10">
                      <v-file-input id="scriptInput" class="pt-6"
                                    clearable
                                    v-model="scriptFiles"
                                    accept=".py, .jar, .zip, .txt, .war"
                                    label="Несколько файлов для сохранения в папку scripts"
                                    color="grey"
                                    small-chips
                                    show-size
                                    truncate-length="15"
                      >
                      </v-file-input>
                    </v-col>
                    <v-col cols="12" sm="2">
                      <br/>
                      <v-btn id="uploadScriptButton" text class="modal-btn" color="primary" v-on:click="isHidden = !isHidden"
                             @click="submitFiles([scriptFiles], false, folder)"
                             depressed
                             :disabled="dialog || isSubmitting || scriptFiles===null"
                      >
                        <v-icon color="primary">mdi-upload-multiple</v-icon>
                      </v-btn>
                    </v-col>
                  </v-row>
                </v-expansion-panel-content>
              </v-expansion-panel>
            </v-expansion-panels>
          </v-col>
          <v-col cols="12" sm="2"></v-col>
        </v-row>
      </v-row>
      <v-dialog
          v-model="isSubmitting"
          hide-overlay
          persistent
          width="300"
      >
        <v-card
            color="accent4"
        >
          <v-card-text>Файл загружается
            <v-progress-linear
                color="blue darken-2"
                indeterminate
                rounded
                height="6"
                v-if="isSubmitting"
            ></v-progress-linear>
          </v-card-text>
        </v-card>
      </v-dialog>
    </v-container>
    <template>
      <div class="text-center ma-2">
        <v-snackbar
            id="messageBar"
            :color=color
            v-model="snackbar"
            @mousemove="snackbar=false"
            max-width="900"
            timeout="2000"
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
    <template>
      <div class="text-center">
        <v-dialog
            v-model="dialog"
            width="1000"
            v-if="dialog"
        >
          <v-card>
            <v-card-title class="headline grey lighten-4">
              Сохранение файла
            </v-card-title>
            <v-card-text>
              <br/>
              Файл с таким же именем уже существует {{ fileTmp.name }}

            </v-card-text>
            <v-card-text>Для сохранения файла необходимо выбрать, что Вы хотите сделать</v-card-text>
            <v-divider></v-divider>


            <v-card-actions>
              <v-btn-toggle color="blue">
                <v-row>
                  <v-col>
                    <v-btn
                        id="renameFileButton"
                        v-model="rename"
                        color="blue"
                        text
                        @click="rename=!rename"
                        :disabled="isSubmitting"
                    >
                      Переименовать
                    </v-btn>
                    <v-row>
                      <v-col cols="12" sm="10">
                        <v-text-field
                            id="newFileNameInput"
                            v-model="fileNameTmp"
                            :placeholder="fileTmp.name.split('.').shift()"
                            :suffix="fileType"
                            v-if="rename"
                            :autofocus="rename"
                            :counter="count"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="1">
                        <v-btn id="uploadRenamedFile" v-if="rename"
                               :disabled="disableSubmitBtn()"
                               @click="submitFile(fileTmp, isDag, false, defineNewFileNameWithExtension(),
                               _.isEmpty(folderNew) ? folder : folderNew)"
                               text>
                          <v-icon color="primary">mdi-file-upload</v-icon>
                        </v-btn>
                      </v-col>
                    </v-row>
                  </v-col>
                  <v-col>

                    <v-btn
                        id="rewriteFileButton"
                        text
                        color="blue"
                        @click="submitFile(fileTmp, isDag, true, '', _.isEmpty(folderNew) ?
                        folder : folderNew)"
                        dialog="false"
                        :disabled="!canOverride || isSubmitting"
                    >
                      Перезаписать
                    </v-btn>
                  </v-col>
                  <v-col>
                    <v-btn
                        id="renameFolderButton"
                        v-model="renameFolder"
                        color="blue"
                        text
                        @click="renameFolder=!renameFolder"
                        v-if="!isDag"
                        :disabled="isSubmitting"
                    >
                      Изменить директорию
                    </v-btn>
                    <v-row>
                      <v-col cols="12" sm="10">
                        <v-text-field
                            id="newFolderNameInput"
                            v-model="folderNew"
                            v-if="!isDag && renameFolder"
                            :placeholder="folder"
                            autofocus
                            :counter="count"
                        ></v-text-field>
                      </v-col>
                      <v-col cols="12" sm="1">
                        <v-btn id="uploadIntoRenamedFolder" v-if="!isDag && renameFolder"
                               :disabled="disableSubmitBtn()"
                               @click="submitFile(fileTmp,isDag, false, fileNameTmp, folderNew)" text>
                          <v-icon color="blue">mdi-file-upload</v-icon>

                        </v-btn>
                      </v-col>
                    </v-row>
                  </v-col>
                </v-row>
                <v-row>
                </v-row>
              </v-btn-toggle>
            </v-card-actions>
          </v-card>
        </v-dialog>
      </div>
    </template>
    <v-overlay v-if="dialog || isSubmitting" dark></v-overlay>
  </v-main>
</template>

<script>
import axios from "axios";
import {maxLength, alphaNum} from 'vuelidate/lib/validators';


export default {
  name: "FileUpload",
  data() {
    return {
      scriptFiles: null,
      dagFile: null,
      dialog: false,
      isSubmitting: false,
      folder: '',
      snackbar: false,
      snackbarText: '',
      color: '',
      isHidden: true,
      hasAccess: true,
      folderHidden: true,
      canOverride: false,
      fileNameTmp: '',
      isDag: false,
      fileTmp: null,
      rename: false,
      renameFolder: false,
      fileType: '.py',
      canUpload: false,
      count: 100,
      folderNew: '',

    }
  },
  watch: {
    dagFile: function (dagFile) {
      this.scriptFiles = null;
      this.isDag = true;
      if (dagFile !== null && dagFile.name > 0) {
        if (!this.checkName(dagFile.name.split('.').shift())) {
          this.handleEditError("В имени файла могут использоваться только буквы, цифры и символы '-', '_'", "grey")
        }
        if (!this.checkFileType(dagFile)) {
          this.handleEditError("Для загрузки в этом поле используйте только файлы с расширением '.py'", "grey")
        }
        var self = this;
        self.isDag = true;
        self.fileTmp = self.dagFile;
//TODO убрать дублирующийся код
        self.checkFileAndFolder(dagFile.name, self.isDag, '')
            .then(function (response) {
              if (!_.isEmpty(response)) {
                self.canUpload = (!response.data['folderExists']
                    || (!response.data['fileExists'] && (response.data['folderExists'] && response.data['isFolderOwner'])))
              }
            });
      }
    }
    ,
    fileNameTmp: function (fileNameTmp) {
      if (fileNameTmp !== null && fileNameTmp.length > 0) {
        if (!this.checkName(fileNameTmp) || !this.checkLength(fileNameTmp)) {
          this.handleEditError("Длина имени должна быть больше 0 и меньше 100 символов. В имени файла могут использоваться только буквы, цифры и символы '-', '_'", "grey")
        }
        var self = this;
        var folder = self.folderNew === '' ? self.folder : self.folderNew;
        self.checkFileAndFolder(fileNameTmp + self.fileType, self.isDag, folder)
            .then(function (response) {
              if (!_.isEmpty(response)) {
                self.canUpload = (!response.data['folderExists']
                    || (!response.data['fileExists'] && (response.data['folderExists'] && response.data['isFolderOwner'])));
                if(!self.canUpload){
                  if(!(response.data['folderExists'] && response.data['isFolderOwner']) && self.dialog && !_.isEmpty(folder)){
                    self.handleEditError("Нет доступа к указанной директории " + folder + ", укажите другое имя", "red")
                  }
                  if(response.data['fileExists'] && self.dialog){
                    self.handleEditError("Файл с таким именем " + self.fileNameTmp + self.fileType + " уже существует в выбранной директори. " +
                        "Переименуйте файл или укажите другую директорию", "grey")
                  }
                }
              }
            });
      }
    }
    ,
    folderNew: function (folderNameTmp) {
      if (folderNameTmp != null) {
        if (!this.checkName(folderNameTmp) || folderNameTmp.length > 100) {
          this.handleEditError("В названии директории могут использоваться только буквы, цифры и символы '-', '_'.", "grey")
        }
        var self = this;
        var fileName = self.fileNameTmp === '' ? self.fileTmp.name.split('.').shift() : self.fileNameTmp;
        self.checkFileAndFolder(fileName + self.fileType, self.isDag, folderNameTmp)
            .then(function (response) {
              if (!_.isEmpty(response)) {
                self.canUpload = (!response.data['folderExists']
                    || (!response.data['fileExists'] && (response.data['folderExists'] && response.data['isFolderOwner'])))
              }
              if(!self.canUpload && self.dialog && !_.isEmpty(self.folderNew)){
                if(!(response.data['folderExists'] && response.data['isFolderOwner'])){
                  self.handleEditError("Нет доступа к указанной директории " + self.folderNew + ", укажите другое название директории", "red")
                }
                else if(response.data['fileExists'] && self.dialog){
                  if(fileName==='NaN.py'){
                    fileName=''
                  }
                  self.handleEditError("В выбранной директории уже существует файл с таким именем " +  fileName + self.fileType +
                      " Укажите другую директорию или переименуйте файл", "grey")
                }
              }
            });
      }
    }
    ,
    folder: function (folder) {
      if (folder != null) {
        if (!this.checkName(folder) || folder.length > 100) {
          this.handleEditError("В названии директории могут использоваться только буквы, цифры и символы '-', '_'.", "red")
        }
      }
    }
    ,

    scriptFiles: function (scriptFiles) {
      this.isDag = false;
      this.dagFile = null;
      if (scriptFiles !== null && _.isEmpty(scriptFiles)) {
        var self = this;
        self.dagFile = null;
      }
    }
    ,
  },
  methods: {

    checkContentType(file) {
      this.defineFileType(file)
      return this.fileType === '.py'
    },

//TODO refactor
    submitFiles(files, isDag, folder) {
      console.log(this.dialog)
      console.log(this.isSubmitting)
      console.log(_.isEmpty(this.scriptFiles))

      let self = this;
      this.isDag = isDag;

      for (const i in files) {
        self.fileTmp = files[i];
        self.fileType = '.' + self.fileTmp.name.split('.').pop();

        const fileSize = files[i].size;
        if (fileSize > 157286400) {
          self.handleEditError("Размер файла " + files[i].name + " " + (fileSize / 1000000).toFixed(2) + "Мб. Максимум 150Мб", "deep-orange accent-3")
          return;
        } else if (!this.checkName(self.fileTmp.name.split('.').shift()) || (!this.checkName(this.folderNew) || !this.checkName(this.folder))) {
          self.handleEditError("В имени файла или директории могут содержаться только буквы, цифры, '-', '_'", "deep-orange accent-3")
          return;
        } else if (!this.checkLength(self.fileTmp.name.split('.').shift())) {
          self.handleEditError("Имя должно состоять не более, чем 100 символов", "deep-orange accent-3")
          return;
        } else {
          self.checkFileAndFolder(self.fileTmp.name, isDag, folder).then(function (response) {
            if (response.data['fileExists'] && response.data['isFolderOwner']) {
              self.canUpload = (!response.data['folderExists'] || response.data['isFolderOwner'])
              self.canOverride = response.data['isFileOwner'] && response.data['fileExists'];
              self.dialog = true;
              self.fileNameTmp = self.fileTmp.name.split('.').shift()
            } else {
              self.submitFile(self.fileTmp, isDag, false, self.fileNameTmp, self.folder)
            }
          });
        }
      }
    }
    ,
    async submitFile(file, isDag, toOverride, newFileName, folder) {
      this.isSubmitting = true;
      if (toOverride && !this.canOverride) {
        this.dialog = false;
        this.isSubmitting = false;
        this.handleEditError("Недостаточно прав для перезаписи", "deep-orange accent-3")
      } else {
        let self = this;

        let headers = {'Content-Type': 'multipart/form-data',};
        let formData = new FormData();
        if (newFileName !== '') {
          self.canOverride = false;
          formData.append('files', file, self.defineNewFileNameWithExtension(newFileName));
          self.fileNameTmp = '';
        } else formData.append('files', file)
        formData.append('toOverride', toOverride);
        self.canOverride = false;
        formData.append('isDag', isDag);
        formData.append('folder', folder);

        await axios.post('/api/upload',
            formData,
            {
              headers: {
                headers
              }
            }
        ).then(function () {
          var path

          if(_.isEmpty(folder)){
            path = ''
          } else {
            path='в директорию ' + folder
          }
          self.handleEditError("Файл " + file.name + " успешно загружен " + path, "light-green accent-4")
        })
            .catch(function (error) {
              self.handleEditError(error.response.data, "light-red")
            }).then(function () {
              self.dialog = false;
              self.isSubmitting = false;
              self.isDag = false;
              self.dagFile = null;
              self.scriptFiles = null;
              self.canOverride = false;
              self.canUpload = false;
              self.fileNameTmp = '';
              self.fileTmp = null;
              self.folder = '';
              self.folderNew = '';
              self.rename = false;
              self.fileType = '.py';

            });
      }
    }
    ,
    checkFileAndFolder(file, isDag, folder) {
      let formData = new FormData();
      formData.append('folder', folder);
      formData.append('fileName', file);
      formData.append('isDag', isDag);

      var headers = {
        'Content-Type': 'multipart/form-data',
      };

      return axios.post('/api/upload/check-file',
          formData,
          {
            headers: {
              headers
            }
          })
          .then(function (response) {
            return response;
          })
          .catch(function (error) {
          });
    },

    handleEditError(response, color) {
      this.snackbar = true;
      this.snackbarText = response;
      this.color = color;
      this.isHidden = true;
    }
    ,
    checkFileType(file) {
      if (file != null) {
        return file.size > 0 && file.name.split('.').pop() === 'py';
      }
    }
    ,

    defineFileType(file) {
      if (file !== null && file.name !== null) {
        this.fileType = '.' + file.name.split('.').pop();
      }
    }
    ,
    disableSubmitBtn() {
      return !this.checkFileName(this.fileNameTmp)
          ||
          !this.checkName(this.folderNew)
          ||
          !this.canUpload
          || this.isSubmitting;
    },
    defineNewFileNameWithExtension() {
      if (!(_.isEmpty(this.fileNameTmp) && _.isEmpty(this.fileTmp) && _.isEmpty(this.fileTmp.name)
          && this.fileNameTmp !== 'undefined' && this.fileTmp.name !== 'undefined')) {
        return this.fileNameTmp + '.' + this.fileTmp.name.split('.').pop()
      }
    },


    checkFileName(fileName) {
      return (!_.isEmpty(fileName) && (this.defineNewFileNameWithExtension(fileName) + this.folderNew) !== (this.fileTmp.name + this.folder) && this.checkName(fileName));
    },

    checkName(name) {
      return !name.match(/[^а-яА-Яa-zA-Z0-9-_]+/);
    },

    checkLength(name) {
      return name.length > 0 && name.length < 100;
    }
  }
}
</script>
<style>
</style>
