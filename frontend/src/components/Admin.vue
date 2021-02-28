<template>
  <div>
    <v-col md="4" offset="8">
      <v-text-field
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
        :items-per-page="10"
        class="elevation-1"
        :search="search"
        sort-by="fullName"
    >
      <template v-slot:item.edit="{ item }">
        <v-dialog v-model="dialogs[item.id]" persistent max-width="900">
          <template v-slot:activator="{ on }">
            <v-btn text class="modal-btn" v-model="item.edit" color="primary" v-on="on">
              <v-icon color="secondary">mdi-pencil</v-icon>
<!--              нужно у каждого разрешенного поставить кружочек -->
<!--                <v-icon v-model="item.dateTo" v-if="item.dateTo>Date.now()" color="green">mdi-circle-medium</v-icon>-->
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
                <div>Предоставить доступ С:</div>
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
                  ></v-time-picker>
                </v-row>
                <br/>
                <div>ПО:</div>
                <br/>
                <v-row>
                  <v-date-picker
                      v-model="datePickerTo[item.id]"
                  ></v-date-picker>
                  &nbsp;
                  &nbsp;
                  &nbsp;
                  <v-time-picker
                      v-model="timePickerTo[item.id]"
                  ></v-time-picker>
                </v-row>
              </v-container>
            </v-card-text>
            <v-spacer></v-spacer>
            <v-card-actions>
              <v-divider></v-divider>
              <v-card-text v-if="timePickerFrom[item.id] !=null && datePickerFrom[item.id] != null && timePickerTo[item.id] !=null && datePickerTo[item.id] != null">
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
          <v-btn text color="green" @click="save(item.id)" :disabled="timePickerFrom[item.id] ==null || datePickerFrom[item.id] == null || timePickerTo[item.id] ==null || datePickerTo[item.id] == null">
            Сохранить
          </v-btn>
          </v-card-actions>
          </v-card>
        </v-dialog>
      </template>
      <template v-slot:item.delete="{ item }">
        <v-btn v-model="item.delete" text color="red">
          <v-icon @click="disable(item.id)">mdi-window-close</v-icon>
        </v-btn>
      </template>
      <template v-slot:item.сhecked="{ item }">
        <v-simple-checkbox
            v-model="checkboxes[item.id]"
        ></v-simple-checkbox>
      </template>
    </v-data-table>
    <br/>

    <v-col></v-col>
    <v-col>
      <v-row>
        <v-spacer></v-spacer>
        <v-text-field v-model="user"
                      label="Поиск пользователя AD по логину"
        ></v-text-field>
        <v-btn text color="accent4" @click="addUser()" :disabled="user.length ==0">
          Добавить
        </v-btn>
      </v-row>
    </v-col>

    <template>
      <div class="text-center ma-2">
        <v-snackbar
            v-model="snackbar"
            @mousemove="snackbarText=false"
        >
          {{ snackbarText }}

          <template v-slot:action="{ attrs }">
            <v-btn
                color="pink"
                text
                v-bind="attrs"
                @click="snackbar = false"
            >
              <v-icon>mdi-cancel</v-icon>
            </v-btn>
          </template>
        </v-snackbar>
      </div>
    </template>
  </div>


</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";
import * as vm from "vm";

export default {
  name: "Admin",

  computed: mapGetters(['getUsers']),
  data: () => ({
    headers: [
      {text: 'id', value: 'id'},
      {text: 'логин', value: 'userName'},
      {text: 'ФИО', value: 'fullName'},
      {text: 'Группа', value: 'group'},
      {text: 'дата создания', value: 'createDate'},
      {text: 'доступ с', value: 'dateFrom'},
      {text: 'доступ по', value: 'dateTo'},
      {text: 'админ', value: 'admin'},
      {text: "редактировать", value: "edit"},
      {text: 'задизейблить', value: "delete"},
      // {text: 'выбрать', value: "сhecked"},

    ],
    dialogs: {},
    dialog: false,
    datePickerFrom: {},
    timePickerFrom: {},
    datePickerTo: {},
    timePickerTo: {},
    checkboxes: {},
    user: '',
    snackbar: false,
    search: '',
    snackbarText: '',
  }),

  created() {
    this.$store.dispatch("fetchUsers")
  },
  methods: {
    disable(id) {
      var self=this;
      axios.post('/api/whitelist/limit-access/' + id).then(function () {
        console.log('SUCCESS!!');
        self.$store.dispatch("fetchUsers");
      }).catch(function () {
        console.log('FAILURE!!');
      });
    },
    save(id) {
      var self = this;
      let formData = new FormData();
      formData.append('dateFrom', self.datePickerFrom[id]);
      formData.append('timeFrom', self.timePickerFrom[id]);
      formData.append('dateTo', self.datePickerTo[id]);
      formData.append('timeTo', self.timePickerTo[id]);
      axios.post('/api/whitelist/grant-access/' + id, formData, {headers: {'Content-Type': 'multipart/form-data',}}
      ).then(function () {
        console.log('SUCCESS!!');
        self.$store.dispatch("fetchUsers");

      }).catch(function () {
        console.log('FAILURE!!');
      }).then(function () {
        self.dialogs[id] = false;
      });
    },
    addUser() {
      var self = this;
      axios.post('/api/whitelist/add-by-username', self.user, {headers: {"Content-Type": "text/plain"}})
          .catch(function (error) {
            self.handleEditError(error.response.data)
          }).then(function () {
        self.user = '';
      });
    },
    handleEditError(response) {
      this.snackbar = true;
      this.snackbarText = response;
    }
  }
}
</script>

<style>
</style>