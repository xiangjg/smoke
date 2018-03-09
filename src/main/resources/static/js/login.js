$(function(){
    $("#loginp").click(function () {
        login();
    });

    document.onkeyup = function (e) {//按键信息对象以函数参数的形式传递进来了，就是那个e
        var code = e.charCode || e.keyCode;  //取出按键信息中的按键代码(大部分浏览器通过keyCode属性获取按键代码，但少部分浏览器使用的却是charCode)
        if (code == 13) {
            login();
        }
    }

    $("#pwd").keydown(function (e) {
        if(e.keyCode==13)
        {
            login();
        }
    });

    var login = function () {
        var userId = $("#userName").val();
        var password = $("#pwd").val();
        $.ajax({
            url:"/toLogin",
            data:{userid:userId,password:password},
            dataType:"json",
            type:"post",
            success: function (result) {
                if(result.code==0){
                    window.location.href = "index";
                }else{
                    layer.msg(result.message);
                }
            }
            /*            error:function(XMLHttpRequest, textStatus, errorThrown){
                             alert(XMLHttpRequest.status);
                             alert(XMLHttpRequest.readyState);
                             alert(textStatus);
                         }*/
        });
    }
    var height = document.documentElement.clientHeight;
    var width = document.documentElement.clientWidth;
    // $("img[name='background']").css("height",height);
    // $("img[name='background']").css("width",width);
    var left = (width - 400) / 2;
    $("#main-container").css("left",left + "px");

});
