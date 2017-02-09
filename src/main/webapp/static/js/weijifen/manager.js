require(['chart', 'daterangepicker'], function(c) {
		var chart = null;
		var chartDatasets = null;
		var templates = {
			flow1: {
				label: '新关注人数',
				fillColor : "rgba(36,165,222,0.1)",
				strokeColor : "rgba(36,165,222,1)",
				pointColor : "rgba(36,165,222,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(36,165,222,1)",
			},
			flow2: {
				label: '取消关注人数',
				fillColor : "rgba(203,48,48,0.1)",
				strokeColor : "rgba(203,48,48,1)",
				pointColor : "rgba(203,48,48,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(203,48,48,1)",
			},
			flow3: {
				label: '净增人数',
				fillColor : "rgba(149,192,0,0.1)",
				strokeColor : "rgba(149,192,0,1)",
				pointColor : "rgba(149,192,0,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(149,192,0,1)",
			},
			flow4: {
				label: '累计人数',
				fillColor : "rgba(231,160,23,0.1)",
				strokeColor : "rgba(231,160,23,1)",
				pointColor : "rgba(231,160,23,1)",
				pointStrokeColor : "#fff",
				pointHighlightFill : "#fff",
				pointHighlightStroke : "rgba(231,160,23,1)"
			}
		};

		function refreshData() {
			if(!chart || !chartDatasets) {
				return;
			}
			var visables = [];
			var i = 0;
			$('.checkbox input[type="checkbox"]').each(function(){
				if($(this).attr('checked')) {
					visables.push(i);
				}
				i++;
			});
			var ds = [];
			$.each(visables, function(){
				var o = chartDatasets[this];
				ds.push(o);
			});
			chart.datasets = ds;
			chart.update();
		}

		var url = './index.php?c=account&a=summary&acid=26&uniacid=26&#aaaa';
		$.post(url, function(data){
			var data = $.parseJSON(data)
			var datasets = data.datasets;
			if(!chart) {
				var label = data.label;
				var ds = $.extend(true, {}, templates);
				ds.flow1.data = datasets.flow1;
				ds.flow2.data = datasets.flow2;
				ds.flow3.data = datasets.flow3;
				ds.flow4.data = datasets.flow4;
				var lineChartData = {
					labels : label,
					datasets : [ds.flow1, ds.flow2, ds.flow3, ds.flow4]
				};

				var ctx = document.getElementById("myChart1").getContext("2d");
				chart = new Chart(ctx).Line(lineChartData, {
					responsive: true
				});
				chartDatasets = $.extend(true, {}, chart.datasets);
			}
			refreshData();
		});

		$('.checkbox input[type="checkbox"]').on('click', function(){
			$(this).attr('checked', !$(this).attr('checked'))
			refreshData();
		});
	});


require(['chart'], function(c) {
	$('.dropdown').click(function(){
		$('.nav.nav-btns>li').removeClass('active');
		$(this).toggleClass('active');
	});
	
	var myLine = new Chart(document.getElementById("myChart").getContext("2d"));
	var datasets = '';
	var label = '';
	var lineChartData = null;
	var obj = null;
	var day_num = "30";
	var show_url = "./index.php?c=platform&a=reply&do=display&m=";
	var add_url = "./index.php?c=platform&a=reply&do=post&m=";
	var data = null;
	
	$.post(location.href, {'m_name' : 'basic'}, function(data){
		data = $.parseJSON(data);
		
		$("#rule").html(data.stat.rule);
		$("#today").html(data.stat.today);
		$("#month").html(data.stat.month);
		$('#show').attr('href', show_url + data.stat.m_name);
		$('#add').attr('href', add_url + data.stat.m_name);

		lineChartData = {
			labels : data.key,
			datasets : [
				{
					fillColor : "rgba(36,165,222,0.1)",
					strokeColor : "rgba(36,165,222,1)",
					pointColor : "rgba(36,165,222,1)",
					pointStrokeColor : "#fff",
					pointHighlightFill : "#fff",
					pointHighlightStroke : "rgba(36,165,222,1)",
					data : data.value
				}
			]
		}
		 obj = myLine.Line(lineChartData, {responsive: true});
	});
	
	$('.nav.nav-btns li[class!="dropdown"]').on('click', function(){
		$('.nav.nav-btns li').removeClass('active');
		$(this).toggleClass('active');
		var m_name = $(this).attr('id');
		
		$.post(location.href, {'m_name' : m_name}, function(data){
			data = $.parseJSON(data);
			
			$("#rule").html(data.stat.rule);
			$("#today").html(data.stat.today);
			$("#month").html(data.stat.month);
			
			$('#show').attr('href', show_url + data.stat.m_name);
			$('#add').attr('href', add_url + data.stat.m_name);

				 for(var i = 0; i <= day_num; i++) {
					obj.datasets[0].points[i].value = data.value[i];
			 }
				obj.update();
		});
	});
	});


function subscribe(){
	$.post("./index.php?c=utility&a=subscribe&", function(){
		setTimeout(subscribe, 5000);
	});
}
function sync() {
	$.post("./index.php?c=utility&a=sync&", function(){
		setTimeout(sync, 60000);
	});
}
$(function(){
	subscribe();
	sync();
});
			function checknotice() {
		$.post("./index.php?c=utility&a=notice&", {}, function(data){
			var data = $.parseJSON(data);
			$('#notice-container').html(data.notices);
			$('#notice-total').html(data.total);
			if(data.total > 0) {
				$('#notice-total').css('background', '#ff9900');
			} else {
				$('#notice-total').css('background', '');
			}
			setTimeout(checknotice, 60000);
		});
	}
	checknotice();
	
	require(['bootstrap']);
	$('.js-clip').each(function(){
		util.clip(this, $(this).attr('data-url'));
	});