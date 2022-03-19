var QQMapWX = require('../../qqmap-wx-jssdk1.2/qqmap-wx-jssdk.min.js');
var qqmapsdk;
var app = getApp()
Page({
  data: {
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
    latitude: '',
    longitude: '',
    centerData: {},
    nearList: [],
    suggestion: [],
    selectedId: 0,
    defaultKeyword: '房产小区',
    keyword: '',
    marker: [],
    marker_to_show: [],
    marker_temp: [],
    latitemp: '',
    longtemp: '',
    brandimg: '',
    com_local: []
  },

  onLoad: async function () {
    let self = this;
    self.mapCtx = wx.createMapContext('myMap')
    qqmapsdk = new QQMapWX({
      key: 'W57BZ-JDB6X-XPA4H-Z76MI-73FF2-24BT4'
    });
    wx.showLoading({
      title: '加载中'
    });
    wx.getLocation({
      type: 'gcj02',
      success(res) {
        const latitude = res.latitude
        const longitude = res.longitude
        const speed = res.speed
        const accuracy = res.accuracy
        //你地址解析
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
              keyword: self.data.defaultKeyword
            })
            // 调用接口
            self.nearby_search();
            that.distance_cal()
          },
        });
      },
      fail(err) {
        //console.log(err)
        wx.hideLoading({});
        wx.showToast({
          title: '定位失败',
          icon: 'none',
          duration: 1500
        })
        setTimeout(function () {
          wx.navigateBack({
            delta: 1
          })
        }, 1500)
      }
    })

    var that = this
    const res = await this.getCom_address()
    for (var i = 0; i < res.data.length; i++) {
      var string = 'marker[' + i + ']'
      console.log(res)
      that.setData({
        [string]: {
          id: Number(res.data[i].cno),
          latitude: res.data[i].lat,
          longitude: res.data[i].lng,
          iconPath: "../../images/Icon/marker.png",
          width: 28,
          height: 32,
          cname: res.data[i].cname,
          cphoto: res.data[0].cno,
          dno: res.data[0].dno,
          caddress: res.data[i].caddress,
          label: {
            content: res.data[i].cname,
            color: '#22ac38',
            fontSize: 14,
            bgColor: "#fff",
            borderRadius: 30,
            borderColor: "#22ac38",
            borderWidth: 1,
            padding: 3
          },
        }
      })
    }
  },

  //获取小区地址以做解析
  getCom_address: function () {
    return new Promise((resolve, reject) => {
      var that = this
      wx.request({
        url: app.globalData.baseUrl + '/community/findAll2',
        header: {
          'content-type': 'application/json' // 默认值,get方法   
        },
        success: function (res) {
          resolve(res)
        },
        fail: function () {
          console.log('1')
        }
      })
    })
  },

  //监听拖动地图，拖动结束根据中心点更新页面
  mapChange: function (e) {
    let self = this;
    if (e.type == 'end' && (e.causedBy == 'scale' || e.causedBy == 'drag')) {
      self.mapCtx.getCenterLocation({
        success: function (res) {
          //console.log(res)
          self.setData({
            nearList: [],
            latitude: res.latitude,
            longitude: res.longitude,
          })
          self.nearby_search();
        }
      })
    }
  },

  //重新定位
  reload: function () {
    var that = this
    console.log(that.data.marker)
  },

  //整理目前选择省市区的省市区列表
  getRegionData: function () {
    let self = this;
    //调用获取城市列表接口
    qqmapsdk.getCityList({
      success: function (res) { //成功后的回调
        //console.log(res)
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
        for (var i = 0; i < res.result[1].length; i++) {
          var name = res.result[1][i].fullname;
          if (self.data.currentRegion.city == name) {
            qqmapsdk.getDistrictByCityId({
              // 传入对应省份ID获得城市数据，传入城市ID获得区县数据,依次类推
              id: res.result[1][i].id,
              success: function (res) {
                districtArr = res.result[0];
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
    });
  },

  onShow: function () {
    let self = this;
  },

  //地图标记点
  addMarker: function (data) {
    var mks = [];
    mks.push({ // 获取返回结果，放到mks数组中
      title: data.title,
      id: data.id,
      addr: data.addr,
      province: data.province,
      city: data.city,
      district: data.district,
      latitude: data.latitude,
      longitude: data.longitude,
      iconPath: "/images/my_marker.png",
      width: 25,
      height: 25
    })
    this.setData({ //设置markers属性，将搜索结果显示在地图中
      markers: mks,
      currentRegion: {
        province: data.province,
        city: data.city,
        district: data.district,
      }
    })
    wx.hideLoading({});
  },

  //点击选择搜索结果
  backfill: function (e) {
    var id = e.currentTarget.id;
    let name = e.currentTarget.dataset.name;
    for (var i = 0; i < this.data.suggestion.length; i++) {
      if (i == id) {
        this.setData({
          centerData: this.data.suggestion[i],
          addListShow: false,
          latitude: this.data.suggestion[i].latitude,
          longitude: this.data.suggestion[i].longitude
        });
        this.nearby_search()
        return;
      }
    }
  },


  SicBo: function (i) {
    return new Promise((resolve, reject) => {
      var that = this
      qqmapsdk.calculateDistance({
        from: that.data.latitude + ',' + that.data.longitude,
        to: that.data.marker[i].latitude + ',' + that.data.marker[i].longitude,
        success: function (res) {
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
    console.log("测试起点坐标", that.data.latitude)
    console.log("测试起点坐标", that.data.longitude)
    that.data.marker.forEach((r) => { //array是后台返回的数据
      r.distance = 0; //r = array[0]的所有数据，这样直接 r.新属性 = 属性值 即可
    })
    that.setData({ //这里划重点 需要重新setData 下才能js 和 wxml 同步，wxml才能渲染新数据
      marker: that.data.marker
    })
    for (let i = 0; i < that.data.marker.length; i++) {
      var string = 'marker[' + i + '].distance'
      // 由于使用for循环调用接口，造成了异步问题，因此将申请调用计算地址封装入promise，并使用async以及await保证同步
      const distance = await that.SicBo(i)
      that.setData({
        [string]: distance
      })
    }
    that.data.marker.sort(that.compare("distance"));
    that.setData({
      marker: that.data.marker
    })
  },

  compare: function (property) {
    return function (a, b) {
      var value1 = a[property];
      var value2 = b[property];
      return value1 - value2;
    }
  },

  //点击选择地图下方列表某项
  chooseCenter: function (e) {
    var id = e.currentTarget.id;
    let name = e.currentTarget.dataset.name;
    for (var i = 0; i < this.data.nearList.length; i++) {
      if (i == id) {
        this.setData({
          selectedId: id,
          centerData: this.data.nearList[i],
          latitude: this.data.marker[i].latitude,
          longitude: this.data.marker[i].longitude,
        });
        this.distance_cal(i)
        this.addMarker(this.data.nearList[id])
        return;
      }
    }
  },

  //显示搜索列表
  showAddList: function () {
    this.setData({
      addListShow: true
    })
  },

  // 根据关键词搜索附近位置
  nearby_search: function () {
    var self = this;
    wx.hideLoading();
    wx.showLoading({
      title: '加载中'
    });
    // 调用接口
    qqmapsdk.search({
      keyword: self.data.keyword, //搜索关键词
      location: self.data.latitude + ',' + self.data.longitude,
      page_size: 20,
      page_index: 1,
      success: function (res) {
        var sug = [];
        for (var i = 0; i < res.data.length; i++) {
          sug.push({ // 获取返回结果，放到sug数组中
            title: res.data[i].title,
            id: res.data[i].id,
            addr: res.data[i].address,
            province: res.data[i].ad_info.province,
            city: res.data[i].ad_info.city,
            district: res.data[i].ad_info.district,
            latitude: res.data[i].location.lat,
            longitude: res.data[i].location.lng
          });
        }
        self.setData({
          selectedId: 0,
          centerData: sug[0],
          nearList: sug,
          suggestion: sug
        })
        self.distance_cal()
        self.addMarker(sug[0]);
      }
    });
  },
  //根据关键词搜索匹配位置
  getsuggest: function (e) {
    var _this = this;
    var keyword = e.detail.value;
    _this.setData({
      addListShow: true
    })
    //调用关键词提示接口
    qqmapsdk.getSuggestion({
      //获取输入框值并设置keyword参数
      keyword: keyword, //用户输入的关键词，可设置固定值,如keyword:'KFC'
      location: _this.data.latitude + ',' + _this.data.longitude,
      page_size: 20,
      page_index: 1,
      success: function (res) {
        var sug = [];
        for (var i = 0; i < res.data.length; i++) {
          sug.push({ // 获取返回结果，放到sug数组中
            title: res.data[i].title,
            id: res.data[i].id,
            addr: res.data[i].address,
            province: res.data[i].province,
            city: res.data[i].city,
            district: res.data[i].district,
            latitude: res.data[i].location.lat,
            longitude: res.data[i].location.lng
          });
        }
        _this.setData({ //设置suggestion属性，将关键词搜索结果以列表形式展示
          suggestion: sug,
          nearList: sug,
          keyword: keyword
        });
      }
    });
  },

  //打开选择省市区页面
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
      currentCity: name,
      currentDistrict: '请选择城市',
    })
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
      keyword: self.data.defaultKeyword
    })
    self.nearby_search();
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
    });
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
    let pages = getCurrentPages(); //获取当前页面js里面的pages里的所有信息。
    let prevPage = pages[pages.length - 2];
    prevPage.setData({
      storeAddress: this.data.centerData.title
    })
    wx.navigateBack({
      delta: 1
    })
  },

  // 点击标志事件
  markertap(e) {
    var that = this
    var id
    for (var i = 0; i < that.data.marker.length; i++) {
      if (that.data.marker[i].id == e.markerId) {
        id = i
      }
    }
    this.setData({
      marker_to_show: this.data.marker[id]
    })
    this.showModal()
  },

  goto_com: function (e) {
    wx.navigateTo({
      url: '../com_detail/com_detail?Cno=' + this.data.marker_to_show.id
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