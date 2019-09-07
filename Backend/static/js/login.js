//定义HTTP连接对象
var xmlHttp;

//实例化HTTP连接对象
function createXmlHttpRequest() {
    if(window.XMLHttpRequest) {
        xmlHttp = new XMLHttpRequest();
    } else if(window.ActiveXObject) {
        xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
    }
}

//发起登录请求
function login() {
    createXmlHttpRequest();
    var name = document.getElementById("username").value;
    var password = document.getElementById("password").value;
    if(name == null || name == "") {
        innerHtml("请输入用户名");
        return;
    }
    if(password == null || password == "") {
        innerHtml("请输入密码");
        return;
    }
    var url = "user.php";
    xmlHttp.open("POST", "http://172.23.175.252:8000/api/user/");
    xmlHttp.onreadystatechange = handleResult;
    xmlHttp.setRequestHeader('Content-Type','application/x-www-form-urlencoded');
    xmlHttp.send("action=login&name=" + name + "&psd=" + password);
}

//处理服务器返回的结果/更新页面
function handleResult() {
    if(xmlHttp.readyState == 4 && xmlHttp.status == 200) {
        var response = xmlHttp.responseText;
        var json = eval('(' + response + ')');
        if(json['login_result']) {
            alert("登录成功！");
            //页面跳转
            window.location.href='index.html';
        } else {
            innerHtml("用户名/密码错误");
        }
    }
}

//插入提示语
function innerHtml(message) {
    document.getElementById("tip").innerHTML = "<span style='font-size:12px; color:red;'>" + message + "</span>";
}

