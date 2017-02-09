

function lookVisitDetail(shopId ,userRecordId){
	$.post("/userManager/promotion/recordVisitDetail.html",{
		async : true,
		method : "POST",
		shopId: shopId,
		userRecordId: userRecordId
	},function(data){
		$('#dialogDetail').html(data);
		$('#dialogDetail').show();
	}, "text");
}



function checkVisitCode(shopId, visitCode) {
	$.post("/wx/record/checkVisitCode.html",{
		async : true,
		method : "POST",
		shopId: shopId,
		visitCode: visitCode
	},function(data){
		if(data == "1") {
			alert("当前提取码已被使用，请更换");
			return;
		} else {
			upload();
		}
	}, "text");
}


function sharefriendsCallback() {
	var shopId = $('#shopId').attr('value');
	$.post("/wx/record/sharefriendsCallback.htm", {
		async : true,
		method : "POST",
		shopId : shopId
	}, function(data){
		// $("#codelevel").css("display","block");
		// document.getElementById("showcode").innerHTML = data;
	}, "text");
}
