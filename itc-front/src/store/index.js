import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    userInfo: JSON.parse(window.sessionStorage.getItem("userInfo") || '{}'),
    msgCount:0,
    breadcrumb:{}
  },
  getters:{
    getUserId(state) {
      if (Object.keys(state.userInfo).length){
        return state.userInfo.uId
      }else{
        return null
      }
    },
    getUserAccount(state) {
      if (Object.keys(state.userInfo).length){
        return state.userInfo.account
      }else{
        return null
      }
    }
  },
  mutations: {
    setBreadcrumb(state, object) {
      state.breadcrumb = object
    },
    saveUser(state, user) {
      window.sessionStorage.setItem("userInfo", JSON.stringify(user))
      state.userInfo = user
    },
    setMsgCount(state, count) {
      state.msgCount = count
    },
    clearUser(state) {
      window.sessionStorage.clear()
      state.userInfo = {}
    },
    //-learnCoin
    decreaseLearnCoin(state, coin) {
      state.userInfo.learnCoin -= coin
      window.sessionStorage.setItem("userInfo", JSON.stringify(state.userInfo))
    }
  },
  actions: {},
  modules: {}
})
