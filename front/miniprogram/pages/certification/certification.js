var app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        formData: {
            Oacconut: "",
            Oname: "",
            Oidnum: ""
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        var that = this
        var openid = wx.getStorageSync('openid')

        var nName = 'formData.Oname'
        wx.request({
            url: app.globalData.baseUrl + '/owner/getOname?id=' + openid,
            success: function (res) {
                that.setData({
                    ['formData.Oacconut']: openid,
                    [nName]: res.data,
                })
            }
        })
        console.log(that.data.formData)
    },

    infoBlur: function (e) {
        var that = this
        var dataset = e.currentTarget.dataset
        var name = dataset.name
        var value = e.detail.value
        var arrName = 'formData.' + name
        that.setData({
            [arrName]: value,
        })
        console.log(that.data.formData)
    },

    doOwnerCert: function (options) {
        var that = this
        var isName = /^[\u4E00-\u9FA5A-Za-z]+$/
        var isId = /\d{17}[\d|x]|\d{15}/
        if (that.data.formData.Oname == "" || that.data.formData.Oidnum == "") {
            wx.showModal({
                title: '提示',
                content: '请先完善实名认证信息',
                showCancel: false
            })
        } else if (!(isName.test(that.data.formData.Oname))) {
            wx.showModal({
                title: '提示',
                content: '姓名格式错误',
                showCancel: false
            });
        } else if (!(isId.test(that.data.formData.Oidnum))) {
            wx.showModal({
                title: '提示',
                content: '身份证号码格式错误',
                showCancel: false
            });
        } else {
            wx.request({
                url: app.globalData.baseUrl + '/owner/doOwnerCert?id=' + that.data.formData.Oacconut,
                method: "POST",
                data: that.data.formData,
                success: function (res) {
                    wx.showToast({
                        title: '认证成功',
                        icon: 'success',
                        duration: 1500,
                        success: function (res) {
                            console.log(res)
                            wx.navigateTo({
                                url: '../face_check/face_check'
                            })
                        }
                    })
                },
                fail: function () {
                    console.log("2")
                }
            })
        }
    }
})