import axios from "axios";

const host = "localhost";
const port = 443;

export default axios.create({
    baseURL: `https://${host}:${port}/api`,
    withCredentials: false
});
