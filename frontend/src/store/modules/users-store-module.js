import usersApi from "../../api/users";

export const usersModule = {
    state: {
        users: []
    },
    getters: {
        getUsers: state => state.users,
    },
    mutations: {
        FETCH_USERS(state, users) {
            state.users = users
            console.log(state.users)
            console.log("users fetched")
        },

    },
    actions: {
        async fetchUsers({commit, getters, dispatch}) {
            const users = await usersApi.getUsers()
            commit('FETCH_USERS', users.data)
        },
    }
}