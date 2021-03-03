import axios from "axios";

const host = window.location.hostname;
const port = 8888;

export default axios.create({
    baseURL: `http://${host}:${port}/api`,
    withCredentials: false
});
