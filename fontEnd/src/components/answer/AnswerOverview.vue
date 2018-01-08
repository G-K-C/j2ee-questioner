<template>
  <div @mouseenter="mouseEnter()" @mouseleave="mouseLeave()">
    <div class="publisherInfo">
      <router-link :to="{ path: `/user/${answerOverview.id}` }">
        <img class="avatar" :src="answerOverview.avatarURL">
        <span class=".publisher"> {{ answerOverview.username }} </span>
      </router-link>
    </div>
    <div class="summary">
      <h3>的回答<el-tag v-if="answerOverview.hidden" type="danger" style="font-weight:normal;position: relative;bottom: 2px">被屏蔽</el-tag>
      </h3>
      <h3>在
        <router-link :to="{ path : '/questionDetail/' + answerOverview.id }">
          {{ answerOverview.questionTitle }}
        </router-link>
      </h3>
      <div class="types">
        {{ answerOverview.questionSubject }} -> {{ answerOverview.questionCourse }}
      </div>
      <div v-if='isAdmin' class="publish-date">
        <el-tooltip v-if='this.itemHoverIndex==1&&this.hasHidden' content="取消屏蔽后用户将能浏览此问题" placement="top">
          <el-button style="margin-right: 8px" :disabled="isSendingHidden"  @click.prevent="unHiddenQuestion()"  size="mini" icon="warning" type="warning">取消屏蔽</el-button>
        </el-tooltip>
        <el-tooltip v-else-if='this.itemHoverIndex==1&&!this.hasHidden' content="屏蔽后所有用户将无法浏览此问题" placement="top">
          <el-button style="margin-right: 8px" :disabled="isSendingHidden"  @click.prevent="hiddenQuestion()"  size="mini" icon="circle-cross" type="danger">屏蔽问题</el-button>
        </el-tooltip>
        发布于 <span style="padding-right: 5px">{{ answerOverview.publishDateTime | moment("YYYY-MM-DD ") }} </span>
      </div>
    </div>
    <div class="info">
      <div class="views">
        <el-tag type="success"><i class="fa fa-thumbs-o-up"></i> {{ answerOverview.thumbsUpCount }}</el-tag>
      </div>
      <div class="answers">
        <el-tag> <i class="fa fa-thumbs-o-down"></i> {{ answerOverview.thumbsDownCount }}</el-tag>
      </div>
      <div class="solved-views">
        <el-tag v-if="answerOverview.isAccepted" type="success">被采纳</el-tag>
        <el-tag v-else type="gray">未采纳</el-tag>
      </div>
    </div>
  </div>
</template>
<style scoped>
  .publisherInfo{
    float: left;
    margin-right: 10px;
    width: 75px;
    text-align: center;
    height: 100%;
    min-width: 50px;
    cursor: pointer;
  }
  .publisherInfo .avatar{
    width: 50px;
    height: 50px;
    margin: 0 auto;
    vertical-align: middle;
    display: block;
    border: green solid 1px;
  }
  .summary{
    padding: 5px;
    float: left;
    margin-right: 10px;
    width: 70%;
    min-height: 50px;
    border-right: solid grey 1px;
    border-left: solid grey 1px;
  }
  .summary h3{
    overflow: hidden;
  }
  .summary a {
    color: #38935f;
  }
  .summary .publish-date{
    clear: left;
    float: right;
  }
  .info{
    float: right;
    width: 15%;
  }
  .info>div {
    margin-bottom: 5px;
  }
</style>
<script>
  import { unHiddenAnswer, hiddenAnswer } from '@/api/answer'
  import '../../../static/UE/ueditor.parse'
  import { Message } from 'element-ui'
  import bus from '../../assets/eventBus.js'
  import { mapGetters } from 'vuex'
  export default {
    props: {
      answerOverview: Object
    },
    data () {
      return {
        itemHoverIndex: null,
        isSendingHidden: false,
        loadingHiddenStatus: false,
        hasHidden: false
      }
    },
    computed: {
      ...mapGetters(['hasLogin', 'user']),
      isAdmin () {
        return this.hasLogin && this.user.roles[0].id==2
      }
    },
    methods: {
      unHiddenAnswer () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const Answerid = this.answerOverview.id
        this.isSendingHidden = true
        let _this = this
        unHiddenAnswer(Answerid).then((response) => {
          if (response.result === true) {
            _this.hasHidden = false
            _this.answerOverview.hidden = false
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
      hiddenAnswer () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        const answerid = this.answerOverview.id
        this.isSendingHidden = true
        let _this = this
        hiddenAnswer(answerid).then((response) => {
          if (response.result === true) {
            _this.hasHidden = true
            _this.answerOverview.hidden = true
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
      mouseEnter () {
        this.itemHoverIndex = 1
        this.hasHidden = this.answerOverview.hidden
      },
      mouseLeave () {
        this.itemHoverIndex = null
      }
    }
  }
</script>
