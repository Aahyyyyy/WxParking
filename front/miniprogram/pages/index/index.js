var QQMapWX = require('../../qqmap-wx-jssdk1.2/qqmap-wx-jssdk.min.js');
var qqmapsdk;
const app = getApp()
wx - Page({

  /**
   * 页面的初始数据
   */
  data: {
    latitude: '',
    longitude: '',
    currentCity: "",
    array1: ['按距离排序', '按均价排序'],
    comList: [],
    comListTemp: [],
    parking_count: [],
    show_status: -1,
    userid: "",
    swiperList: [{
        image_src: "http://r35lytik8.hd-bkt.clouddn.com/image/banner1.jpg",
        open_type: "navigate",
        image_id: 1,
        navigator_url: ""
      },
      {
        image_src: "http://r35lytik8.hd-bkt.clouddn.com/image/banner2.jpg",
        open_type: "navigate",
        image_id: 2,
        navigator_url: ""
      },
      {
        image_src: "http://r35lytik8.hd-bkt.clouddn.com/image/banner3.jpg",
        open_type: "navigate",
        image_id: 3,
        navigator_url: ""
      }
    ],
    tabList: [{
        id: 0,
        value: "推荐车位",
        isActive: true
      },
      {
        id: 1,
        value: "距离最短",
        isActive: false
      },
      {
        id: 2,
        value: "均价最低",
        isActive: false
      }
    ]
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    qqmapsdk = new QQMapWX({
      key: 'W57BZ-JDB6X-XPA4H-Z76MI-73FF2-24BT4'
    });
    this.getaccount()
    this.userid = wx.getStorageSync('openid')
  },

  find_com_detail: async function (e) {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/parking/findParkingCountAll',
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          parking_count: res.data
        })
        for (var i = 0; i < res.data.length; i++) {
          that.data.comList.forEach((r) => { //array是后台返回的数据
            r.avgPrice = that.data.parking_count[r.id - 1].avgPrice; //r = array[0]的所有数据，这样直接 r.新属性 = 属性值 即可
          })
          that.setData({ //这里划重点 需要重新setData 下才能js 和 wxml 同步，wxml才能渲染新数据
            comList: that.data.comList
          })
        }
      }
    })
  },

  SicBo: function (i) {
    return new Promise((resolve, reject) => {
      var that = this

      qqmapsdk.calculateDistance({
        from: wx.getStorageSync('lat') + ',' + wx.getStorageSync('lng'), //若起点有数据则采用起点坐标，若为空默认当前地址
        to: that.data.comList[i].latitude + ',' + that.data.comList[i].longitude,
        success: function (res) { //成功后的回调
          var distance = res.result.elements[0].distance;
          resolve(distance)
        },
        fail: function (error) {
          console.error(error);
        },
        complete: function (res) {
          console.log(res);
        }
      });
    })

  },

  //计算小区与当前坐标的距离
  distance_cal: async function (e) {
    var that = this
    that.data.comList.forEach((r) => { //array是后台返回的数据
      r.distance = 0; //r = array[0]的所有数据，这样直接 r.新属性 = 属性值 即可
    })
    that.setData({ //这里划重点 需要重新setData 下才能js 和 wxml 同步，wxml才能渲染新数据
      comList: that.data.comList
    })

    for (let i = 0; i < that.data.comList.length; i++) {
      var string = 'comList[' + i + '].distance'
      // 由于使用for循环调用接口，造成了异步问题，因此将申请调用计算地址封装入promise，并使用async以及await保证同步
      const distance = await that.SicBo(i)
      console.log(that.data.comList[i].cname);
      console.log("distance:", distance);
      that.setData({
        [string]: distance
      })
    }
    that.setData({
      comList: that.data.comList
    })
  },

  compare: function (property) {
    return function (a, b) {
      var value1 = a[property];
      var value2 = b[property];
      return value1 - value2;
    }
  },

  async city_back(e) {
    console.log("主页的city_back函数", e.detail)
    this.setData({
      currentCity: e.detail
    })

    this.data.comList.splice(0, this.data.comList.length)
    for (var i = 0; i < this.data.comListTemp.length; i++) {
      if (this.data.comListTemp[i].city == e.detail) {
        this.data.comList.push(this.data.comListTemp[i])
      }
    }
    this.setData({
      comList: this.data.comList
    })
    console.log("comListClear", this.data.comListTemp)
    this.distance_cal()
    this.find_com_detail()
  },

  onShow: function (options) {
    this.data.userid = wx.getStorageSync('openid')
    this.getOwner()
  },

  getaccount() {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/community/findAll2',
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        wx.stopPullDownRefresh()
        for (var i = 0; i < res.data.length; i++) {
          var string = 'comListTemp[' + i + ']'
          that.setData({
            [string]: {
              id: res.data[i].cno,
              latitude: res.data[i].lat,
              longitude: res.data[i].lng,
              cname: res.data[i].cname,
              cphoto: res.data[0].cno,
              dno: res.data[0].dno,
              caddress: res.data[i].caddress,
              distance: 0,
              city: res.data[i].Ccity
            }
          })
        }
      },
      fail: function () {
        console.log('1')
      }
    })
  },

  getOwner() {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/owner/findOwnerById?id=' + that.data.userid,
      success: function (res) {
        that.setData({
          ownerInfo: res.data
        })
      }
    })
  },

  isIden() {
    var that = this
    if (that.data.ownerInfo.Oidauthflog == "0") {
      wx.navigateTo({
        url: '../identity/identity'
      })
    } else if (that.data.ownerInfo.Oidauthflog == "1" && that.data.ownerInfo.Ocertificationflog == "0") {
      wx.navigateTo({
        url: '../certification/certification'
      })
    } else if (that.data.ownerInfo.Oidauthflog == "1" && that.data.ownerInfo.Ocertificationflog == "1") {
      wx.navigateTo({
        url: '../userinfo/userinfo'
      })
    } else if (that.data.ownerInfo.Ocertificationflog == '2') {
      wx.navigateTo({
        url: '../userinfo/userinfo'
      })
    } else {
      wx.navigateTo({
        url: '../identity/identity'
      })
    }
  },

  handleTabsItemChange(e) {
    const {
      index
    } = e.detail;
    let {
      tabList
    } = this.data;
    var that = this
    var temp_status
    console.log("e.detail", e.detail)
    tabList.forEach((v, i) => i === index ? v.isActive = true : v.isActive = false);
    if (e.detail.index == 1) {
      that.data.comList.sort(that.compare("distance"));
      temp_status = 0
    } else if (e.detail.index == 2) {
      that.data.comList.sort(that.compare("avgPrice"));
      temp_status = 1
    } else if (e.detail.index == 0) {
      temp_status = -1
    }
    that.setData({
      tabList,
      comList: that.data.comList,
      show_status: temp_status
    })
    console.log("comList", that.data.comList)
    console.log("show_status", that.data.show_status)
  },

  more_detail: function (e) {
    var that = this
    console.log("传值", e)
    console.log(that.data.comList)
    //获取当前点击元素的id(索引值)
    var Id = e.currentTarget.dataset.id;
    //获取当前点击元素的属性值。
    wx.navigateTo({
      url: '../com_detail/com_detail?Cno=' + Id
    })
  },

  // 刷新
  onPullDownRefresh: function () {
    //调用刷新时将执行的方法
    this.onRefresh();
  },

  onRefresh() {
    //在当前页面显示导航条加载动画
    wx.showNavigationBarLoading();
    //显示 loading 提示框。需主动调用 wx.hideLoading 才能关闭提示框
    wx.showLoading({
      title: 'loading',
    })
    setTimeout(() => {
      wx.hideNavigationBarLoading();
      wx.showToast({
        icon: 'loading',
        title: 'Loading',
        mask: true
      })
    }, 100);
    this.getaccount()
  },
})