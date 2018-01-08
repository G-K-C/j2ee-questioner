<template>
  <div class="components-container">
    <div id="question-panel">
      <el-form :model="questionForm" ref="questionForm" :rules="rules" label-width="80px">
        <el-form-item prop="questionTitle" label="问题标题">
          <el-input v-model="questionForm.questionTitle" placeholder="输入问题标题能使问题更快被解决哦"></el-input>
        </el-form-item>
        <el-form-item prop="questionType" label="问题类型">
          <el-select v-model="questionForm.questionType" filterable placeholder="请选择">
            <el-option-group
              v-for="group in groupQuestionType"
              :key="group.label"
              :label="group.label">
              <el-option
                v-for="item in group.options"
                :key="item.value"
                :label="item.label"
                :value="item.value">
              </el-option>
            </el-option-group>
          </el-select>
        </el-form-item>
        <el-form-item label="问题描述">
          <div class="editor-container">
            <UE  :config="config" ref="ue"></UE>
          </div>
        </el-form-item>
      </el-form>
      <el-button :disabled="isSendingRequest" style="float: right; margin-bottom: 10px"  size="small" type="success"  @click="modifyQuestion('questionForm')">修改问题</el-button>
    </div>
  </div>
</template>
<style scoped>
  .components-container{
    margin-left: 5%;
    margin-right: 5%;
    width: 90%;
  }
  .components-container>div{
    margin-top: 10px;
  }
  .editor-container{
    z-index: -999;
  }
</style>
<script>
  import UE from '../UE.vue'
  import { getQuestionType, getQuestion, modifyQuestion } from '@/api/question'
  import { Message } from 'element-ui'
  import { transformQuestionType2Map } from '@/utils/util'
  export default {
    components: {UE},
    data () {
      return {
        defaultMsg: '',
        questionTypeArray: [],
        config: {
          initialFrameWidth: null,
          initialFrameHeight: 250
        },
        questionForm: {
          questionTitle: '',
          questionType: '',
          questionTypeId: ''
        },
        questionContent: '',
        questionId: 0,
        editorReady: false,
        rules: {
          questionTitle: [
            { required: true, message: '请输入问题标题', trigger: 'blur' }
          ],
          questionType: [
            { required: true, message: '请选择问题类型', trigger: 'blur' }
          ]
        },
        isSendingRequest: false
      }
    },
    computed: {
      groupQuestionType () {
        const questionTypeMap = transformQuestionType2Map(this.questionTypeArray)
        let options = []
        for (var key in questionTypeMap) {
          let option = []
          let typeArray = questionTypeMap[key]
          for (var i = 0; i < typeArray.length; i++) {
            option.push({
              label: typeArray[i]['course'],
              value: '' + typeArray[i]['id']
            })
          }
          options.push({
            label: key,
            options: option
          })
        }
        return options
      }
    },
    mounted: function () {
      this.getQuestion()
      this.setContent()
      let editor = this.$refs.ue.getUEditor()
      editor.setContent(this.content)
      getQuestionType().then((response) => {
        if (response.status === '200') {
          this.questionTypeArray = response.result
        }
      })
    },
    methods: {
      getQuestion () {
        const questionId = this.$route.params.questionId
        let _this = this
        getQuestion(questionId).then((response) => {
          _this.questionForm.questionTitle = response.result.questionTitle
          _this.questionForm.questionType = response.result.questionType.course
          _this.questionForm.questionTypeId = response.result.questionType.id
          _this.questionId = response.result.id
          _this.questionContent = response.result.questionContent
          _this.$refs.ue.getUEditor().setContent(_this.questionContent)
        })
      },
      setContent () {
        let _this = this
        this.$nextTick(function () {
          let ueditor = _this.getEditor()
          ueditor.addListener('ready', function () {
            ueditor.setContent(_this.answerContent) // 确保UE加载完成后，放入内容。
            _this.editorReady = true
          })
        })
        if (this.editorReady) {
          this.$refs.ue.getUEditor().setContent(this.questionContent)
        }
      },
      modifyQuestion (formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            let editor = this.$refs.ue.getUEditor()
            var _this = this
            this.isSendingRequest = true
            editor.getKfContent(function (content) {
              modifyQuestion(_this.questionId, _this.questionForm.questionTitle, _this.questionForm.questionTypeId, content, editor.getContentTxt())
                .then((response) => {
                  if (response.status === '201') {
                    Message({
                      message: '问题修改成功！',
                      type: 'success',
                      duration: 2 * 1000
                    })
                    _this.isSendingRequest = false
                  }
                }).catch((e) => {
                  Message({
                    message: '对不起，问题修改失败！',
                    type: 'error',
                    duration: 2 * 1000
                  })
                  _this.isSendingRequest = false
                  _this.resetFields()
                })
            })
            this.$router.push('/questionDetail/' + this.questionId)
          } else {
            return false
          }
        })
      }
    }
  }
</script>
