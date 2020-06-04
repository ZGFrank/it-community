import Vue from 'vue'
import VueRouter from 'vue-router'

import Test from "../views/Test"

import Index from "../views/Index"
import Interchange from "../views/front/interchange/Interchange"
import SearchArticle from "../views/front/interchange/SearchArticle"
import Case from "../views/front/case/Case"
import Exchange from "../views/front/exchange/Exchange"
import Main from "../views/front/interchange/Main"
import Login from "../views/Login"
import User from "../views/front/user/User"
import UserSetting from "../views/front/user/UserSetting"
import UserCenter from "../views/front/user/UserCenter"
import UserPage from "../views/front/user/UserPage"
import UserMessage from "../views/front/user/UserMessage"
import Write from "../views/front/article/Write"
import Detail from "../views/front/article/Detail"

import Back from "../views/back/Back"
import DeskTop from "../views/back/DeskTop"
import CategoryManager from "../views/back/category/CategoryManager"
import ArticleManager from "../views/back/article/ArticleManager"
import CaseReview from "../views/back/case/CaseReview"
import CaseManager from "../views/back/case/CaseManager"
import ExchangeReview from "../views/back/exchange/ExchangeReview"
import ExchangeManager from "../views/back/exchange/ExchangeManager"
import RightsManager from "../views/back/power/RightsManager"
import RoleManager from "../views/back/power/RoleManager"
import AdminManager from "../views/back/power/AdminManager"
import UserManager from "../views/back/user/UserManager"
import NewsManager from "../views/back/news/NewsManager"

Vue.use(VueRouter)

//component: () => import('../views/About.vue')
const routes = [
  {
    path: '/',
    redirect: '/index',
  },
  {
    path: '/test',
    component: Test
  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/back',
    component: Back,
    redirect: '/back/desktop',
    children: [
      {
        path: 'desktop',
        component: DeskTop
      },
      {
        path: 'categoryManager',
        component: CategoryManager
      },
      {
        path: 'articleManager',
        component: ArticleManager
      },
      {
        path: 'caseReview',
        component: CaseReview
      },
      {
        path: 'caseManager',
        component: CaseManager
      },
      {
        path: 'exchangeReview',
        component: ExchangeReview
      },
      {
        path: 'exchangeManager',
        component: ExchangeManager
      },
      {
        path: 'userManager',
        component: UserManager
      },
      {
        path: 'rightsManager',
        component: RightsManager
      },
      {
        path: 'roleManager',
        component: RoleManager
      },
      {
        path: 'adminManager',
        component: AdminManager
      },
      {
        path: 'newsManager',
        component: NewsManager
      }
    ]
  },
  {
    path: '/index',
    component: Index,
    redirect: '/interchange',
    children: [
      {
        path: '/interchange',
        name: 'Interchange',
        component: Interchange,
        redirect: '/main',
        children: [
          {
            path: '/main',
            component: Main
          },
          {
            path: '/articleDetail/:artId',
            component: Detail
          },
          {
            path: '/search',
            component: SearchArticle
          }
        ]
      },
      {
        path: '/case',
        component: Case
      },
      {
        path: '/exchange',
        component: Exchange
      },
      {
        path: '/user',
        component: User,
        children: [
          {
            path: '/userSetting',
            component: UserSetting
          },
          {
            path: '/userCenter',
            component: UserCenter
          },
          {
            path: '/userMessage',
            component: UserMessage
          }
        ]
      },
      {
        path: '/userPage/:uId',
        component: UserPage
      },
      {
        path: '/write/:artId',
        component: Write
      }
    ]
  }

]

const router = new VueRouter({
  routes
})
const remind = new Vue()
// 挂载路由导航守卫
router.beforeEach((to, from, next) => {
  // to 将要访问的路径
  // from 代表从哪个路径跳转而来
  // next 是一个函数，表示放行
  //     next()  放行    next('/login')  强制跳转
  if (to.path.includes("write") || to.path.includes("user")) {
    // 获取token
    const tokenStr = window.sessionStorage.getItem('token')
    if (!tokenStr) return next('/login')
  }
  //处理非常规进入后台
  if (to.path.includes("back")) {
    const user = JSON.parse(window.sessionStorage.getItem("userInfo"))
    if (user == null) {
      remind.$message('您未登录，不能进入系统后台')
      return next('/login')
    }else if (user.roleId == null) {
      remind.$message('您没有权限，不能进入系统后台')
      return next('/main')
    }
  }

  next()
})

export default router
