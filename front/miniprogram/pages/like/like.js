var app = getApp()
Page({

    /**
     * 页面的初始数据
     */
    data: {
        userid: "",
        likeList: []
    },

    /**
     * 生命周期函数--监听页面显示
     */
    onShow: function () {
        this.data.userid = wx.getStorageSync('openid')
        this.getLike()
    },

    getLike() {
        var that = this
        wx.request({
            url: app.globalData.baseUrl + '/like/findLike?id=' + that.data.userid,
            header: {
                'content-type': 'application/json'
            },
            success: function (res) {
                console.log(res.data)
                wx.stopPullDownRefresh()
                that.setData({
                    likeList: res.data
                })
            }
        })
    },

    toDetail: function (event) {
        console.log(event)
        let pno = event.currentTarget.dataset.pno
        wx.navigateTo({
            url: '../park_detail/park_detail?pno=' + pno
        })
    },

    onPullDownRefresh() {
        this.setData({
            input: "",
            likeList: []
        })
        this.getLike()
    }
})