from django.db import models
from stdimage import StdImageField, JPEGField
from django.utils import timezone


#用户
class User(models.Model):
    user_id = models.AutoField(verbose_name=u'用户', primary_key=True)
    user_nickname = models.CharField(verbose_name=u'昵称', max_length=30, default='none', unique=True)#用户设置
    user_password = models.CharField(verbose_name=u'密码', max_length=18, default='123456')
    user_sex = models.CharField(verbose_name=u'性别', max_length=1, default='男')
    user_tel = models.CharField(verbose_name=u'手机号', max_length=10, default='0')
    user_headImage = StdImageField(max_length=100, upload_to='user/headImage', verbose_name='头像',
                               variations={'thumbnail':{'width':50, 'height':50}}, default='user/headImage/headDefault.jpg')
    user_isAdmin = models.BooleanField(verbose_name=u'用户是否为管理员', default=False)#系统预先内置一些管理员账户，前端提交用户注册表单时此项默认置0.当用户登陆时，检查此项是否为1，为1开启管理员权限.
    user_isBindQQ = models.BooleanField(verbose_name=u'用户是否绑定QQ', default=False)
    user_location = models.CharField(verbose_name=u'位置', max_length=30, default=u'中国')
    user_location_x = models.DecimalField(verbose_name=u'用户经度', max_digits=100, decimal_places=5, default=0)
    user_location_y = models.DecimalField(verbose_name=u'用户纬度', max_digits=100, decimal_places=5, default=0)
    user_coupon = models.ManyToManyField(verbose_name=u'用户持有的优惠券', to='Coupon', null=True, blank=True)
    user_TJUCarat = models.IntegerField(verbose_name=u'洋克拉', default=0)#洋克拉：类似于京豆的一种优惠机制
    user_isVIP = models.BooleanField(verbose_name=u'用户是否为VIP', default=False)
    user_email = models.CharField(verbose_name=u'邮箱', max_length=30, default='none')



    def __str__(self):
        return '%s' % self.user_id

    def isAdmin(self):
        if self.user_isAdmin:
            return '是'
        else:
            return '否'

    def isBindQQ(self):
        if self.user_isBindQQ:
            return '是'
        else:
            return '否'


#商店
class Store(models.Model):
    store_id = models.AutoField(verbose_name=u'商店', primary_key=True)
    store_name = models.CharField(verbose_name=u'店名', max_length=100, default='none')
    store_user = models.ForeignKey(to='User', to_field='user_id', on_delete=models.CASCADE, verbose_name=u'店主')
    store_contact_info = models.CharField(verbose_name=u'联系方式', max_length=30, default='none')
    store_info = models.CharField(verbose_name=u'店铺简介', max_length=100, default='这个店主有点懒~')
    store_evaluate = models.DecimalField(max_digits=10, decimal_places=1, verbose_name=u'店铺评价', default=10.0) #10代表未有评价信息
    store_create_time = models.DateField(verbose_name=u'店铺创建时间', auto_now=False, auto_now_add=True)

    def __str__(self):
        return '%s' % self.store_name


#商品类别
class ItemCategory(models.Model):
    item_category_id = models.AutoField(verbose_name=u'商品大类', primary_key=True)
    item_category = models.CharField(verbose_name=u'类别', max_length=100, default='none', unique=True)
    item_category_isHaveSub = models.BooleanField(verbose_name=u'是否有子类', default=False)
    item_category_image = models.ImageField(verbose_name=u'轮播图', upload_to='category/category_image', default='category/category_image/itemDefault.jpg')


    def __str__(self):
        return '%s' % self.item_category


#商品子类
class ItemSubCategory(models.Model):
    item_subCategory_id = models.AutoField(verbose_name=u'商品子类', primary_key=True)
    item_subCategory = models.CharField(verbose_name=u'子类', max_length=100, default='none', unique=True)
    item_subCategory_image = StdImageField(max_length=100, upload_to='category/image', verbose_name=u'子类图',
                               variations={'thumbnail':{'width':50, 'height':50}}, default='category/image/itemDefault.jpg')
    item_subCategory_belong = models.ForeignKey(verbose_name=u'该子类隶属哪个大类', to='ItemCategory', to_field='item_category_id', on_delete=models.SET_NULL, null=True, blank=True)


    def __str__(self):
        return '%d' % self.item_subCategory_id




#查询子类缺省id
def get_default_sub_category_id():
    sub_category = ItemSubCategory.objects.get(item_subCategory_id=1)
    return sub_category.item_subCategory_id


#商品
class Item(models.Model):
    item_id = models.AutoField(verbose_name=u'商品编号', primary_key=True)
    item_name = models.CharField(verbose_name=u'品名', max_length=100, default='none')
    item_price = models.DecimalField(verbose_name=u'价格', max_digits=100, decimal_places=3, default='none')
    item_stoke = models.IntegerField(verbose_name=u'库存', default=9999)
    item_preview_image = StdImageField(max_length=100, upload_to='item/previewImage', verbose_name='商品预览图',
                               variations={'thumbnail':{'width':100, 'height':75}}, default='item/previewImage/itemDefault.jpg')
    item_category = models.ForeignKey(to='ItemCategory', to_field='item_category_id', on_delete=models.SET_NULL, verbose_name=u'类别', null=True, blank=True)
    item_sub_category = models.ForeignKey(to='ItemSubCategory', to_field='item_subCategory_id', on_delete=models.SET(1), verbose_name=u'子类',
                                          default=get_default_sub_category_id())
    item_browse = models.IntegerField(verbose_name=u'浏览量', default=0)
    item_store = models.ForeignKey(to='Store', to_field='store_id', on_delete=models.CASCADE, verbose_name=u'所属商店')
    item_off = models.CharField(verbose_name=u'优惠信息', max_length=10, default=' ')
    item_prePrice = models.DecimalField(verbose_name=u'商品原价', max_digits=100, decimal_places=3, default=0)
    item_sale = models.IntegerField(verbose_name=u'销量', default=0)
    item_create_time = models.DateField(verbose_name=u'商品上架时间', auto_now=False, auto_now_add=True)
    item_VIPOff = models.IntegerField(verbose_name=u'会员减价', default=10)
    item_brand = models.ForeignKey(to='Brand', to_field='brand_id', on_delete=models.SET_NULL, verbose_name=u'品牌', null=True, blank=True)
    item_trade_amount = models.IntegerField(verbose_name=u'交易数量', default=0, null=True, blank=True)


    def __str__(self):
        return '%d : %s' % (self.item_id, self.item_name)




#购物车
class ShoppingCart(models.Model):
    cart_id = models.AutoField(verbose_name=u'购物车', primary_key=True)
    cart_user = models.ForeignKey(to='User', to_field='user_id', on_delete=models.CASCADE, verbose_name=u'用户')
    cart_item = models.ForeignKey(to='Item', to_field='item_id', on_delete=models.CASCADE, verbose_name=u'购物车商品')
    cart_item_amount = models.IntegerField(verbose_name=u'商品数', default=1)

    def __str__(self):
        return '%d' % self.cart_id



#交易信息
class Trade(models.Model):
    trade_id = models.AutoField(verbose_name=u'交易记录', primary_key=True)
    trade_user = models.ForeignKey(to='User', to_field='user_id', on_delete=models.CASCADE, verbose_name=u'用户')
    trade_item = models.ForeignKey(to='Item', to_field='item_id', on_delete=models.CASCADE, verbose_name=u'交易商品')
    trade_amount = models.IntegerField(verbose_name=u'数量', default=1)
    trade_store = models.ForeignKey(to='Store', to_field='store_id', on_delete=models.CASCADE, verbose_name=u'交易商店')
    trade_isEvaluate = models.BooleanField(verbose_name=u'该交易是否已评价', default=False)
    trade_create_time = models.DateField(verbose_name=u'交易创建时间', auto_now=False, auto_now_add=True)
    trade_finish_time = models.DateField(verbose_name=u'交易完成时间', auto_now=True, auto_now_add=False)#每次修改该表任意数据都会变更为最后修改时间
    trade_info = models.IntegerField(verbose_name=u'发货信息', default=0)#0代表未发货，1代表正在运输中，2代表已到达目的地，3代表已确认收货，交易完成
    trade_TJUCarat = models.IntegerField(verbose_name=u'洋克拉', default=10)

    def __str__(self):
        return '%d' % self.trade_id



#群组信息
class Group(models.Model):
    group_id = models.AutoField(verbose_name=u'组', primary_key=True)
    group_name = models.CharField(verbose_name=u'组名', max_length=30, default='none')
    group_info = models.CharField(verbose_name=u'组简介', max_length=30, default=u'这个群主有点懒~')
    group_create_time = models.DateField(verbose_name=u'组创建时间', auto_now=False, auto_now_add=True)


    def __str__(self):
        return '%d' % self.group_id



#商店关注信息
class StoreFollow(models.Model):
    follow_id = models.AutoField(verbose_name=u'店铺关注信息', primary_key=True)
    follow_user = models.ForeignKey(to='User', to_field='user_id', on_delete=models.CASCADE, verbose_name=u'关注者')
    follow_store = models.ForeignKey(to='Store', to_field='store_id', on_delete=models.CASCADE, verbose_name=u'关注店铺')

    def __str__(self):
        return '%d' % self.follow_id


#商品关注信息
class ItemFollow(models.Model):
    follow_id = models.AutoField(verbose_name=u'商品关注信息', primary_key=True)
    follow_user = models.ForeignKey(to='User', to_field='user_id', on_delete=models.CASCADE, verbose_name=u'关注者')
    follow_item = models.ForeignKey(to='Item', to_field='item_id', on_delete=models.CASCADE, verbose_name=u'关注商品')

    def __str__(self):
        return '%d' % self.follow_id


#群组关注信息
class GroupFollow(models.Model):
    follow_id = models.AutoField(verbose_name=u'组关注信息', primary_key=True)
    follow_group = models.ForeignKey(to='Group', to_field='group_id', on_delete=models.CASCADE, verbose_name=u'组')
    follow_user = models.ForeignKey(to='User', to_field='user_id', on_delete=models.CASCADE, verbose_name=u'组员')


    def __str__(self):
        return '%d' % self.follow_id


#轮播图
class Banner(models.Model):
    banner_id = models.AutoField(verbose_name=u'轮播图', primary_key=True)
    banner_number = models.IntegerField(verbose_name=u'序号', default=0)
    banner_image = StdImageField(max_length=100, upload_to='banner/image', verbose_name=u'轮播图',
                               variations={'thumbnail':{'width':50, 'height':50}}, default='banner/image/itemDefault.jpg')
    banner_belong = models.ForeignKey(verbose_name=u'该轮播图属于哪个商品', to='Item', to_field='item_id', on_delete=models.CASCADE)


    def __str__(self):
        return '%d' % self.banner_id




#系统消息
class SystemMessage(models.Model):
    message_id = models.AutoField(verbose_name=u'系统消息', primary_key=True)
    message_content = models.CharField(verbose_name='消息内容', max_length=100, default='none')
    message_type = models.IntegerField(verbose_name=u'消息类型', default=0)#0代表系统消息，优先级最高；1代表一些优惠促销消息等等,2代表交易物流信息等等......
    message_create_time = models.DateField(verbose_name=u'系统消息创建时间', auto_now=False, auto_now_add=True)


    def __str__(self):
        return '%d' % self.message_id



#聊天信息
class ChatMessage(models.Model):
    message_id = models.AutoField(verbose_name=u'聊天信息', primary_key=True)
    message_type = models.IntegerField(verbose_name=u'信息类型', default=0)#0代表用户之间的消息记录，1代表组内消息记录
    message_ori = models.ForeignKey(to='User', to_field='user_id', on_delete=models.SET_NULL, verbose_name=u'发起者', related_name='message_ori', null=True, blank=True)
    message_rec = models.ForeignKey(to='User', to_field='user_id', on_delete=models.SET_NULL, verbose_name=u'接收者', related_name='message_rec', null=True, blank=True)
    message_create_time = models.DateField(verbose_name=u'消息创建时间', auto_now=False, auto_now_add=True)
    message_content = models.CharField(verbose_name=u'消息内容', max_length=100, default='none')
    message_group = models.ForeignKey(to='Group', to_field='group_id', on_delete=models.SET_NULL, verbose_name=u'群组', null=True, blank=True)

    def __str__(self):
        return '%d' % self.message_id


#优惠券
class Coupon(models.Model):
    coupon_id = models.AutoField(verbose_name=u'优惠券', primary_key=True)
    coupon_off = models.IntegerField(verbose_name=u'减多少元', default=0)
    coupon_require = models.IntegerField(verbose_name=u'满多少元', default=100)
    coupon_category = models.ForeignKey(to='ItemCategory', to_field='item_category_id', on_delete=models.CASCADE, verbose_name=u'适用大类', null=True, blank=True)
    coupon_last = models.DurationField(verbose_name=u'有效期')

    def __str__(self):
        return '%d' % self.coupon_id



#每日特价商品
class DailyOffItem(models.Model):
    item_id = models.AutoField(verbose_name=u'每日特价商品', primary_key=True)
    item_off_id = models.ForeignKey(to='Item', to_field='item_id', on_delete=models.CASCADE, verbose_name=u'特价商品id')
    item_off_create_time = models.DateField(verbose_name=u'特价商品创建时间', auto_now=False, auto_now_add=True)

    def __str__(self):
        return '%s' % self.item_id



#商品详情图片
class ItemDetailImage(models.Model):
    image_id = models.AutoField(verbose_name=u'商品详情图', primary_key=True)
    image_url = models.ImageField(verbose_name=u'详情图', upload_to='item/detailImage')
    image_belong = models.ForeignKey(verbose_name=u'隶属哪个商品', to='Item', to_field='item_id', on_delete=models.CASCADE)


    def __str__(self):
        return '%d' % self.image_id


#商品轮播图图片
class ItemBannerImage(models.Model):
    image_id = models.AutoField(verbose_name=u'商品轮播图', primary_key=True)
    image_url = models.ImageField(verbose_name=u'轮播图', upload_to='item/bannerImage')
    image_belong = models.ForeignKey(verbose_name=u'隶属哪个商品', to='Item', to_field='item_id', on_delete=models.CASCADE)


    def __str__(self):
        return '%d' % self.image_id



#评价图片
class EvaluateImage(models.Model):
    image_id = models.AutoField(verbose_name=u'评价图片', primary_key=True)
    image_url = models.ImageField(verbose_name=u'评价图', upload_to='evaluate/image')
    image_belong = models.ForeignKey(verbose_name=u'隶属哪个评价', to='Evaluate', to_field='evaluate_id', on_delete=models.CASCADE)

    def __str__(self):
        return '%d' % self.image_id




#收货地址
class Address(models.Model):
    address_id = models.AutoField(verbose_name=u'收货地址', primary_key=True)
    address_username = models.CharField(max_length=100, verbose_name=u'姓名', null=False, blank=False)
    address_tel = models.CharField(max_length=20, verbose_name=u'联系方式', null=False, blank=False)
    address_detail = models.CharField(max_length=100, verbose_name=u'具体地址', null=False, blank=False)
    address_type = models.CharField(max_length=10, verbose_name=u'地址类型', null=True, blank=True) #比如该地址为学校地址，为家庭地址等
    address_isDefault = models.BooleanField(verbose_name=u'是否为默认地址', default=False)
    address_user = models.ForeignKey(verbose_name=u'隶属哪个用户', to='User', to_field='user_id', on_delete=models.CASCADE)


    def __str__(self):
        return '%d' % self.address_id



#商品的品牌
class Brand(models.Model):
    brand_id = models.AutoField(verbose_name=u'商品品牌', primary_key=True)
    brand_name = models.CharField(max_length=10, verbose_name=u'品牌', null=True, blank=True)


    def __str__(self):
        return '%d' % self.brand_id


#收藏夹
class Collection(models.Model):
    collection_id = models.AutoField(verbose_name=u'收藏夹', primary_key=True)
    collection_user = models.ForeignKey(verbose_name=u'隶属哪个用户', to='User', to_field='user_id', on_delete=models.CASCADE)
    collection_item = models.ForeignKey(verbose_name=u'收藏的商品', to='Item', to_field='item_id', on_delete=models.CASCADE)


    def __str__(self):
        return '%d' % self.collection_id



#浏览历史记录
class BrowseRecord(models.Model):
    record_id = models.AutoField(verbose_name=u'浏览历史记录', primary_key=True)
    record_user = models.ForeignKey(verbose_name=u'隶属哪个用户', to='User', to_field='user_id', on_delete=models.CASCADE)
    record_item = models.ForeignKey(verbose_name=u'浏览的商品', to='Item', to_field='item_id', on_delete=models.CASCADE)
    record_flag = models.IntegerField(verbose_name=u'修改标志位', default=0)
    record_create_time = models.DateTimeField(verbose_name=u'浏览记录创建时间', auto_now=True, auto_now_add=False)


    def __str__(self):
        return '%d' % self.record_id


#搜索历史记录
class SearchRecord(models.Model):
    record_id = models.AutoField(verbose_name=u'搜索历史记录', primary_key=True)
    record_user = models.ForeignKey(verbose_name=u'隶属哪个用户', to='User', to_field='user_id', on_delete=models.CASCADE)
    record_content = models.CharField(max_length=100, verbose_name=u'搜索内容', null=False, blank=False)
    record_flag = models.IntegerField(verbose_name=u'修改标志位', default=0)
    record_create_time = models.DateTimeField(verbose_name=u'搜索记录创建时间', auto_now=True, auto_now_add=False)


    def __str__(self):
        return '%d' % self.record_id


#评价信息
class Evaluate(models.Model):
    evaluate_id = models.AutoField(verbose_name=u'评价', primary_key=True)
    evaluate_user = models.ForeignKey(verbose_name=u'评价的用户', to='User', to_field='user_id', on_delete=models.CASCADE)
    evaluate_item = models.ForeignKey(verbose_name=u'评价的商品', to='Item', to_field='item_id', on_delete=models.CASCADE)
    evaluate_TJUCarat = models.IntegerField(verbose_name=u'评价完成所获得洋克拉', default=10) #评价获得洋克拉数
    evaluate_isAnonymous = models.BooleanField(verbose_name=u'评价是否匿名', default=False) #是否匿名评价
    evaluate_content = models.CharField(max_length=100, verbose_name=u'评价内容', null=True, blank=True)
    evaluate_item_score = models.IntegerField(verbose_name=u'商品评价', default=5) #5表示满评
    evaluate_express_package = models.IntegerField(verbose_name=u'物流包装评价', default=5)
    evaluate_express_speed = models.IntegerField(verbose_name=u'物流速度评价', default=5)
    evaluate_express_service = models.IntegerField(verbose_name=u'物流服务评价', default=5)
    evaluate_create_time = models.DateField(verbose_name=u'评价时间', auto_now=False, auto_now_add=True)


    def __str__(self):
        return '%d' % self.evaluate_id


class Log(models.Model):
    # log_id = models.AutoField(verbose_name=u'用户', primary_key=True)
    log_name = models.CharField(verbose_name=u'name',max_length=30,default="none")

    def __str__(self):
        return '%s' % self.log_name

class temp(models.Model):
    item_name = models.CharField(verbose_name=u'品名', max_length=100, default='none')
    item_price = models.DecimalField(verbose_name=u'价格', max_digits=100, decimal_places=3, default='none')
    item_stoke = models.IntegerField(verbose_name=u'库存', default=9999)

    def __str__(self):
        return '%s' % self.item_name
