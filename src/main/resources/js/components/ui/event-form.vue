<template>
  <v-card>
    <v-card-title class="text-h5">
      Добавить новое событие
    </v-card-title>
    <v-card-text>
      <v-text-field v-model="event.name"
                    type="text"
                    placeholder="Название"
      />
      <v-menu v-model="datePicker"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
              min-width="auto"
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field v-model="date"
                        label="Дата"
                        readonly
                        v-bind="attrs"
                        v-on="on"/>
        </template>
        <v-date-picker v-model="date"
                       @input="datePicker = false"
                       locale="ru"
        />
      </v-menu>
      <v-menu v-model="timePicker"
              ref="menu"
              :close-on-content-click="false"
              transition="scale-transition"
              offset-y
      >
        <template v-slot:activator="{ on, attrs }">
          <v-text-field v-model="time"
                        label="Время"
                        readonly
                        v-bind="attrs"
                        v-on="on"
          />
        </template>
        <v-time-picker v-if="timePicker"
                       v-model="time"
                       format="24hr"
                       full-width
                       @click:minute="$refs.menu.save(time)"
        />
      </v-menu>
      <v-text-field v-model="event.description"
                    type="text"
                    placeholder="Описание"/>
    </v-card-text>
    <v-card-actions>
      <v-btn @click="$emit('close')"
             color="green darken-1"
             text
      >
        Закрыть
      </v-btn>
      <v-spacer/>
      <v-btn @click="saveEvent"
             color="green darken-1"
             text
      >
        Сохранить
      </v-btn>
    </v-card-actions>
  </v-card>
</template>

<script>
import Event from "../../model/Event";

export default {
  name: "event-form",

  data() {
    return {
      event: new Event(null),
      date: '',
      time: '10:00',
      datePicker: false,
      timePicker: false,
    }
  },

  methods: {
    saveEvent() {
      this.event.date = `${this.date} ${this.time}`
      this.$emit('save', this.event)
    }
  }
}
</script>

<style scoped>

</style>