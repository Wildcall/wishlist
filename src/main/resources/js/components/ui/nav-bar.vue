<template>
  <v-app-bar app>
    <v-btn icon
           class="mr-4 ml-1"
           @click="sendActiveTab('event')"
           :style="tab === 'event' ? `background-color: gray` : `white`"
    >
      <v-icon>mdi-calendar-heart</v-icon>
    </v-btn>
    <v-btn icon
           class="mr-4"
           @click="sendActiveTab('gift')"
           :style="tab === 'gift' ? `background-color: gray` : `white`"
    >
      <v-icon>mdi-heart</v-icon>
    </v-btn>
    <v-btn icon
           @click="sendActiveTab('received')"
           :style="tab === 'received' ? `background-color: gray` : `white`"
    >
      <v-icon>mdi-check-outline</v-icon>
    </v-btn>

    <v-spacer></v-spacer>

    <v-menu
        bottom
        left
    >
      <template v-slot:activator="{ on, attrs }">
        <v-btn
            icon
            v-bind="attrs"
            v-on="on"
        >
          <v-icon>mdi-account</v-icon>
        </v-btn>
      </template>
      <v-list>
        <v-list-item>
          <v-list-item-title>Профиль</v-list-item-title>
        </v-list-item>
        <v-list-item>
          <v-list-item-title>Настройки</v-list-item-title>
        </v-list-item>
        <v-list-item href="/api/v1/auth/logout">
          <v-list-item-title>Выйти</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-menu>
  </v-app-bar>
</template>

<script>
export default {
  name: "nav-bar",

  data() {
    return {
      tab: 'event'
    }
  },

  methods: {
    sendActiveTab(tab: string) {
      this.tab = tab
      this.$emit('tab', this.tab)
    }
  }
}
</script>

<style scoped>

</style>