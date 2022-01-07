import filesApi from "../../api/files";
import moment from 'moment'

export const filesModule = {
    state: {
        files: []
    },
    getters: {
        getFiles: state => state.files,
    },
    mutations: {
        FETCH_FILES(state, files) {
            files.map(el => el['actionDateTime'] = moment.utc(el['actionDateTime']).local().format('YYYY-MM-DD HH:mm'))
            state.files = files
            console.log(state.files)
            console.log("files fetched")
        },

    },
    actions: {
        async fetchFiles({commit, getters, dispatch}) {
            const files = await filesApi.getFiles()
            commit('FETCH_FILES', files.data.content)
        },
        async fetchFilesByName({commit, getters, dispatch}, userName) {
            const files = await filesApi.getFilesByName(userName)
            commit('FETCH_FILES', files.data.content)
        },
    }
}