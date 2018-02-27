$(function () {
    var height = window.screen.availHeight - 49 - 40;
    //document.getElementsByName('iframe-page-content').style.height = height + 'px';
    $("iframe[name='iframe-page-content']").height(height);

    console.log(window.screen.availHeight);
    console.log(document.body.clientHeight );
    console.log(document.body.offsetHeight);
    console.log(document.body.scrollHeight );
    console.log(window.screen.height);
})

function checkSession() {
    $.get("getUser", function (data) {
        if (!data) {
            window.location.href = "/login";
            return;
        }
    })
}

//点击菜单触发
function menuClick(menuid) {
    var menuName = $("#" + menuid + "").find("a").text();
    //1.切换样式，现实
    $("#indexShow").text(menuName);
    $("#menuPage li").removeClass("active");
    //$("#menuPage li").removeClass("open")
    $("#" + menuid + "").addClass("active");
    var html = $("#" + menuid + "").find("a").attr("data");
    $("iframe[name='iframe-page-content']").attr("src", html);
    $("iframe[name='iframe-page-content']").show();
    $("div[name='main-content']").hide();
}
function toView(html) {
    $("#menuPage li").removeClass("active");
    //$("#menuPage li").removeClass("open")
    $("iframe[name='iframe-page-content']").attr("src", html);
    $("iframe[name='iframe-page-content']").show();
    $("div[name='main-content']").hide();
    $("#indexShow").text("月报审核");
    //TODO 先把菜单ID 写死，后边再来改成动态获取ID
    /*  $("#menuPage li").removeClass("active open");
      $("#14").addClass("active open");
      $("#16").addClass("active");*/
}

var openChangePwd = function () {
    checkSession();
    layer.open({
        title: "修改密码",
        type: 1,
        btn: ['提交'],
        area: ['300px', '500px'],
        content: $("#changePwd"),
        scrollbar: false,
        yes: function (index) {
            var pwd = $("#user_old_password").val();
            if(pwd != null && pwd !=""){
                $.get("/user/getUserInfo",{pwd:pwd},function (data) {
                    if(data&&data.code == 0){
                        var new_pwd = $("#user_new_password").val();
                        var new_repwd = $("#user_new_repassword").val();
                        if(new_pwd == new_repwd)
                            changePassword(new_pwd,index);
                        else
                            $("#user_new_repassword").tooltip('show');
                    }
                    else{
                        $("#user_old_password").tooltip('show');
                    }
                })
            }
        }
    });
}

var changePassword = function (new_pwd,index) {
    $.post("/user/update/pwd",{password:new_pwd},function (data) {
        layer.close(index);
        if(data&&data.code == 0)

            layer.msg("修改成功");
        else
            layer.msg(data.message);
    })
}