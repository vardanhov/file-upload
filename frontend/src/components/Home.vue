<template>
  <v-card
      class="mx-auto"
      max-width="340"
      flat
  >
    <v-card-text>
      <p class="display-1 text--primary">
      </p>
      <div class="text--primary">
       Инструкция по загрузке файлов
      </div>
    </v-card-text>
    <v-card-actions>
      <v-btn
          text
          color="teal accent-4"
          @click="reveal = true"
      >
        Подробнее
      </v-btn>
    </v-card-actions>

    <v-expand-transition>
      <v-card
          v-if="reveal"
          class="transition-fast-in-fast-out v-card--reveal"
          style="height: 100%;"
      >
        <v-card-text class="pb-0">
          <p class="display-1 text--primary">

          </p>
          <p> Файлы не должны превышать 10 Мб; <br>
              Файлы загружаются в в следующие папки: dags и scripts; <br>
              Родительский скрипт должен лежать в папке dags
          </p>
        </v-card-text>
        <v-card-actions class="pt-0">
          <v-btn
              text
              color="teal accent-4"
              @click="reveal = false"
          >
            Закрыть
          </v-btn>
        </v-card-actions>
      </v-card>
    </v-expand-transition>
  </v-card>
</template>


<script>
export default {
  data: () => ({
    reveal: false,
  }),
}
</script>
