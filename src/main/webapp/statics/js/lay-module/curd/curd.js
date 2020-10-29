layui.define(['layer', 'form', 'select', 'picker', 'table', 'laydate', 'treetable','upload'], function (exports) {
    "use strict";

    let $ = layui.$
        , form = layui.form
        , select = layui.select
        , picker = layui.picker
        , laydate = layui.laydate
        , treetable = layui.treetable
        , upload = layui.upload
        , table = layui.table;
    var defaults = {
    	tableId: "listTable",
    	searchForm: "#searchForm",
    	title: "对象",
    	url: APP.CONTEXT+APP.MODULE+"/"+APP.CONTROLLER+"/lists"
    };
    var curd = {
        init : function(options){
        	$.extend(defaults, options);
        	var listTable = curd.initList(options);
        	curd.initEvent();
        	curd.initForm(defaults.searchForm);
        	return listTable;
        },
        tree : function(options){
        	$.extend(defaults, options);
        	var listTable = curd.initTree(options);
        	curd.initEvent();
        	curd.initForm(defaults.searchForm);
        	return listTable;
        },
        form: function(options){
        	var layerIndex = 0;
        	var dt = {
                title: '',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['100%', '100%'],
                btnAlign: 'c',
                content: '',
                btn: ['确定','取消'],
    			btn1: function(index, layero){
    				var iframeWin = window[layero.find('iframe')[0]['name']];
    				iframeWin.document.getElementById("submitBtn").click();				            			
        		},
        		btn2: function(index, layero){
        			layer.close(layerIndex);
        		}
            };
        	$.extend(dt, options);
    		layerIndex = layer.open(dt);
    		return layerIndex;
        },
        view:function(options){
        	var layerIndex = 0;
        	var dt = {
                title: '',
                type: 2,
                shade: 0.2,
                maxmin:true,
                shadeClose: true,
                area: ['100%', '100%'],
                btnAlign: 'c',
                content: '',
            };
        	$.extend(dt, options);
    		layerIndex = layer.open(dt);
    		return layerIndex;
        },
        frame:function(options){
        	parent.addFrame(options.title,options.content);
        },
        ajax:function(url){
        	layer.confirm('您确定要执行该操作吗?', function(index){
  			  $.get(url,function(res){
  				  if(res.code == '0'){
  	      			 layer.msg(res.msg, {icon: 5});
  	      			 window.actionSuccess();
  				  }else{
   	      			 layer.msg(res.msg);
   	      			 if(res.url) window.location = url;
  				  }
               });
  			  layer.close(index);
        	});	    
        },
        error:function(msg){
        	layer.msg(msg, {icon: 5});
        },
        initList:function(){
        	var listTable = table.render({
                elem: '#'+defaults.tableId,
                url: defaults.url,
                toolbar: '#toolbar',
                defaultToolbar: ['filter', 'exports', 'print', {
                    title: '提示',
                    layEvent: 'LAYTABLE_TIPS',
                    icon: 'layui-icon-tips'
                }],
                cols: defaults.cols,
                limits: [10, 15, 20, 25, 50, 100],
                limit: 15,
                autoSort:false,
                page: true
            });
        	// 监听搜索操作
            form.on('submit(data-search-btn)', function (data) {
                //执行搜索重载
                table.reload(defaults.tableId, {
                    page: {
                        curr: 1
                    }
                    , where: data.field
                }, 'data');

                return false;
            });
            
          //监听排序事件 
            table.on('sort('+defaults.tableId+')', function(obj){
              var param = form.val("searchForm");
              param['orderby'] = obj.field + " "+ (obj.type == null ? 'asc' : obj.type);
              table.reload(defaults.tableId, {
                initSort: obj //记录初始排序，如果不设的话，将无法标记表头的排序状态。
                ,where: param
              });
            });
            
           
            window.actionSuccess = function(){
            	listTable.reload();
            }
            return listTable;
        },
        initTree:function(){
        	treetable.render({
                treeColIndex: 1,
                treeSpid: 0,
                treeIdName: 'id',
                treePidName: 'pid',
                where: defaults.where,
                elem: '#'+defaults.tableId,
                url: defaults.url,
                toolbar: '#toolbar',
                defaultToolbar: ['filter', 'exports', 'print', {
                    title: '提示',
                    layEvent: 'LAYTABLE_TIPS',
                    icon: 'layui-icon-tips'
                }],
                cols: defaults.cols,
                page: false
            }); 
        	
        	// 监听搜索操作
            form.on('submit(data-search-btn)', function (data) {
                //执行搜索重载
            	window.reloadTable(data.field);
                return false;
            });
           
            window.reloadTable = function(where){
            	if(where){
            		defaults.where = where;
            		curd.initTree();
            	}
            }
            window.actionSuccess = function(){
            	window.location.reload();
            }
            window.expandAll = function(){
            	treetable.expandAll('#'+defaults.tableId);
            }
            window.foldAll = function(){
            	treetable.foldAll('#'+defaults.tableId);
            }
        },
        initEvent:function(options){
        	 // 监听工具栏操作
            table.on('toolbar('+defaults.tableId+')', function(obj){
            	var $btn = $(this);
            	var event = obj.event;
            	var layerIndex = 0;
            	var url = $btn.attr('url');
            	
            	if(event.indexOf('_select') > 0){
            		var checkStatus = table.checkStatus(defaults.tableId);
            		if(checkStatus.data.length <1) {
            			curd.error('请至少选择一条数据！');
            			return false;
            		}
            		var ids = '';
                    for(var i=0;i<checkStatus.data.length;i++){
                    	ids += ','+checkStatus.data[i].id;
                    }
                    url = merge(url, {id:ids.substring(1)});
                    event = event.substring(0,event.indexOf('_'));
            	}else if(event.indexOf('_search') > 0){
            		var data = form.val('searchForm');
            		url = merge(url, data);
                    event = event.substring(0,event.indexOf('_'));
            	}
            	
            	
            	switch(event){            	
	            	case 'view': 	   	            		
	            		layerIndex = curd.view({
	            			title: $btn.text(),
	            			content: url
	            		});
	            		break;            		
	            	case 'form':	            		
	            		layerIndex = curd.form({
	            			title: $btn.text(),
	            			content: url
	            		});   
	            		break;  
	            	case 'ajax':
	            		curd.ajax(url);        		 
	            		break;     		
	            	case 'frame':	            		
	            		curd.frame({
	            			title: $btn.text(),
	            			content: url
	            		});
	            		break;
	            	case 'open':
	            		window.open(url);
	            		break;
	            	case 'js':
	                	var fun = $btn.attr('fun');
	                	eval(fun+"();");
	            		break;
            		default:            			
                		break;
            	}
                return false;
            });
            
          //监听工具条 
            table.on('tool('+defaults.tableId+')', function(obj){
              var $btn = $(this);
              var event = obj.event;
              var layerIndex = 0;
              var url = $btn.attr('url');  
              var data = obj.data; //获得当前行数据
              url = merge(url, {id:data.id});
              if(event === 'view'){ //查看
            	  layerIndex = curd.view({
          			title: $btn.text(),
          			content: url
          		});
              } else if(event === 'form'){ //删除
            	  layerIndex = curd.form({
          			title: $btn.text(),
          			content: url
          		});
              } else if(event === 'ajax'){ //编辑
            	  curd.ajax(url);
              } else if(event === 'frame'){
            	  curd.frame({
          			title: $btn.text(),
          			content: url
          		});
              } else if(event === 'open'){
            	  window.open(url);
              }
            });
        },
        initForm:function(formId){
        	var $form = $(formId);
        	$form.find('input[date]').each(function(){
        		var $this=$(this);
        		var type = $this.attr('date');
        		if(!type) type = 'date';
        		
        		var format = $this.attr('format');
        		if(!format) {
        			if(type == 'date') format = 'yyyy-MM-dd'
        			else if(type == 'datetime') format = 'yyyy-MM-dd HH:mm:ss';
        			else if(type == 'month') format = 'yyyy-MM';
        			else if(type == 'year') format = 'yyyy';
        			else if(type == 'time') format = 'HH:mm:ss';
        		}
        		laydate.render({ 
				  elem: $this[0]
				  ,type: type
				  ,trigger: 'click'
				  ,format:format  
				});
        	});
        	$form.find('select[url]').each(function(){
        		select.init($(this));
        	});        	

        	$form.find('input[picker]').each(function(){
        		var $ele = $(this);
        		$ele.on('click',function(){
        			picker.init($ele);
        		});        		
        	});

        	$form.find('input[file]').each(function(){
        		var $this=$(this);
        		var type = $this.attr('file');
        		var exts = $this.attr('exts');
        		var id = $this.attr('id');
        		if(!exts) {
        			if(type == 'excel') exts = 'xls|xlsx'
        			else if(type == 'image') exts = 'jpg|png|gif|bmp|jpeg';
        			else exts = 'jpg|png|gif|bmp|jpeg|xls|xlsx|doc|docx|ppt|pptx|pdf|zip|rar|mp3|mp4';
        		}
        		upload.render({
        			elem: '#'+id
        			,url: APP.CONTEXT+'upload/'+type
        			,accept: 'file'
        			,exts: exts
        			,done: function(res, index, upload){
        				 if(res.code == '0'){
        			    	$('#'+id+"_path").val(res.data.path);
        			    	$('#'+id+"_id").val(res.data.id);
        			    	$this.val(res.data.name);
          				  }else{
           	      			 layer.msg(res.msg, {icon: 5});
           	      			 if(res.url) window.location = res.url;
          				  }
        			}
        		}); 
        	});
        },
        initEditor:function(){
        	$('textarea.editor').each(function(){    					
				UE.getEditor($(this).attr('id'), {autoHeightEnabled: false,initialFrameHeight:360});
			});
        }
    }
    function merge(url, data){
    	if(url.indexOf('?')== -1) url += '?';
    	for(var k in data){
    		url += '&'+k+'='+data[k];
    	}
    	return url;
    }
    //暴露接口
    exports('curd', curd);
});