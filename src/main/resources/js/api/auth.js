import Vue from "vue";

export const Api = {
    async login() {
        return Vue.http.get(`/oauth2/authorization/google`)
    }
}