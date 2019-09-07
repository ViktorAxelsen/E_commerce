from django.conf.urls import include, url
from rest_framework import routers
from api import views
from django.urls import path

route = routers.DefaultRouter()

route.register(r'user', views.UserViewSet)
route.register(r'store', views.StoreViewSet)
route.register(r'itemCategory', views.ItemCategoryViewSet)
route.register(r'itemSubCategory', views.ItemSubCategoryViewSet)
route.register(r'item', views.ItemViewSet)
route.register(r'shoppingCart', views.ShppingCartViewSet)
route.register(r'trade', views.TradeViewSet)
route.register(r'group', views.GroupViewSet)
route.register(r'storeFollow', views.StoreFollowViewSet)
route.register(r'itemFollow', views.ItemFollowViewSet)
route.register(r'groupFollow', views.GroupFollowViewSet)
route.register(r'banner', views.BannerViewSet)
route.register(r'systemMessage', views.SystemMessageViewSet)
route.register(r'chatMessage', views.ChatMessageViewSet)
route.register(r'coupon', views.CouponViewSet)
route.register(r'dailyOffItem', views.DailyOffItemViewSet)
route.register(r'itemDetailImage', views.ItemDetailImageViewSet)
route.register(r'itemBannerImage', views.ItemBannerImageViewSet)
route.register(r'evaluateImage', views.EvaluateImageViewSet)
route.register(r'address', views.AddressViewSet)
route.register(r'brand', views.BrandViewSet)
route.register(r'collection', views.CollectionViewSet)
route.register(r'browseRecord', views.BrowseRecordViewSet)
route.register(r'searchRecord', views.SearchRecordViewSet)
route.register(r'evaluate', views.EvaluateViewSet)

urlpatterns = [
    url('api/', include(route.urls)),
    path('api/get_user_cart_item/<int:pk>', views.get_user_cart_item),
    path('api/get_user_store_follow/<int:pk>', views.get_user_store_follow),
    path('api/get_user_item_follow/<int:pk>', views.get_user_item_follow),
    path('api/item_detail_image/<int:pk>', views.item_detail_image),
    path('api/item_banner_image/<int:pk>', views.item_banner_image),
    path('api/get_user_coupon/<int:pk>', views.get_user_coupon),
    path('api/delete_user_collection/', views.delete_user_collection),
    path('api/get_user_collection/<int:pk>', views.get_user_collection),
    path('api/browse_item/', views.browse_item),
    path('api/get_store_evaluate/<int:pk>', views.get_store_evaluate),
    path('api/search_content/<int:pk>/', views.search_content),
    path('api/whether_user_collect_item/', views.whether_user_collect_item),
    path('api/evaluate_image/<int:pk>', views.evaluate_image),
    path('api/get_item_evaluate_amount/<int:pk>', views.get_item_evaluate_amount),
    path('api/get_item_evaluate_info/<int:pk>', views.get_item_evaluate_info),
    path('api/whether_user_buy_item_in_store/', views.whether_user_buy_item_in_store),
    path('api/get_item_collection_amount/<int:pk>', views.get_item_collection_amount),
    path('api/get_recommend_item/<int:pk>', views.get_recommend_item),
    path('api/get_evaluate_type/', views.get_evaluate_type),
    path('api/get_wait_receive/', views.get_wait_receive),
    path('api/get_wait_evaluate/', views.get_wait_evaluate),
    path('api/get_complete_trade/', views.get_complete_trade),
    path('api/buy_now/', views.buy_now),
    path('api/add_into_cart/', views.add_into_cart),
    path('api/buy_in_cart/', views.buy_in_cart),
    path('api/confirm_receive/<int:pk>', views.confirm_receive),
    path('api/get_history_item/<int:pk>', views.get_history_item),
    path('api/get_user_store_info/<int:pk>', views.get_user_store_info),
    path('api/update_user_address/', views.update_user_address),
    path('api/upload_user_head_image/<int:pk>', views.upload_user_head_image),
    path('api/upload_item_preview_image/<int:pk>', views.upload_item_preview_image),
]