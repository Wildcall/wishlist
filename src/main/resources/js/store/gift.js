import {defineStore} from "pinia";
import {useErrorStore} from "./error";
import Gift from "../model/Gift";
import GiftService from "../services/GiftService";

export interface GiftStore {
    loading: boolean,
    gifts: Gift[]
}

export const useGiftStore = defineStore({
    id: 'gift',

    state: (): GiftStore => ({
        loading: false,
        gifts: []
    }),

    getters: {
        getLoading(state: GiftStore): boolean {
            return state.loading
        },

        getGifts(state: GiftStore): Gift[] {
            return state.gifts
        },

        getReceivedGifts(state: GiftStore): Gift[] {
            return state.gifts.filter(obj => obj.status === 'RECEIVED')
        }
    },

    actions: {
        async create(gift: Gift) {
            console.log("GiftStore / create")
            const errorStore = useErrorStore()
            this.loading = true
            await GiftService.create(gift)
                .then(response => {
                    if (response.data) {
                        this.gifts.push(response.data)
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async getAll() {
            console.log("GiftStore / getAll")
            const errorStore = useErrorStore()
            this.loading = true
            await GiftService.getAll()
                .then(response => {
                    if (response.data) {
                        this.gifts = response.data
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },
    }
})