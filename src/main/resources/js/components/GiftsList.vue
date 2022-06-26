<template>
  <div>
    <v-row no-gutters>
      <v-col
          v-for="gift in gifts"
          :key="gift.id"
          cols="6"
      >
        <gift-card
            class="ma-2"
            :gift="gift"
        />
      </v-col>
    </v-row>
  </div>
</template>

<script>
import GiftCard from "./ui/gift-card.vue"

export default {
  name: "GiftsList",
  components: {GiftCard},
  props: {
    gifts: []
  }
}
</script>

<style scoped>
</style>