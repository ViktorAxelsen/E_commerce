from django.shortcuts import render
from rest_framework import viewsets, filters, pagination
from backends.models import *
from api.serializers import *
from django_filters.rest_framework import DjangoFilterBackend
from django_filters import rest_framework
from api.filter import *
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.decorators import api_view
from itertools import chain
import Levenshtein


class UserViewSet(viewsets.ModelViewSet):
    queryset = User.objects.all().order_by('user_id')
    serializer_class = UserSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter,filters.OrderingFilter,)
    filter_class = UserFilter
    search_fields = ('=user_id', 'user_nickname', '=user_sex', '=user_tel', '=user_isAdmin', 'user_location')
    ordering_field = ('user_id', 'user_nickname', 'user_TJUCarat')



class StoreViewSet(viewsets.ModelViewSet):
    queryset = Store.objects.all().order_by('store_id')
    serializer_class = StoreSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = StoreFilter
    search_fields = ('=store_id', 'store_name', 'store_user', 'store_create_time')
    ordering_field = ('store_id', 'store_name', 'store_create_time', 'store_evaluate')



class ItemCategoryViewSet(viewsets.ModelViewSet):
    queryset = ItemCategory.objects.all().order_by('item_category_id')
    serializer_class = ItemCategorySerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ItemCategoryFilter
    search_fields = ('=item_category_id', 'item_category', 'item_category_isHaveSub')
    ordering_fields = ('item_category_id', 'item_category')


class ItemSubCategoryViewSet(viewsets.ModelViewSet):
    queryset = ItemSubCategory.objects.all().order_by('item_subCategory_id')
    serializer_class = ItemSubCategorySerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ItemSubCategoryFilter
    search_fields = ('=item_subCategory_id', 'item_subCategory')
    ordering_fields = ('item_subCategory_id')



class ItemViewSet(viewsets.ModelViewSet):
    queryset = Item.objects.all().order_by("item_id")
    serializer_class = ItemSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter,filters.OrderingFilter,)
    filter_class = ItemFilter
    search_fields = ('item_name', 'item_price', '=item_id', 'item_category', 'item_sub_category', 'item_sale')
    ordering_fields = ('item_name', 'item_price', 'item_id', 'item_browse', 'item_sale', 'item_stoke', 'item_prePrice', 'item_create_time', 'item_VIPOff')



class ShppingCartViewSet(viewsets.ModelViewSet):
    queryset = ShoppingCart.objects.all().order_by('cart_id')
    serializer_class = ShppingCartSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ShoppingCartFilter
    search_fields = ('=cart_id', 'cart_user', 'cart_item')
    ordering_fields = ('cart_id', 'cart_item_amount')



class TradeViewSet(viewsets.ModelViewSet):
    queryset = Trade.objects.all().order_by('trade_id')
    serializer_class = TradeSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    search_fields = ('=trade_id', 'trade_user', 'trade_item', '=trade_amount', 'trade_store', 'trade_create_time', 'trade_finish_time')
    filter_class = TradeFilter
    ordering_fields = ('trade_id', 'trade_amount', 'trade_create_time', 'trade_finish_time', 'trade_info', 'trade_TJUCarat')


class GroupViewSet(viewsets.ModelViewSet):
    queryset = Group.objects.all().order_by('group_id')
    serializer_class = GroupSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = GroupFilter
    search_fields = ('=group_id', 'group_name', 'group_create_time')
    ordering_fields = ('group_id', 'group_create_time')


class StoreFollowViewSet(viewsets.ModelViewSet):
    queryset = StoreFollow.objects.all().order_by('follow_id')
    serializer_class = StoreFollowSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = StoreFollowFilter
    search_fields = ('=follow_id', 'follow_user', 'follow_store')
    ordering_fields = ('follow_id')


class ItemFollowViewSet(viewsets.ModelViewSet):
    queryset = ItemFollow.objects.all().order_by('follow_id')
    serializer_class = ItemFollowSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ItemFollowFilter
    search_fields = ('=follow_id', 'follow_user', 'follow_item')
    ordering_fields = ('follow_id')


class GroupFollowViewSet(viewsets.ModelViewSet):
    queryset = GroupFollow.objects.all().order_by('follow_id')
    serializer_class = GroupFollowSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = GroupFollowFilter
    search_fields = ('=follow_id', 'follow_group', 'follow_user')
    ordering_fields = ('follow_id')


class BannerViewSet(viewsets.ModelViewSet):
    queryset = Banner.objects.all().order_by('banner_id')
    serializer_class = BannerSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = BannerFilter
    search_fields = ('=banner_id', 'banner_number')
    ordering_fields = ('banner_id', 'banner_number')

class SystemMessageViewSet(viewsets.ModelViewSet):
    queryset = SystemMessage.objects.all().order_by("message_id")
    serializer_class = SystemMessageSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = SystemMessageFilter
    search_fields = ('=message_id', 'message_content')
    ordering_fields = ('message_id', 'message_create_time')


class ChatMessageViewSet(viewsets.ModelViewSet):
    queryset = ChatMessage.objects.all().order_by("message_id")
    serializer_class = ChatMessageSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ChatMessageFilter
    search_fields = ('=message_id', 'message_content')
    ordering_fields = ('message_id', 'message_create_time')



class CouponViewSet(viewsets.ModelViewSet):
    queryset = Coupon.objects.all().order_by('coupon_id')
    serializer_class = CouponSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = CouponFilter
    search_fields = ('=coupon_id', 'coupon_category')
    ordering_fields = ('coupon_id', 'coupon_off', 'coupon_require', 'coupon_last')


class DailyOffItemViewSet(viewsets.ModelViewSet):
    queryset = DailyOffItem.objects.all().order_by('item_id')
    serializer_class = DailyOffItemSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = DailyOffItemFilter
    search_fields = ('=item_id', '=item_off_id')
    ordering_fields = ('item_id', 'item_off_create_time')


class ItemDetailImageViewSet(viewsets.ModelViewSet):
    queryset = ItemDetailImage.objects.all().order_by('image_id')
    serializer_class = ItemDetailImageSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ItemDetailImageFilter
    search_fields = ('=image_id', '=image_belong')
    ordering_fields = ('image_id')



class ItemBannerImageViewSet(viewsets.ModelViewSet):
    queryset = ItemBannerImage.objects.all().order_by('image_id')
    serializer_class = ItemBannerImageSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = ItemBannerImageFilter
    search_fields = ('=image_id', '=image_belong')
    ordering_fields = ('image_id')


class EvaluateImageViewSet(viewsets.ModelViewSet):
    queryset = EvaluateImage.objects.all().order_by('image_id')
    serializer_class = EvaluateImageSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = EvaluateImageFilter
    search_fields = ('=image_id', '=image_belong')
    ordering_fields = ('image_id')


class AddressViewSet(viewsets.ModelViewSet):
    queryset = Address.objects.all().order_by('address_id')
    serializer_class = AddressSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = AddressFilter
    search_fields = ('=address_id')
    ordering_fields = ('address_id')


class BrandViewSet(viewsets.ModelViewSet):
    queryset = Brand.objects.all().order_by('brand_id')
    serializer_class = BrandSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = BrandFilter
    search_fields = ('=brand_id')
    ordering_fields = ('brand_id')


class CollectionViewSet(viewsets.ModelViewSet):
    queryset = Collection.objects.all().order_by('collection_id')
    serializer_class = CollectionSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = CollectionFilter
    search_fields = ('=collection_id')
    ordering_fields = ('collection_id')


class BrowseRecordViewSet(viewsets.ModelViewSet):
    queryset = BrowseRecord.objects.all().order_by('record_id')
    serializer_class = BrowseRecordSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = BrowseRecordFilter
    search_fields = ('=record_id')
    ordering_fields = ('record_id', 'record_create_time')


class SearchRecordViewSet(viewsets.ModelViewSet):
    queryset = SearchRecord.objects.all().order_by('record_id')
    serializer_class = SearchRecordSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = SearchRecordFilter
    search_fields = ('=record_id')
    ordering_fields = ('record_id', 'record_create_time')


class EvaluateViewSet(viewsets.ModelViewSet):
    queryset = Evaluate.objects.all().order_by('evaluate_id')
    serializer_class = EvaluateSerializers
    filter_backends = (rest_framework.DjangoFilterBackend, filters.SearchFilter, filters.OrderingFilter,)
    filter_class = EvaluateFilter
    search_fields = ('=evaluate_id')
    ordering_fields = ('evaluate_id', 'evaluate_item_score', 'evaluate_express_package', 'evaluate_express_speed', 'evaluate_express_service', 'evaluate_create_time')


#输入用户id，返回该用户购物车的所有商品
@api_view(['GET'])
def get_user_cart_item(request, pk):
    if request.method == 'GET':
        cart_info = ShoppingCart.objects.filter(cart_user=pk)
        queryset = set()
        for cart in cart_info:
            item_id = cart.cart_item.item_id #获取到item对象的item_id
            items = Item.objects.filter(item_id=item_id) #构造queryset
            queryset = chain(queryset, items) #链接queryset
        serializer = ItemSerializers(queryset, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#输入用户id，返回该用户关注的所有商店信息
@api_view(['GET'])
def get_user_store_follow(request, pk):
    if request.method == 'GET':
        store_follow_info = StoreFollow.objects.filter(follow_user=pk)
        queryset = set()
        for follow in store_follow_info:
            store_id = follow.follow_store.store_id
            stores = Store.objects.filter(store_id=store_id)
            queryset = chain(queryset, stores)
        serializer = StoreSerializers(queryset, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#输入用户id，返回该用户关注的所有商品信息
@api_view(['GET'])
def get_user_item_follow(request, pk):
    if request.method == 'GET':
        item_follow_info = ItemFollow.objects.filter(follow_user=pk)
        queryset = set()
        for follow in item_follow_info:
            item_id = follow.follow_item.item_id
            items = Item.objects.filter(item_id=item_id)
            queryset = chain(queryset, items)
        serializer = ItemSerializers(queryset, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


# 根据user_id上传用户的头像
@api_view(['POST'])
def upload_user_head_image(request, pk):
    if request.method == 'POST':
        user_instance = User.objects.get(user_id=pk)
        image = request.FILES.get('img')
        user_instance.user_headImage = image
        user_instance.save()
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')


# 根据item_id上传商品预览图
@api_view(['POST'])
def upload_item_preview_image(request, pk):
    if request.method == 'POST':
        item_instance = Item.objects.get(item_id=pk)
        image = request.FILES.get('img')
        item_instance.item_preview_image = image
        item_instance.save()
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')



#输入商品id，可以添加该商品的多张详情图片或返回该商品的详情图片路径
@api_view(['GET', 'POST'])
def item_detail_image(request, pk):
    if request.method == 'POST':
        item_instance = Item.objects.get(item_id=pk)
        image = request.FILES.getlist('img')
        for img in image:
            new_image = ItemDetailImage(
                image_url=img,
                image_belong=item_instance
            )
            new_image.save()
        return Response('success!')
    elif request.method == 'GET':
        images = ItemDetailImage.objects.filter(image_belong=pk)
        serializer = ItemDetailImageSerializers(images, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET And POST Method! ')



#输入商品id，可以添加该商品的多张轮播图片或返回该商品的轮播图片路径
@api_view(['GET', 'POST'])
def item_banner_image(request, pk):
    if request.method == 'POST':
        item_instance = Item.objects.get(item_id=pk)
        image = request.FILES.getlist('img')
        for img in image:
            new_image = ItemBannerImage(
                image_url=img,
                image_belong=item_instance
            )
            new_image.save()
        return Response('success!')
    elif request.method == 'GET':
        images = ItemBannerImage.objects.filter(image_belong=pk)
        serializer = ItemBannerImageSerializers(images, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET And POST Method! ')


#输入评价id，可以添加该评价的多张图片或返回该评价的图片路径
@api_view(['GET', 'POST'])
def evaluate_image(request, pk):
    if request.method == 'POST':
        evaluate_instance = Evaluate.objects.get(evaluate_id=pk)
        image = request.FILES.getlist('img')
        for img in image:
            new_image = EvaluateImage(
                image_url=img,
                image_belong=evaluate_instance
            )
            new_image.save()
        return Response('success!')
    elif request.method == 'GET':
        images = EvaluateImage.objects.filter(image_belong=pk)
        serializer = EvaluateImageSerializers(images, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET And POST Method! ')



#输入用户id，返回该用户的优惠券信息
@api_view(['GET'])
def get_user_coupon(request, pk):
    if request.method == 'GET':
        coupons = User.objects.get(user_id=pk).user_coupon
        serializer = CouponSerializers(coupons, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#输入用户id、商品id直接删除收藏夹的记录
@api_view(['DELETE'])
def delete_user_collection(request):
    if request.method == 'DELETE':
        user_id = request.GET.get('user_id', 0)
        item_id = request.GET.get('item_id', 0)
        if user_id == 0 or item_id == 0:
            return Response('Argument Error!')
        Collection.objects.get(collection_user=user_id, collection_item=item_id).delete()
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow DELETE Method! ')


#输入用户id返回该用户收藏夹中所有商品信息
@api_view(['GET'])
def get_user_collection(request, pk):
    if request.method == 'GET':
        collections = Collection.objects.filter(collection_user=pk)
        queryset = set()
        for collection in collections:
            item_id = collection.collection_item.item_id
            item = Item.objects.filter(item_id=item_id)
            queryset = chain(queryset, item)
        serializer = ItemSerializers(queryset, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#输入用户id、商品id，当前端浏览商品时自动更新浏览记录并合并记录、自动给商品浏览量+1、并返回该商品的信息
@api_view(['GET'])
def browse_item(request):
    if request.method == 'GET':
        user_id = request.GET.get('user_id', 0)
        item_id = request.GET.get('item_id', 0)
        if user_id == 0 or item_id == 0:
            return Response('Argument Error!')
        item = Item.objects.filter(item_id=item_id)
        user = User.objects.filter(user_id=user_id)
        #更新浏览记录并合并
        record = BrowseRecord.objects.filter(record_user=user_id, record_item=item_id)
        if record.exists(): #若用户曾经访问过这个商品
            record = record[0]
            record_flag = record.record_flag + 1
            record.record_flag = record_flag
            record.save()
        else:
            new_record = BrowseRecord(
                record_user=user[0],
                record_item=item[0]
            )
            new_record.save()
        #商品浏览量+1
        item_browse = item[0].item_browse
        item.update(item_browse=item_browse + 1)
        #返回商品信息
        serializer = ItemSerializers(item[0])
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#根据商店id获取该商店的综合评价信息并给store_evaluate赋值 50% evaluate_item + 50% (evaluate_express_package_+ evaluate_express_speed + evaluate_express_service) / 3
@api_view(['GET'])
def get_store_evaluate(request, pk):
    if request.method == 'GET':
        store = Store.objects.filter(store_id=pk)
        items = Item.objects.filter(item_store=pk)
        total_evaluate = 0
        count = 0
        for item in items:
            item_id = item.item_id
            evaluates = Evaluate.objects.filter(evaluate_item=item_id)
            for evaluate in evaluates:
                total_evaluate += (((evaluate.evaluate_express_package + evaluate.evaluate_express_service + evaluate.evaluate_express_speed) / 3) * 0.5 + evaluate.evaluate_item_score * 0.5)
                count += 1
        avg_evaluate = round((total_evaluate / count), 1)
        store.update(store_evaluate=avg_evaluate)
        return Response(avg_evaluate)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#根据用户id和搜索内容更新搜索记录并合并
@api_view(['POST'])
def search_content(request, pk):
    if request.method == 'POST':
        content = request.GET.get('content', '')
        if len(content) == 0:
            return Response('Content Empty!')
        user = User.objects.filter(user_id=pk)
        record = SearchRecord.objects.filter(record_user=pk, record_content=content)
        if record.exists():
            record = record[0]
            record_flag = record.record_flag + 1
            record.record_flag = record_flag
            record.save()
        else:
            new_record = SearchRecord(
                record_user=user[0],
                record_content=content
            )
            new_record.save()
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')


#根据用户id和商品id判断该用户是否收藏了该商品
@api_view(['GET'])
def whether_user_collect_item(request):
    if request.method == 'GET':
        user_id = request.GET.get('user_id', 0)
        item_id = request.GET.get('item_id', 0)
        if user_id == 0 or item_id == 0:
            return Response('Argument Error!')
        user = User.objects.filter(user_id=user_id)
        item = Item.objects.filter(item_id=item_id)
        collection = Collection.objects.filter(collection_user=user_id, collection_item=item_id)
        if collection.exists():
            return Response(1)
        else:
            return Response(0)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#根据商品id返回该商品有多少条评价信息
@api_view(['GET'])
def get_item_evaluate_amount(request, pk):
    if request.method == 'GET':
        item_instance = Item.objects.get(item_id=pk)
        evaluates = Evaluate.objects.filter(evaluate_item=pk)
        return Response(len(evaluates))
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#根据商品id返回该商品的综合评价信息。默认以好评率字符串返回，例如：98%好评
@api_view(['GET'])
def get_item_evaluate_info(request, pk):
    if request.method == 'GET':
        item_instance = Item.objects.get(item_id=pk)
        evaluates = Evaluate.objects.filter(evaluate_item=pk)
        count = 0
        score = 0
        if len(evaluates) == 0:
            return Response('暂无评价')
        for evaluate in evaluates:
            score += evaluate.evaluate_item_score
            count += 1
        rate = round(20 * (score / count))
        return Response(str(rate) + '%好评')
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')




#根据用户id和商店id返回该用户是否在该店购买过商品
@api_view(['GET'])
def whether_user_buy_item_in_store(request):
    if request.method == 'GET':
        user_id = request.GET.get('user_id', 0)
        store_id = request.GET.get('store_id', 0)
        if user_id == 0 or store_id == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        store_instance = Store.objects.get(store_id=store_id)
        trades = Trade.objects.filter(trade_user=user_id, trade_store=store_id)
        if len(trades) == 0:
            return Response(0)
        else:
            for trade in trades:
                if trade.trade_info == 3:
                    return Response(1)
            return Response(0)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#根据商品id返回该商品被多少人收藏
@api_view(['GET'])
def get_item_collection_amount(request, pk):
    if request.method == 'GET':
        item_instance = Item.objects.get(item_id=pk)
        collections = Collection.objects.filter(collection_item=pk)
        return Response(len(collections))
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


#根据商品id返回“推荐商品”的详细信息(智能推荐算法)
@api_view(['GET'])
def get_recommend_item(request, pk):
    if request.method == 'GET':
        item_instance = Item.objects.get(item_id=pk)
        item_sub_category = item_instance.item_sub_category #得到商品的子类
        item_name = item_instance.item_name #得到商品的名字
        items = Item.objects.filter(item_sub_category=item_sub_category).exclude(item_name=item_name) #得到同类的商品
        queryset = set()
        for item in items:
            if Levenshtein.ratio(item_name, item.item_name) >= 0.5:
                item_extra = Item.objects.filter(item_id=item.item_id)
                queryset = chain(queryset, item_extra)
        serializer = ItemSerializers(queryset, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')



#根据商品id返回该商品的好评数、中评数、差评数和有图的评价数并返回评价信息
@api_view(['GET'])
def get_evaluate_type(request):
    if request.method == 'GET':
        item_id = request.GET.get('item_id', 0)
        evaluate_type = request.GET.get('evaluate_type', '')
        return_type = request.GET.get('return_type', 0)
        if item_id == 0 or len(evaluate_type) == 0 or return_type == 0:
            return Response('Argument Error!')
        item_instance = Item.objects.get(item_id=item_id)
        if evaluate_type == 'good':
            evaluates = Evaluate.objects.filter(evaluate_item_score__gte=4, evaluate_item=item_id)
            if return_type == 'amount':
                return Response(len(evaluates))
            elif return_type == 'info':
                serializer = EvaluateSerializers(evaluates, many=True)
                return Response(serializer.data)
        elif evaluate_type == 'normal':
            evaluates = Evaluate.objects.filter(evaluate_item_score__gte=2, evaluate_item_score__lte=3, evaluate_item=item_id)
            if return_type == 'amount':
                return Response(len(evaluates))
            elif return_type == 'info':
                serializer = EvaluateSerializers(evaluates, many=True)
                return Response(serializer.data)
        elif evaluate_type == 'bad':
            evaluates = Evaluate.objects.filter(evaluate_item_score__lte=1, evaluate_item=item_id)
            if return_type == 'amount':
                return Response(len(evaluates))
            elif return_type == 'info':
                serializer = EvaluateSerializers(evaluates, many=True)
                return Response(serializer.data)
        elif evaluate_type == 'image':
            evaluates = EvaluateImage.objects.filter(image_belong__evaluate_item=item_id)
            if return_type == 'amount':
                return Response(len(evaluates))
            elif return_type == 'info':
                queryset = set()
                for evaluate in evaluates:
                    evaluate_instance = Evaluate.objects.filter(evaluate_id=evaluate.evaluate_belong)
                    queryset = chain(queryset, evaluate_instance)
                serializer = EvaluateSerializers(queryset, many=True)
                return Response(serializer.data)
        else:
            return Response('Argument Error!')
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')



#根据用户id返回待收货的商品信息以及数量
@api_view(['GET'])
def get_wait_receive(request):
    if request.method == 'GET':
        user_id = request.GET.get('user_id', 0)
        return_type = request.GET.get('return_type', 0)
        if user_id == 0 or return_type == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        trades = Trade.objects.filter(trade_user=user_id, trade_info__lte=2)
        if return_type == 'amount':
            return Response(len(trades))
        elif return_type == 'info':
            queryset = set()
            for trade in trades:
                item_id = trade.trade_item.item_id
                item = Item.objects.filter(item_id=item_id)
                item.update(item_trade_amount=trade.trade_amount)
                queryset = chain(queryset, item)
            serializer = ItemSerializers(queryset, many=True)
            return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')



#根据用户id返回待评价的商品信息以及数量
@api_view(['GET'])
def get_wait_evaluate(request):
    if request.method == 'GET':
        user_id = request.GET.get('user_id', 0)
        return_type = request.GET.get('return_type', 0)
        if user_id == 0 or return_type == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        trades = Trade.objects.filter(trade_user=user_id, trade_isEvaluate=False)
        if return_type == 'amount':
            return Response(len(trades))
        elif return_type == 'info':
            queryset = set()
            for trade in trades:
                item_id = trade.trade_item.item_id
                item = Item.objects.filter(item_id=item_id)
                queryset = chain(queryset, item)
            serializer = ItemSerializers(queryset, many=True)
            return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')



#根据用户id返回该用户所有已完成的订单数及商品详情
@api_view(['GET'])
def get_complete_trade(request):
    if request.method == 'GET':
        user_id = request.GET.get('user_id', 0)
        return_type = request.GET.get('return_type', 0)
        if user_id == 0 or return_type == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        trades = Trade.objects.filter(trade_user=user_id, trade_info=3)
        if return_type == 'amount':
            return Response(len(trades))
        elif return_type == 'info':
            queryset = set()
            for trade in trades:
                item_id = trade.trade_item.item_id
                item = Item.objects.filter(item_id=item_id)
                queryset = chain(queryset, item)
            serializer = ItemSerializers(queryset, many=True)
            return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')



#根据用户id、商品id、商店id、购买数量，处理前端点击“立即购买”后的逻辑
@api_view(['POST'])
def buy_now(request):
    if request.method == 'POST':
        user_id = request.GET.get('user_id', 0)
        item_id = request.GET.get('item_id', 0)
        store_id = request.GET.get('store_id', 0)
        amount = request.GET.get('amount', 0)
        if user_id == 0 or item_id == 0 or amount == 0 or store_id == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        item_instance = Item.objects.get(item_id=item_id)
        store_instance = Store.objects.get(store_id=store_id)
        #修改对应商品的信息
        after_stoke = item_instance.item_stoke - int(amount)
        if after_stoke < 0:
            return Response('stoke is not enough!')
        item_instance.item_stoke -= int(amount)
        item_instance.item_sale += int(amount)
        item_instance.save()
        #Trade加记录
        new_trade = Trade(
            trade_amount=amount,
            trade_isEvaluate=False,
            trade_info=0,
            trade_TJUCarat=10,
            trade_user=user_instance,
            trade_item=item_instance,
            trade_store=store_instance
        )
        new_trade.save()
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')


#根据用户id、商品id、购买数量，处理前端点击加入购物车的逻辑
@api_view(['POST'])
def add_into_cart(request):
    if request.method == 'POST':
        user_id = request.GET.get('user_id', 0)
        item_id = request.GET.get('item_id', 0)
        amount = request.GET.get('amount', 0)
        if user_id == 0 or item_id == 0 or amount == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        item_instance = Item.objects.get(item_id=item_id)
        # 检查库存
        after_stoke = item_instance.item_stoke - int(amount)
        if after_stoke < 0:
            return Response('stoke is not enough!')
        # 合并同类
        carts = ShoppingCart.objects.filter(cart_user=user_id, cart_item=item_id)
        if len(carts) == 1:
            cart = carts[0]
            amountAfter = cart.cart_item_amount + int(amount)
            carts.update(cart_item_amount=amountAfter)
            return Response('success!')
        else:
            # 购物车加记录
            new_cart = ShoppingCart(
                cart_item_amount=amount,
                cart_user=user_instance,
                cart_item=item_instance
            )
            new_cart.save()
            return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')




#根据用户id、商品id、商店id、购买数量，处理前端在购物车页面进行购买的逻辑
@api_view(['POST'])
def buy_in_cart(request):
    if request.method == 'POST':
        user_id = request.GET.get('user_id', 0)
        item_id = request.GET.get('item_id', 0)
        store_id = request.GET.get('store_id', 0)
        amount = request.GET.get('amount', 0)
        if user_id == 0 or item_id == 0 or amount == 0 or store_id == 0:
            return Response('Argument Error!')
        user_instance = User.objects.get(user_id=user_id)
        item_instance = Item.objects.get(item_id=item_id)
        store_instance = Store.objects.get(store_id=store_id)
        # 修改对应商品的信息
        after_stoke = item_instance.item_stoke - int(amount)
        if after_stoke < 0:
            return Response('stoke is not enough!')
        item_instance.item_stoke -= int(amount)
        item_instance.item_sale += int(amount)
        item_instance.save()
        # Trade加记录
        new_trade = Trade(
            trade_amount=amount,
            trade_isEvaluate=False,
            trade_info=0,
            trade_TJUCarat=10,
            trade_user=user_instance,
            trade_item=item_instance,
            trade_store=store_instance
        )
        new_trade.save()
        #删除购物车记录
        ShoppingCart.objects.filter(cart_user=user_instance, cart_item=item_instance, cart_item_amount=amount).delete()
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')



#根据trade_id进行收货（trade_info赋值3）
@api_view(['POST'])
def confirm_receive(request, pk):
    if request.method =='POST':
        trade = Trade.objects.filter(trade_id=pk)
        trade.update(trade_info=3)
        return Response("success!")
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')



#根据user_id返回改用户浏览记录的商品详情（按时间降序）
@api_view(['GET'])
def get_history_item(request, pk):
    if request.method =='GET':
        user_instance = User.objects.get(user_id=pk)
        records = BrowseRecord.objects.filter(record_user=pk).order_by('-record_create_time')
        queryset = set()
        for record in records:
            item_id = record.record_item.item_id
            item = Item.objects.filter(item_id=item_id)
            queryset = chain(queryset, item)
        serializer = ItemSerializers(queryset, many=True)
        return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')



# 根据user_id判断判断是否开店，若开店返回商店详情；若没开，返回数字0
@api_view(['GET'])
def get_user_store_info(request, pk):
    if request.method =='GET':
        user_instance = User.objects.get(user_id=pk)
        store = Store.objects.filter(store_user=pk)
        if len(store) == 0:
            return Response(0)
        else:
            serializer = StoreSerializers(store[0])
            return Response(serializer.data)
    else:
        return Response('Request Method Error! This API Only Allow GET Method! ')


# 根据user_id以及其他数据进行地址上传，如果已存在该用户的地址则更新记录
@api_view(['POST'])
def update_user_address(request):
    if request.method =='POST':
        user_id = request.GET.get('user_id', 0)
        detail = request.GET.get('detail', 0)
        name = request.GET.get('name', 0)
        tel = request.GET.get('tel', 0)
        if user_id == 0 or detail == 0 or name == 0 or tel == 0:
            return Response("Argument Error!")
        user_instance = User.objects.get(user_id=user_id)
        address = Address.objects.filter(address_user=user_id)
        # 若地址不存在
        if len(address) == 0:
            new_address = Address(
                address_username=name,
                address_tel=tel,
                address_detail=detail,
                address_user=user_instance
            )
            new_address.save()
        else:
            address.update(address_username=name, address_tel=tel, address_detail=detail)
        return Response('success!')
    else:
        return Response('Request Method Error! This API Only Allow POST Method! ')
