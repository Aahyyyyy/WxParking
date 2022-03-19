// pages/pay_more/index.js
const app = getApp()
Page({

  data: {
    order: [],
    // 订金
    orfirst: '',
    // 定金
    ordeposit: '',
    // 已经支付的金额
    orfinal: '',
    area: ''
  },

  // 检测该订单是否开盘，
  astatue_find: function () {
    wx.request({
      url: app.globalData.baseUrl + '/parking/queryParkingAstatus',
      data: {
        orno: wx.getStorageSync('orno'),
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          area: res.data[0]
        })
        console.log(that.data.statuee)
        if (that.data.area.Astatue == "0") {
          wx.showModal({
            title: '未开盘',
            content: '请耐心等待开盘！',
            success(res) {
              if (res.confirm) {
                console.log('用户点击确定')
                wx.navigateTo({
                  url: '../order_list/order_list'
                })
              } else if (res.cancel) {
                console.log('用户点击取消')
              }
              wx.navigateTo({
                url: '../user/user'
              })
            }
          })
        } else {
          // 如果已经开盘，暂时修改页面内order的状态为2,以进入定金支付模式
          let statue = "order.orstatue"
          that.setData({
            [statue]: "2"
          })
        }
      }
    })

  },

  // 查找订单信息
  order_find() {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/order/findOrderByOrno',
      data: {
        orno: wx.getStorageSync('orno'),
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          order: res.data[0]
        })
        that.setData({
          orfirst: that.data.order.ormonry * 0.05,
          ordeposit: that.data.order.ormonry * 0.1,
          orfinal: that.data.order.ormonry - that.data.order.orlast
        })
        console.log(that.data.order)
        if (that.data.order.orstatue == "1") {
          that.astatue_find()
        }
      }
    })
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onShow: function (options) {
    var that = this
    console.log(wx.getStorageSync('orno')),
      this.order_find()

  },

  // 模拟支付效果
  pay_sim: function () {
    var that = this
    var orstatue_next
    var orlast = that.data.order.orlast
    switch (that.data.order.orstatue) {
      case "0":
        orlast = orlast + that.data.orfirst
        orstatue_next = "1"
        break;
      case "2":
        orlast = orlast + that.data.ordeposit
        orstatue_next = "3"
        break;
      case "4":
        orlast = that.data.order.ormonry
        orstatue_next = "5"
        break;
    }
    console.log("传送数据测试")
    console.log(that.data.order.orno)
    console.log(orstatue_next)
    console.log(orlast)

    // 发送请求修改状态位以及已经支付的金额
    wx.request({
      url: app.globalData.baseUrl + '/order/orderUpdateStatueLast',
      data: {
        orno: that.data.order.orno,
        orstatue: orstatue_next,
        orlast: orlast
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {}
    })

    switch (that.data.order.orstatue) {
      case "0":
        this.tips1()
        break;
      case "2":
        this.tips2()
        break;
      case "4":
        this.tips3()
        break;
    }
  },

  tips1: function () {
    wx.showModal({
      title: '订金支付成功',
      content: '请耐心等待开盘！',
      success(res) {
        if (res.confirm) {
          console.log('用户点击确定')
          wx.switchTab({
            url: '../user/user'
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
          wx.switchTab({
            url: '../user/user'
          })
        }
        wx.switchTab({
          url: '../user/user'
        })
      }
    })
  },
  tips2: function () {
    var that = this
    wx.showModal({
      title: '定金支付成功',
      content: '是否继续签订合同？',
      success(res) {
        if (res.confirm) {
          wx.navigateTo({
            url: '../contract/contract?orno=' + that.data.order.orno

          })
        } else if (res.cancel) {
          console.log('用户点击取消')
          wx.navigateTo({
            url: '../order_list/order_list'
          })
        }
      }
    })
  },
  tips3: function () {
    wx.showModal({
      title: '尾款支付成功',
      content: '恭喜您拥有了自己的车位！',
      success(res) {
        if (res.confirm) {
          console.log('用户点击确定')
          wx.switchTab({
            url: '../index/index'
          })
        } else if (res.cancel) {
          console.log('用户点击取消')
          wx.navigateTo({
            url: '../index/index'
          })
        }
      }
    })
  }
})