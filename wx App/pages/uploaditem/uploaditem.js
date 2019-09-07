// pages/text_up/text_up.js
var subid = 0;
var item_id = 0;
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
  onLoad: function (options) {
    if (options.storeId==0)
    {
      wx.request({
        url: 'http://www.mallproject.cn:8000/api/store/?store_user=' + app.globalData.user_id,
        success: data => {
          store_id = data.data[0].store_id
          console.log("这是store_id",store_id)

        }
      })
    }
    else{
      store_id = options.storeId
      console.log("这是store_id", store_id)
    }
    
    wx.request({
      method: 'GET',
      url: 'http://www.mallproject.cn:8000/api/itemSubCategory/',
      success: data => {
        this.setData({
          subCategory: data.data
        })
        console.log(data.data)
      }
    })
  },
  bindPickerChange2: function (e) {
    console.log('picker发送选择改变，携带值为', e.detail.value)
    this.setData({
      objectIndex: e.detail.value
    })
    subid = this.data.subCategory[e.detail.value].item_subCategory_id
    console.log(subid)
  },

  /**
   * 生命周期函数--监听页面初次渲染完成
   */
  onReady: function () {

  },
  saveTextinfo: function (e) {
    var item_name = e.detail.value.item_name;
    var item_price = e.detail.value.item_price;
    var item_stoke = e.detail.value.item_stoke;
    var item_prePrice = e.detail.value.item_prePrice;
    var item_off = e.detail.value.item_off;
    wx.request({
      url: "http://www.mallproject.cn:8000/api/item/",
      method: "POST",
      data: {
        item_name: item_name,
        item_store: store_id,
        item_price: item_price,
        item_stoke: item_stoke,
        item_off: item_off,
        item_subCategory_id: subid,
        item_prePrice: item_prePrice


      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      success: function (res) {
        item_id = res.data.item_id
        console.log(res.data.item_id);
        wx.navigateTo({ url: '../images_upload/image_upload?goodsId=' + item_id })
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