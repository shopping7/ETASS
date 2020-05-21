jQuery(document).ready(function() {

    jQuery.get("KGC/getAllAttr",{},function (attrs) {

        //attr.html里显示所有属性
        var lis = '<tbody>';

        //user_add.html里显示所有属性
        var attr_checkbox_list = '<span class="formwrapper">';
        // 遍历数组，拼接字符串<li>
        for(var i = 0; i<attrs.length; i++){
            var attr_href = "KGC/deleteAttr?id="+attrs[i].id;
            var li = '<tr><td>'+attrs[i].attr+'</td><td class="center"> <a href='+attr_href+' class="delete">删除</a></td></tr>';
            lis += li;

            //<input type="checkbox" name="attr" /> Unchecked Checkbox<br />
            var attr_checkbox = "<input type='checkbox' value="+attrs[i].id+"  name='attribute'/>"+attrs[i].attr+"<br />";
            attr_checkbox_list += attr_checkbox

        }
        //拼接收藏排行榜的li，<li><a href="favoriterank.html">收藏排行榜</a></li>
        lis += '</tbody>'
        attr_checkbox_list += '</span>';
        //将字符串设置到ul的html内容中
        jQuery('#attr_table').append(lis);
        jQuery("#attr_checkbox").append(attr_checkbox_list);
        // jQuery("#attr_checkbox_alter").append(attr_checkbox_list);

    });


    // $("#confirm").click(function(){
    //     MyAlert(myConfirm);
    // });

    jQuery('#attr_add_btn').click(function () {
        jQuery.post('KGC/addAttr',jQuery('#attr_add_form').serialize(),function () {
            alert("添加成功");
            location.href("attr.html");
        });
    });

    jQuery.get('KGC/getAllUser',{},function (data) {
    // <tr>
    //         <td>123</td>
    //         <td>user</td>
    //         <td>医生 心血管 主任</td>
    //     <td class="center"><a href="" class="edit">Edit</a> &nbsp; <a href="" class="delete">Delete</a></td>
    //     </tr>
        var lis = '<tbody>';
        // 遍历数组，拼接字符串<li>
        for(var i = 0; i<data.length; i++){
            var user_del_href = "KGC/deleteUser?user_id="+data[i].user_id;
            var user_alt_href = "user_alter.html?user_id="+data[i].user_id;
            var li = '<tr><td>'+data[i].user_id+'</td><td>'+data[i].username+'</td><td>'+data[i].attrs+'</td><td class="center"><a href='+user_alt_href+' class="edit">修改</a> &nbsp; <a href='+user_del_href+' class="delete">删除</a></td></tr>';
            lis += li;
        }
        //拼接收藏排行榜的li，<li><a href="favoriterank.html">收藏排行榜</a></li>
        lis += '</tbody>'
        //将字符串设置到ul的html内容中
        jQuery('#user_attr').append(lis);
    });


    jQuery('#user_add_btn').click(function () {
        jQuery.ajax({
            //几个参数需要注意一下
            type: "POST",//方法类型
            dataType: "json",//预期服务器返回的数据类型
            url: "KGC/addUser" ,//url
            data: jQuery("#user_add_form").serialize(),
            success: function (data) {
                console.log("SUCCESS");//打印服务端返回的数据(调试用)
                alert(data.successMsg);
            },
            error : function(data) {
                alert(data.errorMsg);
            }
        });
    });



    jQuery('#system_btn').click(function () {
        jQuery.post('KGC/updateSystem',{},function () {
            alert("更新成功");
        });
    });



})