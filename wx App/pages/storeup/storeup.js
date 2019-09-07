// pages/text_up/text_up.js
var subid = 0;
var store_id = 0;
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    subCategory: [],
  },

  /**
   * 生命周期函数--监听页面加载
   */
  

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  saveTextinfo: function (e) {
    var store_name = e.detail.value.store_name;
    var store_contact_info = e.detail.value.store_contact_info;
    var store_info = e.detail.value.store_info;
    wx.request({
      url: "http://www.mallproject.cn:8000/api/store/",
      method: "POST",
      data: {
        store_name:store_name,
        store_info:store_info,
        store_contact_info:store_contact_info,
        store_user: app.globalData.user_id
      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      success: function (res) {
        console.log(res)
        store_id = res.data.store_id
        console.log(res.data.store_id);
        wx.navigateTo({ url: '../uploaditem/uploaditem?storeId=' + store_id })
      },
    })

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