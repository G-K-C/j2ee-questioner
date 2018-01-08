<template>
  <div class="components-container">
    <div id="question-panel" style="margin-top: 40px">
      <h3 style="color: #111111">添加类型</h3>
      <el-form label-width="80px">
        <el-form-item label="所属科目" style="margin-top: 30px;margin-bottom: 30px;margin-left: 12px">
          <el-input v-model="newSubject" style="width: 40%"></el-input>
        </el-form-item>
        <el-form-item label="问题类型" style="margin-top: 40px;margin-bottom: 30px;margin-left: 12px">
          <el-input v-model="newCourse" style="width: 40%"></el-input>
        </el-form-item>
      </el-form>
      <div>
        <el-button :disabled="isSendingRequest" style=" margin-top: 15px;margin-left: 484px"  size="small" type="success"  @click="addType()">确定</el-button>
      </div>
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
  import { getQuestionType } from '@/api/question'
  import { addQuestionType } from '@/api/questionType'
  import { Message } from 'element-ui'
  import { transformQuestionType2Map } from '@/utils/util'
  export default {
    components: {UE},
    data () {
      return {
        newSubject: '',
        newCourse: '',
        defaultMsg: '',
        questionTypeArray: [],
        config: {
          initialFrameWidth: null,
          initialFrameHeight: 250
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
      },
      typeOption () {
        for (var item in this.groupQuestionType) {
          if (item.label == this.subject) {
            return item.options
          }
        }
        return null
      }
    },
    mounted: function () {
      let _this = this
      getQuestionType().then((response) => {
        if (response.status === '200') {
          _this.questionTypeArray = response.result
        }
      })
    },
    methods: {
      isNewType () {
        for (var i = 0; i < this.groupQuestionType.length; i++) {
          if (this.newSubject == this.groupQuestionType[i]['label']) {
            for (var j = 0; j < this.groupQuestionType[i]['options'].length; j++) {
              if (this.newCourse == this.groupQuestionType[i]['options'][j]['label']) {
                return false
              }
            }
          }
        }
        return true
      },
      resetFields () {
        let editor = this.$refs.ue
        editor.clearContent()
        this.questionForm.questionTitle = ''
        this.questionForm.questionType = ''
      },
      addType () {
        this.isSendingRequest = true
        if (!this.newSubject||!this.newCourse) {
          Message({
            message: '问题类型或所属科目不能为空',
            type: 'error',
            duration: 1 * 1000
          })
          this.isSendingRequest = false
          this.newSubject = ''
          this.newCourse = ''
          return
        }
        else if (!this.isNewType()) {
          Message({
            message: '此类型已存在！',
            type: 'error',
            duration: 1 * 1000
          })
          this.isSendingRequest = false
          this.newSubject = ''
          this.newCourse = ''
          return
        }
        else {
          let _this = this
          addQuestionType(this.newCourse, this.newSubject)
            .then((response) => {
              if (response.status === '200') {
                Message({
                  message: '类型修改成功！',
                  type: 'success',
                  duration: 1 * 1000
                })
                _this.isSendingRequest = false
                _this.newSubject = ''
                _this.newCourse = ''
                window.location.reload()
                return
              }
            }).catch((e) => {
              Message({
                message: '类型修改失败，请稍后再试',
                type: 'error',
                duration: 1 * 1000
              })
              _this.isSendingRequest = false
              _this.newSubject = ''
              _this.newCourse = ''
              return
            })
          this.newSubject = ''
          this.newCourse = ''
          return
        }
      }
    }
  }
</script>
