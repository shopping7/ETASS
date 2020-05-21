jQuery(document).ready(function() {

    var user_id = getParameter("user_id");
    if(user_id != null){
        jQuery.get("KGC/alterUserInfo",{user_id:user_id},function (userAlter) {
            var lis = '<p><label>用户名</label><span class="field"><input type="text" name="username"  class="smallinput" value="'+userAlter.user.username+'" /></span></p>';
            lis += '<input type="text" name="user_id"  class="smallinput" value='+userAlter.user.user_id+' style="display:none"/>'
            lis += '<p><label>用户属性</label><span class="formwrapper">';
            for (var i = 0; i < userAlter.attrs.length; i++) {
                var attr_lists = '<input type="checkbox"  name="user_attr" value="'+userAlter.attrs[i].attr+'"/>'+userAlter.attrs[i].attr+'<br />'
                lis += attr_lists;
            }

            jQuery("#user_alter_div").append(lis);
            for (var i = 0; i < userAlter.user.attribute.length; i++) {
                var user_attr = userAlter.user.attribute[i];
                console.log(user_attr);
                jQuery('#user_alter_form input[type=checkbox]').each(function () {
                    if (user_attr==jQuery(this).val()) {
                        jQuery(this).attr("checked", "true");
                    }
                });

            }

        });
    }

    jQuery("#user_add_btn").click(function () {
        jQuery.post("KGC/alterUser",jQuery("#user_alter_form").serialize(),function (data) {
            alert("修改成功")
            location.reload();
        })
    });


    function getParameter(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)","i");
        var r = location.search.substr(1).match(reg);
        if (r!=null) return (r[2]); return null;
    }});