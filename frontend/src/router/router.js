import Vue from 'vue'
import VueRouter from 'vue-router'
import FileUpload from "../components/FileUpload.vue";
import Admin from "../components/Admin.vue";
import Index from "../components/Index.vue";
import Home from "../components/Home.vue";

Vue.use(VueRouter)

const routes = [
    {path: '/', component: Index},
    {path: '/admin', component: Admin},
    {path: '/upload', component: FileUpload},
    {path: '/home', component: Home},
]

export default new VueRouter({
    mode: 'history',
    routes
})
