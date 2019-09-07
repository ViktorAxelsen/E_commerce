
from django.shortcuts import render

# Create your views here.
from backends import models
# from backends.models import login
from django.shortcuts import HttpResponse
from django.shortcuts import redirect
from backends.models import *

import sqlite3






def show(request):
    if request.method == "GET":
        return render(request,"index.html")
    else:
        item_name = request.POST.get("item_name")
        item_price = request.POST.get("item_price")
        item_stoke = request.POST.get("item_stoke")

        temp.objects.create(item_name = item_name,item_price = item_price,item_stoke=item_stoke)

        return redirect("proDetail.html")


def login(request):
    if request.method == "GET":
        return render(request, "login.html")
    else:
    #   str = request.POST.get("user_name")
    #   Log.objects.create(log_name=str)
    #   return redirect("index.html")
        strs = request.POST.get("user_nickname")
        lists = User.objects.all()
        length = len(lists)
        for i in range(0,length):
            if lists[i].user_nickname == strs:
                Log.objects.create(log_name=strs)
                return redirect("index.html")

        return redirect("register.html")


def logout(request):
    Log.objects.all().delete()
    return render(request,"login.html")

def register(request):
    if request.method == "GET" :
        return render(request,"register.html")
    else:
        # user_name = request.POST.get("user_name")
        # user_sex = request.POST.get("user_sex")
        # user_tel = request.POST.get("user_tel")
        # Userss.objects.create(user_name=user_name,user_tel=user_tel,user_sex=user_sex)
        # return redirect("login.html")
        user_nickname = request.POST.get("user_nickname")
        if user_nickname =="":
            return redirect("register.html")
        user_sex = request.POST.get("user_sex")
        user_tel = request.POST.get("user_tel")
        User.objects.create(user_nickname=user_nickname,user_sex = user_sex,user_tel=user_tel)
        return redirect("login.html")

def show_login2(request):
    return render(request,"login2.html")

def show_index(request):
    users = Log.objects.all()
    return render(request,"index.html",{"users":users})


def show_category1(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].item_store_id == store.store_id:
            sub = ItemSubCategory.objects.get(item_subCategory_id=lists[i].item_sub_category_id)
            if sub.item_subCategory=="食品":
                items.append(lists[i])
    return render(request, "category1.html", {"items": items, "users": users})

def show_category2(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].item_store_id == store.store_id:
            sub = ItemSubCategory.objects.get(item_subCategory_id=lists[i].item_sub_category_id)
            if sub.item_subCategory=="服装护理":
                items.append(lists[i])
    return render(request, "category2.html", {"items": items, "users": users})

def show_category3(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].item_store_id == store.store_id:
            sub = ItemSubCategory.objects.get(item_subCategory_id=lists[i].item_sub_category_id)
            if sub.item_subCategory=="家居日用":
                items.append(lists[i])
    return render(request, "category3.html", {"items": items, "users": users})

def show_category4(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].item_store_id == store.store_id:
            sub = ItemSubCategory.objects.get(item_subCategory_id=lists[i].item_sub_category_id)
            if sub.item_subCategory=="科技产品":
                items.append(lists[i])
    return render(request, "category4.html", {"items": items, "users": users})

def show_category5(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].item_store_id == store.store_id:
            sub = ItemSubCategory.objects.get(item_subCategory_id=lists[i].item_sub_category_id)
            if sub.item_subCategory=="创意礼品":
                items.append(lists[i])
    return render(request, "category5.html", {"items": items, "users": users})

def show_category6(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].item_store_id == store.store_id:
            sub = ItemSubCategory.objects.get(item_subCategory_id=lists[i].item_sub_category_id)
            if sub.item_subCategory=="学习用品":
                items.append(lists[i])
    return render(request, "category6.html", {"items": items, "users": users})

def show_category7(request):
    users = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    store = Store.objects.get(store_user=user.user_id)
    lists = Item.objects.all()
    items = []
    length = len(lists)
    for i in range(0,length):
        if lists[i].item_store_id == store.store_id:
            items.append(lists[i])
    return render(request,"category7.html",{"items": items,"users":users})



def show_my_account(request):
    its = Log.objects.all()
    lists = User.objects.all()
    tem = Log.objects.all()
    users = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].user_nickname == tem[0].log_name:
            users.append(lists[i])
            return render(request, "my_account.html", {"users": users,"its":its})
    return render(request, "my_account.html", {"users": users,"its":its})

def update_my_password(request):
    its = Log.objects.all()
    if request == "GET":
        return render(request, "my_change.html", {"its": its})
    else:
        u_name = Log.objects.all()

        update_user = User.objects.get(user_nickname=u_name[0].log_name)
        update_password = request.POST.get("user_password")


        update_user.user_password = update_password

        update_user.save()

        return redirect("my_change.html")

def update_my_account(request):
    its = Log.objects.all()
    if request == "GET":
        return render(request,"my_detail.html",{"its":its})
    else:
        u_name = Log.objects.all()
        update_nickname = request.POST.get("user_nickname")
        update_user = User.objects.get(user_nickname=u_name[0].log_name)
        update_sex = request.POST.get("user_sex")
        update_tel = request.POST.get("user_tel")
        update_isAdmin = request.POST.get("user_isAdmin")
        update_isBindQQ = request.POST.get("user_isBindQQ")
        update_isVIP = request.POST.get("user_isVIP")
        update_location = request.POST.get("user_location")

        update_user.user_nickname = update_nickname
        update_user.user_sex = update_sex
        update_user.user_tel = update_tel
        update_user.user_location = update_location
        if update_isAdmin =="是":
            update_user.user_isAdmin = True
        else:
            update_user.user_isAdmin = False
        if update_isBindQQ =="是":
            update_user.user_isBindQQ = True
        else:
            update_user.user_isBindQQ = False
        if update_isVIP =="是":
            update_user.user_isVIP = True
        else:
            update_user.user_isVIP = False
        update_user.save()

        u_log = Log.objects.get(log_name=u_name[0].log_name)
        u_log.log_name = update_nickname
        u_log.save()
        return redirect("my_account.html")


def show_my_cart(request):
    its = Log.objects.all()

    lists = ShoppingCart.objects.all()
    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    carts = []
    length = len(lists)
    for i in range(0,length):
        if lists[i].cart_user_id ==user.user_id:
            items = Item.objects.get(item_id=lists[i].cart_item_id)
            carts.append(items)


    # items = MyCart.objects.all()
    #同时把cart——id也传到前端，否则之后删除时会出问题
    return render(request,"my_cart.html",{"carts": carts,"its":its,"lists":lists})


def show_my_change(request):
    its = Log.objects.all()
    lists = User.objects.all()
    tem = Log.objects.all()
    users = []
    length = len(lists)
    for i in range(0, length):
        if lists[i].user_nickname == tem[0].log_name:
            users.append(lists[i])
            return render(request, "my_change.html", {"users": users, "its": its})
    return render(request, "my_change.html", {"users": users, "its": its})



def show_my_comment(request):
    its = Log.objects.all()
    return render(request,"my_comment.html",{"its":its})

def show_my_detail(request):
    its = Log.objects.all()
    return render(request,"my_detail.html",{"its":its})

def pay_it(request):
    if request =="GET":
        return render(request,"my_cart.html")
    else:
        tem = Log.objects.all()
        user = User.objects.get(user_nickname=tem[0].log_name)
        items = ShoppingCart.objects.filter(cart_user_id=user.user_id)
        #此函数建立在每一种商品不能重复命名的基础上
        length = len(items)
        for i in range(0,length):
            item = Item.objects.get(item_id=items[i].cart_item_id)

            store = Store.objects.get(store_id=item.item_store_id)

            Trade.objects.create(trade_user=user,trade_item=item,trade_store=store)

        ShoppingCart.objects.filter(cart_user_id=user.user_id).delete()
        return redirect("my_order.html")




def show_my_order(request):
    its = Log.objects.all()

    tem = Log.objects.all()
    user = User.objects.get(user_nickname=tem[0].log_name)
    trades = Trade.objects.filter(trade_user_id=user.user_id)
    items = []
    length= len(trades)
    for i in range(0,length):
        items.append(Item.objects.get(item_id=trades[i].trade_item_id))
    # trades = Trade.objects.all()



    return render(request,"my_order.html",{"its":its,"items":items})





def show_my_store(request):
    its = Log.objects.all()
    return render(request,"my_store.html",{"its":its})

def show_open_store(request):
    its = Log.objects.all()
    return render(request, "open_store.html", {"its": its})

def show_proDetail(request):
    its = Log.objects.all()
    temps = temp.objects.all()
    return render(request,"proDetail.html",{"its":its,"temps":temps})

def show_proDetail2(request):
    its = Log.objects.all()
    return render(request,"proDetail2.html",{"its":its})

def show_address(request):
    its = Log.objects.all()
    lists = Address.objects.all()
    tem = Log.objects.all()
    addresses = []
    length = len(lists)
    user = User.objects.get(user_nickname=tem[0].log_name)
    for i in range(0,length):
        #注意此处和下面create添加时一样user的类型要注意直接放入一整个user对象
        if lists[i].address_user == user:
            addresses.append(lists[i])
    return render(request,"address.html",{"its":its,"addresses":addresses})

def show_opened_store(request):
    its = Log.objects.all()
    lists = Store.objects.all()
    tem = Log.objects.all()
    stores = []
    length = len(lists)
    user = User.objects.get(user_nickname=tem[0].log_name)
    for i in range(0,length):
        if lists[i].store_user == user:
            stores.append(lists[i])
            return render(request,"opened_store.html",{"its":its,"stores":stores})
    return render(request, "open_store.html", {"its": its, "stores": stores})

def open_my_store(request):
    if request =="GET":
        return render(request,"open_store.html")
    else:
        store_name = request.POST.get("store_name")
        if store_name == "":
            return redirect("open_store.html")

        store_contact_info = request.POST.get("store_contact_info")
        store_info = request.POST.get("store_info")
        store_evaluate = request.POST.get("store_evaluate")
        tem = Log.objects.all()
        user = User.objects.get(user_nickname=tem[0].log_name)
        Store.objects.create(store_name = store_name,store_user=user,store_contact_info = store_contact_info,store_info = store_info,store_evaluate= store_evaluate)
        return redirect("opened_store.html")


def add_address(request):
    if request== "GET":
        return render(request,"address.html")
    else:
        address_username = request.POST.get("address_username")
        address_tel = request.POST.get("address_tel")
        address_detail = request.POST.get("address_detail")
        address_type = request.POST.get("address_type")
        kk = request.POST.get("address_isDefault")
        if kk =="是":
            address_isDefault = True
        else:
            address_isDefault = False

        tem = Log.objects.all()
        user = User.objects.get(user_nickname=tem[0].log_name)
        address_user = user.user_id #用于与当前登录账户绑定

        db = sqlite3.connect("db.sqlite3")
        cursor = db.cursor()
        cursor.execute("select user_id from backends_user where user_id='%s'" % address_user)
        a = cursor.fetchone()

        Address.objects.create(address_username = address_username,address_tel = address_tel,address_detail=address_detail,address_type=address_type,address_isDefault=address_isDefault,address_user =user)
        return redirect("address.html")

def add_item(request):
    if request =="GET":
        return render(request,"proDetail.html")
    else:

        item_name = request.POST.get("item_name")

        item = Item.objects.get(item_name=item_name)
        tem = Log.objects.all()
        user = User.objects.get(user_nickname=tem[0].log_name)

        # item_price = request.POST.get("item_price")
        item_number = request.POST.get("item_number")

        temp.objects.all().delete()

        # item_store = request.POST.get("item_store")
        ShoppingCart.objects.create(cart_user=user,cart_item=item,cart_item_amount=item_number)

        return redirect("index.html")











def add_my_store_item(request):
    if request == "GET":
        return render(request,"my_store.html")
    else:
        item_name = request.POST.get("item_name")
        if item_name == "":
            return redirect("my_store.html")
        item_price = request.POST.get("item_price")
        item_stoke = request.POST.get("item_stoke")
        item_subcategory = request.POST.get("item_sub_category")
        item_browse = request.POST.get("item_browse")
        item_off = request.POST.get("item_off")
        item_prePrice = request.POST.get("item_prePrice")
        item_sale = request.POST.get("item_sale")
        item_VIPOff = request.POST.get("item_VIPOff")

        if item_VIPOff =="是":
            item_VIPOff = 9
        else:
            item_VIPOff =10

        subCategory = ItemSubCategory.objects.get(item_subCategory=item_subcategory)

        if subCategory=="":
            ItemSubCategory.objects.create(item_subCategory=item_subcategory)
            subCategory =ItemSubCategory.objects.get(item_subCategory=item_subcategory)

        tem = Log.objects.all()
        user = User.objects.get(user_nickname=tem[0].log_name)
        store = Store.objects.get(store_user=user.user_id)

        Item.objects.create(item_name=item_name,item_price=item_price,item_stoke=item_stoke,item_sub_category=subCategory,
                            item_browse=item_browse,item_off=item_off,item_prePrice=item_prePrice,item_sale=item_sale,
                            item_VIPOff=item_VIPOff,item_store=store)
        return redirect("category7.html")
















def delete_address(request):
    delete_id = request.GET['delete_id']
    Address.objects.get(address_id=delete_id).delete()
    return redirect("address.html")

# def delete_item_in_my_store(request):
#     delete_id = request.GET['delete_id']
#     MyStore.objects.get(s_id=delete_id).delete()
#     return redirect("category7.html")

def delete_item_in_cart(request):
    delete_id = request.GET['delete_id']


    lists = ShoppingCart.objects.filter(cart_item_id=delete_id)
    # length = len(lists)
    for i in range(0,1):
        lists[i].delete()
    # MyCart.objects.get(item_id=delete_id).delete()
    # 给数据库中外键加上on delete cascade 就可以直接连锁删了
    # CourseChoice.objects.get(course_id=delete_cid).delete()
    return redirect("my_cart.html")