import http from './http-client';


export default {
    getById: guid => http.get(`/users/${guid}`, guid),
    getUsers: page => http.get('/whitelist')
}