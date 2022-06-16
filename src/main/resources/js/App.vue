<template>
  <v-app>
    <v-main>
      <div v-if="isAuth">
        <h1>Вы авторизованы</h1>
      </div>
      <div>
        With Google: <a href="/oauth2/authorization/google">click here</a>
      </div>
      <div>
        Logout: <a href="/api/v1/auth/logout">click here</a>
      </div>
      <v-btn
          @click="deleteUser"
      >
        Delete
      </v-btn>
    </v-main>
  </v-app>
</template>

<script>
import Vue from "vue";

export default {
  name: 'App',

  data: () => ({
    isAuth: auth
  }),

  methods: {
    deleteUser() {
      Vue.http.delete("/api/v1/user")
      //     .then(response => {
      //   if (response) {
      //     const location = response.headers.get("location");
      //     if (location)
      //       Vue.http.get(location)
      //   }
      // })
    }
  },

  mounted() {
  }
}
</script>

<style>
</style>
