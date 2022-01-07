import http from './http-client';


export default {
    getGroups: () => http.get(`/groups`),
}