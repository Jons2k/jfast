layui.define(['layer', 'form', 'table'], function (exports) {
    "use strict";

    let $ = layui.$
        , form = layui.form;

    var cache = {};
    var select = function($ele){
    	$.ajaxSettings.async = false;
    };

    select.prototype.init = function ($ele) {
    	var options = {
  	      textField: 'title',
	      idField: 'id'
    	};
    	if($ele.attr('textField')) options.textField = $ele.attr('textField');
    	if($ele.attr('idField')) options.idField = $ele.attr('idField');
    	
    	var html = '';
    	$ele.empty();
    	buildOptions($ele);
    	initEvent($ele); 
    	
    	function buildOptions ($ele) {
        	var html = '';
        	var title = $ele.attr('title');
        	if(title){
        		html += '<option value="">'+title+'</option>';
        	}else{
        		html += '<option value="">请选择</option>';        		
        	}
        	var url = $ele.attr('url');
        	var depend = $ele.attr('depend');
        	if(depend){
        		var $d = $(depend);
        		if($d.length >0 && $d.val()) {
        			var dn = $d.attr('name');
            		if(url.indexOf('?')== -1) url += '?';
            		url += '&'+dn+'='+$d.val();
        		}        		
        	}
        	$.get(url,function(res){
        		if(res.code == '0'){
        			html += addOptions(res.data,0,$ele.attr('value'));
        			$ele.html(html);
        			form.render('select');
        			cache[$ele.attr('name')] = res.data;
        		}else{
   	      			 layer.msg(res.msg);
   	      			 if(res.url) window.location = res.url;
  				}
        	});
        }

       function addOptions(data,level,value){
        	var str = '';
        	for(var i=0;i<data.length;i++){
        		str += '<option value="'+data[i][options.idField]+'"';
        		if(value && value == data[i][options.idField]) str += ' selected '
        		str += '>'+ titles(data[i][options.textField], level) +'</options>';
        		if(data[i].children){
        			str += addOptions(data[i].children, level+1, value);
        		}
        	}
        	return str;
        }
        
        function initEvent($ele){
        	form.on('select('+$ele.attr('lay-filter')+')', function(data){
        		var affect = $ele.attr('affect');
        		var name = $ele.attr('name');
        		if(affect){
        			new select().init($(affect));
        		}
        		var sel = null;
    			var cached = cache[name];
    			for(var i=0;i<cached.length;i++){
    				if(cached[i][options.idField] == data.value){
    					sel = cached[i];
    				}
    			}

    			var sets = {};
        		var $form = outForm($ele);
        		$form.find('input[relation='+name+']').each(function(){
        			var field = $(this).attr('field');
        			sets[$(this).attr('name')] = sel[field]
        		});
        		$form.find('select[relation='+name+']').each(function(){
        			var field = $(this).attr('field');
        			sets[$(this).attr('name')] = sel[field]
        		});
        		form.val($form.attr('lay-filter'),sets);
        		form.render('select');
      		}); 
        }
    };
        
    function titles(title, level){
    	if(level == 0) return title;
    	else{
    		for(var i=0;i<level;i++){
    			title = '--'+title;
    		}
    		return '|'+title;
    	}
    }
    function outForm($ele){
    	return $ele.closest('form');
    }
    function merge(url, data){
    	if(url.indexOf('?')== -1) url += '?';
    	for(var k in data){
    		url += '&'+k+'='+data[k];
    	}
    	return url;
    }
    //暴露接口
    exports('select', new select());
});