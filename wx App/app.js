//app.js
App({
  onLaunch: function () {
    // 展示本地存储能力
    var logs = wx.getStorageSync('logs') || []
    logs.unshift(Date.now())
    wx.setStorageSync('logs', logs)

    // 登录
    wx.login({
      success: res => {
        // 发送 res.code 到后台换取 openId, sessionKey, unionId
      }
    })
    // 获取用户信息
    wx.getSetting({
      success: res => {
        if (res.authSetting['scope.userInfo']) {
          // 已经授权，可以直接调用 getUserInfo 获取头像昵称，不会弹框
          wx.getUserInfo({
            success: res => {
              // 可以将 res 发送给后台解码出 unionId
              this.globalData.userInfo = res.userInfo
              this.register()
              // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
              // 所以此处加入 callback 以防止这种情况
              if (this.userInfoReadyCallback) {
                this.userInfoReadyCallback(res)
              }
            }
          })
        }
      }
    })
    
  },
  register: function (options) {
    let that =this
    wx.request({
      url: "http://www.mallproject.cn:8000/api/user/",
      method: "POST",
      data: {

        user_nickname: that.globalData.userInfo.nickName

      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      success: function (res) {
        console.log(res.data);
        that.globalData.user_id = res.data.user_id
        wx.request({
          url: 'http://www.mallproject.cn:8000/api/user/?user_nickname__icontains=' + this.globalData.userInfo.nickName,
          success: data => {
            that.globalData.user_id = data.data[0].user_id
            console.log("test1 ", that.globalData.user_id)

          }
        })
      },
    })

    wx.request({
      url: 'http://www.mallproject.cn:8000/api/user/?user_nickname__icontains=' + this.globalData.userInfo.nickName,
      success: data => {
        this.globalData.user_id = data.data[0].user_id
        console.log("test1 ", this.globalData.user_id)

      }
    })

  },
  globalData: {
    userInfo: null,
    user_id:1,
  }
})