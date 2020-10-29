package org.pp.modules.sys.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Dict;
import org.pp.modules.sys.vo.Tree;

import com.jfinal.kit.Kv;

public class DictService implements BaseService<Dict>{
	
	public static Dict dao = new Dict().dao();
	private static Map<String,String> maps;

	private String error = "";
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Dict getModel() {
		return dao;
	}
	
	/**
	 * 根据字典值代码查询字典的文本
	 * @param dictCode String 字典代码
	 * @param val String 字典值的代码
	 * @return String 
	 */
	public String getText(String dictCode,String val) {
		mapDict();
		return maps.get(dictCode.toLowerCase()+"_"+val.toLowerCase());
	}

	/**
	 * 根据字典文本查询字典的代码
	 * @param dictCode String 字典代码
	 * @param text String 字典值的文本
	 * @return String
	 */
	public String getVal(String dictCode,String text) {
		mapDict();
		return maps.get(dictCode.toLowerCase()+"_"+text);
	}
	
	/**
	 * 将数据字典映射成一个MAP
	 */
	private static void mapDict() {
		if(maps != null) return;
		
		List<Dict> list = dao.findAll();
		maps = new HashMap<>();
		for(Dict d:list) {
			if("d".equalsIgnoreCase(d.getType())) {
				for(Dict v:list) {
					if(v.getPid().equals(d.getId())) {
						maps.put(d.getCode().toLowerCase()+"_"+v.getCode().toLowerCase(), v.getTitle());
						maps.put(d.getCode().toLowerCase()+"_"+v.getTitle(),v.getCode().toLowerCase());
					}
				}
			}
		}
	}
	
	
	/**
	 * 生成树形菜单
	 * @param pid long 上级ID
	 * @return List<Tree>
	 */
	public List<Tree> left(long pid){
		List<Tree> tree = new ArrayList<>();
		Kv cond = Kv.by("pid = ", pid);
		cond.put("type != ", "v");
		List<Dict> list = all(cond);
		if(list== null || list.size() == 0) return null;
		
		for(Dict d:list) {
			Tree t = new  Tree();
			t.setId(d.getId()+"");
			t.setField(d.getType());
			t.setTitle(d.getTitle());
			
			List<Tree> s = left(d.getId());
			if(s != null && s.size() >0) {
				t.setChildren(s);
			}
			t.setSpread(true);
			tree.add(t);
		}
		return tree;
	}
	
	/**
	 * 生成下拉选项
	 * @param pid long 上级ID
	 * @return List<Tree>
	 */
	public List<Dict> category(){
		Kv cond = Kv.by("pid = ", 0);
		cond.put("type = ", "c");
		return all(cond);
	}
	
	@Override
	public boolean insert(Dict bean) {
		if("c".equalsIgnoreCase(bean.getType()) || "d".equalsIgnoreCase(bean.getType())) {
			Dict d = findFirst(Kv.by("code = ", bean.getCode()));
			if(d != null) {
				error = "代码已经存在："+bean.getCode();
				return false;
			}
			if(bean.getPid() == null) bean.setPid(0);
		}else {
			if(bean.getPid() == null) {
				error = "请先选择字典";
				return false;
			}
		}		
		
		return bean.save();
	}
	
	@Override
	public boolean update(Dict bean) {
		if(bean.getPid() == null) bean.setPid(0);
		
		return bean.update();
	}
}
