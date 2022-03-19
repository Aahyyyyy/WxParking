var app = getApp()
Page({

    data: {
        orno: "",
        orstatus: "",
        orderInfo: []
    },

    onLoad: function (options) {
        this.setData({
            orno: options.orno
        })
        this.getOrderInfo()
    },

    getOrderInfo() {
        var that = this
        wx.request({
            url: app.globalData.baseUrl + '/order/findOrderById?orno=' + that.data.orno,
            header: {
                'content-type': 'application/json'
            },
            success: function (res) {
                console.log(res.data)
                that.setData({
                    orderInfo: res.data
                })
            }
        })
    },

    toPay() {
        var that = this
        wx.setStorageSync('orno', that.data.orno)
        wx.navigateTo({
            url: '../pay_more/pay_more'
        })
    },

    toSig() {
        var that = this
        wx.navigateTo({
            url: '../contract/contract?orno=' + that.data.orno
        })
    }
})