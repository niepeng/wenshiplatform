//设置当前菜单的动作	
function setAction(menuId){
		if($('#menuTitle_'+menuId).val()==''){
			alert("请先给菜单定义名称");
			return ;
		}
		
		
		if(menuId.length == 6){
			if($('#menuTitle_'+menuId).val().length>4){
				alert("一级菜单最多4个汉字");
				return ;
			}
		}else{
			if($('#menuTitle_'+menuId).val().length>6){
				alert("二级菜单最多6个汉字");
				return ;
			}
		}
		
		var type =$('#menuType_'+menuId).val(); 
		if(type == '' || type == undefined){
			type = 'view';
		}
		var content = $('#menuData_'+menuId).val(); 
		if(content == '' || content == undefined){
			if(type == 'view'){
				content = "http://";
			}
		}
		$('#menuId').html('');
		$('#ipt-click').val('');
		$('#ipt-view').val('');
		$('#ipt-'+type).val(content);
		if($('#dialog').is(':hidden')){
		  $('#dialog').show();
		  initRadio(type);
		  radioChange();
		  $('#menuId').html(menuId);
		}
		};
		
		//初始化菜单的动作的值（关键词还是url）	
		function initRadio(type){
			var radios = document.getElementsByName("ipt");
			for( radio in radios) {
			    var	val = radios[radio].value;
			    if(val ==type){
			    	radios[radio].checked =true;
			    }
			}
		}
		
		
		function  radioChange(){
	       var radios = document.getElementsByName("ipt");
           var val;
           for( radio in radios) {
           if(radios[radio].checked) {
               val = radios[radio].value;
			   $('#'+val).show();
           }else{
		    val = radios[radio].value;
			$('#'+val).hide();
		   }
         }
	  }
	  
		//保存菜单的动作
	  function saveAction(){
	    var menuId = $('#menuId').html();
	    var radios = document.getElementsByName("ipt");
		 for( radio in radios) {
           if(radios[radio].checked) {
               var val = radios[radio].value;
			   var value =  $('#ipt-'+val).val();
			   $('#menuType_'+menuId).val(val);
			   $('#menuData_'+menuId).val(value);
           }
		  }
		  $('#dialog').hide();
	  }
	    
//删除当前子菜单
function deleteMenu(menuId){
    $('.'+menuId).val('');
    $('#'+menuId).hide();
    $('.'+menuId).val('');
}
//删除当前一级菜单
function  deleteParentmenu(menuId){
	for(var i = 1; i<=5 ;i++){
		$('.'+menuId+"_"+i).val('');
		$('#'+menuId+"_"+i).hide();
	}
	$('.'+menuId).val('');
}

//添加子菜单
function addSubMenu(menuId){
	for(var i = 1; i<=5 ;i++){
		if($('#'+menuId+"_"+i).is(':hidden')){
			$('#'+menuId+"_"+i).show();
			break;
		}
		if(i == 5){
			alert("子菜单最多为5个");
		}
	}
}


//保存所有菜单上传json到服务器
function saveMenuJson(){
	var wxMenuJson = "";
	for(var i = 1 ;i<=3 ;i++ ){
		var parentTitle = "";
		var title = $('#menuTitle_menu_'+i).val();
		var data = $('#menuData_menu_'+i).val();
		var type = $('#menuType_menu_'+i).val();
		var id = $('#menuId_menu_'+i).val();
		if(title == '' || title == undefined){
			continue;
		}
		if(i!=1){
			wxMenuJson =wxMenuJson+",";
		}
		parentTitle= title;
		if(title.length>4){
			alert("一级菜单最多4个汉字");
			return ;
		}
		
		wxMenuJson =wxMenuJson + "{\"menuName\":\""+title+"\","+"\"menuType\":\""+type+"\","+"\"menuContent\":\""+data+"\",\"parentMenuName\":\""+"-1"+"\",\"parentMenuId\":"+id+"}";
		for(var j = 1 ; j<= 5 ;j++){
			
			 title = $('#menuTitle_menu_'+i+"_"+j).val();
			 data = $('#menuData_menu_'+i+"_"+j).val();
			 type = $('#menuType_menu_'+i+"_"+j).val();
			 if(title == '' || title == undefined){
				continue;
			 }
			 if(title.length>6){
					alert("二级菜单最多6个汉字");
					return ;
			}
			 if(data == '' || data == undefined || type == '' || type == undefined){
				 alert("每个菜单都必须设置好菜单的相应事件，不能为空");
					return ;
			 }
			 wxMenuJson =wxMenuJson+",";
			 wxMenuJson =wxMenuJson + "{\"menuName\":\""+title+"\","+"\"menuType\":\""+type+"\","+"\"menuContent\":\""+data+"\",\"parentMenuName\":\""+parentTitle+"\",\"parentMenuId\":"+id+"}";
		}
	}
	wxMenuJson = "["+wxMenuJson+"]";
	console.log(wxMenuJson);
	var shopId = $('#shopId').val();
	$.post("/userManager/editDefaultMenu.html", {
		async : true,
		method : "POST",
		shopId:shopId,
		wxMenuJson : wxMenuJson
	}, function(data){
		if(data == "true"){
			alert("保存成功");
		}else
		   alert("保存失败");
	}, "text");
}

function  checkFormFrist(){
	  if($('#title').val() == ''){
	    $('#title_message').html("回复规则名称不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#title_message').html("");
	  }
	  
	  if($('#desc').val() == ''){
	    $('#desc_message').html("触发关键字不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#desc_message').html("");
	  }
	  
	  if($('#url').val() == ''){
	    $('#url_message').html("标题不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#url_message').html("");
	  }
	}

function  checkFormSecond(){
	   //品质
	  if($('#qrquality').val() == ''){
	    $('#up_qrquality_message').html("品质不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_qrquality_message').html("");
	  }
	  
	  //关键字
	  if($('#msgkey').val() == ''){
	    $('#up_msgkey_message').html("关键字不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_msgkey_message').html("");
	  }
	  
	  
	  //二维码    上边距  左边距   宽 高
	  
	  if($('#qrcodeLeft').val() == ''){
	    $('#up_qrcodeLeft_message').html("二维码左边距不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_qrcodeLeft_message').html("");
	  }
	  
	  if($('#qrcodeTop').val() == ''){
	    $('#up_qrcodeTop_message').html("二维码上边距不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_qrcodeTop_message').html("");
	  }
	  
	  if($('#qrcodeWidth').val() == ''){
	    $('#up_qrcodeWidth_message').html("二维码宽不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_qrcodeWidth_message').html("");
	  }
	  
	  if($('#qrcodeHeight').val() == ''){
	    $('#up_qrcodeHeight_message').html("二维码高不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_qrcodeHeight_message').html("");
	  }
	  
	  
	  //头像   二维码    上边距  左边距   宽 高
	  
	  if($('#showHead').is(':checked')){//如果选中 则做处理
	  
	   if($('#headLeft').val() == ''){
	    $('#up_headLeft_message').html("头像左边距不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_headLeft_message').html("");
	  }
	  
	  if($('#headTop').val() == ''){
	    $('#up_headTop_message').html("头像上边距不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_headTop_message').html("");
	  }
	  
	  if($('#headWidth').val() == ''){
	    $('#up_headWidth_message').html("头像宽不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_headWidth_message').html("");
	  }
	  
	  if($('#headHeight').val() == ''){
	    $('#up_headHeight_message').html("头像高不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_headHeight_message').html("");
	  }
	 }
	  
	 //名称显示  左边距  上边距  大小  颜色
	   if($('#nameShow').is(':checked')){
	   if($('#nameLeft').val() == ''){
	    $('#up_nameLeft_message').html("姓名左边距不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_nameLeft_message').html("");
	  }
	  
	  if($('#nameTop').val() == ''){
	    $('#up_nameTop_message').html("姓名上边距不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_nameTop_message').html("");
	  }
	  
	  if($('#nameSize').val() == ''){
	    $('#up_nameSize_message').html("姓名宽不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_nameSize_message').html("");
	  }
	  
	  if($('#colorpicker-text').val() == ''){
	    $('#up_colorpicker_message').html("姓名颜色不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_colorpicker_message').html("");
	  }
	   
	  //关注积分  第一次 关注   一级粉丝   二级粉丝
	  
	   if($('#firstFollowScore').val() == ''){
	    $('#up_firstFollowScore_message').html("首页关注积分不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_firstFollowScore_message').html("");
	  }
	  
	  
	   if($('#firstPromotionScore').val() == ''){
	    $('#up_firstPromotionScore_message').html("一级推广奖励积分不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_firstPromotionScore_message').html("");
	  }
	  
	   if($('#secondPromotionScore').val() == ''){
	    $('#up_secondPromotionScore_message').html("二级推广奖励积分不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_secondPromotionScore_message').html("");
	  }
	  
	   if($('#genCodeMsg').val() == ''){
	    $('#up_genCodeMsg_message').html("生成图片文字提示不能为空。");
		document.body.scrollTop=0;
	    return false;
	  }else{
	    $('#up_genCodeMsg_message').html("");
	  }
	  
	   }
	}


function showUserCommissionDetail(userId,shopId){
	$.post("/userManager/youzan/commissionDetail.html", {
		async : true,
		method : "POST",
		shopId:shopId,
		userId : userId
	}, function(data){
		$('#dialogDetail').html(data);
		$('#dialogDetail').show();
	}, "text");
}

function showDetail(userId,firUserId,shopId){
	$.post("/userManager/youzan/commissionRankDetailAjax.html", {
		async : true,
		method : "POST",
		shopId:shopId,
		userId:userId,
		firUserId : firUserId
	}, function(data){
		$('#dialogDetail').html(data);
		$('#dialogDetail').show();
	}, "text");
}

function addConfig(){
	 var userId= $('#user_id_check').val();
	 var  self = $('#self').val();
	 var  derect =   $('#derect').val();
	 var  indrect =  $('#indrect').val();
	 var shopId = $('#shopId').val();
	 
	$.post("/userManager/youzan/addCommissionConfigPersonal.html", {
		async : true,
		method : "POST",
		shopId:shopId,
		userId:userId,
		self:self,
		derect:derect,
		indrect:indrect
	}, function(data){
		if(data == 'true'){
			alert("保存成功");
		}else{
			alert("保存失败");
		}
	}, "text");
}

function lookVisitDetai(articleId ,shareUserId){
	 var shopId = $('#shopId').val();
	$.post("/userManager/promotion/visitDetail.html",{
		async : true,
		method : "POST",
		articleId:articleId,
		shareUserId:shareUserId,
		shopId:shopId
	},function(data){
		$('#dialogDetail').html(data);
		$('#dialogDetail').show();
	}, "text");
}

function goPageVisit(page){
	 var shopId = $('#shopId').val();
	 var articleId = $('#articleId_ajax').val();
	 var shareUserId = $('#shareUserId_ajax').val();
	$.post("/userManager/promotion/visitDetail.html",{
		async : true,
		method : "POST",
		articleId:articleId,
		shareUserId:shareUserId,
		shopId:shopId,
		page:page
	},function(data){
		$('#dialogDetail').html(data);
		$('#dialogDetail').show();
	}, "text");
}


function deleteDOM(index ,id ,shopId){
	$.post("/shop/deleteCart.html",{
		async : true,
		method : "POST",
		id:id,
		shopId:shopId
	},function(data){
		if(data.indexOf('true')!=-1){
			  $('#showlist'+index).remove();
			  $('#editing'+index).remove();
		}else{
			alert('操作异常,删除失败,请刷新页面后尝试!');
		}
	}, "text");
}
