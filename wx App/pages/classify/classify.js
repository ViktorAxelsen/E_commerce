// pages/classify/classify.js
// const ajax = require('../../utils/ajax.js');
// const utils = require('../../utils/util.js');
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    classifyItems: [],
    curNav: 1,  
    curIndex: 0  ,//表示现在应该点亮哪一个
    sub:null
  },


  //事件处理函数  
  switchRightTab: function (e) {
    // 获取item项的id，和数组的下标值  
    let id = e.target.dataset.id,
      index = parseInt(e.target.dataset.index);
    // 把点击到的某一项，设为当前index  
    this.setData({
      curNav: id,
      curIndex: index
    })
    this.getSub()
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    
    that.classifyShow();
    that.getSub();
  },
  
  getSub: function (options) {
    wx.request({
      url: 'http://www.mallproject.cn:8000/api/itemSubCategory/?item_subCategory_belong=' + this.data.curNav,
      success: data => {
        this.setData({
          sub: data.data,

        })
        console.log(this.data.sub)
      }
    })

  },
  classifyShow: function (success) {
    var that = this;
    wx.request({
      method: 'GET',
      url: 'http://www.mallproject.cn:8000/api/itemCategory/',
      success: data => {
        that.setData({
          classifyItems: data.data
        })
        
      }
    })
  },
})