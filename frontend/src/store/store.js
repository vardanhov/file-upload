import Vue from 'vue'
import Vuex from 'vuex'
import {usersModule} from "./modules/users-store-module";
import {filesModule} from "./modules/files-store-module";
import {groupsModule} from "./modules/group-store-module";

Vue.use(Vuex);
export default new Vuex.Store({
    modules: {
        users: usersModule,
        files: filesModule,
        groups:groupsModule
    }
})