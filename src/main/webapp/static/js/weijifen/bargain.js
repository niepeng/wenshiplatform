

$(".mod_detail_hd").click(function(){
	var val=$(this).parent().attr("class");
	if(val.indexOf("open",0) >= 0) {
		$(this).parent().removeClass('open');
	} else {
		$(this).parent().addClass('open');
	}
});

