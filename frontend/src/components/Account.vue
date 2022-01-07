<template>
  <div>
    <v-col md="4" offset="8">
      <v-text-field id="searchFile"
                    v-model="search"
                    append-icon="mdi-magnify"
                    label="История"
                    single-line
                    hide-details
      ></v-text-field>
    </v-col>
    <v-data-table
        :headers="headers"
        :items="getFiles"
        :items-per-page="10"
        class="elevation-1"
        :search="search"
        sort-by="createDate"
    >
      <template v-slot:item.actionDateTime="{ item }">
        <td label="actionDateTime"> {{ item.actionDateTime }}</td>
      </template>
      <template v-slot:item.actionType="{ item }">
        <td label="actionType"> {{ item.actionType }}</td>
      </template>
      <template v-slot:item.fileName="{ item }">
        <td label="fileName"> {{ item.fileName }}</td>
      </template>
      <template v-slot:item.path="{ item }">
        <td label="path"> {{ item.path }}</td>
      </template>
      <template v-slot:item.description="{ item }">
        <td label="description"> {{ item.description }}</td>
      </template>
      <template v-slot:item.isNewDirectory="{ item }">
        <td label="isNewDirectory"> {{ item.isNewDirectory }}</td>
      </template>
      <template v-slot:item.isDag="{ item }">
        <td label="isDag"> {{ item.isDag }}</td>
      </template>

    </v-data-table>

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
  name: "Account",

  computed:
      mapGetters(['getFiles']),

  data: () => ({
    headers: [
      {text: 'Дата и время', value: 'actionDateTime'},
      {text: 'Действие', value: 'actionType'},
      {text: 'Имя файла', value: 'fileName'},
      {text: 'Директория', value: 'path'},
      {text: 'Описание', value: 'description'},
      {text: 'Новая директория', value: 'isNewDirectory'},
      {text: 'Является ли дагом', value: 'isDag'}
    ],
    dialogs: {},
    dialog: false,
    snackbar: false,
    search: '',
    snackbarText: '',
  }),

  created() {
    this.$store.dispatch("fetchFiles")
  }
  ,

  methods: {
    handleEditError(response) {
      this.snackbar = true;
      this.snackbarText = response;
    }
  }
}
</script>

<style>
</style>