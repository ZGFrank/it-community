import Vue from 'vue'
import App from './App.vue'
//element-ui
import './plugins/element.js'
//路由
import router from './router'
//数据访问
import http from "./network/http";
//vuex
import store from './store'
//markdown编辑器
import mavonEditor from 'mavon-editor'
import 'mavon-editor/dist/css/index.css'
//富文本编辑器
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css'
import 'quill/dist/quill.snow.css'
import 'quill/dist/quill.bubble.css'

import WriteBar from "./components/article/WriteBar"

import getFriendlyTime from "./assets/js/TimeUtils"

Vue.use(mavonEditor)

Vue.config.productionTip = false
Vue.prototype.$http = http
Vue.prototype.$md =  mavonEditor.markdownIt
Vue.prototype.$baseURL = 'http://192.168.101.21:8888/itc'
Vue.prototype.$fileURL = 'http://192.168.56.110/'
Vue.prototype.$getFriendlyTime = getFriendlyTime

Vue.component("WriteBar",WriteBar)
Vue.use(VueQuillEditor)
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
