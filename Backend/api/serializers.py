from rest_framework import serializers
from backends.models import *



class UserSerializers(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = '__all__'

class StoreSerializers(serializers.ModelSerializer):
    class Meta:
        model = Store
        fields = '__all__'



class ItemCategorySerializers(serializers.ModelSerializer):
    class Meta:
        model = ItemCategory
        fields = '__all__'


class ItemSubCategorySerializers(serializers.ModelSerializer):
    class Meta:
        model = ItemSubCategory
        fields = '__all__'


class ItemSerializers(serializers.ModelSerializer):
    class Meta:
        model = Item
        fields = '__all__'



class ShppingCartSerializers(serializers.ModelSerializer):
    class Meta:
        model = ShoppingCart
        fields = '__all__'


class TradeSerializers(serializers.ModelSerializer):
    class Meta:
        model = Trade
        fields = '__all__'


class GroupSerializers(serializers.ModelSerializer):
    class Meta:
        model = Group
        fields = '__all__'


class StoreFollowSerializers(serializers.ModelSerializer):
    class Meta:
        model = StoreFollow
        fields = '__all__'


class ItemFollowSerializers(serializers.ModelSerializer):
    class Meta:
        model = ItemFollow
        fields = '__all__'
        # fields = ('follow_id', 'follow_user', 'follow_item')


class GroupFollowSerializers(serializers.ModelSerializer):
    class Meta:
        model = GroupFollow
        fields = '__all__'


class BannerSerializers(serializers.ModelSerializer):
    class Meta:
        model = Banner
        fields = '__all__'


class SystemMessageSerializers(serializers.ModelSerializer):
    class Meta:
        model = SystemMessage
        fields = '__all__'


class ChatMessageSerializers(serializers.ModelSerializer):
    class Meta:
        model = ChatMessage
        fields = '__all__'


class CouponSerializers(serializers.ModelSerializer):
    class Meta:
        model = Coupon
        fields = '__all__'


class DailyOffItemSerializers(serializers.ModelSerializer):
    class Meta:
        model = DailyOffItem
        fields = '__all__'



class ItemDetailImageSerializers(serializers.ModelSerializer):
    class Meta:
        model = ItemDetailImage
        fields = '__all__'


class ItemBannerImageSerializers(serializers.ModelSerializer):
    class Meta:
        model = ItemBannerImage
        fields = '__all__'


class EvaluateImageSerializers(serializers.ModelSerializer):
    class Meta:
        model = EvaluateImage
        fields = '__all__'


class AddressSerializers(serializers.ModelSerializer):
    class Meta:
        model = Address
        fields = '__all__'


class BrandSerializers(serializers.ModelSerializer):
    class Meta:
        model = Brand
        fields = '__all__'


class CollectionSerializers(serializers.ModelSerializer):
    class Meta:
        model = Collection
        fields = '__all__'


class BrowseRecordSerializers(serializers.ModelSerializer):
    class Meta:
        model = BrowseRecord
        fields = '__all__'


class SearchRecordSerializers(serializers.ModelSerializer):
    class Meta:
        model = SearchRecord
        fields = '__all__'


class EvaluateSerializers(serializers.ModelSerializer):
    class Meta:
        model = Evaluate
        fields = '__all__'
