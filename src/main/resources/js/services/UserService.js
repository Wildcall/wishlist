import axios, {AxiosResponse} from "axios";
import qs from "qs";
import {User} from "../model/User";

export default class UserService {

    static async registration(name: string, email: string, password: string): Promise<AxiosResponse<User>> {
        console.log("UserService / login")
        const data = {
            name,
            email,
            password
        }
        return axios.post(`/api/v1/user/registration`, data)
    }

    static async login(email: string, password: string): Promise<AxiosResponse<User>> {
        console.log("UserService / login")
        const data = {
            email,
            password
        }
        return axios.post(`/api/v1/auth/login`, qs.stringify(data))
    }

    static async info(): Promise<AxiosResponse<User>> {
        console.log("UserService / info")
        return axios.get(`/api/v1/user`)
    }

    static async delete(): Promise<AxiosResponse<string>> {
        console.log("UserService / delete")
        return axios.delete(`/api/v1/user`)
    }
}
