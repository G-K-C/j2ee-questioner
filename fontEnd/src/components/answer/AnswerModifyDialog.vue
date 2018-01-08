<template>
  <el-dialog size="large" :visible.sync="dialogVisible">
    <div v-if="userInfo" id="userinfo" style="position: fixed; top: 10px; margin-bottom: 20px">
      <h3>感谢您的回答</h3>
      <img class="useravatar" :src="userInfo.avatarURL"/>
      <span>{{ userInfo.username }}</span>
    </div>
    <div style="margin-top: 20px">
      <div>
        <UE :fullscreen="false"  :config="config" ref="ue"></UE>
      </div>
    </div>
    <div slot="footer" class="dialog-footer">
      <el-button @click.prevent="dialogVisible = false">取 消</el-button>
      <el-button type="primary" @click.prevent="modifyAnswer()">确 定</el-button>
    </div>
  </el-dialog>
</template>
<style scoped>
  #userinfo .useravatar{
    width: 38px;
    height: 38px;
    vertical-align: top;
    margin-bottom: 20px;
    margin-right: 20px;
  }
</style>
<script>
  import UE from '@/components/UE.vue'
  import { getUserAnswerByQuestionId, modifyAnswer } from '@/api/answer.js'
  import { Message } from 'element-ui'
  export default {
    components: { UE },
    data () {
      return {
        dialogVisible: false,
        config: {
          initialFrameWidth: 1100,
          initialFrameHeight: 250
        },
        editorReady: false,
        answerContent: '',
        answerId: 0
      }
    },
    props: {
      userInfo: {
        type: Object,
        default: function () {
          return {
            username: '',
            avatarURL: ''
          }
        }
      },
      questionId: {
        type: null,
        default: 0
      }
    },
    mounted: function () {
      this.getAnswer()
    },
    methods: {
      getAnswer () {
        let _this = this
        getUserAnswerByQuestionId(this.questionId).then((response) => {
          if (response.status === '404') {
            Message({
              message: '找不到回答！',
              type: 'error',
              duration: 1000
            })
          } else if (response.status === '200') {
            if (response.result) {
              _this.answerContent = response.result.answerContent
              _this.answerId = response.result.id
            }
          }
        }).catch((e) => {
          Message({
            message: '获取回答失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      open () {
        if (!this.dialogVisible) {
          this.$emit('dialogOpen')
        }
        this.dialogVisible = true
        let _this = this
        this.$nextTick(function () {
          let ueditor = _this.getEditor()
          ueditor.addListener('ready', function () {
            ueditor.setContent(_this.answerContent) // 确保UE加载完成后，放入内容。
            _this.editorReady = true
          })
        })
        if (this.editorReady) {
          this.getEditor().setContent(this.answerContent)
        }
      },
      close () {
        if (this.dialogVisible) {
          this.$emit('dialogClose')
        }
        this.dialogVisible = false
      },
      getEditor () {
        return this.$refs.ue.getUEditor()
      },
      modifyAnswer () {
        let _this = this
        modifyAnswer(this.answerId, this.getEditor().getContent()).then((response) => {
          if (response.status === '201') {
            _this.dialogVisible = false
            Message({
              message: '修改回答成功！',
              type: 'success',
              duration: 1000
            })
            this.$router.push('/questionDetail/' + this.questionId)
          }
          else {
            _this.dialogVisible = false
            Message({
              message: '修改回答失败，请稍后重试！',
              type: 'error',
              duration: 1000
            })
          }
        }).catch((e) => {
          _this.dialogVisible = false
          Message({
            message: '修改回答失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      }
    }
  }
</script>
