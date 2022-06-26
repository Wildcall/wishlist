import axios from "axios";

async function loadEvent(token) {
    return axios.get(`/api/v1/reserved/event?token=${token}`)
}

export {loadEvent}