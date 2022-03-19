var app = getApp()
const amapFile = require('../../lib/amap-wx.130.js')
Page({

  /**
   * 页面的初始数据
   */
  data: {
    input: "",
    comList: []
  },

  TimeId: -1,

  onShow: function () {
    this.getCity()
    this.getCommunity()
  },

  getCity() {
    var myAmapFun = new amapFile.AMapWX({
      key: '0214dcff2e3a806f75abd2a0edfa5681'
    });
    myAmapFun.getRegeo({
      success: (res) => {
        console.log(res[0].regeocodeData.addressComponent.city)
        this.setData({
          address: res[0].regeocodeData.addressComponent.city
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
        wx.stopPullDownRefresh()
        that.setData({
          comList: res.data
        })
      }
    })
  },

  getComByKey: function (e) {
    var that = this
    that.setData({
      input: e.detail.value
    })
    clearTimeout(this.TimeId)
    this.TimeId = setTimeout(() => {
      wx.request({
        url: app.globalData.baseUrl + '/community/getComByKey?key=' + that.data.input,
        success: function (res) {
          console.log(res.data)
          that.setData({
            comList: res.data
          })
        }
      })
    }, 500)
  },

  more_detail: function (e) {
    var that = this
    var Id = e.currentTarget.dataset.com_id;
    wx.navigateTo({
      url: '../com_detail/com_detail?Cno=' + Id
    })
  },

  handleTabsItemChange(e) {
    const {
      index
    } = e.detail;
    let {
      tabList
    } = this.data;
    tabList.forEach((v, i) => i === index ? v.isActive = true : v.isActive = false);
    this.setData({
      tabList
    })
  },

  onPullDownRefresh() {
    this.setData({
      input: "",
      comList: []
    })
    this.getCommunity()
  }
})