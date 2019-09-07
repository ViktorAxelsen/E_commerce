// page/component/orders/orders.js
var app = getApp()
Page({
  data: {
    address: {},
    hasAddress: false,
    total: 0,
    orders: []
  },
  get_order_item: function (options) {

    wx.request({
      method: 'GET',
      url: 'http://www.mallproject.cn:8000/api/get_wait_receive/?user_id=' + app.globalData.user_id+'&return_type=info',
      success: data => {
        this.setData
        console.log(data.data)
        this.setData({
          orders: data.data
        })
        this.getTotalPrice()
      }
    })


  },
  onLoad: function (options) {
    this.get_order_item()

  },


  onShow: function () {
    const self = this;
    wx.getStorage({
      key: 'address',
      success(res) {
        self.setData({
          address: res.data,
          hasAddress: true
        })
      }
    })
  },

  /**
   * 计算总价
   */
  getTotalPrice() {
    let orders = this.data.orders;
    let total = 0;
    for (let i = 0; i < orders.length; i++) {
      total += orders[i].item_price * orders[i].item_trade_amount;
    }
    this.setData({
      total: total
    })
  },

  toPay() {
    wx.showModal({
      title: '提示',
      content: '本系统只做演示，支付系统已屏蔽',
      text: 'center',
      complete() {
        wx.switchTab({
          url: '/page/component/user/user'
        })
      }
    })
  }
})