import Vue from 'vue'
import App from './App.vue'
import {createPinia, PiniaVuePlugin} from 'pinia';
import vuetify from './plugins/vuetify'
import VueResource from "vue-resource";

Vue.use(PiniaVuePlugin)
const pinia = createPinia()
Vue.use(VueResource)

new Vue({
    pinia,
    vuetify,
    render: a => a(App)
}).$mount('#app')