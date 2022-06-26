import {defineStore} from "pinia";
import {AxiosError} from "axios";
import {ApiError, SubError} from "../model/ApiError";

export interface ErrorState {
    error: boolean,
    apiError: ApiError
}

export const useErrorStore = defineStore({
    id: 'error',

    state: (): ErrorState => ({
        error: false,
        apiError: {},
    }),

    getters: {
        hasError(state: ErrorState): boolean {
            return state.error
        },

        getMessage(state: ErrorState): string {
            if (state?.apiError?.message) return state.apiError.message
            return "Unexpected error"
        },

        getStatus(state: ErrorState): string {
            return state.apiError.status
        },

        getSubErrors(state: ErrorState): [SubError] {
            return state.apiError.subErrors
        }
    },

    actions: {
        save(axiosError: AxiosError) {
            console.log("Error / save")
            this.$patch({
                apiError: axiosError.response?.data,
                error: true
            })
        },

        remove() {
            console.log("Error / remove")
            this.$patch({
                apiError: {},
                error: false
            })
        }
    }
})