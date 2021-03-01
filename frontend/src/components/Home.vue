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
          class="white--text align-end"
          gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
          height="200px"
      >
        <v-card-title v-text="cardFirst.title"></v-card-title>
      </v-img>

      <v-card-actions>
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
      <v-card-text>
        Данный инструмент является планировщиком для выдачи прав на загрузку дагов и скриптов. При загрузке файлов
        owner и group равны имени и группе пользователя.
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
                gradient="to bottom, rgba(0,0,0,.1), rgba(0,0,0,.5)"
                height="200px"
                width ="800px"
            >
              <v-card-title v-text="card.title"></v-card-title>
            </v-img>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn
                  color="primary"
                  text
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
      { title: 'Загрузка файлов', src: '', flex: 6 },
      { title: 'Панель управления пользователями', src: '', flex: 6 },
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
