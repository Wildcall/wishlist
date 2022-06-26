import axios, {AxiosResponse} from "axios";
import Tag from "../model/Tag";

const URI = "/api/v1/tag";

export default class TagService {

    static async create(tag: Tag): Promise<AxiosResponse<Tag>> {
        console.log("TagService / create")
        return axios.post(URI, tag)
    }

    static async update(tag: Tag): Promise<AxiosResponse<Tag>> {
        console.log("TagService / update")
        return axios.put(`${URI}/${tag.id}`, tag)
    }

    static async getAll(): Promise<AxiosResponse<Tag[]>> {
        console.log("TagService / getAll")
        return axios.get(URI)
    }

    static async delete(tag: Tag): Promise<AxiosResponse<number>> {
        console.log("TagService / delete")
        return axios.delete(`${URI}/${tag.id}`)
    }
}
