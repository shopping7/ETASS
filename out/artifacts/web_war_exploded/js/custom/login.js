jQuery(document).ready(function() {

    jQuery('#btn_sub').click(function () {
        //2.发送ajax请求，提交表单数据
        jQuery.post("user/login", jQuery("#loginForm").serialize(), function (data) {
            //data : {flag:false,errorMsg:''}
            if (data.flag) {
                //登录成功
                location.href = "upload_file.html";
            } else {
                //登录失败
                jQuery("#errorMsg").html(data.errorMsg);
            }
        });
    });
});