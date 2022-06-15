<template>
  <v-container class="d-flex justify-center">
    <v-card max-width="500" min-width="300">
      <v-card-title>
        About project
      </v-card-title>
      <v-card-actions>
        <v-btn
            @click="showInfo"
        >
          test
        </v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>
import Vue from "vue";

export default {
  name: "About",
  methods: {
    showInfo() {
      Vue.http.get('/api/v1/user')
    }
  }
}
</script>

<style scoped>

</style>