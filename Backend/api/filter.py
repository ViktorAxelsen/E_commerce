import django_filters
from backends.models import *


class UserFilter(django_filters.rest_framework.FilterSet):
    min_carat = django_filters.NumberFilter(field_name='user_TJUCarat', lookup_expr='gte')
    max_carat = django_filters.NumberFilter(field_name='user_TJUCarat', lookup_expr='lte')

    user = django_filters.CharFilter(field_name='user_nickname', lookup_expr='exact')

    class Meta:
        model = User
        fields = {
            'user_id': ['exact'],
            'user_nickname': ['icontains'],
            'user_sex': ['exact'],
            'user_tel': ['exact'],
            'user_isAdmin': ['exact'],
            'user_isVIP': ['exact'],
            'user_location': ['icontains'],
            'user_TJUCarat': ['exact'],
            'user_password': ['exact'],
        }


class StoreFilter(django_filters.rest_framework.FilterSet):
    create_date = django_filters.DateFilter(field_name='store_create_time', lookup_expr='exact')
    min_store_create_time = django_filters.DateFilter(field_name='store_create_time', lookup_expr='gte')
    max_store_create_time = django_filters.DateFilter(field_name='store_create_time', lookup_expr='lte')

    min_evaluate = django_filters.NumberFilter(field_name='store_evaluate', lookup_expr='gte')
    max_evaluate = django_filters.NumberFilter(field_name='store_evaluate', lookup_expr='lte')


    class Meta:
        model = Store
        fields = {
            'store_id': ['exact'],
            'store_name': ['icontains'],
            'store_user': ['exact'],
            'store_evaluate': ['exact'],
        }


class ItemCategoryFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = ItemCategory
        fields = {
            'item_category_id': ['exact'],
            'item_category': ['icontains'],
            'item_category_isHaveSub': ['exact'],
        }


class ItemSubCategoryFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = ItemSubCategory
        fields = {
            'item_subCategory_id': ['exact'],
            'item_subCategory': ['icontains'],
            'item_subCategory_belong': ['exact'],
        }



class ItemFilter(django_filters.rest_framework.FilterSet):
    min_price = django_filters.NumberFilter(field_name='item_price', lookup_expr='gte')
    max_price = django_filters.NumberFilter(field_name='item_price', lookup_expr='lte')

    min_stoke = django_filters.NumberFilter(field_name='item_stoke', lookup_expr='gte')
    max_stoke = django_filters.NumberFilter(field_name='item_stoke', lookup_expr='lte')

    min_browse = django_filters.NumberFilter(field_name='item_browse', lookup_expr='gte')
    max_browse = django_filters.NumberFilter(field_name='item_browse', lookup_expr='lte')

    min_sale = django_filters.NumberFilter(field_name='item_sale', lookup_expr='gte')
    max_sale = django_filters.NumberFilter(field_name='item_sale', lookup_expr='lte')

    min_item_create_time = django_filters.DateFilter(field_name='item_create_time', lookup_expr='gte')
    max_item_create_time = django_filters.DateFilter(field_name='item_create_time', lookup_expr='lte')

    min_VIPOff = django_filters.NumberFilter(field_name='item_VIPOff', lookup_expr='gte')
    max_VIPOff = django_filters.NumberFilter(field_name='item_VIPOff', lookup_expr='lte')

    class Meta:
        model = Item
        fields = {
            'item_id': ['exact'],
            'item_name': ['icontains'],
            'item_price': ['exact'],
            'item_stoke': ['exact'],
            'item_category': ['exact'],
            'item_sub_category': ['exact'],
            'item_browse': ['exact'],
            'item_store': ['exact'],
            'item_off': ['exact'],
            'item_prePrice': ['exact'],
            'item_sale': ['exact'],
            'item_create_time': ['exact'],
            'item_VIPOff': ['exact'],
            'item_brand': ['exact'],
        }




class ShoppingCartFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = ShoppingCart
        fields = {
            'cart_id': ['exact'],
            'cart_user': ['exact'],
            'cart_item': ['exact'],
        }


class TradeFilter(django_filters.rest_framework.FilterSet):
    min_trade_amount = django_filters.NumberFilter(field_name='trade_amount', lookup_expr='gte')
    max_trade_amount = django_filters.NumberFilter(field_name='trade_amount', lookup_expr='lte')

    min_trade_create_time = django_filters.DateFilter(field_name='trade_create_time', lookup_expr='gte')
    max_trade_create_time = django_filters.DateFilter(field_name='trade_create_time', lookup_expr='lte')

    min_trade_finish_time = django_filters.DateFilter(field_name='trade_finish_time', lookup_expr='gte')
    max_trade_finish_time = django_filters.DateFilter(field_name='trade_finish_time', lookup_expr='lte')

    min_carat = django_filters.NumberFilter(field_name='trade_TJUCarat', lookup_expr='gte')
    max_carat = django_filters.NumberFilter(field_name='trade_TJUCarat', lookup_expr='lte')

    min_trade_info = django_filters.NumberFilter(field_name='trade_info', lookup_expr='gte')
    max_trade_info = django_filters.NumberFilter(field_name='trade_info', lookup_expr='lte')


    class Meta:
        model = Trade
        fields = {
            'trade_id': ['exact'],
            'trade_user': ['exact'],
            'trade_item': ['exact'],
            'trade_amount': ['exact'],
            'trade_store': ['exact'],
            'trade_create_time': ['exact'],
            'trade_finish_time': ['exact'],
            'trade_info': ['exact'],
            'trade_TJUCarat': ['exact'],
            'trade_isEvaluate': ['exact'],
        }


class GroupFilter(django_filters.rest_framework.FilterSet):
    min_group_create_time = django_filters.DateFilter(field_name='group_create_time', lookup_expr='gte')
    max_group_create_time = django_filters.DateFilter(field_name='group_create_time', lookup_expr='lte')

    class Meta:
        model = Group
        fields = {
            'group_id': ['exact'],
            'group_name': ['icontains'],
            'group_create_time': ['exact'],
        }


class StoreFollowFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = StoreFollow
        fields = {
            'follow_id': ['exact'],
            'follow_user': ['exact'],
            'follow_store': ['exact'],
        }


class ItemFollowFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = ItemFollow
        fields = {
            'follow_id': ['exact'],
            'follow_user': ['exact'],
            'follow_item': ['exact'],
        }


class GroupFollowFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = GroupFollow
        fields = {
            'follow_id': ['exact'],
            'follow_group': ['exact'],
            'follow_user': ['exact'],
        }


class BannerFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = Banner
        fields = {
            'banner_id': ['exact'],
            'banner_number': ['exact'],
        }


class SystemMessageFilter(django_filters.rest_framework.FilterSet):
    min_message_create_time = django_filters.DateFilter(field_name='message_create_time', lookup_expr='gte')
    max_message_create_time = django_filters.DateFilter(field_name='message_create_time', lookup_expr='lte')

    class Meta:
        model = SystemMessage
        fields = {
            'message_id': ['exact'],
            'message_content': ['icontains'],
            'message_type': ['exact'],
            'message_create_time': ['exact'],
        }


class ChatMessageFilter(django_filters.rest_framework.FilterSet):
    min_message_create_time = django_filters.DateFilter(field_name='message_create_time', lookup_expr='gte')
    max_message_create_time = django_filters.DateFilter(field_name='message_create_time', lookup_expr='lte')

    class Meta:
        model = ChatMessage
        fields = {
            'message_id': ['exact'],
            'message_type': ['exact'],
            'message_ori': ['exact'],
            'message_rec': ['exact'],
            'message_create_time': ['exact'],
            'message_content': ['icontains'],
            'message_group': ['exact'],
        }


class CouponFilter(django_filters.rest_framework.FilterSet):
    min_off = django_filters.NumberFilter(field_name='coupon_off', lookup_expr='gte')
    max_off = django_filters.NumberFilter(field_name='coupon_off', lookup_expr='lte')

    min_require = django_filters.NumberFilter(field_name='coupon_require', lookup_expr='gte')
    max_require = django_filters.NumberFilter(field_name='coupon_require', lookup_expr='lte')

    min_last = django_filters.DurationFilter(field_name='coupon_last', lookup_expr='gte')
    max_last = django_filters.DurationFilter(field_name='coupon_last', lookup_expr='lte')

    class Meta:
        model = Coupon
        fields = {
            'coupon_id': ['exact'],
            'coupon_off': ['exact'],
            'coupon_require': ['exact'],
            'coupon_category': ['exact'],
            'coupon_last': ['exact'],
        }


class DailyOffItemFilter(django_filters.rest_framework.FilterSet):
    min_item_off_create_time = django_filters.DateFilter(field_name='item_off_create_time', lookup_expr='gte')
    max_item_off_create_time = django_filters.DateFilter(field_name='item_off_create_time', lookup_expr='lte')

    class Meta:
        model = DailyOffItem
        fields = {
            'item_id': ['exact'],
            'item_off_id': ['exact'],
            'item_off_create_time': ['exact'],
        }


class ItemDetailImageFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = ItemDetailImage
        fields = {
            'image_id': ['exact'],
            'image_belong': ['exact'],
        }


class ItemBannerImageFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = ItemBannerImage
        fields = {
            'image_id': ['exact'],
            'image_belong': ['exact'],
        }


class EvaluateImageFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = EvaluateImage
        fields = {
            'image_id': ['exact'],
            'image_belong': ['exact'],
        }


class AddressFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = Address
        fields = {
            'address_id': ['exact'],
            'address_username': ['icontains'],
            'address_tel': ['exact'],
            'address_detail': ['icontains'],
            'address_type': ['exact'],
            'address_isDefault': ['exact'],
            'address_user': ['exact'],
        }


class BrandFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = Brand
        fields = {
            'brand_id': ['exact'],
            'brand_name': ['icontains'],
        }


class CollectionFilter(django_filters.rest_framework.FilterSet):

    class Meta:
        model = Collection
        fields = {
            'collection_id': ['exact'],
            'collection_user': ['exact'],
            'collection_item': ['exact'],
        }


class BrowseRecordFilter(django_filters.rest_framework.FilterSet):
    min_record_create_time = django_filters.DateTimeFilter(field_name='record_create_time', lookup_expr='gte')
    max_record_create_time = django_filters.DateTimeFilter(field_name='record_create_time', lookup_expr='lte')

    class Meta:
        model = BrowseRecord
        fields = {
            'record_id': ['exact'],
            'record_user': ['exact'],
            'record_item': ['exact'],
            'record_flag': ['exact'],
            'record_create_time': ['exact'],
        }


class SearchRecordFilter(django_filters.rest_framework.FilterSet):
    min_record_create_time = django_filters.DateTimeFilter(field_name='record_create_time', lookup_expr='gte')
    max_record_create_time = django_filters.DateTimeFilter(field_name='record_create_time', lookup_expr='lte')

    class Meta:
        model = SearchRecord
        fields = {
            'record_id': ['exact'],
            'record_user': ['exact'],
            'record_content': ['icontains'],
            'record_flag': ['exact'],
            'record_create_time': ['exact'],
        }


class EvaluateFilter(django_filters.rest_framework.FilterSet):
    min_carat = django_filters.NumberFilter(field_name='evaluate_TJUCarat', lookup_expr='gte')
    max_carat = django_filters.NumberFilter(field_name='evaluate_TJUCarat', lookup_expr='lte')

    min_item_score = django_filters.NumberFilter(field_name='evaluate_item_score', lookup_expr='gte')
    max_item_score = django_filters.NumberFilter(field_name='evaluate_item_score', lookup_expr='lte')

    min_express_package = django_filters.NumberFilter(field_name='evaluate_express_package', lookup_expr='gte')
    max_express_package = django_filters.NumberFilter(field_name='evaluate_express_package', lookup_expr='lte')

    min_express_speed = django_filters.NumberFilter(field_name='evaluate_express_speed', lookup_expr='gte')
    max_express_speed = django_filters.NumberFilter(field_name='evaluate_express_speed', lookup_expr='lte')

    min_express_service = django_filters.NumberFilter(field_name='evaluate_express_service', lookup_expr='gte')
    max_express_service = django_filters.NumberFilter(field_name='evaluate_express_service', lookup_expr='lte')

    min_create_time = django_filters.DateFilter(field_name='evaluate_create_time', lookup_expr='gte')
    max_create_time = django_filters.DateFilter(field_name='evaluate_create_time', lookup_expr='lte')


    class Meta:
        model = Evaluate
        fields = {
            'evaluate_id': ['exact'],
            'evaluate_user': ['exact'],
            'evaluate_item': ['exact'],
            'evaluate_TJUCarat': ['exact'],
            'evaluate_isAnonymous': ['exact'],
            'evaluate_content': ['icontains'],
            'evaluate_item_score': ['exact'],
            'evaluate_express_package': ['exact'],
            'evaluate_express_speed': ['exact'],
            'evaluate_express_service': ['exact'],
            'evaluate_create_time': ['exact'],
        }
