layui.define(['echarts','echartsTheme','table'], function(exports) {
	let $ = layui.$
    	,echarts = layui.echarts
    	,table = layui.table;
    
    var echartsHelper = function(){}

    /**
     * options参数
     * 
     * 	ele:ElementID
     * 	title:string
     * 	type:bar|line|pine|gauge
     *  mult:boolean
     * 	data:url或object
     * 	param:object 参数
     * 	style:其它echart支持的参数                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                         
     * 
     */
    echartsHelper.prototype.init = function(options){
    	let me = this;
    	var types = echartsHelper.styles[options.type];
    	if(!types) {
    		console.log('undefined type');
    		return;
    	}
    	
    	if(typeof options.data == "string"){
    		layui.$.get(options.data, options.param, function(res){
    			options.data = res.data;
    			return me.render(options);
    		});
    	}else{
    		return me.render(options);
    	}
    };

    echartsHelper.prototype.render = function(options){
    	if(options.type == 'table'){
    		return this.renderTable(options);
    	}
    	var defaults = echartsHelper.styles[options.type];
    	defaults.title.text=options.title;
    	defaults.type=options.type;
    	defaults.data=options.data;
    	if(options.style) layui.$.extend(defaults,options.style);
    	this.parseData(defaults);
    	
    	var myChart = echarts.init(document.getElementById(options.ele)); //  初始化
    	myChart.setOption(defaults); 
    	return myChart;
    };
    
    echartsHelper.prototype.renderTable = function(options){
    	var defaults = echartsHelper.styles[options.type];
    		defaults.elem = '#'+options.ele;
    		defaults.data = options.data;
    	if(options.style) layui.$.extend(defaults,options.style);
    	return table.render(defaults);
    };

    echartsHelper.prototype.parseData = function(options){
    	if(options.type == 'line' || options.type == 'bar'){
    		if(options.mult){
    			options.legend.data = new Array();
    			options.xAxis.data = new Array();
    			options.series.data = new Array();
    			for(var i=0;i<options.data.length;i++){
    				options.xAxis.data = setsPush(options.xAxis.data,options.data[i]['category']);
    				options.legend.data = setsPush(options.xAxis.data,options.data[i]['title']);
    			}
    			for(var i=0;i<options.legend.data;i++){	
    				var ld = {};
    				ld.name = options.legend.data[i];
    				ld.type = options.type;
    				ld.stack = '总量';
    				ld.data = new Array();
    				for(var i=0;i<options.xAxis.data;i++){	
    					var v = getData(options.data,options.xAxis.data[i],options.legend.data[i]);
    					if(v == null) v=0;
    					ld.data.push(v);
    				}
    			}
    		}else{
        		options.series[0].type = options.type;
    			options.xAxis.data = new Array();
    			options.series[0].data = new Array();
    			for(var i=0;i<options.data.length;i++){
    				setsPush(options.xAxis.data,options.data[i]['title']);
    				setsPush(options.series[0].data,options.data[i]['value']);
    			}
    		}
    	}else if(options.type == 'pie'){
    		options.legend.data = new Array();
    		options.series[0].data = new Array();
    		options.series[0].type = 'pie';
    		for(var i=0;i<options.data.length;i++){
    			options.legend.data.push(options.data[i].title);
    			options.series[0].data.push({value:options.data[i].value,name:options.data[i].title});
    		}
    	}else if(options.type == 'guage'){
    		options.series[0].data = new Array();
    		options.series[0].type = 'guage';
    		for(var i=0;i<options.data.length;i++){
    			options.legend.data.push({value:options.data[i].value,name:options.data[i].title});
    		}
    	}else{
    		
    	}
    };
    
    function setsPush(arr, val){
    	for(var i=0;i<arr.length;i++){
    		//if(arr[i] == val) return;
    	}
    	arr.push(val);
    }

    function getVal(data, category, legend){
    	for(var i=0;i<data.length;i++){
    		if(data[i]['category'] == category && data[i]['title'] == legend) {
    			return data[i]['value'];
    		}
    	}
    	return null;
    }

    /**
     * 基本样式
     */
    echartsHelper.styles = {
    	line : { //拆线图
    		title : { text: '一般折线图',left: 'center'},
    		tooltip : {
    			trigger: 'axis'
    		},
    		toolbox: {
    			show : true,
    			feature : {
    				saveAsImage : {show: true}
    			}
    		},
    		xAxis : 
    			{
    				type : 'category',
    				boundaryGap : true,
    				data : ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    			}
    		,
    		yAxis : [
    			{
    				type : 'value',
    				axisLabel : {
    					formatter: '{value} '
    				}
    			}
    		],
    		series : [{
    				data: [820, 932, 901, 934, 1290, 1330, 1320],
    				type: 'line'
    			}
    		]
    	},
    	bar : { //柱状图
    		title: { text: '一般柱状图',left: 'center'},
    		tooltip: {
    			trigger: 'axis'
    		},
    		toolbox: {
    			show: true,
    			feature: {
    				saveAsImage: { show: true }
    			}
    		},
    		xAxis: 
    			{
    				type: 'category',
    				boundaryGap: true,
    				data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
    			}
    		,
    		yAxis: [
    			{
    				type: 'value',
    				axisLabel: {
    					formatter: '{value} '
    				}
    			}
    		],
    		series: [{
    			data: [820, 932, 901, 934, 1290, 1330, 1320],
    			type: 'bar'
    		}
    		]
    	},
    	pie :{ //饼图
    	    title: {
    	        text: '某站点用户访问来源',
    	        left: 'center'
    	    },
    	    tooltip: {
    	        trigger: 'item',
    	        formatter: '{a} <br/>{b} : {c} ({d}%)'
    	    },
    	    legend: {
    	        orient: 'vertical',
    	        left: 'left',
    	        data: ['直接访问', '邮件营销', '联盟广告', '视频广告', '搜索引擎']
    	    },
    	    series: [
    	        {
    	            name: '访问来源',
    	            type: 'pie',
    	            radius: '55%',
    	            center: ['50%', '60%'],
    	            data: [
    	                {value: 335, name: '直接访问'},
    	                {value: 310, name: '邮件营销'},
    	                {value: 234, name: '联盟广告'},
    	                {value: 135, name: '视频广告'},
    	                {value: 1548, name: '搜索引擎'}
    	            ],
    	            emphasis: {
    	                itemStyle: {
    	                    shadowBlur: 10,
    	                    shadowOffsetX: 0,
    	                    shadowColor: 'rgba(0, 0, 0, 0.5)'
    	                }
    	            }
    	        }
    	    ]
    	},
    		gauge : { // 仪表盘
    		    tooltip: {
    		        formatter: '{a} <br/>{b} : {c}%'
    		    },
    		    toolbox: {
    		        feature: {
    		            restore: {},
    		            saveAsImage: {}
    		        }
    		    },
    		    series: [
    		        {
    		            name: '业务指标',
    		            type: 'gauge',
    		            detail: {formatter: '{value}%'},
    		            data: [{value: 50, name: '完成率'}]
    		        }
    		    ]
    		},
    	table:{
    	    height: 240
    	    ,cols: [[
               {type: "numbers", title: '排序', width: 50}
      	      ,{field: 'title', title: '分类', width:150}
      	      ,{field: 'value', title: '值', width:100}
      	    ]]
    	}
    };
    
	echarts.registerTheme('walden', layui.echartsTheme);
	exports('echartsHelper', new echartsHelper());
});	