import Vue from 'vue'
import Router from 'vue-router'
import Login from '@/components/Login'
import AnswerList from '@/components/main-content/AnswerList'
import Index from '@/components/Index'
import Content from '@/components/main-content/Content'
import QuestionList from '@/components/main-content/QuestionList'
import EditQuestion from '@/components/question/EditQuestion'
import QuestionDetail from '@/components/question/QuestionDetail'
import UserProfile from '@/components/user/UserProfile'
import Home from '@/components/user/Home'
import Followers from '@/components/user/Followers'
import MyFollow from '@/components/user/MyFollow'
import Reputation from '@/components/user/Reputation'
import UserAnswer from '@/components/user/UserAnswer'
import UserQuestion from '@/components/user/UserQuestion'
import NotFound from '@/components/NotFound'
import Admin from '@/components/user/Admin'
import HiddenQuestionList from '@/components/question/HiddenQuestionList'
import ModifyQuestion from '@/components/question/ModifyQuestion'
import EditQuestionType from '@/components/question/EditQuestionType'
import AddQuestionType from '@/components/question/AddQuestionType'
import QuestionSingleAnswer from '@/components/question/QuestionSingleAnswer'

Vue.use(Router)

export default new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      component: Index,
      children: [
        {
          path: '/',
          component: Content,
          children: [
            {
              name: 'allQuestion',
              path: '/',
              component: QuestionList,
              meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'editQuestion', path: '/editQuestion', component: EditQuestion, meta: {needAuth: false}
        },
        {
          name: 'questionContent',
          path: 'questions',
          component: Content,
          children: [
            {
              name: 'questions', path: '/questions/questionType/:questionType', component: QuestionList, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'searchQuestion',
          path: 'questions',
          component: Content,
          children: [
            {
              name: 'search', path: '/questions/searchQuestion/:keyWord', component: QuestionList, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'searchQuestion',
          path: 'questions',
          component: Content,
          children: [
            {
              name: 'search', path: '/questions/searchQuestion', component: QuestionList, meta: {needAuth: false}
            }
          ]
        },
        {
          path: '/user/:userId',
          component: UserProfile,
          children: [
            { name: 'userDefaultHome', path: '/', component: Home, meta: {needAuth: false, index: '1'} },
            { name: 'userHome', path: '/user/:userId/home', component: Home, meta: {needAuth: false, index: '1'} },
            { name: 'followers', path: '/user/:userId/followers', component: Followers, meta: {needAuth: false, index: ''} },
            { name: 'myFollow', path: '/user/:userId/myFollow', component: MyFollow, meta: {needAuth: false, index: ''} },
            { name: 'reputation', path: '/user/:userId/reputation', component: Reputation, meta: {needAuth: false, index: '5'} },
            { name: 'userAnswer', path: '/user/:userId/userAnswer', component: UserAnswer, meta: {needAuth: false, index: '3'} },
            { name: 'userQuestion', path: '/user/:userId/userQuestion', component: UserQuestion, meta: {needAuth: false, index: '2'} }
          ]
        },
        {
          name: 'admin',
          path: '/admin',
          component: Admin,
          children: [
            {
              name: 'hiddenQuestion', path: '/admin/hiddenQuestion', component: HiddenQuestionList, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'admin',
          path: '/admin',
          component: Admin,
          children: [
            {
              name: 'hiddenAnswer', path: '/admin/hiddenAnswer', component: AnswerList, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'admin',
          path: '/admin',
          component: Admin,
          children: [
            {
              name: 'modifyType', path: '/admin/modifyType', component: EditQuestionType, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'admin',
          path: '/admin',
          component: Admin,
          children: [
            {
              name: 'addType', path: '/admin/addType', component: AddQuestionType, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'admin',
          path: '/admin',
          component: Admin,
          children: [
            {
              name: 'allQuestions', path: '/admin/allQuestion', component: QuestionList, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'admin',
          path: '/admin',
          component: Admin,
          children: [
            {
              name: 'questions', path: '/admin/questions/questionType/:questionType', component: QuestionList, meta: {needAuth: false}
            }
          ]
        },
        {
          name: 'questionDetail',
          path: '/questionDetail/:questionId',
          component: QuestionDetail,
          meta: {needAuth: false}
        },
        {
          name: 'modifyQuestion',
          path: '/questionDetail/:questionId/modify',
          component: ModifyQuestion,
          meta: {needAuth: false}
        },
        {
          name: 'questionSingleAnswer', path: '/questionDetail/:questionId/:answerId', component: QuestionSingleAnswer, meta: {needAuth: false}
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: Login,
      props: (route) => ({ query: route.query.register }),
      meta: {needAuth: false}
    },
    {
      path: '*',
      name: 'notFound',
      component: NotFound,
      meta: {needAuth: false}
    }
  ]
})
