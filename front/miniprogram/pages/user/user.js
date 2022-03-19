var app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userid: "",
        userinfo: {},
        ownerInfo: [],
        isShow: false
    },

    onLoad: function () {
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
                                            that.setData({
                                                userid: res.data.openid
                                            })
                                        }
                                    })
                                }
                            })
                            wx.setStorageSync('userInfo', res.userInfo)
                            console.log("openid", wx.getStorageSync('openid'))
                            that.setData({
                                isShow: true,
                                userinfo: res.userInfo
                            });
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
    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.data.userid = wx.getStorageSync('openid')
        this.getOwner()
    },

    getOwner() {
        var that = this
        console.log(that.data.openid)
        wx.request({
            url: app.globalData.baseUrl + '/owner/findOwnerById?id=' + that.data.userid,
            success: function (res) {
                that.setData({
                    ownerInfo: res.data
                })
            }
        })
    },

    isIden() {
        var that = this
        if (that.data.ownerInfo.Oidauthflog == "0") {
            wx.navigateTo({
                url: '../identity/identity'
            })
        } else if (that.data.ownerInfo.Oidauthflog == "1" && that.data.ownerInfo.Ocertificationflog == "0") {
            wx.navigateTo({
                url: '../certification/certification'
            })
        } else if (that.data.ownerInfo.Oidauthflog == "1" && that.data.ownerInfo.Ocertificationflog == "1") {
            wx.navigateTo({
                url: '../userinfo/userinfo'
            })
        } else if (that.data.ownerInfo.Ocertificationflog == '2') {
            wx.navigateTo({
                url: '../userinfo/userinfo'
            })
        } else {
            wx.navigateTo({
                url: '../identity/identity'
            })
        }
    },

    bindGetUserInfo: function (e) {
        var that = this;
        wx.getSetting({
            success: function (res) {
                if (res.authSetting['scope.userInfo']) {
                    wx.getUserInfo({
                        success: function (res) {
                            wx.login({
                                success: res => {
                                    // 直接使用微信的提供的接口直接获取 openid ，方法如下：
                                    wx.request({
                                        url: 'https://api.weixin.qq.com/sns/jscode2session?appid=wx3f72b49c54becbff&secret=a20defe1abea0ecfd239fc24cba7a67f&js_code=' + res.code + '&grant_type=authorization_code',
                                        success: res => {
                                            wx.setStorageSync('openid', res.data.openid)
                                            that.setData({
                                                userid: res.data.openid
                                            })
                                        }
                                    })
                                }

                            })
                            wx.setStorageSync('userInfo', res.userInfo)
                            that.setData({
                                isShow: true,
                                userinfo: res.userInfo
                            });
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
    }
})