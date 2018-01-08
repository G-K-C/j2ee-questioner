<template>
  <div>
    <question @onPublisherId="initQuestionPublisherId($event)" :isAllAnswer="false"></question>
    <div v-if="(this.hasLogin&&this.user.roles[0].id==2)||!this.questionIsHidden" style="padding-right: 1rem">
      <el-button @click="$router.push({ path: `/questionDetail/${questionId}` })" style="float: right" size="mini"  type="success">查看该问题的所有答案</el-button>
    </div>
    <div v-if="(this.hasLogin&&this.user.roles[0].id==2)||!this.questionIsHidden" id="answers-panel" v-loading.lock="loadingAnswer">
      <answer v-if="answer" :isCurrentUser="isCurrentUser" :answer="answer" ></answer>
    </div>
  </div>
</template>
<style scoped>
  #answers-panel{
    border: 1px solid #ddd;
    background: #f9fafc;
    padding: 2rem;
    margin: 1rem;
    clear: both;
  }
</style>
<script>
  import Question from '../question/QuestionComponent.vue'
  import Answer from '../answer/Answer.vue'
  import { getQuestionSingleAnswer } from '@/api/answer'
  import { getQuestionHiddenStatus } from '@/api/question'
  import { Message } from 'element-ui'
  import { mapGetters } from 'vuex'

  export default {
    components: { 'question': Question, 'answer': Answer },
    data () {
      return {
        questionId: null,
        questionIsHidden: true,
        answerId: null,
        answer: null,
        loadingAnswer: false,
        publisherId: null
      }
    },
    computed: {
      ...mapGetters(['hasLogin', 'user']),
      isCurrentUser: function () {
        return this.user !== null && this.publisherId === this.user.id
      }
    },
    mounted: function () {
      this.questionId = this.$route.params.questionId
      this.answerId = this.$route.params.answerId
      this.questionIsHidden = this.getQuestionHidden()
      this.getAnswer()
    },
    methods: {
      getAnswer () {
        this.isLoadingData = true
        let _this = this
        getQuestionSingleAnswer(this.questionId, this.answerId).then((response) => {
          if (response.status === '404') {
            Message({
              message: '找不到答案！',
              type: 'error',
              duration: 1000
            })
          } else if (response.status === '200') {
            _this.answer = response.result
          }
          _this.isLoadingData = false
        }).catch((e) => {
          Message({
            message: '获取答案失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      getQuestionHidden () {
        let _this = this
        getQuestionHiddenStatus(this.questionId).then((response) => {
          if (response.status === '404') {
            Message({
              message: '找不到问题！',
              type: 'error',
              duration: 1000
            })
          } else if (response.status === '200') {
            _this.questionIsHidden = response.result
          }
          _this.isLoadingData = false
        }).catch((e) => {
          Message({
            message: '获取答案失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      initQuestionPublisherId (userId) {
        this.publisherId = userId
      }
    },
    watch: {
      '$route' (to, from) {
        this.questionId = to.params.questionId
        this.answerId = to.params.answerId
        this.getAnswer()
      }
    }
  }
</script>
