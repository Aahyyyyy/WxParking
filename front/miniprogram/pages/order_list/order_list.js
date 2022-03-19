var app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    userid: "",
    tabList: [{
        id: 0,
        value: "全部订单",
        isActive: true
      },
      {
        id: 1,
        value: "已预约",
        isActive: false
      },
      {
        id: 2,
        value: "待支付",
        isActive: false
      },
      {
        id: 3,
        value: "待签约",
        isActive: false
      },
      {
        id: 4,
        value: "已完成",
        isActive: false
      }
    ],
    orderList: []
  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function (options) {
    // 获取当前的小程序的页面栈-数组，长度最大是10页面
    // 数组中索引最大的为当前页面
    let pages = getCurrentPages();
    let currentPage = pages[pages.length - 1]
    const {
      type
    } = currentPage.options
    console.log(type)
    this.changeTitleByIndex(type)
    this.data.userid = wx.getStorageSync('openid')
    this.getOrder()
  },

  getOrder() {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/order/findOrder?id=' + that.data.userid,
      header: {
        'content-type': 'application/json'
      },
      success: function (res) {
        console.log(res.data)
        wx.stopPullDownRefresh()
        that.setData({
          orderList: res.data
        })
      }
    })
  },

  toDetail: function (event) {
    console.log(event.currentTarget.dataset.orno)
    wx.navigateTo({
      url: '../order_detail/order_detail?orno=' + event.currentTarget.dataset.orno,
    })
  },

  deleteOrder: function (event) {
    var that = this
    wx.showModal({
      cancelColor: 'cancelColor',
      title: '提示',
      content: '确定取消订单?',
      success: function (res) {
        if (res.cancel) {

        } else {
          wx.request({
            url: app.globalData.baseUrl + '/order/deleteOrder?orno=' + event.currentTarget.dataset.orno,
            header: {
              'content-type': 'application/json'
            },
            success: function (res) {
              wx.navigateTo({
                url: '../order_list/order_list'
              })
            }
          })
        }
      }
    })
  },

  changeTitleByIndex(index) {
    let {
      tabList
    } = this.data
    tabList.forEach((v, i) => i == index ? v.isActive = true : v.isActive = false);
    this.setData({
      tabList
    })
  },

  handleTabsItemChange(e) {
    const {
      index
    } = e.detail;
    console.log(index)
    this.changeTitleByIndex(index)
  },

  onPullDownRefresh() {
    this.setData({
      orderList: []
    })
    this.getOrder()
  }
})