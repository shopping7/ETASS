jQuery(document).ready(function(){

    $('#btn_sub').click(function () {
        //2.发送ajax请求，提交表单数据
        $.post("user/login",$("#loginForm").serialize(),function (data) {
            //data : {flag:false,errorMsg:''}
            if(data.flag){
                //登录成功
                location.href="upload_file.html";
            }else{
                //登录失败
                $("#errorMsg").html(data.errorMsg);
            }
        });
    });

    jQuery("iframe[name=iframeContent]").on("load", function() {
        // var responseText = jQuery("iframe")[0].contentDocument.body.getElementsByTagName("pre")[0].innerHTML;
        // alert(responseText);
        //以下就可以判断并处理返回值
    });

    jQuery("#logout").click(function () {
        jQuery.post("user/logout",{},function () {

        })
    });


    jQuery.get("user/getUserAttr",{},function (data) {
        jQuery("#user_alter").val(data.username);
        jQuery("#user_Attr").html(data.attrs);
    });

    jQuery("#user_alter_btn").click(function () {
        var username = jQuery('#user_alter').val();
        if(username != null){
            jQuery.ajax({
                //几个参数需要注意一下
                type: "POST",//方法类型
                dataType: "json",//预期服务器返回的数据类型
                url: "user/userEdit" ,//url
                data: {username:username},
                success: function (data) {
                    console.log("SUCCESS");
                    alert(data.successMsg);
                },
                error : function(data) {
                    alert(data.errorMsg);
                }
            });
        }else{
            alert("用户名不能为空");
        }

    });

});
