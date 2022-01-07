<template>
  <v-card>
    <v-app-bar
        color="primary"
        dense
        dark
        clipped-right
        flat
        app
    >
      <v-toolbar-title>Загрузчик дагов</v-toolbar-title>
      <v-spacer></v-spacer>
      <div class="text-center">
        <v-menu offset-y>
          <template v-slot:activator="{ on }" >
            <v-btn
                color="primary"
                v-on="on"
                text
            >
              <v-icon id="menu" color="white">mdi-menu</v-icon>
            </v-btn>
          </template>
          <v-list
              nav
              dense
          >
            <v-list-item-group
                v-model="group"
                active-class="grey--text text--accent-4"
            >
              <v-list-item
                  v-for="(link, i) in links"
                  :key="i"
                  :to="link.url"
                  @click="drawer=false"
                  v-ripple
              >
                <v-tooltip>
                  <template v-slot:activator="{ on } ">
                    <v-list-item-icon>
                      <v-icon
                          text round
                          v-text="link.icon"
                      ></v-icon>
                    </v-list-item-icon>
                    <v-list-item-content>
                      <v-list-item-title
                          v-text="link.label"
                      >
                      </v-list-item-title>
                    </v-list-item-content>
                  </template>
                  <span v-text="link.label"></span>
                </v-tooltip>
              </v-list-item>
            </v-list-item-group>
          </v-list>
          <form method="post" action="/logout" style="width: 100%; background-color: #fff">
            <v-btn id="exit" type="submit" text>
              <v-icon color="grey">mdi-logout</v-icon>
              <span style="color: gray; margin-left: 33px"></span> Выйти
            </v-btn>
          </form>
        </v-menu>
      </div>
    </v-app-bar>
  </v-card>
</template>

<script>
import axios from "axios";

export default {
  data: () => ({
    drawer: false,
    group: null,
    links: [
      {
        name: 'На главную',
        label: 'На главную',
        url: '/home',
        icon: 'mdi-home-outline',
      },
      {
        label: 'Профиль',
        url: '/account',
        icon: 'mdi-account',
      },
      {
        label: 'Отправить файл',
        url: '/upload',
        icon: 'mdi-paperclip',
      },
      {
        label: 'Панель управления',
        url: '/admin',
        icon: 'mdi-account-group',
      },
      {
        label: 'Аудит',
        url: '/audit',
        icon: 'mdi-account-details',
      },

    ]

  }),

}
</script>
