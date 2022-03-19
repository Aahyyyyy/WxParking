const app = getApp()
const anumber = app.globalData.anumber;
Page({

  /**
   * 页面的初始数据
   */
  data: {
    input: '',
    area: "B1层",
    ano: 'A01',
    Cno: "",
    index1: 0,
    index2: 0,
    index3: 0,
    index4: -1,
    array1: [],
    array2: ['全部', '可售', '已定', '已售'],
    array3: ['正序', '倒序'],
    type: 0,
    choseQuestionBank: "区域选择",
    parking_list: [],
    TimeID: -1,
    showActionsheet: true,
    groups: [{
        name: 'B1层',
        id: 1
      },
      {
        name: 'B2层',
        id: 2
      },
    ]
  },

  more_detail: function (e) {
    var that = this
    //获取当前点击元素的id(索引值)
    //获取当前点击元素的属性值。
    var mesg = e.currentTarget.dataset.item.pno;
    //跳转到详情页 
    wx.navigateTo({
      url: '../park_detail/park_detail?pno=' + mesg,
    })
  },

  //第一次申请车位列表
  request_parking: function (e) {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/parking/queryAllParking',
      data: {
        ano: wx.getStorageSync('anumber'),
        cno: e.cno,
        bno: e.bno
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          parking_list: res.data
        })
      }
    })
  },

  onLoad(options) {
    var that = this
    console.log(options)
    that.setData({
      Cno: options.cno,
      Bno: options.bno
    })
    //申请车位区域数据
    wx.request({
      url: app.globalData.baseUrl + '/parking/allParkingAname',
      data: {
        cno: options.cno,
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
            array1: res.data,
          }),
          wx.setStorageSync('anumber', res.data[0].ano)
        that.request_parking(options)
      },
    })
  },

  getParkByKey: function (e) {
    var that = this
    that.setData({
      input: e.detail.value
    })
    clearTimeout(this.TimeId)
    this.TimeId = setTimeout(() => {
      wx.request({
        url: app.globalData.baseUrl + '/parking/getParkByKey',
        data: {
          key: that.data.input,
          ano: that.data.array1[that.data.index1].ano,
          cno: that.data.Cno,
          pstatus: that.data.index3,
          bno: that.data.Bno
        },
        success: function (res) {
          console.log(res.data)
          that.setData({
            parking_list: res.data
          })
        }
      })
    }, 500)
  },

  //车位信息请求函数，要求参数，区域编号，小区编号
  request_parking_by_area: function () {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/parking/queryAllParking',
      data: {
        ano: that.data.array1[that.data.index1].ano,
        cno: that.data.Cno,
        bno: that.data.Bno,
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          parking_list: res.data
        })
        console.log(that.data.parking_list)
      }
    })
  },

  //车位信息请求函数，要求参数：区域编号，小区编号，筛选状态
  request_parking_by_pstatus: function () {
    var that = this
    wx.request({
      url: app.globalData.baseUrl + '/parking/queryParkingPstatus',
      data: {
        ano: that.data.array1[that.data.index1].ano,
        cno: that.data.Cno,
        pstatus: that.data.index4,
        bno: that.data.Bno
      },
      header: {
        'content-type': 'application/json' // 默认值,get方法   
      },
      success: function (res) {
        that.setData({
          parking_list: res.data
        })
        console.log(that.data.parking_list)
      }
    })
  },

  //区域picker筛选事件
  bindPickerChange1: function (e) {
    var that = this
    this.setData({
      index1: e.detail.value
    })
    //判断此时的筛选的状态
    if (that.data.index4 == "-1") {
      this.request_parking_by_area()
    } else {
      this.request_parking_by_pstatus()
    }
  },

  //状态picker筛选事件
  bindPickerChange2: function (e) {
    var that = this
    this.setData({
      index4: e.detail.value - 1,
      index2: e.detail.value
    })
    //选中第一个全部
    if (that.data.index4 == "-1") {
      this.request_parking_by_area()
    }
    //选中下面三个
    else {
      this.request_parking_by_pstatus()
    }
  },

  bindPickerChange3: function (e) {
    var that = this
    that.setData({
      index3: e.detail.value
    })
    if(that.data.index3 == "0") {
      that.data.parking_list.sort(that.compare1("Pdistance"));
      that.setData({
        parking_list: that.data.parking_list
      })
    } else {
      that.data.parking_list.sort(that.compare2("Pdistance"));
      that.setData({
        parking_list: that.data.parking_list
      })
    }
  },

  

  compare1: function (property) {
    return function (a, b) {
      var value1 = a[property];
      var value2 = b[property];
      return value2 - value1;
    }
  },

  compare2: function (property) {
    return function (a, b) {
      var value1 = a[property];
      var value2 = b[property];
      return value1 - value2;
    }
  },

  // 防抖动
  inputTyping: function (e) {
    this.TimeID = setTimeout(() => {
      //4.准备发送请求获取数据
      this.search_Typing(e);
    }, 1000);
  },

  inputTyping: function (e) {
    clearTimeout(this.TimeID);
    this.data.parking_list.forEach((r) => { //array是后台返回的数据
      r.search_status = false; //r = array[0]的所有数据，这样直接 r.新属性 = 属性值 即可
    })
    this.setData({ //这里划重点 需要重新setData 下才能js 和 wxml 同步，wxml才能渲染新数据
      parking_list: this.data.parking_list
    })
    console.log("e", e)
    console.log("查看parkinglist的搜索状态", this.data.parking_list)
    console.log("查看inputValue", this.data.inputValue)

    this.setData({
      inputValue: e.detail.value
    })

    // 初始查询数组
    this.setData({
      searchResultTemp: []
    })
    // 最终查询结果
    this.setData({
      searchResult: []
    })
    // 输入的值
    this.setData({
      inputVal: e.detail
    })
    // 将总数据中存在输入内容的数据存到查询数组中
    this.data.parking_list.forEach(res => {
      if (this.data.inputValue.length == 0) {
        res.search_status = true
      } else if (res.pno.indexOf(this.data.inputValue) > -1 && this.data.inputValue.length > 0) {
        res.search_status = true
      }
    })
    // 更新查询数组
    this.setData({
      parking_list: this.data.parking_list
    })
  },

  viewImg1() {
    wx.previewImage({
      current: 'http://r35lytik8.hd-bkt.clouddn.com/image/parking1.png',
      urls: [
        'http://r35lytik8.hd-bkt.clouddn.com/image/parking1.png'
      ]
    })
  },

  viewImg2() {
    wx.previewImage({
      current: 'http://r35lytik8.hd-bkt.clouddn.com/image/parking2.png',
      urls: [
        'http://r35lytik8.hd-bkt.clouddn.com/image/parking2.png'
      ]
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
      wx.hideLoading();
      wx.hideNavigationBarLoading();
      wx.showToast({
        icon: 'loading',
        title: 'Loading',
        mask: true
      })
    }, 100);
    this.getaccount()
  }
})