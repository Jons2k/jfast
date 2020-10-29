layui.define(['layer', 'transfer'], function (exports) {
    "use strict";

    let $ = layui.$
    	, layer = layui.layer
        , transfer = layui.transfer;

    var cache = {};
    var picker = function($ele){
    	
    };

    picker.prototype.init = function ($ele) {
    	var options = {
  	      textField: 'title',
	      idField: 'id',
	      elem: $ele.attr('picker')
    	};
    	if($ele.attr('textField')) options.textField = $ele.attr('textField');
    	if($ele.attr('idField')) options.idField = $ele.attr('idField');
    	options.url = $ele.attr('url');
    	if(!options.url) return;
    	options.values = $('#'+options.elem).val();  
    	
    	openDialog($ele);
    	
    	function openDialog ($ele) {
    		var layerIndex = 0;
        	var dt = {
                title: '请选择',
                type: 1,
                shade: 0.2,
                shadeClose: true,
                area: '500px',
                btnAlign: 'c',
                offset: 't',
                content: '<div id="'+options.elem+'Picker"></div>',
                btn: ['确定','取消'],
                success: function(layero, index){
                	buildTransfer();
                },
    			btn1: function(index, layero){
    				getData();
        			layer.close(layerIndex);		            			
        		},
        		btn2: function(index, layero){
        			layer.close(layerIndex);
        		}
            };
    		layerIndex = layer.open(dt);    		
        }
    	
    	function buildTransfer(){
    		$.get(options.url,function(res){
        		if(res.code == '0'){
        			var data = new Array();
        			for(var i=0;i<res.data.length;i++){
        				data.push({
        					"value": res.data[i][options.idField] //数据值
        					,"title": res.data[i][options.textField] //数据标题
        					,"disabled": res.data[i]['status'] ? false:true  //是否禁用
        					,"checked": false //是否选中
        				});
        			}
        			transfer.render({
        				 elem: '#'+options.elem+'Picker'
        				 ,id: options.elem
        				,data: data
        			    ,title: ['待选择', '已选择']
        				,value: options.values ? options.values.split(',') : []
        			 	,showSearch: true
        			});
        			 
        		}else{
   	      			 layer.msg(res.msg);
   	      			 if(res.url) window.location = res.url;
  				}
        	});
    	}
    	
    	function getData(){
    		var data = transfer.getData(options.elem); 
    		var sv='',st='';
    		for(var i=0;i<data.length;i++){
    			sv += ','+data[i]['value'];
    			st += ','+data[i]['title'];
    		}
    		if(sv.length > 1) sv = sv.substring(1);
    		if(st.length > 1) st = st.substring(1);
    		
			$ele.val(st);
			$('#'+options.elem).val(sv);
    	}
    };
     
    //暴露接口
    exports('picker', new picker());
});