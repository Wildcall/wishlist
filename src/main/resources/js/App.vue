<template>
<v-app>
  <v-main>
  <div>
    With Google: <a href="/oauth2/authorization/google">click here</a>
  </div>
  <div>
    Logout: <a href="/api/v1/auth/logout">click here</a>
  </div>
  </v-main>
</v-app>
</template>

<script lang="ts">
import { defineComponent } from 'vue'

export default defineComponent ({
  name: 'App',

  data() {
  },

  mounted() {
  }
})
</script>

<style>
</style>
