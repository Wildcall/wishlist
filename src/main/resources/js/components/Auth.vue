<template>
  <v-container
      class="d-flex justify-center"
  >
    <v-card max-width="500"
            min-width="300"
            :flat="$vuetify.breakpoint.mobile"
    >
      <v-card-title class="justify-center">
        Войти
      </v-card-title>
      <v-card-text>
        <v-card flat
                class="pa-4">
          <v-text-field
              v-if="registrationForm"
              v-model="form.name"
              type="text"
              placeholder="Введите имя"
          />
          <v-text-field
              v-model="form.email"
              type="text"
              placeholder="Введите почту"
          />
          <v-text-field
              v-model="form.password"
              type="password"
              placeholder="Введите пароль"
          />
          <v-card-actions>
            <v-row>
              <v-col
                  class="d-flex justify-center"
                  cols="12">
                <v-btn
                    @click="authAction"
                    color="white"
                >
                  {{ registrationForm ? 'Регистрация' : 'Войти' }}
                </v-btn>
              </v-col>
              <v-col
                  class="d-flex justify-center align-center"
                  cols="12">
                {{ registrationForm ? 'Уже есть аккаунт?' : 'Еще нет аккаунта?' }}
                <v-btn
                    small
                    text
                    @click="changeForm"
                    class="font-weight-bold"
                >
                  {{ registrationForm ? 'Войти' : 'Создать' }}
                </v-btn>
              </v-col>
            </v-row>
          </v-card-actions>
        </v-card>
        <div class="d-flex justify-center">или</div>
        <div class="d-flex justify-center">
          <a
              :href="link"
          >
            <v-img
                :src="googleOauthIcon"
                contain/>
          </a>
        </div>
      </v-card-text>
    </v-card>
  </v-container>
</template>

<script>

import {useUserStore} from "../store/user";

export default {
  name: "Auth",

  setup() {
    return {
      userStore: useUserStore()
    }
  },

  data() {
    return {
      form: {
        name: '',
        email: '',
        password: ''
      },
      loginForm: true,
      registrationForm: false,

      googleOauthIcon: '../assets/btn_google_light_normal_hdpi.9.png',
      link: '/oauth2/authorization/google',
    }
  },

  methods: {
    changeForm() {
      this.registrationForm = !this.registrationForm
      this.resetForm()
    },

    async authAction() {
      if (this.registrationForm)
        this.registration()
      else
        this.login()
    },

    registration() {
      this.userStore.registration(this.form.name, this.form.email, this.form.password)
    },

    login() {
      this.userStore.login(this.form.email, this.form.password)
    },

    resetForm() {
      this.form = {}
    }
  }
}
</script>

<style scoped>

</style>