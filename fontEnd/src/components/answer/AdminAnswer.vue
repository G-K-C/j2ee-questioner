<template>
  <div class="answer">
    <div>
      <router-link class="user-info" :to="{ path: `/user/${answer.account.id}/` }">
        <span><img :src="answer.account.avatarURL"   class="user-avatar"/></span>
        <span>{{ answer.account.username }}</span>
      </router-link>
      发布于 <span>{{ $moment(answer.answerDateTime).fromNow() }}</span>
    </div>
    <div class="answer-info" v-html="answer.answerContent">
    </div>
    <div class="feedback">
      <el-tooltip effect="dark" content="答案对人有帮助，有参考价值" placement="top">
        <span ><i class="fa fa-thumbs-o-up fa-lg"></i> {{ answer.thumbsUpCount }} </span>
      </el-tooltip>
      <el-tooltip effect="dark" content="答案没帮助，是错误的答案，答非所问" placement="top">
        <span ><i class="fa fa-thumbs-o-down fa-lg"></i> {{ answer.thumbsDownCount }} </span>
      </el-tooltip>
      <span @click="toggleComment()"  class="comment"><i class="fa  fa-comments-o fa-lg"></i>
            <span v-show="!showComment">{{ answer.commentCount || 0 }}条评论</span>
            <span v-show="showComment"> 收起评论 </span>
      </span>
      <span v-if="answer.accepted">
        <el-tooltip effect="dark" content="提问者已经采纳了该回答" placement="top">
          <i style="color: green" class="el-icon-circle-check"></i>
        </el-tooltip>
      </span>
      <span v-else="">
        <el-tooltip effect="dark" content="提问者对该答案没有做出反馈" placement="top">
          <i class="el-icon-circle-check"></i>
        </el-tooltip>
      </span>
      <div v-if="this.hasLogin&&this.user.roles[0].id==2" style="float: right;margin-right: 292px;margin-top: -5px" >
        <el-tooltip v-if='this.hasHidden' content="取消屏蔽后此回答将可见" placement="top">
          <el-button :disabled="isSendingHidden" @click.prevent="unHiddenAnswer()"  size="small" icon="warning" type="warning" style="position: relative;right: -50px;top: -15px">
            取消屏蔽</el-button>
        </el-tooltip>
        <el-tooltip v-else content="屏蔽后此回答将不可见" placement="top">
          <el-button :disabled="isSendingHidden" @click.prevent="hiddenAnswer()"  size="small" icon="circle-cross" type="danger" style="position: relative;right: -50px;top: -15px">
            屏蔽回答</el-button>
        </el-tooltip>
      </div>
    </div>
    <div class="commentDetail" v-show="showComment">
      <comment :answerId="answer.id"></comment>
    </div>
  </div>
</template>
<style scoped>
  .answer{
    border-bottom: 1px solid grey;
    margin-bottom: 10px;
  }
  .answer .user-avatar{
    width: 38px;
    height: 38px;
    border-radius: 2px;
    background: #fff;
    vertical-align: top;
    margin-right: 10px;
    margin-bottom: 10px;
  }
  .feedback>span{
    margin-right: 10px;
  }
  .feedback>span{
    cursor: pointer;
  }
  .feedback>span:hover{
    color: #017E66;
  }
  .user-info {
    cursor: pointer;
  }
</style>
<script>
  import Comment from '../comment/Comment.vue'
  import bus from '../../assets/eventBus'
  import { hiddenAnswer, unHiddenAnswer } from '@/api/answer'
  import '../../../static/UE/ueditor.parse'
  import { Message } from 'element-ui'
  import { mapGetters } from 'vuex'
  import { initShare } from '@/utils/util'
  export default {
    components: { 'comment': Comment },
    data () {
      return {
        isFeedback: false,
        showComment: false,
        questionId: null,
        isSendingHidden: false,
        hasHidden: this.answer.hidden
      }
    },
    props: {
      answer: Object,
      isCurrentUser: Boolean
    },
    computed: {
      showAccept: function () {
        return this.isCurrentUser && !this.answer.accepted
      },
      ...mapGetters(['hasLogin', 'user'])
    },
    mounted: function () {
      this.$nextTick(function () {
        window.uParse('.answer', {
          rootPath: '../../static/UE/'
        })
      })
      this.questionId = this.$route.params.questionId
      initShare()
    },
    methods: {
      toggleComment () {
        if (this.showComment === false) {
          bus.$emit('comment_' + this.answer.id)
        }
        this.showComment = !this.showComment
        this.$emit('onToggleComment')
      },
      hiddenAnswer () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const answerId = this.answer.id
        this.isSendingHidden = true
        let _this = this
        hiddenAnswer(answerId).then((response) => {
          if (response.result === true) {
            _this.hasHidden = true
            Message({
              message: '屏蔽回答成功！',
              type: 'success',
              duration: 1000
            })
          }
          _this.isSendingHidden = false
        }).catch((e) => {
          Message({
            message: '屏蔽回答失败，请稍后再试！',
            type: 'error',
            duration: 1000
          })
          _this.isSendingHidden = false
        })
      },
      unHiddenAnswer () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const answerId = this.answer.id
        this.isSendingHidden = true
        let _this = this
        unHiddenAnswer(answerId).then((response) => {
          this.hasHidden = false
          if (response.result === true) {
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
      }
    }
  }
</script>
