<template>
  <div id="questionContent" v-loading='questionLoading' element-loading-text="拼命加载中">
    <div id="question-top-panel">
      <div class="question-title">
        <h3>
          {{ question.questionTitle }}
        </h3>
      </div>
    </div>
    <div v-if="(this.hasLogin&&this.user.roles[0].id==2)||!question.questionIsHidden||(question.questionIsHidden&&this.hasLogin&&this.user.id==this.question.accountId)" id="question-detail"
         v-html="question.questionContent">
    </div>
    <div v-if="this.hasLogin&&this.user.roles[0].id==2" class="question-info" >
      <el-tooltip v-if='this.question.questionIsHidden' content="取消屏蔽后用户将能浏览此问题" placement="top">
        <el-button :disabled="isSendingHidden" @click.prevent="unHiddenQuestion()"  size="small" icon="warning" type="warning" style="position: relative;right: -50px;top: -15px">
          取消屏蔽</el-button>
      </el-tooltip>
      <el-tooltip v-else content="屏蔽后所有用户将无法浏览此问题" placement="top">
        <el-button :disabled="isSendingHidden" @click.prevent="hiddenQuestion()"  size="small" icon="circle-cross" type="danger" style="position: relative;right: -50px;top: -15px">
          屏蔽问题</el-button>
      </el-tooltip>
    </div>
    <div v-if="(!this.question.questionIsHidden&&this.hasLogin&&this.user.roles[0].id!=2&&this.user.id!=this.question.accountId)||(!this.question.questionIsHidden&&!this.hasLogin)" class="question-info">
      <el-tooltip v-if="this.hasFollow" content="取消关注后将不会获得更新提醒" placement="top">
        <el-button :disabled="isSendingFollow" @click.prevent="unFollowQuestion()"  size="small" type="warning">取消关注</el-button>
      </el-tooltip>
      <el-tooltip v-else content="关注之后将获得更新提醒" placement="top">
        <el-button :loading="loadingFollowStatus" :disabled="isSendingFollow" @click.prevent="followQuestion()"  size="small" type="success">关注问题</el-button>
      </el-tooltip>
      <el-tooltip v-if="!this.hasAnswer" content="为该问题贡献自己的答案吧" placement="top">
        <el-button @click.prevent="writeAnswer()" size="small" icon="edit" type="info">写回答</el-button>
      </el-tooltip>
      <el-tooltip v-else content="去修改自己的回答" placement="top">
        <el-button @click.prevent="modifyAnswer()" size="small" icon="edit" type="info" >修改回答</el-button>
      </el-tooltip>
    </div>
    <div v-if="this.hasLogin&&this.user.id == this.question.accountId" class="question-info" style="margin-right: -50px;margin-top: -30px">
      <el-tooltip content="修改已发布的问题" placement="top">
        <el-button @click.prevent="modifyQuestion()" size="small" icon="edit" type="info">修改问题</el-button>
      </el-tooltip>
    </div>
    <el-alert v-if="question.questionIsHidden&&(!this.hasLogin||(this.hasLogin&&this.user.roles[0].id!=2&&this.user.id!=this.question.accountId))" title="非常抱歉，该问题已被屏蔽，您暂时无法查看"
              type="warning" close-text="知道了" @close="back" show-icon style="position: relative ;left: -1125px;top: 83px;width: 22%;">
    </el-alert>
    <div v-if="!question.questionIsHidden&&(!this.hasLogin||user.roles[0].id == 1)" @mouseover="setShareUrl()" class="jiathis_style" style="clear: both;margin-bottom: 0px;margin-left: 1606px">
      <span class="jiathis_txt">分享到：</span>
      <a class="jiathis_button_cqq"></a>
      <a class="jiathis_button_weixin"></a>
      <a class="jiathis_button_qzone"></a>
      <a class="jiathis_button_tsina"></a>
      <a href="http://www.jiathis.com/share" class="jiathis jiathis_txt jtico jtico_jiathis" target="_blank"></a>
    </div>
    <answer-dialog v-if="(this.hasLogin&&this.user.roles[0].id==1)||!this.question.questionIsHidden" :userInfo="user" @submitAnswer="submit" ref="dialog"></answer-dialog>
    <modify-answer-dialog v-if="(this.hasLogin&&this.user.roles[0].id==1)||!this.question.questionIsHidden" :userInfo="user" :questionId="this.question.questionId" ref="modifyDialog"></modify-answer-dialog>
  </div>
</template>
<style scoped>
  #questionContent{
    border: 1px solid #ddd;
    background: #f9fafc;
    padding: 2rem;
    margin: 1rem;
  }

  #question-detail{
    clear: both;
    margin-top: 70px;
    overflow-x: auto;
    overflow-y: hidden;
  }

  .question-title{
    float: left;
    width: 60%;
  }
  .question-info{
    margin-bottom: -10px;
    margin-right: 30px;
    float: right;
    width: 20%;
  }
  .question-info span {
    width: 100%;
  }

</style>
<script>
  import { getQuestion, unFollowQuestion, followQuestion, unHiddenQuestion, hiddenQuestion, hasFollowQuestion, postAnswer, getAnswerStatus } from '@/api/question'
  import { getUserAnswerByQuestionId } from '@/api/answer'
  import '../../../static/UE/ueditor.parse'
  import { Message } from 'element-ui'
  import AnswerInputDialog from '@/components/answer/AnswerInputDialog'
  import AnswerModifyDialog from '@/components/answer/AnswerModifyDialog'
  import bus from '../../assets/eventBus.js'
  import { mapGetters } from 'vuex'
  import { initShare } from '@/utils/util'
  export default {
    components: { 'answer-dialog': AnswerInputDialog, 'modify-answer-dialog': AnswerModifyDialog },
    props: {
      isAllAnswer: {
        type: Boolean,
        default: true
      }
    },
    data () {
      return {
        question: {
          questionTitle: '',
          questionContent: '',
          questionId: this.$route.params.questionId,
          questionIsHidden: true,
          accountId: 0
        },
        hasFollow: false,
        hasAnswer: false,
        questionLoading: false,
        userId: 1,
        questionId: null,
        isSendingFollow: false,
        loadingFollowStatus: false,
        isSendingHidden: false,
        loadingHiddenStatus: false
      }
    },
    mounted () {
      this.getQuestion()
      let _this = this
      _this.getHasAnswer()
      bus.$off('loginSuccess')
      bus.$on('loginSuccess', function () {
        _this.getFollowStatus()
      })
      initShare()
    },
    computed: {
      ...mapGetters(['hasLogin', 'user'])
    },
    methods: {
      modifyQuestion () {
        this.$router.push(this.question.questionId + '/modify')
      },
      unFollowQuestion () {
        const questionId = this.question.questionId
        this.isSendingFollow = true
        let _this = this
        unFollowQuestion(questionId).then((response) => {
          if (response.result === true) {
            _this.hasFollow = false
            Message({
              message: '取消关注成功！',
              type: 'success',
              duration: 1000
            })
          }
          _this.isSendingFollow = false
        }).catch((e) => {
          Message({
            message: '取消关注失败，请稍后再试！',
            type: 'error',
            duration: 1000
          })
          _this.isSendingFollow = false
        })
      },
      followQuestion () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const questionId = this.question.questionId
        this.isSendingFollow = true
        let _this = this
        followQuestion(questionId).then((response) => {
          if (response.result === true) {
            _this.hasFollow = true
            Message({
              message: '关注问题成功！',
              type: 'success',
              duration: 1000
            })
          }
          _this.isSendingFollow = false
        }).catch((e) => {
          Message({
            message: '关注问题失败，请稍后再试！',
            type: 'error',
            duration: 1000
          })
          _this.isSendingFollow = false
        })
      },
      unHiddenQuestion () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const questionId = this.question.questionId
        this.isSendingHidden = true
        let _this = this
        unHiddenQuestion(questionId).then((response) => {
          if (response.result === true) {
            _this.question.questionIsHidden = false
            Message({
              message: '取消屏蔽成功！',
              type: 'success',
              duration: 1000
            })
          }
          _this.isSendingHidden = false
        }).catch((e) => {
          Message({
            message: '取消屏蔽失败，请稍后再试！',
            type: 'error',
            duration: 1000
          })
          _this.isSendingHidden = false
        })
      },
      hiddenQuestion () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const questionId = this.question.questionId
        this.isSendingHidden = true
        let _this = this
        hiddenQuestion(questionId).then((response) => {
          if (response.result === true) {
            _this.question.questionIsHidden = true
            Message({
              message: '屏蔽问题成功！',
              type: 'success',
              duration: 1000
            })
          }
          _this.isSendingHidden = false
        }).catch((e) => {
          Message({
            message: '屏蔽问题失败，请稍后再试！',
            type: 'error',
            duration: 1000
          })
          _this.isSendingHidden = false
        })
      },
      getQuestion () {
        this.questionLoading = true
        const questionId = this.$route.params.questionId
        let _this = this
        getQuestion(questionId).then((response) => {
          _this.question.questionTitle = response.result.questionTitle
          _this.question.questionContent = response.result.questionContent
          _this.question.questionId = response.result.id
          _this.getFollowStatus()
          if (response.result.hidden == null || !response.result.hidden) {
            _this.question.questionIsHidden = false
          }
          else {
            _this.question.questionIsHidden = true
          }
          _this.questionLoading = false
          _this.$nextTick(function () {
            _this.questionLoading = false
            _this.$emit('onPublisherId', response.result.publisher.id)
            _this.question.accountId = response.result.publisher.id
            window.uParse('#questionContent', {
              rootPath: '../../static/UE/'
            })
          })
        }).catch((e) => {
          Message({
            message: '获取问题失败！',
            type: 'error',
            duration: 5 * 1000
          })
        })
      },
      getFollowStatus () {
        if (!this.hasLogin) {
          return
        }
        this.loadingFollowStatus = true
        const questionId = this.question.questionId
        let _this = this
        hasFollowQuestion(questionId).then((response) => {
          _this.hasFollow = response.result
          _this.loadingFollowStatus = false
        }).catch((e) => {
          Message({
            message: '获取信息失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      getHasAnswer () {
        if (!this.hasLogin) {
          return
        }
        const questionId = this.question.questionId
        getUserAnswerByQuestionId(questionId).then((response) => {
          if (response.result != null) {
            this.hasAnswer = true
          }
          else {
            this.hasAnswer = false
          }
        }).catch((e) => {
          Message({
            message: '获取信息失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      submit: function (answer) {
        let _this = this
        postAnswer(answer, this.question.questionId).then((response) => {
          Message({
            message: '感谢你的答案!',
            type: 'success',
            duration: 5 * 1000
          })
          _this.hasAnswer = true
          if (this.isAllAnswer) {
            this.$emit('getAnswer')
          }
        }).catch((e) => {
          Message({
            message: '对不起，回答失败，请稍后再试！',
            type: 'error',
            duration: 5 * 1000
          })
        })
      },
      writeAnswer () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        this.$refs.dialog.open()
      },
      modifyAnswer () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        this.$refs.modifyDialog.open()
      },
      setShareUrl () {
        window.jiathis_config = window.location.host + '/questionDetail/' + this.questionId
      },
      back () {
        this.$router.back(-1)
      }
    }
  }
</script>
