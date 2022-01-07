<template>
  <div>
    <v-col md="4" offset="8">
      <v-text-field id="search"
                    v-model="search"
                    append-icon="mdi-magnify"
                    label="Поиск пользователей"
                    single-line
                    hide-details
      ></v-text-field>
    </v-col>
    <v-data-table
        :headers="headers"
        :items="getUsers"
        :items-per-page=size
        class="elevation-1"
        :search="search"
        sort-by="fullName"
        calculate-widths
    >
<!--      <template v-slot:item.disable="{ item }">-->
<!--        <v-tooltip top color="#2196f3">-->
<!--          <template v-slot:activator="{ on, attrs }">-->
<!--            <v-btn label="disable" v-model="item.disable" text color="red">-->
<!--              <v-icon @click="disable(item.id)" v-on="on">mdi-window-close</v-icon>-->
<!--            </v-btn>-->
<!--          </template>-->
<!--          <span> Блокировать </span>-->
<!--        </v-tooltip>-->
<!--      </template>-->
      <template v-slot:item.edit="{ item }">
        <v-tooltip top color="#2196f3" >
          <template v-slot:activator="{on, attrs}">
            <v-dialog v-model="dialogs[item.id]" persistent max-width="900" v-on="on">
              <template v-slot:activator="{ on }" >
                <v-btn label="edit" text class="modal-btn" v-model="item.edit" color="primary" v-on="on" >
                  <v-icon color="grey" v-on="attrs">mdi-pencil</v-icon>
                  <v-icon v-model="item.role" v-if="item.role==='USER'" color="green">mdi-circle-medium</v-icon>
                </v-btn>
              </template>
              <v-card>
                <v-container>
                  <v-row>
                    <v-card-title class="headline" style="color: gray">Настройка времени для {{ item.fullName }} группа
                      {{ item.group }}
                    </v-card-title>
                    <v-spacer></v-spacer>
                    <v-btn text @click.stop="$set(dialogs, item.id, false)">
                      <v-icon color="red">mdi-window-close</v-icon>
                    </v-btn>

                  </v-row>
                </v-container>
                <v-card-text>
                  <v-container>
                    <div style="font-size: 16px; color: #666666" class="font-weight-medium">Предоставить доступ С:</div>
                    <br/>
                    <v-row>
                      <v-date-picker
                          v-model="datePickerFrom[item.id]"
                      ></v-date-picker>
                      &nbsp;
                      &nbsp;
                      &nbsp;
                      <v-time-picker
                          v-model="timePickerFrom[item.id]"
                          format="24hr"
                      ></v-time-picker>
                    </v-row>
                    <br/>
                    <div style="font-size: 16px; color: #666666" class="font-weight-medium">ПО:</div>
                    <br/>
                    <v-row>
                      <v-date-picker
                          v-model="datePickerTo[item.id]"
                      ></v-date-picker>
                      &nbsp;
                      &nbsp;
                      <v-time-picker
                          format="24hr"
                          v-model="timePickerTo[item.id]"
                          default
                      ></v-time-picker>
                    </v-row>
                  </v-container>
                </v-card-text>
                <v-spacer></v-spacer>
                <v-card-actions>
                  <v-divider></v-divider>
                  <v-card-text
                      v-if="timePickerFrom[item.id] !=null && datePickerFrom[item.id] != null && timePickerTo[item.id] !=null && datePickerTo[item.id] != null">
                <span
                    style="font-size: 11px">Предоставить доступ для {{ item.fullName }} с {{ datePickerFrom[item.id] }} {{
                    timePickerFrom[item.id]
                  }}
                  по {{ datePickerTo[item.id] }}  {{ timePickerTo[item.id] }}</span>
                  </v-card-text>

                  <v-card-text v-else>
                <span style="font-size: 11px; color: red">Укажите все параметры для доступа
                </span>
                  </v-card-text>

                  <v-btn id="saveDateTime" text color="green" @click="save(item.id)"
                         :disabled="timePickerFrom[item.id] ==null || datePickerFrom[item.id] == null || timePickerTo[item.id] ==null || datePickerTo[item.id] == null">
                    Сохранить
                  </v-btn>
                </v-card-actions>
              </v-card>
            </v-dialog>
          </template>
          <span> Редактировать </span>
        </v-tooltip>
      </template>
      <template v-slot:item.disable="{ item }">
        <v-tooltip top color="#2196f3">
          <template v-slot:activator="{ on, attrs }">
            <v-btn label="disable" v-model="item.disable" text color="red">
              <v-icon @click="disable(item.id)" v-on="on">mdi-window-close</v-icon>
            </v-btn>
          </template>
          <span> Блокировать </span>
        </v-tooltip>
      </template>
      <template v-slot:item.more="{ item }">
        <v-tooltip top color="#2196f3">
          <template v-slot:activator="{ on, attrs }">
            <v-btn label="more" v-model="item.more" text color="blue">
              <v-icon @click="openMore(item)" v-on="on">mdi-dots-horizontal</v-icon>
            </v-btn>
          </template>
          <span>Подробнее</span>
        </v-tooltip>
      </template>
      <template v-slot:item.remove="{ item }">
        <v-tooltip top color="#2196f3">
          <template v-slot:activator="{ on, attrs }">
            <v-btn label="remove" v-model="item.remove" text color="red">
              <v-icon @click="remove(item.id)" v-on="on">mdi-close-octagon</v-icon>
            </v-btn>
          </template>
          <span>Удалить</span>
        </v-tooltip>
      </template>
      <template v-slot:item.сhecked="{ item }">
        <v-checkbox
            id="userCheckbox"
            v-model="userSelected"
            :value="item.userName"
        ></v-checkbox>
      </template>

      <template v-slot:item.fullName="{ item }" >
        <td label="fullName"> {{ item.fullName }} </td>
      </template>
      <template v-slot:item.userName="{ item }" >
        <td label="userName"> {{ item.userName }} </td>
      </template>
      <template v-slot:item.group="{ item }" >
        <td label="group"> {{ item.group }} </td>
      </template>
      <template v-slot:item.createDate="{ item }" >
        <td label="createDate"> {{ convertDateTimeToUtc(item.createDate) }} </td>
      </template>
      <template v-slot:item.dateFrom="{ item }" >
        <td label="dateFrom"> {{ convertDateTimeToUtc(item.dateFrom) }} </td>
      </template>
      <template v-slot:item.dateTo="{ item }" >
        <td label="dateTo"> {{ convertDateTimeToUtc(item.dateTo) }} </td>
      </template>
      <template v-slot:item.role="{ item }" >
        <td label="role"> {{ item.role }} </td>
      </template>
      <template v-slot:item.addedBy="{ item }" >
        <td label="addedBy"> {{ item.addedBy }} </td>
      </template>

    </v-data-table>
    <br/>

    <v-row>
      <v-col cols="12" sm="2">
        <v-btn id="addToGroup" text
               :disabled="_.isEmpty(userSelected)"
               @click="groupManagement=true"
        >Добавить в группу
          <v-icon color="primary">mdi-account-multiple-plus</v-icon>
        </v-btn>
      </v-col>
      <v-col cols="12" sm="2">
        <v-btn id="addNewGroup" text @click="showGroups()">Новая группа
          <v-icon color="primary">mdi-folder-plus</v-icon>
        </v-btn>
      </v-col>
      <v-col cols="12" sm="1"></v-col>
      <v-col cols="12" sm="4">
        <v-text-field id="searchInAD" v-model="user" prepend-inner-icon="mdi-account"
                      label="Поиск пользователя AD по логину"
        ></v-text-field>
      </v-col>
      <v-col cols="12" sm="1">
        <v-btn id="addAdUser" text color="accent4" @click=checkUser(user)>
          <v-icon color="primary">mdi-account-plus</v-icon>
        </v-btn>
      </v-col>
      <v-col cols="12" sm="1">
        <v-btn text color="primary">
          <v-icon>mdi-package-down</v-icon>
        </v-btn>
      </v-col>
      <v-col cols="12" sm="1">
        <v-spacer></v-spacer>
        <v-tooltip top color="#2196f3">
          <template v-slot:activator="{ on, attrs }">
            <v-btn id="passwordChange" v-if="isNativeUser" text @click="passwordChange=true">
              <v-icon color="primary" v-on="on">mdi-lock-reset</v-icon>
            </v-btn>
          </template>
          <span>Изменение пароля</span>
        </v-tooltip>
      </v-col>
    </v-row>

    <div class="text-center ma-2">
      <v-snackbar
          v-model="snackbar"
          @mousemove="snackbarText=false"
          :color="snackbarColor"
          timeout="1500"
      >
        {{ snackbarText }}

        <template>
          <v-btn
              color="white"
              text
              @click="snackbar = false"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </template>
      </v-snackbar>
    </div>
    <v-dialog v-model="userHistory" fullscreen scrollable>
      <v-container>
        <v-card color="white">
          <v-container>
            <v-row>
              <v-card-title>История пользователя</v-card-title>
              <v-spacer></v-spacer>
              <v-btn text @click="userHistory=false">
                <v-icon color="red">
                  mdi-window-close
                </v-icon>
              </v-btn>
            </v-row>
            <v-divider></v-divider>
            <v-row>
              <v-spacer></v-spacer>
              <v-col cols="12" sm="3">
                <v-text-field id="searchFile"
                              v-model="searchHistory"
                              append-icon="mdi-magnify"
                              label="Поиск по истории"
                              single-line
                              hide-details
                ></v-text-field>
              </v-col>
            </v-row>
            <v-data-table
                :items="items"
                :headers="itemHeaders"
                :items-per-page="15"
                :search="searchHistory"
            >

              <template v-slot:item.actionDateTime="{ item }" >
                <td label="actionDateTime"> {{ item.actionDateTime }} </td>
              </template>
              <template v-slot:item.actionType="{ item }" >
                <td label="actionType"> {{ item.actionType }} </td>
              </template>
              <template v-slot:item.fileName="{ item }" >
                <td label="fileName"> {{ item.fileName }} </td>
              </template>
              <template v-slot:item.path="{ item }" >
                <td label="path"> {{ item.path }} </td>
              </template>
              <template v-slot:item.description="{ item }" >
                <td label="description"> {{ item.description }} </td>
              </template>

            </v-data-table>
            <v-row>
              <v-spacer></v-spacer>
              <br/>
              <v-btn text color="primary" @click="downloadLog()">
                <v-icon>mdi-package-down</v-icon>
              </v-btn>
            </v-row>
          </v-container>
        </v-card>
      </v-container>
    </v-dialog>
    <v-dialog v-model="groupManagement" scrollable fullscreen
              overlay-color="white">
      <v-container>
        <v-col cols="12" sm="9">
          <v-card outlined rounded color="white">
            <v-container>
              <v-row>
                <v-col cols="12" sm="1"></v-col>
                <v-card-title style="color:dodgerblue;" v-if="!_.isEmpty(userSelected) && !isGroupAdding">Добавление
                  пользователей
                  {{ userSelected }} в группу
                </v-card-title>
                <v-card-title style="color:dodgerblue;" v-else>Группы пользователей</v-card-title>
                <v-spacer></v-spacer>
                <v-btn id="closeGroupWindow" text @click="closeGroupManagmentWindow()" color="red">
                  <v-icon>
                    mdi-window-close
                  </v-icon>
                </v-btn>
              </v-row>
              <v-divider></v-divider>
              <v-row>
                <v-col cols="12" sm="8"></v-col>
                <v-col cols="12" sm="3">

                  <v-text-field id="searchGroup"
                                v-model="searchGroup"
                                append-icon="mdi-magnify"
                                label="Поиск групп"
                                single-line
                                hide-details
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12" sm="1"></v-col>
                <v-col cols="12" sm="10">
                  <v-data-table
                      :items="getGroups"
                      :headers="!isGroupAdding || !_.isEmpty(ldapUser) ? groupHeadersExtended : groupHeaders"
                      :items-per-page="5"
                      :search="searchGroup"
                  >
                    <template v-slot:item.selected="{ item }">
                      <v-checkbox
                          v-model="groupSelected"
                          :value="item.groupName"
                          v-if="_.isEmpty(userSelected) || _.isEmpty(ldapUser)"
                      ></v-checkbox>
                    </template>
                    <template v-slot:item.remove="{ item }">
                      <v-col class="text-right">
                        <v-tooltip top color="#2196f3">
                          <template v-slot:activator="{ on, attrs }">
                            <v-btn label="remove" v-model="item.remove" align="right" text color="red">
                              <v-icon @click="removeGroup(item.id)" v-on="on" align="right">mdi-close-octagon</v-icon>
                            </v-btn>
                          </template>
                          <span>Удалить</span>
                        </v-tooltip>
                      </v-col>
                    </template>
                  </v-data-table>
                  <v-btn id="addUserToGroup" @click="assignGroup(groupSelected, userSelected)"
                         :disabled="_.isEmpty(groupSelected)"
                         text color="primary"
                         v-if="!isGroupAdding && _.isEmpty(ldapUser)">
                    Добавить пользователей в группу
                  </v-btn>
                </v-col>
              </v-row>
              <br/>
              <v-row>
                <v-spacer></v-spacer>
                <v-col cols="12" sm="3">
                  <v-text-field id="newGroupName" v-model="groupName"
                                label="Название новой группы"
                                color="primary"
                  >
                  </v-text-field>
                  <br/>
                  <br/>
                </v-col>
                <v-col cols="12" sm="1">
                  <v-btn id="addNewGroupBtn"@click="addGroup(groupName)" text
                         :disabled="_.isEmpty(groupName) || !this.checkName(groupName) ">
                    <v-icon color="primary">mdi-content-save</v-icon>
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-col>
      </v-container>
    </v-dialog>

    <v-dialog v-model="whiteListUserGroup" scrollable fullscreen
              overlay-color="white">
      <v-container>
        <v-col cols="12" sm="12">
          <v-card outlined rounded color="white">
            <v-container>
              <v-row>
                <v-col cols="12" sm="1"></v-col>
                <v-card-title style="color:dodgerblue;">Добавление пользователя {{ ldapUser }} в группу
                </v-card-title>
                <v-spacer></v-spacer>
                <v-btn id="closeUserToGroupWindow" text @click="whiteListUserGroup=false" color="red">
                  <v-icon>
                    mdi-window-close
                  </v-icon>
                </v-btn>
              </v-row>
              <v-divider></v-divider>
              <v-row>
                <v-col cols="12" sm="8"></v-col>
                <v-col cols="12" sm="3">
                  <v-text-field id="searchGroup"
                                v-model="searchGroup"
                                append-icon="mdi-magnify"
                                label="Поиск групп"
                                single-line
                                hide-details
                  ></v-text-field>
                </v-col>
              </v-row>
              <v-row>
                <v-col cols="12" sm="1"></v-col>
                <v-col cols="12" sm="10">
                  <v-data-table
                      :items="getGroups"
                      :headers="groupHeadersExtended"
                      :items-per-page="5"
                      :search="searchGroup"
                  >
                    <template v-slot:item.selected="{ item }">
                      <v-checkbox
                          v-model="groupSelected"
                          :value="item.groupName"
                      ></v-checkbox>
                    </template>
                  </v-data-table>
                  <v-btn id="setGroupForUser" @click="addUser(groupSelected, ldapUser)"
                         :disabled="_.isEmpty(groupSelected)"
                         text color="primary">
                    Назначить в группу перед добавлением в Whitelist
                  </v-btn>
                </v-col>
              </v-row>
            </v-container>
          </v-card>
        </v-col>
      </v-container>
    </v-dialog>

    <v-dialog v-model="passwordChange" scrollable fullscreen
              overlay-color="white">
      <v-container>
        <v-col cols="12" sm="12">
          <v-card outlined rounded color="white">
            <v-container>
              <v-row>
                <v-card-title style="color:dodgerblue;">Изменение пароля
                </v-card-title>
                <v-spacer></v-spacer>
                <v-btn text @click="passwordChange=false" color="red">
                  <v-icon>
                    mdi-window-close
                  </v-icon>
                </v-btn>
              </v-row>
              <v-divider></v-divider>
              <template>
                <form>
                  <v-col cols="12" sm="1"></v-col>
                  <v-col cols="12" sm="11">
                    <v-text-field id="oldPassword"
                                  v-model="oldPassword"
                                  :counter="10"
                                  label="Пароль"
                                  required
                    ></v-text-field>
                    <v-text-field id="newPassword"
                                  v-model="newPassword"
                                  :counter="10"
                                  label="Новый пароль"
                                  required
                    ></v-text-field>
                    <v-text-field id="newPasswordRepeat"
                                  v-model="newPasswordRepeat"
                                  :counter="10"
                                  label="Повторите пароль"
                                  required
                    ></v-text-field>

                    <v-row>
                      <v-spacer></v-spacer>
                      <v-btn id="submitChangePassword"
                             class="mr-4"
                             @click="changePassword()"
                             text
                             color="primary"
                      >
                        Сохранить
                      </v-btn>
                    </v-row>
                  </v-col>
                </form>
              </template>
            </v-container>
          </v-card>
        </v-col>
      </v-container>
    </v-dialog>


    <v-overlay v-if="groupManagement || userHistory || dialog || whiteListUserGroup || passwordChange" dark></v-overlay>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";
import * as vm from "vm";
import Account from "./Account.vue";
import mapActions from "vuex/dist/vuex.mjs";
import moment from 'moment'

export default {
  name: "Admin",
  computed: {
    ...mapGetters(['getUsers', 'getGroups', 'getCurrentUser']),

    isNativeUser () {
      return this.$store.getters.getCurrentUser.isNative === true;
    },
  },

  data: () => ({
    headers: [
      {text: 'Выбрать', value: "сhecked"},
      {text: 'ФИО', value: 'fullName'},
      {text: 'Логин', value: 'userName'},
      {text: 'Группа', value: 'group'},
      {text: 'Дата создания', value: 'createDate'},
      {text: 'Доступ с', value: 'dateFrom'},
      {text: 'Доступ по', value: 'dateTo'},
      {text: 'Роль', value: 'role'},
      {text: 'Кем добавлен', value: 'addedBy'},
      {text: "Редактировать", value: "edit"},
      {text: "Подробнее", value: "more"},
      {text: 'Блокировать', value: "disable"},
      {text: "Удалить", value: "remove"},
    ],
    itemHeaders: [
      {text: 'Дата и время', value: 'actionDateTime'},
      {text: 'Действие', value: 'actionType'},
      {text: 'Имя файла', value: 'fileName'},
      {text: 'Директория', value: 'path'},
      {text: 'Описание', value: 'description'},
      {text: 'Новая директория', value: 'isNewDirectory'},
      {text: 'Является ли дагом', value: 'isDag'}
    ],
    groupHeaders: [
      {text: 'Название', value: 'groupName'},
      {text: "Удалить", value: "remove", align: 'center', width:'5px'}
    ],
    groupHeadersExtended: [
      {text: 'выбрать', value: "selected"},
      {text: 'Название', value: 'groupName'},
    ],
    dialogs: {},
    dialog: false,
    datePickerFrom: {},
    timePickerFrom: {},
    datePickerTo: {},
    timePickerTo: {},
    userSelected: [],
    user: '',
    snackbar: false,
    snackbarColor: 'grey',
    search: '',
    searchGroup: '',
    snackbarText: '',
    userHistory: '',
    items: [],
    page: 0,
    size: 10,
    groupName: '',
    groupManagement: false,
    groupSelected: null,
    isGroupAdding: false,
    ldapUser: null,
    whiteListUserGroup: false,
    searchHistory: '',
    passwordChange: false,
    oldPassword: '',
    newPassword: '',
    newPasswordRepeat: '',

  }),

  created() {
    this.$store.dispatch("fetchUsers")
    this.$store.dispatch("fetchCurrentUser")
    this.$store.dispatch("fetchGroups")
  },

  watch: {
    groupSelected: function (groupSelected) {
      var self = this;
      if (!_.isEmpty(groupSelected)) {
        self.assignGroup(groupSelected.groupName, self.userSelected)
        self.handleEditError(self.userSelected + " успешно добавлены в группу " + groupSelected.groupName, 'light-green accent-4')
        self.groupSelected = null;
        self.userSelected = null;
      }
    },

    groupName: function (groupName) {
      if (!_.isEmpty(groupName) && !this.checkName(groupName)) {
        this.handleEditError("Возможно использовать только буквы, цифры, '-' и '_'", 'red')
      }

    }
  },

  methods: {
    convertDateTimeToUtc(date) {
      return date ? moment.utc(date).local().format('YYYY-MM-DD HH:mm') : '';
    },

    disable(id) {
      var self = this;
      axios.post('/api/whitelist/limit-access/' + id).then(function () {
        self.$store.dispatch("fetchUsers");
        self.handleEditError("Доступ для пользователя сброшен", 'light-green accent-4')
      }).catch(function () {
        self.handleEditError("Доступа не может быть сброшен", 'red')
      });
    },

    remove(id) {
      var self = this;
      axios.delete('/api/whitelist/' + id).then(function () {
        self.$store.dispatch("fetchUsers");
        self.handleEditError("Пользователь успешно удалён", 'light-green accent-4')
      }).catch(function () {
        self.handleEditError("Невозможно удалить пользователя", 'red')
      });
    },

    save(id) {
      var self = this;
      let formData = new FormData();

      var fromDateTime = moment(self.datePickerFrom[id] + " " + self.timePickerFrom[id]).utc();
      formData.append('dateFrom', fromDateTime.format("YYYY-MM-DD"));
      formData.append('timeFrom', fromDateTime.format("HH:mm"));

      var toDateTime = moment(self.datePickerTo[id] + " " + self.timePickerTo[id]).utc();
      formData.append('dateTo', toDateTime.format("YYYY-MM-DD"));
      formData.append('timeTo', toDateTime.format("HH:mm"));

      axios.post('/api/whitelist/grant-access/' + id, formData, {headers: {'Content-Type': 'multipart/form-data',}}
      ).then(function () {
        self.$store.dispatch("fetchUsers");
      }).catch(function () {
        self.handleEditError("Ошибка выдачи прав", 'red')
      }).then(function () {
        self.dialogs[id] = false;
      });
    },

    addUser() {
      var self = this;
      this.ldapUser = this.user;
      var formData = new FormData();
      if (_.isEmpty(self.groupSelected)) {
        self.groupSelected = 'test'
      }
      formData.append('userName', self.user);
      formData.append('groupName', self.groupSelected);

      var headers = {
        'Content-Type': 'multipart/form-data',
      };

      axios.post('/api/whitelist/add-by-username',
          formData, {
            headers: {
              headers
            }
          })
          .catch(function (error) {
            self.handleEditError(error.response.data, 'grey')
          }).then(function () {
        self.$store.dispatch("fetchUsers")
        self.user = '';
        self.ldapUser = null;
        self.groupManagement = false;
        self.whiteListUserGroup = false;
        self.groupSelected = null;
      });
    },

    handleEditError(response, color) {
      this.snackbar = true;
      this.snackbarText = response;
      this.snackbarColor = color;
    },

    checkUser(username) {
      var self = this;
      var formData = new FormData();
      formData.append('username', username);

      var headers = {
        'Content-Type': 'multipart/form-data',
      };
      axios.post('/api/whitelist/check-by-username',formData, {
        headers: {
          headers
        }
      }).then(function () {
        self.whiteListUserGroup=true
      }).catch(function (error) {
        self.handleEditError(error.response.data, 'red')
      });
    },



    openMore(user) {
      var self = this;

      self.items = [];
      axios.get('/api/admin/events/' + user.userName).then(function (response) {
        self.items = response.data.content
        self.items.map(el => el['actionDateTime'] = moment.utc(el['actionDateTime']).local().format('YYYY-MM-DD HH:mm'))
        self.userHistory = true;
      })
          .catch(function (error) {
            self.handleEditError("Невозможно получить историю пользователя", 'grey')
          });
    },

    showGroups() {
      console.log("Show group")
      this.isGroupAdding = true
      this.$store.dispatch("fetchGroups")
      this.groupManagement = true;
    },

    addGroup(groupName) {

      var self = this;
      var formData = new FormData();
      formData.append('groupName', groupName);

      var headers = {
        'Content-Type': 'multipart/form-data',
      };
      // self.items = [];
      axios.post('/api/groups/create', formData, {
        headers: {
          headers
        }
      }).then(function () {
        self.$store.dispatch("fetchGroups")
        self.handleEditError("Группа успешно создана", 'light-green accent-4')
        self.groupName = null;
      })
          .catch(function (error) {
            self.handleEditError("Невозможно создать группу", 'red')
          });
    },

    removeGroup(id) {
      var self = this;
      axios.delete('/api/groups/delete/' + id).then(function () {
        self.$store.dispatch("fetchGroups");
        self.handleEditError("Группа успешно удалён", 'light-green accent-4')
      }).catch(function () {
        self.handleEditError("Невозможно удалить группу kjndkjdlkf", 'red')
      });
    },

    assignGroup(group, users) {
      var self = this;
      if (this.checkName(group)) {
        var formData = new FormData();
        formData.append('groupName', group);
        formData.append('users', users);

        var headers = {
          'Content-Type': 'multipart/form-data',
        };
        self.items = [];
        axios.post('/api/users/add-into-group', formData, {
          headers: {
            headers
          }
        }).then(function (response) {
          self.$store.dispatch("fetchUsers")
          self.checkboxes = []
          self.handleEditError(users + " добавлен(ы) в группу " + group, 'light-green accent-4')
        })
            .catch(function (error) {
              self.handleEditError("Невозможно добавить " + users + " в группу " + group, 'red')
            })
      } else {
        self.handleEditError("В названии директории можно использовать только буквы, цифры, '-' и '_'", 'red')
      }
    },

    checkName(name) {
      return !name.match(/[^а-яА-Яa-zA-Z0-9-_]+/);
    },

    closeGroupManagmentWindow() {
      this.groupManagement = false
      this.isGroupAdding = false
      this.groupSelected = null
      this.userSelected = []
    },

    changePassword() {
      if (this.newPassword !== this.newPasswordRepeat) {
        this.handleEditError("Пароли должны совпадать", 'red')
      }
      var self = this;
      if (this.checkName(this.newPassword)) {
        var formData = new FormData();
        formData.append('password', self.newPassword);
        formData.append('oldPassword', self.oldPassword);

        var headers = {
          'Content-Type': 'multipart/form-data',
        };
        self.items = [];
        axios.post('/api/users/user/updatePassword', formData, {
          headers: {
            headers
          }
        }).then(function (response) {
          self.oldPassword = null;
          self.newPassword = null;
          self.newPasswordRepeat = null;
          self.passwordChange = false;
          self.handleEditError("Пароль изменен", 'light-green accent-4')

        })
            .catch(function () {
              self.handleEditError("Невозможно изменить пароль", 'red')
            })
      }
    }
  }
}
</script>

<style>
</style>