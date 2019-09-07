// pages/mall/mall.js
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    mallinfo:[],
    ismall:false,
    searchShow:[],
  },
  getinfo: function (options) {
    var that = this
    wx.request({
      url: 'http://www.mallproject.cn:8000/api/store/?store_user=' + app.globalData.user_id,
      success: data => {
        if(data.data.length == 0){
        this.setData({
          ismall:false
        })
        }
        else{
          wx.request({
            method: 'GET',
            url: 'http://www.mallproject.cn:8000/api/item/?item_store=' + data.data[0].store_id,
            success: function (res) {
              if(res.data.length>0)
              {
                wx.navigateTo({
                  url: '../mallitem/mallitem?storeid=' + data.data[0].store_id,
                  
                })
                console.log(data.data[0].store_id)
              }
              else{
                wx.navigateTo({
                  url: '../uploaditem/uploaditem?storeid=' + data.data[0].store_id,
                })
                console.log(data.data[0].store_id)
              }
            }
          })  
        
        }
      }
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    this.getinfo()
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