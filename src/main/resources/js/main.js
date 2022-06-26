import Vue from 'vue'
import App from './App.vue'
import {createPinia, PiniaVuePlugin} from 'pinia';
import vuetify from './plugins/vuetify'

Vue.use(PiniaVuePlugin)
const pinia = createPinia()

new Vue({
    pinia,
    vuetify,
    render: a => a(App)
}).$mount('#app')