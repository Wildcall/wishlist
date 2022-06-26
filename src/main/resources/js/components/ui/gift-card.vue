<template>
  <v-card v-if="gift">
    <v-card-title>
      {{ gift.name }}
    </v-card-title>
    <v-card-subtitle>
      {{ gift.description }}
    </v-card-subtitle>
    <v-card-text>
      <div v-if="gift.link">
        Можно купить тут
      </div>
      <div v-if="gift.picture">
        {{ gift.picture }}
      </div>
      <div v-if="gift.status">
        {{ gift.status }}
      </div>
      <div v-if="gift.eventId">
        {{ gift.eventId }}
      </div>
      <div v-if="gift.tagId">
        {{ gift.tagId }}
      </div>
    </v-card-text>
  </v-card>
</template>

<script>
import Gift from "../../model/Gift";

export default {
  name: "gift-card",

  props: {
    gift: Gift
  }
}
</script>

<style scoped>

</style>