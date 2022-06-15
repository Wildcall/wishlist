<template>
  <v-app>
    <v-app-bar app>
      <v-container class="d-flex justify-end">
        <v-btn
            v-if="isAuth"
            href="/api/v1/auth/logout"
            text
            plain
        >
          Logout
        </v-btn>
      </v-container>
    </v-app-bar>
    <v-main>
      <auth v-if="!isAuth"/>
      <about v-if="isAuth"/>
    </v-main>
  </v-app>
</template>

<script lang="js">
import Auth from "./components/Auth.vue";
import About from "./components/About.vue";

export default {
  name: 'App',

  components: {About, Auth},

  data() {
    return {
      isAuth: auth
    }
  },

  methods: {},

  mounted() {
  }
}
</script>

<style>
</style>
