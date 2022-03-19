var app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        index: "-1",
        openid: "",
        comList: [],
        ownerInfo: [],
        formData: {
            Oacconut: "",
            Oname: "",
            Ophonenum: "",
            Oaddress: "",
            Oemail: ""
        }
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.data.openid = wx.getStorageSync('openid')
        this.getOwner()
        this.getCommunity()
    },

    getOwner() {
        var that = this
        console.log(that.data.openid)
        wx.request({
            url: app.globalData.baseUrl + '/owner/findOwnerById?id=' + that.data.openid,
            success: function (res) {
                console.log(res.data)
                that.setData({
                    ownerInfo: res.data,
                    ['formData.Oname']: res.data.Oname,
                    ['formData.Oidnum']: res.data.Oidnum,
                    ['formData.Ophonenum']: res.data.Ophonenum,
                    ['formData.Oemail']: res.data.Oemail,
                    ['formData.Oaddress']: res.data.Oaddress
                })
            }
        })
    },

    getCommunity() {
        var that = this
        wx.request({
            url: app.globalData.baseUrl + '/community/findAll2',
            header: {
                'content-type': 'application/json' // 默认值,get方法   
            },
            success: function (res) {
                console.log(res.data)
                that.setData({
                    comList: res.data
                })
            }
        })
    },

    infoBlur: function (e) {
        var that = this
        var dataset = e.currentTarget.dataset
        var name = dataset.name
        var value = e.detail.value
        var arrName = 'formData.' + name
        that.setData({
            [arrName]: value
        })
        console.log(that.data.formData)
    },

    bindComChange: function (e) {
        var that = this;
        wx.setStorageSync('index', e.detail.value)
        this.setData({
            index: e.detail.value
        })
    },

    doOwnerModify: function (e) {
        var that = this
        var openid = wx.getStorageSync('openid')
        var index = wx.getStorageSync('index')
        var isPhone = /0?(13|14|15|18)[0-9]{9}/
        var isEmail = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/
        var accName = 'formData.Oacconut'
        var addName = 'formData.Oaddress'
        if (that.data.comList[index].caddress == null) {
            that.setData({
                [accName]: openid,
                [addName]: "1"
            })
        } else {
            that.setData({
                [accName]: openid,
                [addName]: that.data.comList[index].caddress
            })
        }
        console.log(that.data.formData.Oaddress)
        if (that.data.formData.Oaddress == "" || that.data.formData.Ophonenum == "") {
            wx.showModal({
                title: '提示',
                content: '请先完善个人信息',
                showCancel: false
            })
        } else if (!(isPhone.test(that.data.formData.Ophonenum))) {
            wx.showModal({
                title: '提示',
                content: '联系方式格式错误',
                showCancel: false
            });
        } else if (!(isEmail.test(that.data.formData.Oemail))) {
            wx.showModal({
                title: '提示',
                content: '电子邮箱格式错误',
                showCancel: false
            });
        } else {
            wx.request({
                url: app.globalData.baseUrl + '/owner/doOwnerModify',
                method: "POST",
                data: that.data.formData,
                success: function (res) {
                    wx.showToast({
                        title: '修改成功',
                        icon: 'success',
                        duration: 1500,
                        success: function (res) {
                            wx.switchTab({
                                url: '../user/user'
                            })
                        }
                    })
                }
            })
        }
    }
})