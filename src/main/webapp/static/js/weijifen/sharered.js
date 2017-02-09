

function share(type) {
	var articleId = $('#key_articleId').attr('value');
	var shopId = $('#key_shopId').attr('value');
	 
	$.post("/wx/view/userShare.htm", {
		async : true,
		method : "POST",
		shopId : shopId,
		articleId : articleId,
		type : type
	}, function(data){
		if(type == 1 && data.length >10){
			$('#luckPrizeNum').html(data);
		    $('#luckPrize').show();
		}
	}, "text");

}



function shareAdv(type) {
	var articleId = $('#key_articleId').attr('value');
	var shopId = $('#key_shopId').attr('value');
	 
	$.post("/wx/view/userShareAdv.htm", {
		async : true,
		method : "POST",
		shopId : shopId,
		articleId : articleId,
		type : type
	}, function(data){
		
	}, "text");

}




function loadReply(page) {
	var articleId = $('#key_articleId').attr('value');
	var shopId = $('#key_shopId').attr('value');
	
	var loading = "#js_cmt_loading" + page;
	if(page > 1) {
		var loadmore = "#loadmore" + (page -1);
		$(loadmore).hide();
	}
	
	
	$(loading).show();
	
	$.post("/wx/view/showArticleReply.htm", {
		async : true,
		method : "POST",
		shopId : shopId,
		articleId : articleId,
		page : page
	}, function(data){
		var resultValue = $('#replyContentDiv').html() + data;
		$('#replyContentDiv').html(resultValue);
		$('#replyContentDiv').show();
		$(loading).hide();
	}, "text");

}