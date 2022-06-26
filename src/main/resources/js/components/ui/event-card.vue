<template>
  <v-card class="ma-2" flat>
    <v-card-title>
      {{event.name}}
    </v-card-title>
    <v-subheader>
      {{event.date}}
    </v-subheader>
  </v-card>
</template>

<script>
import Event from "../../model/Event";

export default {
  name: "event-card",
  props: {
    event: Event
  }
}
</script>

<style scoped>

</style>