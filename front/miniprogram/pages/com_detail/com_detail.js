const app = getApp()
Page({
  data: {
    Cno: 'C01',
    com: [],
    buildingMarker: [],
    parking_count: [],
    Owner: []
  },

  // 事件处理函数
  bindViewTap() {
    wx.navigateTo({
      url: '../logs/logs'
    })
  },

  // 获取该小区的楼盘列表
  findBuilding: function (e) {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/building/findBuildByCno',
      data: {
        cno: e.Cno
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        console.log("res.data", res.data)
        that.setData({
          building: res.data
        })
        var buildingMarker = 'buildingMarker[' + 0 + ']'
        that.setData({
          building: res.data,
          [buildingMarker]: {
            id: 0,
            latitude: that.data.com[0].lat,
            longitude: that.data.com[0].lng,
            alpha: 0,
            width: 36,
            height: 36,
            label: {
              content: "正门",
              color: '#191919',
              fontSize: 14,
              bgColor: "#fff",
              borderColor: "#191919",
              borderWidth: 2,
              padding: 6
            }
          }
        })

        for (var i = 0; i < res.data.length; i++) {
          var buildingMarker = 'buildingMarker[' + (i + 1) + ']'
          that.setData({
            [buildingMarker]: {
              id: res.data[i].bno,
              latitude: res.data[i].lat,
              longitude: res.data[i].lng,
              iconPath: "../../images/Icon/marker2.png",
              width: 36,
              height: 36,
              callout: { //不适用自定义气泡的东西，在这里面写样式和内容
                content: res.data[i].bname,
                textAlign: "center",
                borderWidth: 0,
                bgColor: 'rgba(255,255,255,0.2)', //背景颜色，可使用rgba
                anchorY: 28,
                anchorX: 0,
                fontSize: 16,
                display: "ALWAYS"
              },
            }
          })
          if (res.data[i].bname == that.data.Owner.Bname && that.data.Owner.Oaddress == that.data.com[0].caddress) {
            var buildingMarker = 'buildingMarker[' + (i + 1) + ']'
            console.log("that.data.Owner.bname", that.data.Owner.Bname)
            that.setData({
              [buildingMarker]: {
                id: res.data[i].bno,
                latitude: res.data[i].lat,
                longitude: res.data[i].lng,
                iconPath: "../../images/Icon/marker2.png",
                width: 36,
                height: 36,
                callout: { //不适用自定义气泡的东西，在这里面写样式和内容
                  anchorX: 0,
                  anchorY: 28,
                  fontSize: 16,
                  borderWidth: 0,
                  display: "ALWAYS",
                  textAlign: "center",
                  content: res.data[i].bname,
                  bgColor: 'rgba(255,255,255,0.2)',
                },
                label: {
                  padding: 6,
                  anchorY: 0,
                  anchorX: -37,
                  fontSize: 14,
                  borderWidth: 3,
                  bgColor: "#fff",
                  content: "我的位置",
                  color: 'rgb(22, 191, 0)',
                  borderColor: "rgb(22, 191, 0)",
                }
              }
            })
          } else if (i == 1) {
            var buildingMarker = 'buildingMarker[' + (i + 1) + '].iconPath'
            that.setData({
              [buildingMarker]: "../../images/Icon/marker3.png"
            })
          }
        }

      }
    })
  },

  onLoad(options) {
    var that = this
    that.setData({
      Cno: options.Cno
    })
    console.log("测试cno", that.data.Cno)
    wx.request({
      url: app.globalData.baseUrl + '/community/findCom',
      data: {
        cno: options.Cno
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res1) {
        that.setData({
            com: res1.data
          }),
          console.log("小区", that.data.com)
        that.getOwner(options)

      }
    })
    //申请小区车位状态数据
    wx.request({
      url: app.globalData.baseUrl + '/parking/findParkingCount',
      data: {
        cno: options.Cno
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          parking_count: res.data
        })
        console.log("测试小区车位状态数量")
        console.log(that.data.parking_count)
      }
    })

  },

  onShow() {
    console.log("openid", wx.getStorageSync('openid'))
  },

  getUserInfo(e) {
    console.log(e)
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },

  more_detail: function () {
    var that = this
    console.log(that.data.com[0].cno)
    var value = wx.getStorageSync('Cno')
    if (value) {
      wx.removeStorageSync('Cno')
    }
    wx.setStorageSync('Cno', that.data.com[0].cno)
    wx.navigateTo({
      url: '../park_list/park_list'
    })
  },

  // 跳转到对应楼盘下的车位列表
  markertap: function (e) {
    var that = this
    // 根据二者地址是否相同判断其是否可以查看
    if (that.data.Owner.Oaddress == that.data.com[0].caddress) {
      wx.navigateTo({
        url: '../park_list/park_list?bno=' + e.detail.markerId + '&cno=' + that.data.com[0].cno
      })
    } else {
      wx.showModal({
        title: '无法查看',
        content: '您未认证或非本小区业主，无法查看车位',
        success(res) {
          if (res.confirm) {
            console.log('用户点击确定')
          } else if (res.cancel) {
            console.log('用户点击取消')
          }
        }
      })
    }
  },


  getOwner(options) {
    var that = this
    console.log("测试openid", wx.getStorageSync('openid'))
    wx.request({
      url: app.globalData.baseUrl + '/owner/findOwnerById?id=' + wx.getStorageSync('openid'),
      success: function (res) {
        console.log("that.data.Owner.bname", res)
        that.setData({
          Owner: res.data
        })
        that.findBuilding(options)
      }
    })
  }
})