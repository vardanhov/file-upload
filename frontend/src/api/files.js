import http from './http-client';


export default {
    getFilesByName: userName => http.get(`/admin/events/${userName}`, userName),
    getFiles: (page, size) => http.get(`/users/history`),
    // getFilesByName: name => http.get(`/users/history-by-name/${name}`, name)
}