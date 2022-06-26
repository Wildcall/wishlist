import axios, {AxiosResponse} from "axios";
import Event from "../model/Event";

const URI = "/api/v1/event";

export default class EventService {

    static async create(event: Event): Promise<AxiosResponse<Event>> {
        console.log("EventService / create")
        return axios.post(URI, event)
    }

    static async update(event: Event): Promise<AxiosResponse<Event>> {
        console.log("EventService / update")
        return axios.put(`${URI}/${event.id}`, event)
    }

    static async getAll(): Promise<AxiosResponse<Event[]>> {
        console.log("EventService / getAll")
        return axios.get(URI)
    }

    static async delete(event: Event): Promise<AxiosResponse<number>> {
        console.log("EventService / delete")
        return axios.delete(`${URI}/${event.id}`)
    }
}
