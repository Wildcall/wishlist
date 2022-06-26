import {defineStore} from "pinia";
import {useErrorStore} from "./error";
import Tag from "../model/Tag";
import TagService from "../services/TagService";

export interface TagStore {
    loading: boolean,
    tags: Tag[]
}

export const useTagStore = defineStore({
    id: 'tag',

    state: (): TagStore => ({
        loading: false,
        tags: []
    }),

    getters: {
        getLoading(state: TagStore): boolean {
            return state.loading
        },

        getTags(state: TagStore): Tag[] {
            return state.tags
        }
    },

    actions: {
        async create(tag: Tag) {
            console.log("TagStore / create")
            const errorStore = useErrorStore()
            this.loading = true
            await TagService.create(tag)
                .then(response => {
                    if (response.data) {
                        this.tags.push(response.data)
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },

        async getAll() {
            console.log("TagStore / getAll")
            const errorStore = useErrorStore()
            this.loading = true
            await TagService.getAll()
                .then(response => {
                    if (response.data) {
                        this.tags = response.data
                    }
                })
                .catch(error => errorStore.save(error))
                .finally(() => this.loading = false)
        },
    }
})