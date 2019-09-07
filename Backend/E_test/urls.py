"""E_test URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/2.2/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
from django.contrib import admin
from django.urls import path, include, re_path
from . import settings
from django.views.static import serve
from backends import views
from django.conf import settings # at
from django.conf.urls.static import static

urlpatterns = [
    path('admin/', admin.site.urls),
    path('', include('api.urls')),
    re_path('media/(?P<path>.*)', serve, {"document_root": settings.MEDIA_ROOT}),
    path('login',views.login),
    path('logout',views.logout),
    path('login.html',views.login),
    # path('login.html',views.show_login2),
    path('register.html',views.register),
    path('register',views.register),

    path('login2.html',views.show_login2),
    path('index.html',views.show_index),
    path('category1.html',views.show_category1),
    path('category2.html',views.show_category2),
    path('category3.html',views.show_category3),
    path('category4.html',views.show_category4),
    path('category5.html',views.show_category5),
    path('category6.html',views.show_category6),
    path('category7.html',views.show_category7),
    path('my_account.html',views.show_my_account),
    path('update_my_account',views.update_my_account),
    path('update_my_password',views.update_my_password),

    path('my_cart.html',views.show_my_cart),
    path('my_change.html',views.show_my_change),
    path('my_comment.html',views.show_my_comment),
    path('my_detail.html',views.show_my_detail),
    path('my_order.html',views.show_my_order),
    path('my_store.html',views.show_my_store),
    path('open_store.html',views.show_open_store),
    path('open_my_store',views.open_my_store),
    path('opened_store.html',views.show_opened_store),

    path('proDetail.html',views.show_proDetail),
    path('proDetail2.html',views.show_proDetail2),
    path('address.html',views.show_address),
    path('add_address',views.add_address),
    path('delete_address',views.delete_address),
    path('pay_it',views.pay_it),

    path('proDetail.html',views.add_item),
    path('add_item',views.add_item),
    path('add_my_store_item',views.add_my_store_item),

    # path('delete_item_in_my_store',views.delete_item_in_my_store),
    path('delete_item_in_cart',views.delete_item_in_cart),

    path('show',views.show),
]