var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    orfinal: ''
  },

  order_find(e) {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/order/findOrderByOrno',
      data: {
        orno: e.orno,
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          order: res.data[0]
        })
        that.setData({
          orfinal: that.data.order.ormonry - that.data.order.orlast
        })
        console.log("测试orfinal")
        console.log(that.data.orfinal)
      }

    })
  },

  onLoad: function (options) {
    var that = this
    console.log(wx.getStorageSync('order')),
      this.order_find(options)

  },

  way_choose1: function (options) {
    this.way_change("1")
    wx.navigateTo({
      url: '../pay_more/pay_more'
    })
  },

  way_choose2: function (options) {
    this.way_change("2")
  },

  way_choose3: function (options) {
    this.way_change("3")
  },

  // 发送请求修改支付方式
  way_change: function (way) {
    var that = this
    console.log("测试支付方式修改")
    console.log(way),
    wx.request({
      url: app.globalData.baseUrl + '/order/orderUpdatePayway',
      data: {
        orno: that.data.order.orno,
        orpayment: way
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      }
    })
  }
})