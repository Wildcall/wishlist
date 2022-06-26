import {defineStore} from "pinia";
import {useErrorStore} from "./error";
import Event from "../model/Event";
import EventService from "../services/EventService";

export interface EventStore {
    loading: boolean,
    events: Event[]
}

export const useEventStore = defineStore({
    id: 'event',

    state: (): EventStore => ({
        loading: false,
        events: []
    }),

    getters: {
        getLoading(state: EventStore): boolean {
            return state.loading
        },

        getActualEvents(state: EventStore): Event[] {
            return state.events
                .filter(obj => new Date() <= new Date(obj.date))
                .sort((a, b) => new Date(a.date) - new Date(b.date))
        },

        getExpEvents(state: EventStore): Event[] {
            return state.events
                .filter(obj => new Date() > new Date(obj.date))
                .sort((a, b) => new Date(a.date) - new Date(b.date))
        },
    },

    actions: {
        async create(event: Event) {
            console.log("EventStore / create")
            const errorStore = useErrorStore()
            this.loading = true
            await EventService.create(event)
                .then(response => {
                    if (response.data) {
                        this.events.push(response.data)
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async getAll() {
            console.log("EventStore / getAll")
            const errorStore = useErrorStore()
            this.loading = true
            await EventService.getAll()
                .then(response => {
                    if (response.data) {
                        this.events = response.data
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },
    }
})