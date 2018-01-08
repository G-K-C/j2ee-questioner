<template>
  <div v-loading.lock="isCheckingUser">
  <div v-if="userIdIsValidate">
  <div class="profile">
    <header class="profile__heading">
      <el-row  style="padding-left: 5%; margin-top: 10px">
        <el-col :span="3">
          <div @click="uploadAvatar()" v-if="isCurrentUser" class="userAvatar uploadImg">
            <el-tooltip  class="item" effect="dark" content="点此上传头像" placement="top">
              <img :src="loginUser.avatarURL">
            </el-tooltip>
            <my-upload field="avatar"
                       @crop-upload-success="cropUploadSuccess"
                       v-model="avatarUploadShow"
                       :width="100"
                       :height="100"
                       url="http://111.230.231.91:8080/uploadAvatar"
                       :headers="headers"
                       img-format="png"></my-upload>
          </div>
          <div v-else class="userAvatar">
            <a>
              <img :src="user.avatarURL"/>
            </a>
          </div>
        </el-col>
        <el-col :span="10">
          <div style="margin-left: 20px" class="userInfo">
            <h2>{{ user.username }}</h2>
            <div class="profile__heading--award">
              <a class="profile__rank-btn">
                <span class="h4">{{ user.creditPoint }}</span>
                <span class="profile__rank-btn-text">声望</span>
              </a>
            </div>
            <div class="profile__heading--award-badge">
              <span class="badge">
                <span> <i style="color: gold" class="fa fa-star"></i> </span>
                <span> 0 </span>
              </span>
              <span class="badge">
                <span> <i style="color: grey" class="fa fa-star"></i> </span>
                <span> 0 </span>
              </span>
              <span class="badge">
                <span> <i style="color: saddlebrown" class="fa fa-star"></i> </span>
                <span> 0 </span>
              </span>
            </div>
          </div>
        </el-col>
        <el-col :span="8">
          <div class="profile__heading--desc">
            <div class="profile__heading--desc-heading">
              <h4>个人简介
              <span @click="showEditProfilePanel=!showEditProfilePanel" class="editProfile" v-if="isCurrentUser" style="float: right"><i class="fa fa-edit"></i>编辑</span>
              </h4>






            </div>
            <div class="profile__heading--desc-body">
              <div class="profile__desc">
                <div v-if="isCurrentUser">

                  <div v-if="!showEditProfilePanel" style="white-space: pre">
                    <div v-if="user.profile === null || user.profile === ''">
                      暂时没有个人简历<span style="margin-left: 5px"> <a @click="showEditProfilePanel=true" href="javascript:void 0">去添加?</a></span>
                    </div>
                    <div v-else v-html="user.profile">
                    </div>
                  </div>
                  <div id="editor-profile-panel" v-if="showEditProfilePanel">
                    <el-input  :rows=4 type="textarea" v-model="profile">
                    </el-input>
                    <div class="button-panel" style="padding-top: 5px;float: right">
                      <el-button @click="showEditProfilePanel=false" size="mini">取消</el-button>
                      <el-button :disabled="isSavingProfile" @click="saveProfile()" size="mini" type="success">保存</el-button>
                    </div>
                  </div>
                </div>
                <div v-else style="white-space: pre">
                  <div  v-html="user.profile || '该用户很懒，什么都没有留下.....'">
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-col>
      </el-row>
    </header>
    <div class="container-warp">
      <div class="container">
        <el-row style="padding-left: 5%">
          <el-col :span="4">
            <div class="follow-panel">
              <el-button :disabled="isSendingFollow" v-loading.lock="isLoadingFollowUserStatus"  @click="followUser()" v-if="!isCurrentUser && !hasFollow"  size="small" type="success" icon="star-on">加关注</el-button>
              <el-button :disabled="isSendingFollow" v-loading.lock="isLoadingFollowUserStatus" @click="unFollowUser()" v-if="!isCurrentUser && hasFollow" size="small" type="success" icon="star-off">取消关注</el-button>
              <div v-loading.lock="loadingFollowInfo" style="margin-top: 10px" class="follower-panel">
                <el-row style="padding-bottom:15px; border-bottom: solid 1px black">
                  <el-col :span="12" style="border-right: solid 1px #5CB85C">
                    <a @click="myFollow()" href="javascript:void">
                      <span>关注了</span>
                      <span style="display: block"> {{ followed }} 人 </span>
                    </a>
                  </el-col>
                  <el-col :span="12" style="padding-left: 15px">
                    <a @click="myFollower()" href="javascript:void">
                      <span>粉丝</span>
                      <span style="display: block"> {{ followers }} 人 </span>
                    </a>
                  </el-col>
                </el-row>
              </div>
            </div>
            <div class="operation-option">
              <el-menu :defaultActive="activeIndex">
                <el-menu-item @click="$router.push({ path: `/user/${userId}/` })" index="1"><i class="fa fa-home fa-lg"></i>{{ infoPrefix }}主页</el-menu-item>
                <el-menu-item @click="$router.push({ path: `/user/${userId}/userQuestion` })"  index="2"><span v-loading.lock="isGettingQuestionCount"><i class="fa fa-question-circle fa-lg"></i>{{ infoPrefix }}提问 {{ questionCount }}</span> </el-menu-item>
                <el-menu-item @click="$router.push({ path: `/user/${userId}/userAnswer` })" index="3"><span v-loading.lock="isGettingAnswerCount"><i class="fa fa-key fa-lg"></i>{{ infoPrefix }}回答 {{ answerCount }}</span></el-menu-item>
                <el-menu-item @click="$router.push({ path: `/user/${userId}/reputation` })" index="5"><i class="fa fa-star fa-lg"></i>声望记录</el-menu-item>
              </el-menu>
            </div>
          </el-col>
          <el-col :span="19" style="padding-left: 20px">
            <router-view></router-view>
          </el-col>
        </el-row>
        <div style="margin-bottom: 100px">

        </div>
      </div>
    </div>
  </div>
  </div>
  <div v-if="userIdIsValidate === false">
    <div style="margin: 100px 50px">
      <el-alert
        title="找不到该用户"
        type="error"
        description="该用户不存在或已被永久注销" :closable="false">
      </el-alert>
    </div>
  </div>
  </div>
</template>
<style>
  h2,h3 {
    font-weight: 500;
  }
  .pagination {
    float: right;
    margin-top: 10px;
  }
  .follow {
    padding: 12px 5px;
    border-bottom: solid 1px #666666;
  }
</style>
<style scoped>
  .profile__heading {
    background-color: #f6f6f6;
    padding: 30px 0 25px 0;
  }
  .profile__heading h2 {
    font-weight: 500;
  }
  .profile__rank-btn {
    margin-top: 5px;
    background: #DFF0D8;
    border: 1px solid #5CB85C;
    border-radius: 100px;
    font-size: 15px;
    color: #43894E;
    padding: 4px 12px;
    line-height: 1;
    display: inline-block;
  }
  .profile__rank-btn-text {
    font-size: 14px;
    margin-left: 1px;
  }
  .profile__heading--award {
    display: inline-block;
  }
  .profile__heading--award-badge {
    display: inline-block;
    margin-left: 15px;
    line-height: 1;
    color: #333;
    vertical-align: middle;
  }
  .profile__heading--award-badge .badge {
    margin-left: 10px;
  }
  .profile__heading--desc-heading {
    height: 36px;
    background-color: #ddd;
    margin-left: -140px;
    margin-right: 150px;
    padding: 5px 12px;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
  }


  .profile__heading--desc-body {
    background-color: #EEEEEE;
    margin-left: -140px;
    margin-right: 150px;
    font-size: 13px;
    color: #666666;
    overflow: auto;
    height: 170px;
    padding: 20px;
    font-family: "Source Code Pro", Consolas, Menlo, Monaco, "Courier New", monospace;
  }
  .container-warp {
    margin-top: 30px;
  }
  .follower-panel a {
    color: black;
  }
  .follower-panel a:hover {
    color: grey;
  }


  .profile__heading--desc-heading .editProfile {
    color: #999;
    font-size: 15px;
    font-weight: normal;

  }
  .profile__heading--desc-heading .editProfile:hover {

    cursor: pointer;
    color: #8A6D3B;
  }

  .container .operation-option .el-menu-item i {
    margin-right: 5px;
  }
  .userAvatar img {
    width: 100%;
    border-radius: 50%;
    background: white;
    vertical-align: middle
  }
  .uploadImg {
    cursor: pointer;
  }
</style>
<script>
  import { getUser, getFollowInfo, hasFollowUser, followUser, unFollowUser, saveProfile } from '@/api/user'
  import { getUserQuestionCount } from '@/api/question'
  import { getUserAnswerCount } from '@/api/answer'
  import { Message } from 'element-ui'
  import { mapGetters } from 'vuex'
  import myUpload from 'vue-image-crop-upload'
  import bus from '../../assets/eventBus'
  export default {
    components: { 'my-upload': myUpload },
    data () {
      return {
        userId: '',
        userIdIsValidate: '',
        isCheckingUser: false,
        user: null,
        loadingFollowInfo: false,
        followers: 0,
        followed: 0,
        activeIndex: '1',
        avatarUploadShow: false,
        headers: null,
        hasFollow: false,
        isSendingFollow: false,
        isLoadingFollowUserStatus: false,
        isGettingAnswerCount: false,
        isGettingQuestionCount: false,
        answerCount: null,
        questionCount: null,
        showEditProfilePanel: false,
        profile: null,
        isSavingProfile: false
      }
    },
    mounted () {
      this.updateUserInfo()
      const _this = this
      bus.$off('loginSuccess')
      bus.$on('loginSuccess', function () {
        _this.updateFollowUserStatus()
      })
    },
    computed: {
      ...mapGetters({loginUser: 'user'}),
      ...mapGetters(['token', 'hasLogin']),
      isCurrentUser: function () {
        return this.loginUser !== null && parseInt(this.loginUser.id) === parseInt(this.userId)
      },
      infoPrefix: function () {
        return this.isCurrentUser ? '我的' : '他的'
      }
    },
    methods: {
      updateUserInfo () {
        this.userId = this.$route.params.userId
        this.isCheckingUser = true
        this.loadingFollowInfo = true
        this.getUserInfo()
        this.getFollowInfo()
        this.updateFollowUserStatus()
        this.getQuestionAnswerCount()
      },
      getUserInfo () {
        let _this = this
        getUser(this.userId).then((response) => {
          _this.userIdIsValidate = response.status === '200'
          _this.isCheckingUser = false
          _this.user = response.result
        }).catch((e) => {
          Message({
            message: '请求用户的信息出错，请稍后再试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      getFollowInfo () {
        let _this = this
        getFollowInfo(this.userId).then((response) => {
          _this.loadingFollowInfo = false
          _this.followers = response.result.followerCount
          _this.followed = response.result.followedCount
        }).catch((e) => {
          Message({
            message: '获取用户的关注信息失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      getQuestionAnswerCount () {
        this.isGettingAnswerCount = true
        let _this = this
        getUserAnswerCount(this.userId).then((response) => {
          _this.isGettingAnswerCount = false
          _this.answerCount = response.result
        }).catch((e) => {
          Message({
            message: '获取信息失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
        this.isGettingQuestionCount = true
        getUserQuestionCount(this.userId).then((response) => {
          _this.isGettingQuestionCount = false
          _this.questionCount = response.result
        }).catch((e) => {
          Message({
            message: '获取信息失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      uploadAvatar () {
        let token = this.token
        this.headers = {
          Authorization: token
        }
        this.avatarUploadShow = true
      },
      cropUploadSuccess (response, field) {
        this.$store.dispatch('changeAvatar', response.result)
      },
      followUser () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        this.isSendingFollow = true
        const _this = this
        followUser(this.userId).then((response) => {
          if (response.status === '200') {
            if (response.result === true) {
              _this.hasFollow = true
              Message({
                message: '关注用户成功！',
                type: 'success',
                duration: 1000
              })
            }
          }
          _this.isSendingFollow = false
        }).catch((e) => {
          Message({
            message: '关注用户失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      unFollowUser () {
        if (!this.hasLogin) {
          bus.$emit('requestLogin')
          return
        }
        this.isSendingFollow = true
        const _this = this
        unFollowUser(this.userId).then((response) => {
          if (response.status === '200') {
            if (response.result === true) {
              _this.hasFollow = false
              Message({
                message: '取消关注成功',
                type: 'success',
                duration: 1000
              })
            }
          }
          _this.isSendingFollow = false
        }).catch((e) => {
          Message({
            message: '取消关注失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      updateFollowUserStatus () {
        if (this.isCurrentUser || !this.hasLogin) {
          return
        }
        this.isLoadingFollowUserStatus = true
        let _this = this
        hasFollowUser(this.userId).then((response) => {
          _this.isLoadingFollowUserStatus = false
          _this.hasFollow = response.result
        }).catch((e) => {
          Message({
            message: '加载数据失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
        })
      },
      myFollow () {
        let aimPath = '/user/' + this.userId + '/myFollow'
        this.$router.push({ path: aimPath })
        this.getFollowInfo()
      },
      myFollower () {
        let aimPath = '/user/' + this.userId + '/followers'
        this.$router.push({ path: aimPath })
        this.getFollowInfo()
      },
      saveProfile () {
        this.isSavingProfile = true
        let _this = this
        saveProfile(this.profile).then((response) => {
          if (response.status === '200') {
            _this.user.profile = response.result.profile
            _this.isSavingProfile = false
            _this.showEditProfilePanel = false
          }
        }).catch((e) => {
          Message({
            message: '保存个人简介失败，请稍后重试！',
            type: 'error',
            duration: 1000
          })
          this.showEditProfilePanel = false
        })
      }
    },
    beforeRouteUpdate (to, from, next) {
      this.activeIndex = to.meta.index
      next()
    },
    watch: {
      '$route' (to, from) {
        if (from.params.userId !== to.params.userId) {
          this.updateUserInfo()
        }
      },
      showEditProfilePanel () {
        if (this.showEditProfilePanel && this.user !== null) {
          this.profile = this.user.profile
        }
      }
    }
  }
</script>
