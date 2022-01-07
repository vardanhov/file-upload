import groupsApi from "../../api/groups.js";

export const groupsModule = {
    state: {
        groups: []
    },
    getters: {
        getGroups: state => state.groups,
    },
    mutations: {
        FETCH_GROUPS(state, groups) {
            state.groups = groups
        },

    },
    actions: {
        async fetchGroups({commit, getters, dispatch}) {
            const groups = await groupsApi.getGroups()
            commit('FETCH_GROUPS', groups.data)
            console.log(groups.data)
        },
    }
}