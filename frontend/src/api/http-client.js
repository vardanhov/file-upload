import axios from "axios";

const host = "localhost";
const port = 8888;

export default axios.create({
    baseURL: `http://${host}:${port}/api`,
    withCredentials: false
});
