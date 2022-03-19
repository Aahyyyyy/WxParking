const app = getApp()
Page({
    data: {
        convList: [{
                id: "backend",
                userSig: "eJwtzMEKgkAUheF3mW0hV*2KI7QpkSBLysz12Ex2sURUNI3ePVOX5zvwf9jFD7VGlcxhhgZsOW6SKq-pTiMn4papXM5XJTNRFCSZo68ALB044PSod0GlGhwRDQCYtKbX3yyTo4mcm3OF0qEsqti3-aa-HpNogW4bnF13u7Gfadm1p2AfHTyve*z6PgzTNfv*AKTNMo8_",
                name: "系统通知",
                img: "../../images/Icon/system.png",
                msg: "系统检测到您尚未完成实名认证，请尽快完成"
            },
            {
                id: "saler",
                userSig: "eJyrVgrxCdYrSy1SslIy0jNQ0gHzM1NS80oy0zLBwsWJOUAaIlGckp1YUJCZomRlaGJgYGZoYGlgCpFJrSjILEoFipuamhoZGBhAREsyc0FiZsaWpsamZkYWUFMy04Hm*iQnhxY6*UYEhuXH6JcVuho6ReaZR7kHeoTH6JsWJRV55ERWOptm*Vc4Zzil2yrVAgCR-jIx",
                name: "活动消息",
                img: "../../images/Icon/actmsg.png",
                msg: "您关注的车位开盘啦！"
            },
            {
                id: "backend",
                userSig: "eJwtzMEKgkAUheF3mW0hV*2KI7QpkSBLysz12Ex2sURUNI3ePVOX5zvwf9jFD7VGlcxhhgZsOW6SKq-pTiMn4papXM5XJTNRFCSZo68ALB044PSod0GlGhwRDQCYtKbX3yyTo4mcm3OF0qEsqti3-aa-HpNogW4bnF13u7Gfadm1p2AfHTyve*z6PgzTNfv*AKTNMo8_",
                name: "官方客服",
                img: "../../images/Icon/kefu.png",
                msg: "亲，请问有什么需要帮助的嘛"
            }
        ],
        userSig: "eJyrVgrxCdYrSy1SslIy0jNQ0gHzM1NS80oy0zLBwvnleUAaIlGckp1YUJCZomRlaGJgYGZoYGlgCpFJrSjILEoFipuamhoZGBhAREsyc0FiZsaWpiaW5gaGUFMy04HmuhZ7JmbkZruEmQVZFod7OEdUlppHWhiFZfj5JVo4ZpRElcboV3mUOTkWFHjaKtUCAGxPMgs_",
        userInfo: {},
        hasUserInfo: false,
        canIUse: wx.canIUse('button.open-type.getUserInfo')
    },

    onLoad() {
        if (app.globalData.userInfo) {
            this.setData({
                userInfo: app.globalData.userInfo,
                hasUserInfo: true
            })
        } else if (this.data.canIUse) {
            // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
            // 所以此处加入 callback 以防止这种情况
            app.userInfoReadyCallback = res => {
                this.setData({
                    userInfo: res.userInfo,
                    hasUserInfo: true
                })
            }
        } else {
            // 在没有 open-type=getUserInfo 版本的兼容处理
            wx.getUserInfo({
                success: res => {
                    app.globalData.userInfo = res.userInfo
                    this.setData({
                        userInfo: res.userInfo,
                        hasUserInfo: true
                    })
                }
            })
        }
    },

    toChat: function (e) {
        console.log(e.currentTarget.dataset.id)
        let id = e.currentTarget.dataset.id
        let userID = 'owner'
        let to_user = id
        let userSig = this.data.userSig;
        let conversationID = "C2C" + to_user;
        console.log(userSig);
        wx.navigateTo({
            url: `../chat/chat?userID=${userID}&to_user=${to_user}&userSig=${userSig}&conversationID=${conversationID}`
        })
    }
})