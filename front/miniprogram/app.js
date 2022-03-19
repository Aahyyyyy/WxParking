wx - App({
    onLaunch: function () {
        var that = this;
        wx.getSetting({
            success: function (res) {
                if (res.authSetting['scope.userInfo']) {
                    wx.getUserInfo({
                        success: function (res) {
                            console.log(res.code)
                            wx.login({
                                success: res => {
                                    // 直接使用微信的提供的接口直接获取 openid ，方法如下：
                                    wx.request({
                                        url: 'https://api.weixin.qq.com/sns/jscode2session?appid=wx3f72b49c54becbff&secret=a663e34ce2cdad43aaf128a6ffbcc264&js_code=' + res.code + '&grant_type=authorization_code',
                                        success: res => {
                                            wx.setStorageSync('openid', res.data.openid)
                                        }
                                    })
                                }
                            })
                            wx.setStorageSync('userInfo', res.userInfo)
                            console.log("openid", wx.getStorageSync('openid'))
                        }
                    });
                } else {
                    // 用户没有授权
                    // 改变 isHide 的值，显示授权页面
                    that.setData({
                        isShow: false
                    });
                }
            }
        })
    },

    globalData: {
        baseUrl: "http://localhost:8081",
        userInfo: null,
        anumber: '',
        city: ''
    }
})