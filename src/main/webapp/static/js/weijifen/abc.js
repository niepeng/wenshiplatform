(function(){
/*
    注意：$.mggScrollImg返回的scrollImg对象上有
            next，prev，go三个方法，可以实现外部对滚动索引的控制。
    如：scrollImg.next();//会切换到下一张图片
        scrollImg.go(0);//会切换到第一张图片
*/
var scrollImg = $.mggScrollImg('.imgbox ul',{
    loop : true,//循环切换
    auto : true,//自动切换
    callback : function(ind){//这里传过来的是索引值
        $('#page').text(ind+1);
    }
});
})()