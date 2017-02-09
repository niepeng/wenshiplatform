	

function goPage(page){
	$('#tableExtendPerson').html('');
	$('#loading').show();
	var userFatherId = $('#userFatherId').val();
	var shopId = $('#shopId').val();
	
	var type =  $('#type').val();
	$.post("/user/userCenterPage/extendPersonAjax.html", {
		async : true,
		method : "POST",
		shopId:shopId,
		userFatherId:userFatherId,
		page:page,
		type:type
	}, function(data){
		$('#loading').hide();
		$('#tableExtendPerson').html(data);
	}, "text");
}	
	
	function goPageAjax(page ,fatherId){
		$('#userFatherId').val(fatherId);
		$('#type').val(2);
		goPage(page);
	}

	
	function getCheckCode(){
		var shopId = $('#shopId').val();
		var phoneNum =  $('#phoneNum').val();
		if(phoneNum.length !=11){
		   alert("手机号为11位0~9数字，请核对手机号！");
		   return ;	
		}
		
		
		$.post("/user/userCenterPage/getPhoneCode.html", {
			async : true,
			method : "POST",
			shopId:shopId,
			phoneNum:phoneNum
		}, function(data){
			
		}, "text");
		
//		 $('#showCode').attr("disabled",true);
//		 $('#showCode').attr("style","border:1px solid rgb(240,240,240);background: rgb(240,240,240)  ;color:#9E9E9E");
//		 $('#showCode').val("s后可重新获取");
		 
		 settime( document.getElementById('showCode'));
		 
		 
	}
	
	

var countdown = 60;
function settime(val) {
	if (countdown == 0) {
		val.removeAttribute("disabled");
		$('#showCode').attr("style","");
		val.value = "免费获取验证码";
		countdown = 60;
		return ;
	} else {
		val.setAttribute("disabled", true);
		$('#showCode').attr("style","border:1px solid rgb(240,240,240);background: rgb(240,240,240)  ;color:#9E9E9E");
		val.value = "重新发送(" + countdown + "s)";
		countdown--;
	}
	setTimeout(function() {
		settime(val);
	}, 1000);
} 
	
	