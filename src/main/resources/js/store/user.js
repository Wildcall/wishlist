import {User} from "../model/User";
import {defineStore} from "pinia";
import {useErrorStore} from "./error";
import UserService from "../services/UserService";

export interface UserStore {
    auth: boolean,
    loading: boolean,
    user: User
}

export const useUserStore = defineStore({
    id: 'user',

    state: (): UserStore => ({
        auth: false, loading: false, user: {}
    }),

    getters: {
        getAuth(state: UserStore): boolean {
            return state.auth
        },

        getLoading(state: UserStore): boolean {
            return state.loading
        },

        getUser(state: UserStore): User {
            return state.user
        }
    },

    actions: {
        async registration(name: string, email: string, password: string) {
            console.log("UserStore / registration")
            const errorStore = useErrorStore()
            this.loading = true
            await UserService.registration(name, email, password)
                .then(response => {
                    if (response.data) {
                        this.user = response.data
                        this.auth = true
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async login(email: string, password: string) {
            console.log("UserStore / login")
            const errorStore = useErrorStore()
            this.loading = true
            await UserService.login(email, password)
                .then(response => {
                    if (response.data) {
                        this.user = response.data
                        this.auth = true
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async info() {
            console.log("UserStore / info")
            const errorStore = useErrorStore()
            this.loading = true
            await UserService.info()
                .then(response => {
                    if (response.data) {
                        this.user = response.data
                        this.auth = true
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },
    }
})