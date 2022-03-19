var app = getApp()
Page({

  data: {
    ctx: "",
    msg: "",
    src: "",
    userid: "",
    base64: "",
    baidutoken: ""
  },

  onLoad() {
    this.data.userid = wx.getStorageSync('openid')
    this.ctx = wx.createCameraContext()
  },

  takePhoto() {
    var that = this
    that.ctx.takePhoto({
      quality: 'high',
      success: (res) => {
        that.setData({
          src: res.tempImagePath
        })
        wx.getFileSystemManager().readFile({
          filePath: that.data.src,
          encoding: 'base64',
          success: (res) => {
            that.setData({
              base64: res.data
            })
          }
        })
        //this.getBaiduToken();
        that.checkPhoto()
      }
    })
    wx.showToast({
      title: '请重试',
      icon: 'loading',
      duration: 500
    })
  },

  error(e) {
    console.log(e.detail)
  },

  getBaiduToken() {
    var that = this
    wx.request({
      url: 'https://aip.baidubce.com/oauth/2.0/token',
      data: {
        grant_type: 'client_credentials',
        client_id: 'IoBzfYtcQuHAcR9THXeaw2Ri',
        client_secret: 'kU52H0WflvAi5qQkLABylHuMwouuDwaG'
      },
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        that.setData({
          baidutoken: res.data.access_token
        })
        that.uploadPhoto()
      }
    })
  },

  uploadPhoto() {
    var that = this;
    //上传人脸进行注册-----test
    wx.request({
      url: 'https://aip.baidubce.com/rest/2.0/face/v3/faceset/user/add?access_token=' + this.data.baidutoken,
      method: 'POST',
      data: {
        image: this.data.base64,
        image_type: 'BASE64',
        group_id: 'owners', //自己建的用户组id
        user_id: 'owner1' //这里获取用户昵称
      },
      header: {
        'content-type': 'application/json' // 默认值
      },
      success(res) {
        that.setData({
          msg: res.data.error_msg
        })
        //做成功判断
        if (that.data.msg == "pic not has face") {
          wx.showToast({
            title: '未捕获到人脸',
            icon: 'error',
          })
        }
        if (that.data.msg == 'SUCCESS') {
          wx.showToast({
            title: '人脸录入成功',
            icon: 'success',
          })
        }
      }
    })
  },

  checkPhoto() {
    var that = this;
    wx.request({
      url: 'https://aip.baidubce.com/oauth/2.0/token',
      data: {
        grant_type: 'client_credentials',
        client_id: 'IoBzfYtcQuHAcR9THXeaw2Ri',
        client_secret: 'kU52H0WflvAi5qQkLABylHuMwouuDwaG'
      },
      header: {
        'content-type': 'application/json'
      },
      success(res) {
        that.setData({
          baidutoken: res.data.access_token
        })
        that.validPhoto();
      }
    })
  },

  validPhoto() {
    var that = this;
    //上传人脸进行 比对
    wx.request({
      url: 'https://aip.baidubce.com/rest/2.0/face/v3/search?access_token=' + that.data.baidutoken,
      method: 'POST',
      data: {
        image: this.data.base64,
        image_type: 'BASE64',
        group_id_list: 'owners', //自己建的用户组id,
      },
      header: {
        'Content-Type': 'application/json' // 默认值
      },
      success(res) {
        console.log(res.data)
        that.setData({
          msg: res.data.error_msg
        })

        //做成功判断
        if (that.data.msg == "pic not has face") {
          wx.showToast({
            title: '未捕获到人脸',
            icon: 'error',
          })
        }
        if (that.data.msg == 'SUCCESS') {
          if (res.data.result.user_list[0].score > 60) {
            wx.showToast({
              title: '人脸识别成功',
              icon: 'success',
            })
            wx.request({
              url: app.globalData.baseUrl + '/owner/doOwnerCheck?id=' + that.data.userid,
              success: function (res) {
                wx.switchTab({
                  url: '../user/user'
                })
              }
            })
          } else {
            wx.showToast({
              title: '人脸识别失败',
              icon: 'error',
            })
          }
        }
      }
    })
  }
})