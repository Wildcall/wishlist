<template>
  <v-app>
    <v-main>
      <reserve v-if="reserveToken"
               :token="reserveToken"
      />
      <div v-else>
        <auth v-if="!this.userStore.getAuth"/>
        <Main v-if="this.userStore.getAuth"/>
      </div>
    </v-main>
  </v-app>
</template>

<script lang="js">
import Auth from "./components/Auth.vue";
import Reserve from "./components/Reserve.vue";
import {useUserStore} from "./store/user";
import NavBar from "./components/ui/nav-bar.vue";
import Main from "./components/Main.vue";

export default {
  name: 'App',

  components: {Main, NavBar, Auth, Reserve},

  setup() {
    return {
      userStore: useUserStore()
    }
  },

  data() {
    return {
      reserveToken: reserveToken
    }
  },

  methods: {},

  mounted() {
    if (auth) {
      this.userStore.info()
    }
  }
}
</script>

<style>
</style>
