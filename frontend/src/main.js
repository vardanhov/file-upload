import Vue from 'vue'
import Vuetify from 'vuetify'
import '@babel/polyfill'
import './api/resource'
import router from './router/router'
import App from './components/Index.vue'
import store from './store/store.js'
import {Vuelidate} from "vuelidate";
import colors from 'vuetify/lib/util/colors'
import _ from 'lodash'

Vue.config.productionTip = false;

Vue.use(Vuetify);
Vue.use(Vuelidate);
Vue.set(Vue.prototype, "_", _);

new Vue({
    el: '#app',
    vuetify: new Vuetify({
            theme: {
                themes: {
                    light: {
                        primary: colors.blue,
                        secondary: colors.grey.lighten4,
                        accent: colors.blue.lighten3,
                        secondaryAccent: colors.blueGrey.lighten5,
                        error: colors.red.lighten4,
                        info: '#2196F3',
                        success: '#4CAF50',
                        warning: '#FFC107',
                        element: colors.shades.white,
                    }
                }
            },
        }
    ),
    store,
    router,
    render: a => a(App)
})