// pages/parking_detail/parking_detail.js
const app = getApp()
Page({

  data: {
    parking_list: [],
    Owner: [],
    collect: [],
    collect_flag: 0,
    chooseSize: false,
    animationData: {},
  },


  // 由Oaccount获取Ono
  getOwner() {
    var that = this
    console.log(that.data.openid)
    wx.request({
      url: app.globalData.baseUrl + '/owner/findOwnerById?id=' + wx.getStorageSync('openid'),
      success: function (res) {
        wx.setStorageSync('ono', res.data.Ono)
        that.findStatus()
      }
    })
  },


  findStatus: function () {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/owner/findOwnerStatus',
      data: {
        ono: wx.getStorageSync('ono')
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          Owner: res.data
        })
      }
    })
  },

  onLoad: function (options) {
    var that = this
    var pno = options.pno;

    wx.request({
      url: app.globalData.baseUrl + '/parking/queryParkingInfoPno',
      data: {
        pno: options.pno
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          parking_list: res.data
        })
        console.log("parkinglist", that.data.parking_list[0])
        that.getOwner()
        that.isLike()
      }
    })
  },
  isLike: function () {
    var that = this
    // 查询是否收藏该车位
    wx.request({
      url: app.globalData.baseUrl + '/like/queryLike',
      data: {
        pno: that.data.parking_list[0].pno,
        ano: that.data.parking_list[0].ano,
        cno: that.data.parking_list[0].cno,
        oacconut: wx.getStorageSync('openid')
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          collect: res.data
        })
        console.log(that.data.collect.length)
        console.log(res.data)
        if (that.data.collect.length == 0) {
          that.setData({
            collect_flag: 0
          })
        } else {
          that.setData({
            collect_flag: 1
          })
        }
      }
    })
  },

  //确认业主是否进行了认证
  buy_confirm: function (e) {
    var that = this
    //判断该车位是否处于订单状态
    if (that.data.parking_list[0].pstatus != "0") {
      if (that.data.parking_list[0].pstatus == "1") {
        wx.showModal({
          title: '购买失败',
          content: '该车位已经被预定',
          success(res) {
            if (res.confirm) {
              console.log('用户点击确定')
            } else if (res.cancel) {
              console.log('用户点击取消')
            }
          }
        })
      } else {
        wx.showModal({
          title: '购买失败',
          content: '该车位已经出售',
          success(res) {
            if (res.confirm) {
              console.log('用户点击确定')
            } else if (res.cancel) {
              console.log('用户点击取消')
            }
          }
        })
      }
    } else {
      if (that.data.Owner[0].Ocertificationflog == "0") {
        console.log("判断成功")
        wx.showModal({
          title: '认证未完成',
          content: '您的身份认证尚未完成',
          success(res) {
            if (res.confirm) {
              console.log('用户点击确定')
            } else if (res.cancel) {
              console.log('用户点击取消')
            }
          }
        })
      } else {
        if (that.data.Owner[0].Oidauthflog == "0") {
          wx.showModal({
            title: '认证未完成',
            content: '您的业主认证尚未完成',
            success(res) {
              if (res.confirm) {
                console.log('用户点击确定')
              } else if (res.cancel) {
                console.log('用户点击取消')
              }
            }
          })
        } else {
          that.showModal()
        }
      }
    }
  },

  // 订单确认点击主函数
  buy_reserve: function () {
    //需异步
    this.add_Order()
  },

  // 根据用户是预约或是购买情况，产生不同提示
  model_choose: function () {
    var that = this
    this.order_find()
    var orstatus
    if (that.data.parking_list[0].astatus == 0) orstatus = 0
    else orstatus = 2
    if (orstatus == 0) {
      wx.showModal({
        title: '预订成功！',
        content: '是否立刻支付订金？',
        success(res) {
          if (res.confirm) {
            console.log('用户点击确定')
            wx.navigateTo({
              url: '../pay_more/pay_more'
            })
          } else if (res.cancel) {
            console.log('用户点击取消')
            wx.switchTab({
              url: '../user/user'
            })
          }
        }
      })
    } else {
      wx.showModal({
        title: '认购成功',
        content: '是否立即支付定金？',
        success(res) {
          if (res.confirm) {
            console.log('用户点击确定')
            wx.navigateTo({
              url: '../pay_more/pay_more'
            })
          } else if (res.cancel) {
            console.log('用户点击取消')
            wx.switchTab({
              url: '../user/user'
            })
          }
        }
      })
    }
  },

  order_find: function () {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/order/findOrderByOther',
      data: {
        ono: wx.getStorageSync('ono'),
        pno: that.data.parking_list[0].pno,
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        console.log("尝试查询订单记录")
        console.log(res.data[0])
        wx.setStorageSync('orno', res.data[0].orno)
      }
    })
  },

  //获取当前订单的时间
  get_time: function () {
    var timestamp = Date.parse(new Date());
    var date = new Date(timestamp);
    //获取年份  
    var Y = date.getFullYear();
    //获取月份  
    var M = (date.getMonth() + 1 < 10 ? '0' + (date.getMonth() + 1) : date.getMonth() + 1);
    //获取当日日期 
    var D = date.getDate() < 10 ? '0' + date.getDate() : date.getDate();
    var time = Y + '.' + M + '.' + D

    return time
  },

  add_Order: function (e) {
    var that = this
    var orstatus
    if (that.data.parking_list[0].astatus == 0) orstatus = 0
    else orstatus = 2
    var order_time = this.get_time().trim()
    console.log(order_time)
    console.log(that.data.parking_list[0])
    wx.request({
        url: app.globalData.baseUrl + '/order/insertOrder',
        data: {
          ono: wx.getStorageSync('ono'),
          pno: that.data.parking_list[0].pno,
          ormonry: that.data.parking_list[0].psalernuitprice,
          ordate: order_time,
          orstatue: orstatus,
          orpayment: '1',
        },
        header: {
          'content-type': 'application/json' // 默认值,get方法   
        },
        success: function (res) {
          console.log("新增订单记录，申请完毕")
          console.log(res.data)
          // 这里查询订单
          that.model_choose()
        }
      }),

      wx.request({
        url: app.globalData.baseUrl + '/parking/updateParkStatus',
        data: {
          ano: that.data.parking_list[0].ano,
          pno: that.data.parking_list[0].pno
        },
        header: {
          'content-type': 'application/json' // 默认值,get方法   
        },
        success: function (res) {
          that.setData({}),
            console.log("修改车位状态申请，执行完毕")
        }
      })
  },

  // 添加收藏函数
  collect_add: function (e) {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/like/addLike',
      data: {
        pno: that.data.parking_list[0].pno,
        ano: that.data.parking_list[0].ano,
        cno: that.data.parking_list[0].cno,
        oacconut: wx.getStorageSync('openid')
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
            collect_flag: 1
          }),
          console.log(res)
      }
    })
  },

  //取消收藏函数
  collect_cancel: function (e) {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/like/deleteLike',
      data: {
        pno: that.data.parking_list[0].pno,
        ano: that.data.parking_list[0].ano,
        cno: that.data.parking_list[0].cno,
        oacconut: wx.getStorageSync('openid')
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
            collect_flag: 0
          }),

          console.log(res)
      }
    })
  },

  // 底部弹窗js
  //显示对话框
  showModal: function () {
    // 显示遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
      showModalStatus: true
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export()
      })
    }.bind(this), 200)
  },
  
  //隐藏对话框
  hideModal: function () {
    // 隐藏遮罩层
    var animation = wx.createAnimation({
      duration: 200,
      timingFunction: "linear",
      delay: 0
    })
    this.animation = animation
    animation.translateY(300).step()
    this.setData({
      animationData: animation.export(),
    })
    setTimeout(function () {
      animation.translateY(0).step()
      this.setData({
        animationData: animation.export(),
        showModalStatus: false
      })
    }.bind(this), 200)
  }
})