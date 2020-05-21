/*
 * 	Additional function for forms.html
 *	Written by ThemePixels	
 *	http://themepixels.com/
 *
 *	Copyright (c) 2012 ThemePixels (http://themepixels.com)
 *	
 *	Built for Amanda Premium Responsive Admin Template
 *  http://themeforest.net/category/site-templates/admin-templates
 */

jQuery(document).ready(function(){
	
	///// FORM TRANSFORMATION /////
	jQuery('input:checkbox, input:radio, select.uniformselect, input:file').uniform();


	///// DUAL BOX /////
	var db = jQuery('#dualselect').find('.ds_arrow .arrow');	//get arrows of dual select
	var sel1 = jQuery('#dualselect select:first-child');		//get first select element
	var sel2 = jQuery('#dualselect select:last-child');			//get second select element
	
	sel2.empty(); //empty it first from dom.
	
	db.click(function(){
		var t = (jQuery(this).hasClass('ds_prev'))? 0 : 1;	// 0 if arrow prev otherwise arrow next
		if(t) {
			sel1.find('option').each(function(){
				if(jQuery(this).is(':selected')) {
					jQuery(this).attr('selected',false);
					var op = sel2.find('option:first-child');
					sel2.append(jQuery(this));
				}
			});	
		} else {
			sel2.find('option').each(function(){
				if(jQuery(this).is(':selected')) {
					jQuery(this).attr('selected',false);
					sel1.append(jQuery(this));
				}
			});		
		}
	});
	
	
	
	///// FORM VALIDATION /////
	jQuery("#form1").validate({
		rules: {
			firstname: "required",
			lastname: "required",
			email: {
				required: true,
				email: true,
			},
			location: "required",
			selection: "required"
		},
		messages: {
			firstname: "Please enter your first name",
			lastname: "Please enter your last name",
			email: "Please enter a valid email address",
			location: "Please enter your location"
		}
	});
	
	
	///// TAG INPUT /////
	
	jQuery('#download_kw').tagsInput();
	
	jQuery('#upload_kw').tagsInput();

	
	///// SPINNER /////
	
	jQuery("#spinner").spinner({min: 0, max: 100, increment: 2});
	
	
	///// CHARACTER COUNTER /////
	
	jQuery("#textarea2").charCount({
		allowed: 120,		
		warning: 20,
		counterText: 'Characters left: '	
	});
	
	
	///// SELECT WITH SEARCH /////
	jQuery(".chzn-select").chosen();

	// jQuery("#upload_btn").click(function () {
	//
	// 	// formData.append("file",$("#upload_file").file[0]);
	// 	var form = jQuery("#upload_file_form");
	// 	var formData = new FormData($("#upload_file_form")[0]);
	// 	$.ajax({
	// 		type: "POST",
	// 		data:formData,
	// 		url: "/uploadFile",
	// 		contentType: false,
	// 		processData: false,
	// 		success:function (data) {
	// 			alert("上传成功")
	// 		},
	// 		error: function (data) {
	// 			alert("上传失败！")
	// 		}
	// 	})
	// });

	jQuery("iframe[name=iframeContent]").on("load", function() {
		// var responseText = jQuery("iframe")[0].contentDocument.body.getElementsByTagName("pre")[0].innerHTML;
		// alert(responseText);
		//以下就可以判断并处理返回值
	});



	jQuery.get("user/getUserAttr",{},function (data) {
		jQuery("#user_alter").val(data.username);
		jQuery("#user_Attr").html(data.attrs);
		// var lis = '<p><label>用户名</label><span class="field"><input type="text" placeholder='+data.username+'   name="username" id="username_alter" class="smallinput" /></span></p>';
		// lis += '<p><label>用户属性</label><span class="field">'+data.attrs+'</span></p>';
		// lis += '<p class="stdformbutton"><button class="submit radius2" id="user_alter_btn">提交</button></p>';
		// jQuery('#user_alter_form').append(lis);
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

	})
});