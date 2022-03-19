const app = getApp()
Page({
    data: {
        orno: ''
    },

    onLoad: function (options) {
        this.setData({
            orno:options.orno
        })
    },

    confirm:function(){
        var that = this
        wx.showModal({
            title: '车位产权合同',
            content: '请确认您已认真阅读合同！',
            success (res) {
              if (res.confirm) {
                that.updateStatue()
                wx.navigateTo({
                  url: '../pay_way/pay_way?orno=' + that.data.orno,
                })
              } 
            }
          })
    },

    updateStatue:function(e){
        var that = this
        // 发送请求修改状态位以及已经支付的金额
        wx.request({
        url:app.globalData.baseUrl + '/community/order_update_statue',
        data: {
        orno:that.data.orno,
        orstatue: 4,
        },
        header: {
            'content-type': 'application/json' // 默认值,get方法   
        },
        success: function (res) {
        }
        })    
    },
})