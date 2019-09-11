function register() {
    if (!checkname()) {
        return false;
    } else if (!checkpass()) {
        return false;
    } else if (!checkemail()) {
        return false;
    }
    return true;
}

function checkname() {
    var name = document.getElementById("uName").value;
    var ts = document.getElementById("namets");
    if (name.length < 3 || name.length > 15) {
        ts.innerHTML = "用户名长度须在3-15之间!";
        ts.style.color = "red";
        return false;
    }
    $.post("checkUserName.action", {" userName": name}, function (data) {
        var d = $.parseJSON(data);
        //console.log(d.success);
        if (d.success != true) {
            ts.innerHTML = '用户名已存在!';
            ts.style.color = 'red';
            return true;
        }
    });
    ts.innerHTML = '用户名可以使用！';
    ts.style.color = 'green';
    return true;
}

function checkpass() {
    var userPass = $("#uPass").val();

    var pts = document.getElementById("passts");

    if (userPass.length < 6 || userPass.length > 15) {
        pts.innerHTML = "密码长度须在6-15之间!";
        pts.style.color = "red";
        return false;
    }
    pts.innerHTML = "密码可以使用!";
    pts.style.color = "green";
    return true;
}

function checkrpass() {
    var userPass = $("#uPass").val();
    var userRPass = $("#uRPass").val();
    var prts = document.getElementById("passrts");
    if (userPass != userRPass) {
        prts.innerHTML = "两次密码输入不一致!";
        prts.style.color = "red";
        return false;
    }
    prts.innerHTML = "输入一致!";
    prts.style.color = "green";
    return true;
}

function checkemail() {
    var userEmail = $("#uEmail").val();
    var ets = document.getElementById("emailts");
    if (!isEmail(userEmail)) {
        ets.innerHTML = "邮箱格式不正确!";
        ets.style.color = "red";
        return false;
    }
    ets.innerHTML = "邮箱可以使用!";
    ets.style.color = "green";
    return true;
}

function isEmail(str) {
    var reg = /[a-z0-9-]{1,30}@[a-z0-9-]{1,65}.[a-z]{3}/;
    return reg.test(str);
}