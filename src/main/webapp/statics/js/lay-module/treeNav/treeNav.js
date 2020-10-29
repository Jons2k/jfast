layui.define(['layer', 'tree', 'table'], function (exports) {
    "use strict";

    let $ = layui.$
        , tree = layui.tree
        , table = layui.table;

    var defaults = {
    	elem: "#leftTree",
        listTable:'listTable',
        url: APP.CONTEXT+APP.MODULE+"/"+APP.CONTROLLER+"/left",
        click:function(obj){
        	 //执行搜索重载
            table.reload(defaults.listTable, {
                page: {
                    curr: 1
                }
                , where: {pid:obj.data.id}
            }, 'data');
        }
    };
    
    var treeNav = {
    	init : function (options) {
    
	    	$.extend(defaults, options);
	    	
	    	$.get(defaults.url,function(res){
	    		if(res.code == '0'){
	    			defaults.data = res.data
	    			tree.render(defaults);
	    		}
	    	});
    	}
    };        
    
    //暴露接口
    exports('treeNav', treeNav);
});