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
    >
      <template v-slot:item.edit="{ item }">
        <v-dialog v-model="dialogs[item.id]" persistent max-width="800">
          <template v-slot:activator="{ on }">
            <v-btn text class="modal-btn" v-model="item.edit" color="primary" v-on="on">
              <v-icon color="secondary">mdi-pencil</v-icon>
            </v-btn>
          </template>
          <v-card>
            <v-container>
              <v-row>
                <v-card-title class="headline" style="color: gray">Настройка времени для {{ item.username }}
                </v-card-title>
                <v-spacer></v-spacer>
                <v-btn text @click.stop="$set(dialogs, item.id, false)">
                  <v-icon color="red">mdi-window-close</v-icon>
                </v-btn>
              </v-row>
            </v-container>
            <v-card-text>
              <v-container>
                <div>Сервис доступен С:</div>
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
                <div>Сервис доступен ПО:</div>
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
                <v-spacer></v-spacer>
                  <v-btn text color="green" @click="save(item.id)">
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
      <template v-slot:item.upload="{ item }">
        <v-card-text>{{ item.upload }}
          <v-icon v-model="item.upload" v-if="item.upload" color="green">mdi-circle-medium</v-icon>
          <v-icon v-model="item.upload" v-else color="red">mdi-circle-medium</v-icon>
        </v-card-text>
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
        label="Введите логин пользователя из AD"
    ></v-text-field>
    <v-btn text color="accent4" @click="sendUser()">
     Добавить
    </v-btn>
      </v-row>
    </v-col>


<!--    <v-dialog v-model="dialog" persistent max-width="1000">-->
<!--      <template v-slot:activator="{ on }">-->
<!--        <v-btn text class="modal-btn" v-model="dialog" color="primary" v-on="on">-->
<!--          Редактировать выбранных-->
<!--        </v-btn>-->
<!--      </template>-->
<!--      <v-card>-->
<!--        <v-card-title class="headline grey lighten-2">-->
<!--          Назначить время и дату выбранным пользователям-->
<!--        </v-card-title>-->
<!--        <v-card-text>-->
<!--          <v-container>-->
<!--            <v-row justify="center">-->
<!--              <v-date-picker></v-date-picker>-->
<!--              &nbsp;-->
<!--              &nbsp;-->
<!--              &nbsp;-->
<!--              <v-time-picker v-model="timePicker1"></v-time-picker>-->
<!--            </v-row>-->
<!--          </v-container>-->

<!--        </v-card-text>-->
<!--        <v-divider></v-divider>-->

<!--        <v-card-actions>-->
<!--          <v-spacer></v-spacer>-->
<!--          <v-layout row justify-right>-->
<!--            <v-btn text color="green" @click="save(item.id)">-->
<!--              Сохранить-->
<!--            </v-btn>-->
<!--          </v-layout>-->
<!--        </v-card-actions>-->
<!--      </v-card>-->
<!--    </v-dialog>-->
  </div>

</template>

<script>
import {mapGetters} from "vuex";
import axios from "axios";

export default {
  name: "Admin",

  computed: mapGetters(['getUsers']),
  data: () => ({
    headers: [
      {text: 'id', value: 'id'},
      {text: 'логин', value: 'username'},
      {text: 'дата создания', value: 'createDate'},
      {text: 'дата обновления', value: 'trigger'},
      {text: 'права на загрузку', value: 'upload'},
      {text: 'админ', value: 'admin'},
      {text: "редактировать", value: "edit"},
      {text: 'задизейблить', value: "delete"},
      {text: 'выбрать', value: "сhecked"},

    ],
    dialogs: {},
    dialog: false,
    datePickerFrom: {},
    timePickerFrom: {},
    datePickerTo: {},
    timePickerTo: {},
    checkboxes: {},
    user:'',

    search: ''
  }),

  created() {
    this.$store.dispatch("fetchUsers")
  },
  methods: {
    disable(id) {
      axios.post('/api/users/limit-access/' + id).then(function () {
        console.log('SUCCESS!!');
      }).catch(function () {
        console.log('FAILURE!!');
      });
    },
    save(id) {
      var self = this;
      axios.post('/api/users/grant-access/' + id, {
        dateTimeFrom: self.datePickerFrom[id] + " " + self.timePickerFrom[id],
        dateTimeTo:self.datePickerTo[id] + " " + self.timePickerTo[id],
      }).then(function () {
        console.log('SUCCESS!!');
      }).catch(function () {
        console.log('FAILURE!!');
      }).then(function (){
        self.dialogs[id] = false;
      });
    },
    sendUser(){
      var self = this;
      axios.post('/api/users/search', {
        user: self.user
      }).then(function () {
        console.log('SUCCESS!!');
      }).catch(function () {
        console.log('FAILURE!!');
      }).then(function (){
        self.user = null;
      });

    }
  }
}
</script>

<style>

</style>