function showHideDiv(id){
	$.post("/fanceClub/detailWxPNInfo.html", {
		async : true,
		method : "POST",
		wxPNInfoId:id
	}, function(data){
		$('#howDiv').html(data);
		$('#howDiv').show();
	}, "text");
}