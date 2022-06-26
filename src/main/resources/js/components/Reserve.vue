<template>
  <v-container class="d-flex justify-center">
    <v-card
        v-if="event"
        max-width="500"
        min-width="300">
      <v-card-title>
        {{ event.name }}
      </v-card-title>
      <v-card-subtitle
          v-if="event.description"
      >
        {{ event.description }}
      </v-card-subtitle>
      <v-card-text>
        <gift v-for="gift in event.giftsSet"
              :key="gift.id"
              :gift="gift"
              class="mb-4"
        />
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>
import Event from "../model/Event";
import Gift from "./GiftsList.vue";
import {loadEvent} from "../api/reserve";

export default {
  name: "Reserve",
  components: {Gift},
  data() {
    return {
      event: null
    }
  },

  props: {
    token: null
  },

  mounted() {
    if (this.token)
      loadEvent(this.token).then(r => {
        if (r.data) {
          this.event = new Event(r.data)
        }
      })
  }
}
</script>

<style scoped>

</style>