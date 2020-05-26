jQuery(document).ready(function(){

    jQuery('#btn_sub').click(function () {
        //2.发送ajax请求，提交表单数据
        jQuery.post("user/login",jQuery("#loginForm").serialize(),function (data) {
            //data : {flag:false,errorMsg:''}
            if(data.flag){
                //登录成功
                location.href="upload_file.html";
            }else{
                //登录失败
                jQuery("#errorMsg").html(data.errorMsg);
            }
        });
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

    jQuery.post("user/getPKAndSK",{},function () {
        
    })
    
    jQuery("#getSK").click(function () {
        jQuery.post("user/getSK",{},function (data) {

        })
    });
    jQuery("#getPK").click(function () {
        jQuery.post("user/getPK",{},function (data) {

        })
    });
    jQuery("#getTheta_id").click(function () {
        jQuery.post("user/getTheta_id",{},function (data) {

        })
    });

    jQuery("#upload_file_form").ajaxForm(function (data) {
        alert(data)
    });

});
