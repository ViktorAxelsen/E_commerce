// pages/mine/mine.js
var app = getApp()
Page({
  data: {
    userInfo: {},
    hasUserInfo: false,
    canIUse: wx.canIUse('button.open-type.getUserInfo'),
  orderItems:[  
  
    {
      typeId: 0,
      name: '订单详情',
      url: 'bill',
      imageurl: '../../images/order.png',
    },
    
    {
      typeId: 1,
      name: '我的商店',
      url: 'shangdian',
      imageurl: '../../images/store.png',
    },
    {
      typeId: 2,
      name: '添加商品',
      url: 'upload',
      imageurl: '../../images/upload.png',
    },
  ]
  },
  //事件处理函数
  toOrder: function (e) {
    console.log(e.currentTarget.dataset.typeid)
    var typeid = e.currentTarget.dataset.typeid
    if(typeid == 0){
    wx.navigateTo({
      url: '../purchase/purchase'
    })
    }
    else if(typeid ==1 ){
        this.toMall()
    }
    else{
      this.toUpload()
    }
  },
  toMall: function () {
    wx.navigateTo({
      url: '../mall/mall'
    })
  },
  toUpload: function () {
    wx.navigateTo({
      url: '../uploaditem/uploaditem?storeId=0'
    })
  },
  onLoad: function () {
    if (app.globalData.userInfo) {
      this.setData({
        userInfo: app.globalData.userInfo,
        hasUserInfo: true
      })
    } else if (this.data.canIUse) {
      // 由于 getUserInfo 是网络请求，可能会在 Page.onLoad 之后才返回
      // 所以此处加入 callback 以防止这种情况
      app.userInfoReadyCallback = res => {
        wx.request({
          url: 'http://www.mallproject.cn:8000/api/user/?user_nickname__icontains=' + res.userInfo.nickName,
          success: data => {
            app.globalData.user_id = data.data[0].user_id
            console.log("test1 ", app.globalData.user_id)

          }
        })
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
          wx.request({
            url: 'http://www.mallproject.cn:8000/api/user/?user_nickname__icontains=' + res.userInfo.nickName,
            success: data => {
              app.globalData.user_id = data.data[0].user_id
              console.log("test1 ", app.globalData.user_id)

            }
          })
          this.setData({
            userInfo: res.userInfo,
            hasUserInfo: true
          })
        }
      })
    }
  },
  getUserInfo: function (e) {
    console.log(e)
    app.globalData.userInfo = e.detail.userInfo
    let that = this
    wx.request({
      url: "http://www.mallproject.cn:8000/api/user/",
      method: "POST",
      data: {

        user_nickname: e.detail.userInfo.nickName

      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      success: function (res) {
        console.log(res.data);
        app.globalData.user_id = res.data.user_id
        wx.request({
          url: 'http://www.mallproject.cn:8000/api/user/?user_nickname__icontains=' + e.detail.userInfo.nickName,
          success: data => {
            app.globalData.user_id = data.data[0].user_id
            console.log("test1 ", app.globalData.user_id)

          }
        })
      },
    })

    wx.request({
      url: 'http://www.mallproject.cn:8000/api/user/?user_nickname__icontains=' + e.detail.userInfo.nickName,
      success: data => {
        app.globalData.user_id = data.data[0].user_id
        console.log("test1 ", app.globalData.user_id)

      }
    })
    this.setData({
      userInfo: e.detail.userInfo,
      hasUserInfo: true
    })
  },
  myAddress: function (e) {
    wx.navigateTo({ url: '../addressList/addressList' });
  },
  myLikes: function (e) {
    wx.navigateTo({ url: '../likes/likes' });
  }
})
