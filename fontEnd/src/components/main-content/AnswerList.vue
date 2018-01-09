<template>
  <div>
    <div id="answer-list">
      <div  v-loading='isLoadingAnswer' element-loading-text="玩命加载中">
        <div v-if="isNoData">
          <el-alert style="margin-top: 10px" :closable="false" title="对不起，找不到任何回答！" type="info" show-icon>
          </el-alert>
          <div style="clear: both"></div>
        </div>
        <div v-else>
          <span>
        <el-radio-group id="choose-params-panel" v-model="sortParamRadio" @change="radioChange()">
          <el-radio-button label="时间最新"></el-radio-button>
          <el-radio-button label="得票最高"></el-radio-button>
        </el-radio-group>
      </span>
          <div style="clear: both"></div>
          <div class="answer-overview" v-for="answerOverview in answerOverviewList">
            <answer-overview :answerOverview="answerOverview"></answer-overview>
          </div>
          <div id="pagination-panel">
            <el-pagination
              @size-change="handleSizeChange"
              @current-change="handleCurrentChange"
              :current-page="currentPage + 1"
              :page-sizes="pageSizes"
              :page-size='pageSize'
              layout="total, sizes, prev, pager, next, jumper"
              :total='total'>
            </el-pagination>
          </div>
          <div style="clear: both; margin-bottom: 20px"></div>
        </div>
      </div>
    </div>
  </div>
</template>
<style scoped>
  #answer-list{
    margin-left: 5%;
    margin-right: 5%;
    width: 90%;
  }

  div.answer-overview{
    border-bottom: 2px solid #c0ccda;
    padding: 12px 0 10px 0;
    background: #f9fafd;
    overflow: hidden;
    position: relative;
    margin-bottom: 5px;
  }

  #answer-list .answer-overview:hover{
    box-shadow: #666 0px 0px 10px;
  }

  #answer-list #pagination-panel{
    float: right;
  }

  #choose-params-panel {
    float: right;
    margin-bottom: 10px;
  }

</style>
<script>
  import { getHiddenAnswerByDateTime, getHiddenAnswerByThumbsUpCount } from '@/api/answer'
  import { Message } from 'element-ui'
  import bus from '../../assets/eventBus.js'
  import store from '@/store'
  import { mapGetters } from 'vuex'
  import AnswerOverview from '../../components/answer/AnswerOverview.vue'
  export default {
    components: { 'answer-overview': AnswerOverview },
    beforeRouteEnter (to, from, next) {
      store.dispatch('enter_answerList_page')
      next(true)
    },
    data () {
      return {
        currentPage: 0,
        pageSize: 5,
        total: 0,
        isLoadingAnswer: true,
        answerOverviewList: [],
        pageSizes: [5, 10],
        userId: 1,
        sortParamRadio: '时间最新'
      }
    },
    created () {
      this.updateAnswer()
    },
    methods: {
      updateAnswer () {
        this.fetchHiddenAnswer()
        this.resetData()
      },
      radioChange () {
        this.fetchHiddenAnswer()
      },
      handleSizeChange (val) {
        this.pageSize = val
        this.fetchHiddenAnswer()
      },
      handleCurrentChange (val) {
        this.currentPage = val - 1
        this.fetchHiddenAnswer()
      },
      fetchHiddenAnswer () {
        this.isLoadingAnswer = true
        if (this.sortParamRadio === '时间最新') {
          let _this = this
          getHiddenAnswerByDateTime(this.currentPage, this.pageSize).then((response) => {
            if (response.status === '200') {
              _this.answerOverviewList = response.result.content
              _this.total = response.result.total
            }
            _this.isLoadingAnswer = false
          }).catch((e) => {
            _this.handleError(e)
          })
        }
        else {
          let _this = this
          getHiddenAnswerByThumbsUpCount(this.currentPage, this.pageSize).then((response) => {
            if (response.status === '200') {
              _this.answerOverviewList = response.result.content
              _this.total = response.result.total
            }
            _this.isLoadingAnswer = false
          }).catch((e) => {
            _this.handleError(e)
          })
        }
      },
      handleError (e) {
        Message({
          message: '无法获取问题，请稍后重试！',
          type: 'error',
          duration: 1000
        })
        this.isLoadingAnswer = false
      },
      resetData () {
        this.currentPage = 0
        this.pageSize = 5
        this.total = 0
        this.isLoadingAnswer = true
        this.answerOverviewList = []
      }
    },
    computed: {
      ...mapGetters(['hasLogin', 'user']),
      isNoData: function () {
        return !this.isLoadingAnswer && this.answerOverviewList.length === 0
      }
    },
    watch: {
      '$route' (to, from) {
        this.updateAnswer()
      }
    },
    beforeRouteLeave (to, from, next) {
      store.dispatch('leave_answerList_page')
      next(true)
    }
  }
</script>
