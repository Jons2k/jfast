package org.pp.core;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.jfinal.kit.Base64Kit;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;

public abstract class AdminController<M extends Model<M>> extends BaseController{
		
	/**
	 * 分页查询
	 */
	public void lists() {
		BaseService<M> service = getService();	
		Page<M> page = service.page(search(), getPage(), getPageSize(), getOrder());
		renderJson(RestObject.success(page.getTotalRow(), page.getList()));
	}
	
	/**
	 * 查询全部
	 */
	public void slist() {
		BaseService<M> service = getService();	
		List<M> list = service.all(search(), getOrder());
		if(null != getPara("format")) {
			renderJson(RestObject.success(list.size(), list));
		}else {
			renderJson(RestObject.success(list.size(), list));
		}
	}

	/**
	 * 保存新数据
	 */
	public void save() {
		BaseService<M> service = getService();	
		M  bean = (M)getBean(service.getModel().getClass(), "");
		if(service.insert(bean)){
			renderJson(RestObject.success("保存数据成功！", ""));
		}else {
			renderJson(RestObject.error("保存数据失败！"+service.getError()));
		}
	}

	/**
	 * 更新数据
	 */
	public void update() {
		String ids = getPara("id");
		if(StrKit.isBlank(ids)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		BaseService<M> service = getService();	
		M bean = service.find(Long.valueOf(ids));
		if(bean == null) {
			renderJson(RestObject.error("对象不存在!"+service.getError()));
		}
		M data= (M)getBean(service.getModel().getClass(), "");
		for(String pn: data._getAttrNames()) {
			String spn = StrKit.toCamelCase(pn);
			if(isParaExists(spn)) {
				bean.set(pn, data.get(pn));
			}
		}
		if(service.update(bean)){
			renderJson(RestObject.success("更新数据成功！", ""));
		}else {
			renderJson(RestObject.error("更新数据失败！"+service.getError()));
		}
	}
	
	/**
	 * 查询详细信息
	 */
	public void info() {
		String ids = getPara("id");
		if(StrKit.isBlank(ids)) {
			renderJson(RestObject.error("非法请求!"));
			return;
		}
		BaseService<M> service = getService();	
		Model<M>  bean = service.find(Long.valueOf(ids));
		if(bean == null) {
			renderJson(RestObject.error("对象不存在!"+service.getError()));
		}else {
			renderJson(RestObject.success(bean));
		}		
	}

	/**
	 * 根据主键删除
	 */
	public void delete() {
		String ids = getPara("id");
		if(StrKit.isBlank(ids)) {
			renderJson(RestObject.error("请至少选择一个操作对象!"));
			return;
		}

		BaseService<M> service = getService();	
		String[] ida = ids.split(",");
		if(Db.tx(() -> {
			for(String id:ida) {
				if(!service.delete(Long.valueOf(id))) {
					return false;
				}
			}
			return true;
		})) {
			renderJson(RestObject.success("删除数据成功!"));
			return;
		}else {
			renderJson(RestObject.error("删除数据失败!"));
			return;
		}		
	}

	/**
	 * 批量导入
	 */
	public void imports() {
		if(isParaBlank("excel")) {
			renderJson(RestObject.error("请先上传文件!"));
		}
		ExcelService service = (ExcelService)getService();	
		if(service.imports(getPara("excel"), getPara("error", "1"), getPara("repeat", "1"))){
			renderJson(RestObject.success("导入数据成功!"));
		}else {
			renderJson(RestObject.error("导入数据失败!"+service.getError()));
		}	
	}

	/**
	 * 批量导出
	 */
	public void exports() {
		BaseService<M> service = getService();	
		List<M> list = service.all(search(), getOrder());
		ExcelService es = (ExcelService)service;	
		try {
			  //获取要下载文件的名称
	        String fileName = es.fileName();
	        
	        //解决获得中文参数的乱码
	        fileName = new String(fileName.getBytes(),"UTF-8");//美女.jpg
	        //获得请求头中的User-Agent
	        String agent = getRequest().getHeader("User-Agent");
	        //根据不同浏览器进行不同的编码
	        String fileNameEncoder = "";
	        if (agent.contains("MSIE")) {
	            // IE浏览器
	            fileNameEncoder = URLEncoder.encode(fileName, "utf-8");
	            fileNameEncoder = fileNameEncoder.replace("+", " ");
	        } else if (agent.contains("Firefox")) {
	            // 火狐浏览器
	            fileNameEncoder = "=?utf-8?B?" +Base64Kit.encode(fileName.getBytes("utf-8")) + "?=";
	        } else {
	            // 其它浏览器
	            fileNameEncoder = URLEncoder.encode(fileName, "utf-8");                
	        }
	        HttpServletResponse response = getResponse();
	        response.setContentType(getRequest().getServletContext().getMimeType(fileName));
	        response.setHeader("Content-Disposition", "attachment;filename=" + fileNameEncoder);
			es.exports(response.getOutputStream(),(List)list);
			renderNull();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	protected Kv search() {
		return Kv.create();
	}

	protected int getPage() {
		return getParaToInt("page", 1);
	}
	
	protected int getPageSize() {
		return getParaToInt("limit", 15);
	}
	
	protected String getOrder() {
		return get("orderby" , "id desc");
	}
	protected abstract BaseService<M> getService();
}
