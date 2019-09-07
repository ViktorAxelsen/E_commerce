// pages/search1/search1.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    search: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {

  },
  // 搜索
  // 搜索
  goSearch: function (e) {
    var that = this;
    var formData = e.detail.value;
    if (formData) {

      wx.request({

        method: 'GET',
        url: 'http://www.mallproject.cn:8000/api/item/?item_name__icontains=' + formData,
        success: function (res) {
          that.setData({
            search: res.data,
          })
          if (res.data.length == 0) {
            wx.showToast({
              title: '无相关商品',
              icon: 'none',
              duration: 1500
            })
            wx.switchTab({
              url: '../home/home',
            })
          } else {
            let str = JSON.stringify(res.data);
            wx.navigateTo({
              url: '../searchResult/searchResult?data=' + str
            })
          }

          // console.log(res.data.msg)
        }
      })
    } else {

      wx.showToast({
        title: '输入不能为空',
        icon: 'none',
        duration: 1500
      })

    }
  },
  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },

  /**
   * 生命周期函数--监听页面显示
   */
  onShow: function () {

  },

  /**
   * 生命周期函数--监听页面隐藏
   */
  onHide: function () {

  },

  /**
   * 生命周期函数--监听页面卸载
   */
  onUnload: function () {

  },

  /**
   * 页面相关事件处理函数--监听用户下拉动作
   */
  onPullDownRefresh: function () {

  },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {

  },

  /**
   * 用户点击右上角分享
   */
  onShareAppMessage: function () {

  }
})