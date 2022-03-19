var QQMapWX = require('../../qqmap-wx-jssdk1.2/qqmap-wx-jssdk.min.js');
var qqmapsdk;
var app = getApp()
const amapFile = require('../../lib/amap-wx.130.js')
Component({
  /**
   * 组件的属性列表
   */
  properties: {

  },

  /**
   * 组件的初始数据
   */
  data: {
    latitude: '',
    longitude: '',
    chooseCity: false,
    address: "",
    addlen: 0,
    addListShow: false,
    chooseCity: false,
    regionShow: {
      province: false,
      city: false,
      district: true
    },
    regionData: {},
    currentRegion: {
      province: '选择城市',
      city: '选择城市',
      district: '选择城市',
    },
    currentProvince: '选择城市',
    currentCity: '选择城市',
    currentDistrict: '选择城市',
  },

  ready() {
    let self = this
    qqmapsdk = new QQMapWX({
      key: 'W57BZ-JDB6X-XPA4H-Z76MI-73FF2-24BT4'
    });
    wx.getLocation({
      type: 'gcj02',
      success(res) {
        console.log(res)
        const latitude = res.latitude
        const longitude = res.longitude
        const speed = res.speed
        const accuracy = res.accuracy
        // 逆地址解析
        qqmapsdk.reverseGeocoder({
          location: {
            latitude: latitude,
            longitude: longitude
          },
          success: function (res) {
            self.setData({
              latitude: latitude,
              longitude: longitude,
              currentRegion: res.result.address_component,
            })
            wx.setStorageSync('lat', latitude)
            wx.setStorageSync('lng', longitude)
          },
        });
      },
      fail(err) {
        wx.hideLoading({});
        wx.showToast({
          title: '定位失败',
          icon: 'none',
          duration: 1500
        })
      }
    })

    var myAmapFun = new amapFile.AMapWX({
      key: '0214dcff2e3a806f75abd2a0edfa5681'
    });
    myAmapFun.getRegeo({
      success: (res) => {
        console.log("城市？", res[0].regeocodeData.addressComponent.city)
        string1 = "currentRegion.city"
        this.setData({
          [string1]: res[0].regeocodeData.addressComponent.city
        })
        this.city_back()
      }
    })
  },

  /**
   * 组件的方法列表
   */
  methods: {
    city_back: function () {
      this.triggerEvent('backDataTap', this.data.currentRegion.city)
    },

    location_back: function () {
      var location = [
        this.data.latitude,
        this.data.longitude
      ]
      this.triggerEvent('backDataTap', location)
    },

    getLocation: function () {
      var myAmapFun = new amapFile.AMapWX({
        key: '0214dcff2e3a806f75abd2a0edfa5681'
      });
      myAmapFun.getRegeo({
        success: (res) => {
          this.setData({
            address: res[0].regeocodeData.addressComponent.city
          })
        }
      })
    },

    // 选择城市列表
    // 打开选择省市区页面
    chooseCity: function () {
      let self = this;
      self.getRegionData();
      self.setData({
        chooseCity: true,
        regionShow: {
          province: false,
          city: false,
          district: true
        },
        currentProvince: self.data.currentRegion.province,
        currentCity: self.data.currentRegion.city,
        currentDistrict: self.data.currentRegion.district,
      })
    },

    getRegionData: function () {
      let self = this;
      //调用获取城市列表接口
      qqmapsdk.getCityList({
        success: function (res) {
          let provinceArr = res.result[0];
          let cityArr = [];
          let districtArr = [];
          for (var i = 0; i < provinceArr.length; i++) {
            var name = provinceArr[i].fullname;
            if (self.data.currentRegion.province == name) {
              if (name == '北京市' || name == '天津市' || name == '上海市' || name == '重庆市') {
                cityArr.push(provinceArr[i])
              } else {
                qqmapsdk.getDistrictByCityId({
                  // 传入对应省份ID获得城市数据，传入城市ID获得区县数据,依次类推
                  id: provinceArr[i].id,
                  success: function (res) {
                    cityArr = res.result[0];
                    self.setData({
                      regionData: {
                        province: provinceArr,
                        city: cityArr,
                        district: districtArr
                      }
                    })
                  }
                });
              }
            }
          }
        }
      });
    },

    //选择省
    showProvince: function () {
      this.setData({
        regionShow: {
          province: true,
          city: false,
          district: false
        }
      })
    },

    //选择城市
    showCity: function () {
      this.setData({
        regionShow: {
          province: false,
          city: true,
          district: false
        }
      })
    },

    //选择地区
    showDistrict: function () {
      this.setData({
        regionShow: {
          province: false,
          city: false,
          district: true
        }
      })
    },

    //选择省之后操作
    selectProvince: function (e) {
      let self = this;
      let id = e.currentTarget.dataset.id;
      let name = e.currentTarget.dataset.name;
      self.setData({
        currentProvince: name,
        currentCity: '请选择城市',
      })
      if (name == '北京市' || name == '天津市' || name == '上海市' || name == '重庆市') {
        var provinceArr = self.data.regionData.province;
        var cityArr = [];
        for (var i = 0; i < provinceArr.length; i++) {
          if (provinceArr[i].fullname == name) {
            cityArr.push(provinceArr[i])
            self.setData({
              regionData: {
                province: self.data.regionData.province,
                city: cityArr,
                district: self.data.regionData.district
              }
            })
            self.showCity();
            return;
          }
        }
      } else {
        let bj = self.data.regionShow;
        self.getById(id, name, bj)
      }
    },
    //选择城市之后操作
    selectCity: function (e) {
      let self = this;
      let id = e.currentTarget.dataset.id;
      let name = e.currentTarget.dataset.name;
      self.setData({
        currentRegion: {
          province: self.data.currentProvince,
          city: name,
        },
        currentCity: name,
        currentDistrict: '请选择城市',
        chooseCity: false,
      })
      this.city_back()
      let bj = self.data.regionShow;
      self.getById(id, name, bj)
    },

    //选择区县之后操作
    selectDistrict: function (e) {
      let self = this;
      let id = e.currentTarget.dataset.id;
      let name = e.currentTarget.dataset.name;
      let latitude = e.currentTarget.dataset.latitude;
      let longitude = e.currentTarget.dataset.longitude;
      self.setData({
        currentDistrict: name,
        latitude: latitude,
        longitude: longitude,
        currentRegion: {
          province: self.data.currentProvince,
          city: self.data.currentCity,
          district: name
        },
        chooseCity: false,
      })
      console.log("组件中的全局变量city", app.globalData.city)
    },

    //根据选择省市加载市区列表
    getById: function (id, name, bj) {
      let self = this;
      qqmapsdk.getDistrictByCityId({
        // 传入对应省份ID获得城市数据，传入城市ID获得区县数据,依次类推
        id: id, //对应接口getCityList返回数据的Id，如：北京是'110000'
        success: function (res) {
          if (bj.province) {
            self.setData({
              regionData: {
                province: self.data.regionData.province,
                city: res.result[0],
                district: self.data.regionData.district
              }
            })
            self.showCity();
          } else if (bj.city) {
            self.setData({
              regionData: {
                province: self.data.regionData.province,
                city: self.data.regionData.city,
                district: res.result[0]
              }
            })
            self.showDistrict();
          } else {
            self.setData({
              chooseCity: false,
            })
          }
        }
      })
    },

    //返回上一页或关闭搜索页面
    back1: function () {
      if (this.data.addListShow) {
        this.setData({
          addListShow: false
        })
      } else {
        wx.navigateBack({
          delta: 1
        })
      }
    },

    //关闭选择省市区页面
    back2: function () {
      this.setData({
        chooseCity: false
      })
    },

    //确认选择地址
    selectedOk: function () {
      let pages = getCurrentPages();
      let prevPage = pages[pages.length - 2];
      prevPage.setData({
        storeAddress: this.data.centerData.title
      })
      wx.navigateBack({
        delta: 1
      })
    }
  }
})