package org.pp.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.pp.core.BaseService;
import org.pp.modules.sys.model.Category;
import org.pp.modules.sys.vo.Tree;

import com.jfinal.kit.Kv;

public class CategoryService implements BaseService<Category>{
	
	public static Category dao = new Category().dao();
	private String error = "";
	
	/**
	 * 生成树形菜单
	 * @param pid long 上级ID
	 * @return List<Tree>
	 */
	public List<Tree> tree(long pid){
		List<Tree> tree = new ArrayList<>();
		List<Category> list = all(Kv.by("pid = ", pid), "list_sort ASC");
		if(list== null || list.size() == 0) return null;
		
		for(Category d:list) {
			Tree t = new  Tree();
			t.setId(d.getId()+"");
			t.setField(d.getId()+"");
			t.setTitle(d.getTitle());
			
			List<Tree> s = tree(d.getId());
			if(s != null && s.size() >0) {
				t.setChildren(s);
			}
			t.setSpread(true);
			tree.add(t);
		}
		return tree;
	}
		
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Category getModel() {
		return dao;
	}
}
