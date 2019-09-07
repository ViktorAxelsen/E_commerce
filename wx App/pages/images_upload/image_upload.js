// pages/images_upload/image_upload.js
var goodsId = 0
// pages/productReleased/productReleased.js
var app = getApp();
Page({

  /**
   * 页面的初始数据
   */
  data: {
    tempFilePaths: [],
    banner: [], //轮播图片
    bannerNew: [],
    bannerAll: [],


    detail: [], //详情图片
    detailNew: [],
    detailAll: [],
    preview: [], //详情图片
    previewNew: [],
    previewAll: [],
    checkUp: true, //判断从编辑页面进来是否需要上传图片
    chooseViewShowDetail: true,
    chooseViewShowBanner: true,
    chooseViewShowpreview: true,
    params: {
      productID: 0,
      contentFile: "",
      bannerFile: "",
      check: false,
    },
    dis: false,
  },
  onLoad: function (options) {
    var that = this;
    goodsId = options.goodsId;
    console.log('goodsId:' + goodsId);
  },
  /**
   * 生命周期函数--监听页面加载
   */

  // uploadOneImage: function () {
  //   let that = this;
  //   wx.chooseImage({
  //     count: 1, // 默认9
  //     sizeType: ['original', 'compressed'], // 可以指定是原图还是压缩图，默认二者都有
  //     sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
  //     success: res => {
  //       wx.showToast({
  //         title: '正在上传...',
  //         icon: 'loading',
  //         mask: true,
  //         duration: 1000
  //       })
  //       // 返回选定照片的本地文件路径列表，tempFilePath可以作为img标签的src属性显示图片
  //       let tempFilePaths = res.tempFilePaths;

  //       that.setData({
  //         tempFilePaths: tempFilePaths
  //       })
  //       /**
  //        * 上传完成后把文件上传到服务器
  //        */
  //       var count = 0;

  //       for (var i = 0, h = tempFilePaths.length; i < h; i++) {
  //         //上传文件
  //         console.log("开始上传")
  //         wx.uploadFile({
  //           url: 'http://www.mallproject.cn:8000/api/item_detail_image/1',

  //           filePath: tempFilePaths[i],
  //           name: 'img',
  //           header: {

  //           },
  //           success: function (res) {
  //             count++;
  //             //如果是最后一张,则隐藏等待中  
  //             if (count == tempFilePaths.length) {
  //               wx.hideToast();
  //             }
  //           },
  //           fail: function (res) {
  //             wx.hideToast();
  //             wx.showModal({
  //               title: '错误提示',
  //               content: '上传图片失败',
  //               showCancel: false,
  //               success: function (res) { }
  //             })
  //           }
  //         });
  //       }

  //     }
  //   })
  // },

  /** 选择图片detail */
  chooseDetail: function () {
    var that = this;
    if (that.data.detail.length < 3) {
      wx.chooseImage({
        count: 3,
        sizeType: ['compressed'],
        sourceType: ['album', 'camera'],
        success: function (photo) {
          //detail中包含的可能还有编辑页面下回显的图片，detailNew中包含的只有所选择的图片
          let detail = that.data.detail;
          detail = detail.concat(photo.tempFilePaths);
          let detailNew = that.data.detailNew
          detailNew = detailNew.concat(photo.tempFilePaths)
          that.setData({
            detail: detail,
            detailNew: detailNew,
            checkUp: false
          })
          that.chooseViewShowDetail();

          if (that.data.productID != 0) {
            let params = {
              productID: that.data.productID,
              isBanner: false,
              index: -1,
            }
          
          }
        }
      })
    } else {
      wx.showToast({
        title: '限制选择3个文件',
        icon: 'none',
        duration: 1000
      })
    }
  },
  upload: function () {
    var count = 0;
    var tempFilePaths = this.data.banner
    var tempDetailPaths = this.data.detail
    var preview = this.data.preview
    
    for (var i = 0, h = tempFilePaths.length; i < h; i++) {
      //上传文件
      console.log("开始上传")
      wx.uploadFile({
        url: 'http://www.mallproject.cn:8000/api/item_banner_image/' + goodsId,

        filePath: tempFilePaths[i],
        name: 'img',
        header: {

        },
        success: function (res) {
          count++;
          //如果是最后一张,则隐藏等待中  
          if (count == tempFilePaths.length) {
            wx.hideToast();
          }
        },
        fail: function (res) {
          wx.hideToast();
          wx.showModal({
            title: '错误提示',
            content: '上传图片失败',
            showCancel: false,
            success: function (res) { }
          })
        }
      });
    }
    for (var i = 0, h = tempDetailPaths.length; i < h; i++) {
      wx.uploadFile({
        url: 'http://www.mallproject.cn:8000/api/item_detail_image/' + goodsId,

        filePath: tempDetailPaths[i],
        name: 'img',
        header: {

        },
        success: function (res) {
          count++;
          //如果是最后一张,则隐藏等待中  
          if (count == tempDetailPaths.length) {
            wx.hideToast();
          }
        },
        fail: function (res) {
          wx.hideToast();
          wx.showModal({
            title: '错误提示',
            content: '上传图片失败',
            showCancel: false,
            success: function (res) { }
          })
        }
      });
      console.log("redirect")
      
    }
    for (var i = 0, h = preview.length; i < h; i++) {
      wx.uploadFile({
        url: 'http://www.mallproject.cn:8000/api/upload_item_preview_image/' + goodsId,

        filePath: preview[i],
        name: 'img',
        header: {

        },
        success: function (res) {
          count++;
          //如果是最后一张,则隐藏等待中  
          if (count == preview.length) {
            wx.hideToast();
          }
         
        },
        fail: function (res) {
          wx.hideToast();
          wx.showModal({
            title: '错误提示',
            content: '上传图片失败',
            showCancel: false,
            success: function (res) { }
          })
        }
      });
    }
    wx.switchTab({
      url: '../mine/mine',
    })
  },


  /** 删除图片detail */
  deleteImvDetail: function (e) {
    var that = this;
    var detail = that.data.detail;
    var itemIndex = e.currentTarget.dataset.id;
    if (that.data.productID != 0) {
      wx.showModal({
        title: '提示',
        content: '删除不可恢复，请谨慎操作',
        success(res) {
          if (res.confirm) {
            detail.splice(itemIndex, 1);
            that.setData({
              detail: detail,
              checkUp: false
            })
            that.chooseViewShowDetail();
            let params = {
              productID: that.data.productID,
              isBanner: false,
              index: itemIndex,
            }
           
          }
        }
      })
    } else {
      detail.splice(itemIndex, 1);
      that.setData({
        detail: detail,
        checkUp: false
      })
      that.chooseViewShowDetail();
    }
  },


  /** 是否隐藏图片选择detail */
  chooseViewShowDetail: function () {
    if (this.data.detail.length >= 3) {
      this.setData({
        chooseViewShowDetail: false
      })
    } else {
      this.setData({
        chooseViewShowDetail: true
      })
    }
  },

  /** 查看大图Detail */
  showImageDetail: function (e) {
    var detail = this.data.detail;
    var itemIndex = e.currentTarget.dataset.id;
    wx.previewImage({
      current: detail[itemIndex], // 当前显示图片的http链接
      urls: detail // 需要预览的图片http链接列表
    })
  },


  /** 选择图片Banner */
  chooseBanner: function () {
    var that = this;
    if (that.data.banner.length < 2) {
      wx.chooseImage({
        count: 2, //最多选择4张图片- that.data.imgArr.length,
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (photo) {
          var banner = that.data.banner;
          banner = banner.concat(photo.tempFilePaths);
          var bannerNew = that.data.bannerNew;
          bannerNew = bannerNew.concat(photo.tempFilePaths);
          that.setData({
            banner: banner,
            bannerNew: bannerNew,
            checkUp: false
          })
          that.chooseViewShowBanner();
          if (that.data.productID != 0) {
            let params = {
              productID: that.data.productID,
              isBanner: false,
              index: -1,
            }
           
          }
        }
      })

    } else {
      wx.showToast({
        title: '限制选择2个文件',
        icon: 'none',
        duration: 1000
      })
    }
  },

  /** 删除图片Banner */
  deleteImvBanner: function (e) {
    var that = this
    var banner = that.data.banner;
    var itemIndex = e.currentTarget.dataset.id;
    if (that.data.productID != 0) {
      wx.showModal({
        title: '提示',
        content: '删除不可恢复，请谨慎操作',
        success(res) {
          if (res.confirm) {
            banner.splice(itemIndex, 1);
            that.setData({
              banner: banner,
              checkUp: false
            })
            that.chooseViewShowBanner();
            let params = {
              productID: that.data.productID,
              isBanner: true,
              index: itemIndex,
            }
            
          }
        }
      })
    } else {
      banner.splice(itemIndex, 1);
      that.setData({
        banner: banner,
        checkUp: false
      })
      that.chooseViewShowBanner();
    }
  },


  /** 是否隐藏图片选择Banner*/
  chooseViewShowBanner() {
    if (this.data.banner.length >= 2) {
      this.setData({
        chooseViewShowBanner: false
      })
    } else {
      this.setData({
        chooseViewShowBanner: true
      })
    }
  },

  /** 查看大图Banner */
  showImageBanner: function (e) {
    var banner = this.data.banner;
    var itemIndex = e.currentTarget.dataset.id;
    wx.previewImage({
      current: banner[itemIndex], // 当前显示图片的http链接
      urls: banner // 需要预览的图片http链接列表
    })

  },
  /** 选择图片preview */
  choosepreview: function () {
    var that = this;
    if (that.data.preview.length < 2) {
      wx.chooseImage({
        count: 1, //最多选择4张图片- that.data.imgArr.length,
        sizeType: ['compressed'], // 可以指定是原图还是压缩图，默认二者都有
        sourceType: ['album', 'camera'], // 可以指定来源是相册还是相机，默认二者都有
        success: function (photo) {
          var preview = that.data.preview;
          preview = preview.concat(photo.tempFilePaths);
          var previewNew = that.data.previewNew;
          previewNew = previewNew.concat(photo.tempFilePaths);
          that.setData({
            preview: preview,
            previewNew: previewNew,
            checkUp: false
          })
          that.chooseViewShowpreview();
          if (that.data.productID != 0) {
            let params = {
              productID: that.data.productID,
              ispreview: false,
              index: -1,
            }
           
          }
        }
      })

    } else {
      wx.showToast({
        title: '限制选择2个文件',
        icon: 'none',
        duration: 1000
      })
    }
  },

  /** 删除图片preview */
  deleteImvpreview: function (e) {
    var that = this
    var preview = that.data.preview;
    var itemIndex = e.currentTarget.dataset.id;
    if (that.data.productID != 0) {
      wx.showModal({
        title: '提示',
        content: '删除不可恢复，请谨慎操作',
        success(res) {
          if (res.confirm) {
            preview.splice(itemIndex, 1);
            that.setData({
              preview: preview,
              checkUp: false
            })
            that.chooseViewShowpreview();
            let params = {
              productID: that.data.productID,
              ispreview: true,
              index: itemIndex,
            }
          
          }
        }
      })
    } else {
      preview.splice(itemIndex, 1);
      that.setData({
        preview: preview,
        checkUp: false
      })
      that.chooseViewShowpreview();
    }
  },


  /** 是否隐藏图片选择preview*/
  chooseViewShowpreview() {
    if (this.data.preview.length >= 2) {
      this.setData({
        chooseViewShowpreview: false
      })
    } else {
      this.setData({
        chooseViewShowpreview: true
      })
    }
  },

  /** 查看大图preview */
  showImagepreview: function (e) {
    var preview = this.data.preview;
    var itemIndex = e.currentTarget.dataset.id;
    wx.previewImage({
      current: preview[itemIndex], // 当前显示图片的http链接
      urls: preview // 需要预览的图片http链接列表
    })

  },
})