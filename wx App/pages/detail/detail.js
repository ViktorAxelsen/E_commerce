// pages/detail/detail.js
// const ajax = require('../../utils/ajax.js');
// const utils = require('../../utils/util.js');
var imgUrls = [];
var detailImg = [];
var goodsId = null;
var goods = null;
const app = getApp()
Page({

  /**
   * 页面的初始数据
   */
  data: {
    isLike: false,
    showDialog: false,
    goods: null,
    indicatorDots: true, //是否显示面板指示点
    autoplay: true, //是否自动切换
    interval: 3000, //自动切换时间间隔,3s
    duration: 1000, //  滑动动画时长1s

  },
  getlikes: function (options) {

    wx.request({
      method: 'GET',
      url: 'http://www.mallproject.cn:8000/api/whether_user_collect_item/?item_id='+goodsId+'&user_id='+app.globalData.user_id,
      success: data => {
        isLike: data.data

      }
    })


  },
  //预览图片
  previewImage: function (e) {
    var current = e.target.dataset.src;
    wx.previewImage({
      current: current, // 当前显示图片的http链接  
      urls: this.data.imgUrls // 需要预览的图片http链接列表  
    })
  },
  getLike()
  {
    var flag = 0
    wx.request({
      url: 'http://www.mallproject.cn:8000/api/whether_user_collect_item/?item_id='+goodsId+'&user_id='+app.globalData.user_id,
      success: data => {
        if (data.data == 1) {
          this.setData({
            isLike: !this.data.isLike
          });
        }
        }
      })
     
  },
  // 收藏
  addLike() {
    this.setData({
      isLike: !this.data.isLike
    });
    if(this.data.isLike == true){
    wx.request({
      method: 'POST',
      url: 'http://www.mallproject.cn:8000/api/collection/',
      data:
      {
        collection_user:app.globalData.user_id,
        collection_item: goodsId
      },
    })}
    else{
      wx.request({
        method: 'DELETE',
        url: 'http://www.mallproject.cn:8000/api/delete_user_collection/?item_id='+goodsId+"&user_id="+app.globalData.user_id,
        
       
      })

    }
  },
  // 跳到购物车
  toCar() {
    wx.switchTab({ url: '../cart1/cart1' })
  },
  purchase: function (user_id, item_id, store_id = 1, amount = 1) {


    wx.request({
      url: "http://www.mallproject.cn:8000/api/buy_now/?user_id=" + user_id + "&item_id=" + item_id + "&store_id=" + store_id + "&amount=" + amount,
      method: "POST",
      data: {
        user_id: user_id,
        item_id: item_id,
        store_id: store_id,
        amount: amount

      },
      header: {
        "Content-Type": "application/x-www-form-urlencoded"
      },
      success: function (res) {
        console.log(res.data);
      }
    })

  },
  cart_purchase: function (user_id, item_id,  amount = 1) {


    wx.request({
      url: "http://www.mallproject.cn:8000/api/add_into_cart/?user_id="+user_id+"&item_id="+item_id+"&amount="+amount,
      method: "POST",
      data: {
        user_id: user_id,
        item_id: item_id,
        
        amount: amount

      },
      
      success: function (res) {
        console.log(res.data);
      }
    })

  },
  // 立即购买
  immeBuy() {
    this.purchase(app.globalData.user_id,goodsId)
    wx.showToast({
      title: '购买成功',
      icon: 'success',
      duration: 2000
    });
  },

  /**
   * 生命周期函数--监听页面加载
   */
  onLoad: function (options) {
    var that = this;
    goodsId = options.goodsId;
    console.log('goodsId:' + goodsId);
    //加载商品详情
    that.getLike();
    that.getBannarImage();
    that.getDetailImage();
    that.goodsInfoShow();
  },
  getBannarImage: function(success)
  {
    var that=this;
    wx.request({
      url: 'http://www.mallproject.cn:8000/api/item_banner_image/' + goodsId,
      success: data => {
        var goodsItem = data.data;
        for (var i = 0; i < goodsItem.length; i++) {
          imgUrls[i] = "http://www.mallproject.cn:8000"+ goodsItem[i].image_url;
          console.log(imgUrls[i])
        }


      }
    })

  },
   getDetailImage: function(success)
  {
    var that=this;
    wx.request({
      url: 'http://www.mallproject.cn:8000/api/item_detail_image/' + goodsId,
      success: data => {
        var goodsItem = data.data;
        for (var i = 0; i < goodsItem.length; i++) {
          detailImg[i] = "http://www.mallproject.cn:8000"+ goodsItem[i].image_url;
          console.log(imgUrls[i])
        }


      }
    })

  },
  goodsInfoShow: function (success) {
    var that = this;
    wx.request({
      method: 'GET',
      url: 'http://www.mallproject.cn:8000/api/item/' + goodsId,
      success: data => {
        var goodsItem = data.data;
       
        
        goods = {
          imgUrls: imgUrls,
          title: goodsItem.item_name,
          price: goodsItem.item_price,
          privilegePrice: goodsItem.item_prePrice,
          detailImg: detailImg,
          imgUrl: goodsItem.item_preview_image,
          buyRate: goodsItem.item_sale,
          store: goodsItem.item_store,
          goodsId: goodsId,
          count: 1,
          totalMoney: goodsItem.item_price,
        }

        that.setData({
          goods: goods
        })
        console.log(goods.title)
      }
    })
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

  },
  /**
   * sku 弹出
   */
  toggleDialog: function () {
    this.setData({
      showDialog: !this.data.showDialog
    });
  },
  /**
   * sku 关闭
   */
  closeDialog: function () {
    console.info("关闭");
    this.setData({
      showDialog: false
    });
  },
  /* 减数 */
  delCount: function (e) {
    console.log("刚刚您点击了减1");
    var count = this.data.goods.count;
    // 商品总数量-1
    if (count > 1) {
      this.data.goods.count--;
    }
    // 将数值与状态写回  
    this.setData({
      goods: this.data.goods
    });
    this.priceCount();
  },
  /* 加数 */
  addCount: function (e) {
    console.log("刚刚您点击了加1");
    var count = this.data.goods.count;
    // 商品总数量-1  
    if (count < 10) {
      this.data.goods.count++;
    }
    // 将数值与状态写回  
    this.setData({
      goods: this.data.goods
    });
    this.priceCount();
  },
  //价格计算
  priceCount: function (e) {
    this.data.goods.totalMoney = this.data.goods.price * this.data.goods.count;
    this.setData({
      goods: this.data.goods
    })
  },
  /**
   * 加入购物车
   */
  addCar: function (e) {
    var goods = this.data.goods;
    goods.isSelect = false;
    var count = this.data.goods.count;

    var title = this.data.goods.title;
    this.cart_purchase(app.globalData.user_id, goodsId, count)
    if (title.length > 13) {
      goods.title = title.substring(0, 13) + '...';
    }

    // 获取购物车的缓存数组（没有数据，则赋予一个空数组）  
    var arr = wx.getStorageSync('cart') || [];
    console.log("arr,{}", arr);
    if (arr.length > 0) {
      // 遍历购物车数组  
      for (var j in arr) {
        // 判断购物车内的item的id，和事件传递过来的id，是否相等  
        if (arr[j].goodsId == goodsId) {
          // 相等的话，给count+1（即再次添加入购物车，数量+1）  
          arr[j].count = arr[j].count + 1;
          // 最后，把购物车数据，存放入缓存（此处不用再给购物车数组push元素进去，因为这个是购物车有的，直接更新当前数组即可）  
          try {
            wx.setStorageSync('cart', arr)
          } catch (e) {
            console.log(e)
          }
          //关闭窗口
          wx.showToast({
            title: '加入购物车成功！',
            icon: 'success',
            duration: 2000
          });
          this.closeDialog();
          // 返回（在if内使用return，跳出循环节约运算，节约性能） 
          return;
        }
      }
      // 遍历完购物车后，没有对应的item项，把goodslist的当前项放入购物车数组  
      arr.push(goods);
    } else {
      arr.push(goods);
    }
    // 最后，把购物车数据，存放入缓存  
    try {
      wx.setStorageSync('cart', arr)
      // 返回（在if内使用return，跳出循环节约运算，节约性能） 
      //关闭窗口
      wx.showToast({
        title: '加入购物车成功！',
        icon: 'success',
        duration: 2000
      });
      this.closeDialog();
      return;
    } catch (e) {
      console.log(e)
    }


  }
})