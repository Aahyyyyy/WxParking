const app = getApp()
const amapFile = require('../../lib/amap-wx.130.js')
Page({
    /**
     * 页面的初始数据
     */
    data: {
        index1: 0,
        index2: 0,
        address: "",
        comList: [],
        buildList: [],
        formData: {
            Oacconut: "",
            Oname: "",
            Ophonenum: "",
            Oaddress: "",
            Oemail: "",
            Bname: ""
        }
    },

    /**
     * 生命周期函数--监听页面加载
     */
    onLoad: function (options) {
        this.getCommunity()
    },

    getCommunity() {
        var that = this;
        wx.request({
            url: app.globalData.baseUrl + '/community/findAll2',
            header: {
                'contenttype': 'application/json'
            },
            success: function (res) {
                console.log(res.data)
                wx.setStorageSync('comList', res.data)
                that.setData({
                    comList: res.data,
                })
                that.getBuilding()
            }
        })
    },

    getBuilding() {
        var that = this
        var index1 = wx.getStorageSync('index1')
        var cno = that.data.comList[index1].cno
        console.log(typeof cno)
        wx.request({
            url: app.globalData.baseUrl + '/building/findBuildByCno?cno=' + cno,
            success: function (res) {
                console.log(res.data)
                that.setData({
                    buildList: res.data
                })
            }
        })
    },

    bindComChange: function (e) {
        var that = this;
        that.setData({
            index1: e.detail.value
        })
        wx.setStorageSync('index1', e.detail.value)
        that.getBuilding()
    },

    bindBuildChange: function (e) {
        var that = this;
        that.setData({
            index2: e.detail.value
        })
        wx.setStorageSync('index2', e.detail.value)
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

    doOwnerIden: function (e) {
        var that = this
        var openid = wx.getStorageSync('openid')
        var index1 = wx.getStorageSync('index1')
        var index2 = wx.getStorageSync('index2')
        console.log(openid)
        var isName = /^[\u4E00-\u9FA5A-Za-z]+$/
        var isPhone = /0?(13|14|15|18)[0-9]{9}/
        var isEmail = /\w[-\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\.)+[A-Za-z]{2,14}/
        var accName = 'formData.Oacconut'
        var addName = 'formData.Oaddress'
        var builName = 'formData.Bname'
        if (that.data.comList == null) {
            that.setData({
                [accName]: openid,
                [addName]: "1",
                [builName]: "1"
            })
        } else {
            that.setData({
                [accName]: openid,
                [addName]: that.data.comList[index1].caddress,
                [builName]: that.data.buildList[index2].bname
            })
        }
        console.log(that.data.formData.Oaddress)
        console.log(that.data.formData.Bname)
        if (that.data.formData.Oaddress == "" || that.data.formData.Oname == "" || that.data.formData.Ophonenum == "" || that.data.formData.Bname == "") {
            wx.showModal({
                title: '提示',
                content: '请先完善业主基础信息',
                showCancel: false
            })
        } else if (!(isName.test(that.data.formData.Oname))) {
            wx.showModal({
                title: '提示',
                content: '姓名格式错误',
                showCancel: false
            });
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
                url: app.globalData.baseUrl + '/owner/doOwnerIden',
                method: "POST",
                data: that.data.formData,
                success: function (res) {
                    wx.showToast({
                        title: '认证成功',
                        icon: 'success',
                        duration: 1500,
                        success: function (res) {
                            wx.navigateTo({
                                url: '../certification/certification'
                            })
                        }
                    })
                }
            })
        }
    }
})