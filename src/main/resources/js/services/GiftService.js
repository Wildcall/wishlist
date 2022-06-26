import axios, {AxiosResponse} from "axios";
import Gift from "../model/Gift";

const URI = "/api/v1/gift";

export default class GiftService {

    static async create(gift: Gift): Promise<AxiosResponse<Gift>> {
        console.log("GiftService / create")
        return axios.post(URI, gift)
    }

    static async update(gift: Gift): Promise<AxiosResponse<Gift>> {
        console.log("GiftService / update")
        return axios.put(`${URI}/${gift.id}`, gift)
    }

    static async getAll(): Promise<AxiosResponse<Gift[]>> {
        console.log("GiftService / getAll")
        return axios.get(URI)
    }

    static async delete(gift: Gift): Promise<AxiosResponse<number>> {
        console.log("GiftService / delete")
        return axios.delete(`${URI}/${gift.id}`)
    }
}
