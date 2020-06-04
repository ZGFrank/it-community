import axios from 'axios'

const http = axios.create({
  baseURL: 'http://192.168.101.21:8888/itc',
  timeout: 50000
})

//请求前拦截器
http.interceptors.request.use(
  config => {
    const token = window.sessionStorage.getItem('token')
    if (token)
      config.headers.Authorization = token
    return config
  },
  err => {
  }
)

//响应后拦截器
http.interceptors.response.use(res => res.data)
export default http