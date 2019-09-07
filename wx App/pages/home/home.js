// pages/home/home.js
var sectionData = [];
var ifLoadMore = null;
var page = 1;//默认第一页
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    currentTab: 0,
    banners: null,
    indicatorDots: true, //是否显示面板指示点
    autoplay: true, //是否自动切换
    interval: 3000, //自动切换时间间隔,3s
    duration: 1000, //  滑动动画时长1s
    brands: null,
    hidden: false,
    newGoods: null,
    menus: null
  },
  navigate: function (options) {
    wx.navigateTo({
      url: '../search/search'
    })
  },
  /**
   * 生命周期函数--监听页面加载
   */

  onLoad: function (options) {
    var that = this;
    //加载banner轮播
    that.bannerShow();
    that.classShow();
    //加载福利专场
    that.newGoodsShow();
  },
  bannerShow: function (success) {
    var that = this;

    wx.request({
      url: 'http://www.mallproject.cn:8000/api/banner',
      
      success: res => {
        console.log(res)
        if (res.statusCode == 200) {
          this.setData({
            banners: res.data
          })
        }
      }
    })
  },
  classShow: function (success) {
    var that = this;

    wx.request({
      url: 'http://www.mallproject.cn:8000/api/itemCategory',
     
      success: res => {
        console.log(res)
        if (res.statusCode == 200) {
          this.setData({
            menus: res.data
          })
          console.log(res.data)
        }
      }
    })
  },
  toOrder: function (e) {
    console.log(e.currentTarget.dataset.typeid)
    var typeid = e.currentTarget.dataset.typeid

    wx.navigateTo({
      url: '../category/category?categoryId=' + typeid
    })


  },
  newGoodsShow: function (success) {
    var that = this;

    wx.request({
      url: 'http://www.mallproject.cn:8000/api/item',
      
      success: res => {
        console.log(res)
        if (res.statusCode == 200) {
          this.setData({
            newGoods: res.data
          })
        }
      }
    })
  },
  // newGoodsShow: function (success) {
  //   var that = this;
  //   wx.request({
  //     method: 'GET',
  //     url: 'http://www.mallproject.cn:8000/api/item',
  //     success: data => {

  //       var length = data.data.length
  //       if (ifLoadMore) {
  //         //加载更多
  //         if (length > 0) {
  //           console.log(data.data)
  //           //日期以及title长度处理
  //           for (var i in data.data) {
  //             //商品名称长度处理
  //             var name = data.data[i].item_name;
  //             if (name.length > 26) {
  //               data.data[i].name = name.substring(0, 23) + '...';
  //             }
  //           }
  //           sectionData['newGoods'] = sectionData['newGoods'].concat(data.data);

  //         } else {
  //           ifLoadMore = false;
  //           this.setData({
  //             hidden: true
  //           })
  //           wx.showToast({
  //             title: '暂无更多内容！',
  //             icon: 'loading',
  //             duration: 2000
  //           })
  //         }

  //       } else {
  //         if (ifLoadMore == null) {
  //           ifLoadMore = true;

  //           //日期以及title长度处理
  //           for (var i in data.data) {
  //             //商品名称长度处理
  //             var name = data.data[i].name;
  //             if (name.length > 26) {
  //               data.data[i].name = name.substring(0, 23) + '...';
  //             }
  //           }
  //           sectionData['newGoods'] = data.data;//刷新
  //         }

  //       }
  //       that.setData({
  //         newGoods: sectionData['newGoods'],
  //         // isHideLoadMore: true
  //       });
  //       wx.stopPullDownRefresh();//结束动画
  //     }
  //   })
  // },

  /**
   * 页面上拉触底事件的处理函数
   */
  onReachBottom: function () {
    console.log("上拉");
    var that = this;
    console.log('加载更多');
    if (ifLoadMore != null) {
      that.newGoodsShow();

    }
  },
  show: function (e) {
    //用来直接跳转到bannar上的相关id
    var that = this;
    var goodsId = e.currentTarget.dataset.goodsid;
    console.log('goodsId:' + goodsId);
    //新增商品用户点击数量
    // that.goodsClickShow(goodsId);
    //跳转商品详情
    wx.navigateTo({ url: '../detail/detail?goodsId=' + goodsId })
  },
  catchTapCategory: function (e) {
    //用来处理商品本身的tap id
    var that = this;
    var goodsId = e.currentTarget.dataset.goodsid;
    console.log('goodsId:' + goodsId);
    //新增商品用户点击数量
    //that.goodsClickShow(goodsId);
    //跳转商品详情
    wx.navigateTo({ url: '../detail/detail?goodsId=' + goodsId })
  },
  // goodsClickShow(goodsId) {
  //   console.log('增加商品用户点击数量');
  //   var that = this;
  //   ajax.request({
  //     method: 'GET',
  //     url: 'goods/addGoodsClickRate?key=' + utils.key + '&goodsId=' + goodsId,
  //     success: data => {
  //       console.log("用户点击统计返回结果：" + data.message)
  //     }
  //   })
  // },
})