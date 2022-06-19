<template>
  <v-container class="d-flex justify-center">
    <v-card max-width="500" min-width="300">
      <v-card-title class="justify-center">
        Войти
      </v-card-title>
      <v-card-actions class="flex-column">
        <v-img
            v-if="!loginDialog"
            @click="showLoginDialog"
            contain
            max-height="50"
            max-width="200"
            :src="emailSignin"
        />
        <v-form v-else>
          <v-text-field>

          </v-text-field>
          <v-text-field>

          </v-text-field>
          <v-btn>Войти</v-btn>
        </v-form>
        <v-divider inset class="mt-4 mb-4" dark/>
        <a :href="link">
          <v-img
              contain
              max-height="50"
              max-width="200"
              :src="googleSignin"
          />
        </a>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<script>

export default {
  name: "Auth",

  data() {
    return {
      loginDialog: false,
      link: '/oauth2/authorization/google',
      googleSignin: '../assets/btn_google_signin.png',
      emailSignin: '../assets/btn_email_signin.png'
    }
  },

  methods: {
    showLoginDialog() {
      this.loginDialog = true
    }
  }
}
</script>

<style scoped>

</style>