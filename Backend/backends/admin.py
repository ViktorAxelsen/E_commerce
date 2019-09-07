from django.contrib import admin
from backends.models import *


@admin.register(User)
class User(admin.ModelAdmin):
    list_display = ['user_id', 'user_nickname', 'user_sex', 'user_tel', 'user_location', 'isAdmin', 'isBindQQ']


@admin.register(Store)
class Store(admin.ModelAdmin):
    list_display = ['store_id', 'store_name', 'store_user']


@admin.register(ItemCategory)
class ItemCategory(admin.ModelAdmin):
    list_display = ['item_category_id', 'item_category']


@admin.register(ItemSubCategory)
class ItemSubCategory(admin.ModelAdmin):
    list_display = ['item_subCategory_id', 'item_subCategory', 'item_subCategory_image']


@admin.register(Item)
class Item(admin.ModelAdmin):
    list_display = ['item_id', 'item_name', 'item_price', 'item_stoke', 'item_category', 'item_sub_category', 'item_sale']


@admin.register(ShoppingCart)
class ShoppingCart(admin.ModelAdmin):
    list_display = ['cart_id', 'cart_user', 'cart_item']


@admin.register(Trade)
class Trade(admin.ModelAdmin):
    list_display = ['trade_id', 'trade_user', 'trade_item', 'trade_amount', 'trade_store']


@admin.register(Group)
class Group(admin.ModelAdmin):
    list_display = ['group_id', 'group_name', 'group_info', 'group_create_time']


@admin.register(StoreFollow)
class StoreFollow(admin.ModelAdmin):
    list_display = ['follow_id', 'follow_user', 'follow_store']


@admin.register(ItemFollow)
class ItemFollow(admin.ModelAdmin):
    list_display = ['follow_id', 'follow_user', 'follow_item']


@admin.register(GroupFollow)
class GroupFollow(admin.ModelAdmin):
    list_display = ['follow_id', 'follow_group', 'follow_user']


@admin.register(Banner)
class Banner(admin.ModelAdmin):
    list_display = ['banner_id', 'banner_number', 'banner_image']


@admin.register(SystemMessage)
class SystemMessage(admin.ModelAdmin):
    list_display = ['message_id', 'message_content', 'message_type', 'message_create_time']

@admin.register(ChatMessage)
class ChatMessage(admin.ModelAdmin):
    list_display = ['message_id', 'message_type', 'message_ori', 'message_rec', 'message_create_time', 'message_content', 'message_group']


@admin.register(Coupon)
class Coupon(admin.ModelAdmin):
    list_display = ['coupon_id']


@admin.register(DailyOffItem)
class DailyOffItem(admin.ModelAdmin):
    list_display = ['item_id']


@admin.register(ItemDetailImage)
class ItemDetailImage(admin.ModelAdmin):
    list_display = ['image_id']


@admin.register(ItemBannerImage)
class ItemBannerImage(admin.ModelAdmin):
    list_display = ['image_id']


@admin.register(EvaluateImage)
class EvaluateImage(admin.ModelAdmin):
    list_display = ['image_id']


@admin.register(Address)
class Address(admin.ModelAdmin):
    list_display = ['address_id']


@admin.register(Brand)
class Brand(admin.ModelAdmin):
    list_display = ['brand_id']


@admin.register(Collection)
class Collection(admin.ModelAdmin):
    list_display = ['collection_id']


@admin.register(BrowseRecord)
class BrowseRecord(admin.ModelAdmin):
    list_display = ['record_id']


@admin.register(SearchRecord)
class SearchRecord(admin.ModelAdmin):
    list_display = ['record_id']


@admin.register(Evaluate)
class Evaluate(admin.ModelAdmin):
    list_display = ['evaluate_id']