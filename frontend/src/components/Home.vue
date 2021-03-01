<template>
  <v-card
      class="mx-auto"
      max-width="800"
      flat
  >
    <div class="pt-16"></div>
    <div class="pt-4"></div>

    <v-card>
      <v-img
          :src="cardFirst.src"
          class="white--text align-end card_img"
          gradient="to bottom, rgba(0,119,221, .2), rgba(0,119,221, .7)"
          height="200px"
      >
        <v-card-title style="font-size: 40px;" v-text="cardFirst.title"></v-card-title>
      </v-img>

      <v-card-actions style="background: rgba(0,119,221, .1)">
        <v-spacer></v-spacer>
        <v-btn
            icon
            @click="show = !show"
        >
          <v-icon>{{ show ? 'mdi-chevron-up' : 'mdi-chevron-down' }}</v-icon>
        </v-btn>
      </v-card-actions>
      <v-expand-transition>
        <div v-show="show">
          <v-divider></v-divider>

      <v-card-text style="font-weight: 900; font-size: 12px;">
        Данный инструмент является планировщиком для выдачи прав на загрузку дагов и скриптов.
        <br>
        При загрузке файлов "владелец" и "группа" соответствуют имени и группе пользователя.
      </v-card-text>

        </div>
      </v-expand-transition>
    </v-card>

    <v-container fluid>
      <v-row dense>
        <v-col
            v-for="card in cards"
            :key="card.title"
            :cols="card.flex"
        >

          <v-card >
            <v-img
                :src="card.src"
                class="white--text align-end"
                gradient="to bottom, rgba(0,119,221, .2), rgba(0,119,221, .7)"
                height="200px"
                width ="800px"
            >
              <v-card-title  v-text="card.title"></v-card-title>
            </v-img>

            <v-card-actions style="background: rgba(0,119,221, .1)">
              <v-spacer></v-spacer>
              <v-btn
                  color="primary"
                  text
                  :to="card.url"
                  :title="card.label"
              >
                Открыть
              </v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </v-card>
</template>


<script>
export default {
  data: () => ({
    reveal: false,
    show: false,
    cardFirst: { title: 'Инструкция', src: '', flex: 11 },
    cards: [
      { title: 'Загрузка файлов', src: '', flex: 6, label: 'Отправить файл', url: '/upload' },
      { title: 'Панель управления пользователями', src: '', flex: 6, label: 'Панель управления', url: '/admin' },
    ],
    links: [
      {
        label: 'Отправить файл',
        url: '/upload'
      },
      {
        label: 'Панель управления',
        url: '/admin'
      }
    ]
  }),
}
</script>
