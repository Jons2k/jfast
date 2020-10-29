package org.pp.modules.sys.service;

import java.util.ArrayList;
import java.util.List;

import org.pp.core.BaseService;
import org.pp.core.ExcelService;
import org.pp.modules.sys.model.Department;
import org.pp.modules.sys.vo.Tree;

import com.jfinal.aop.Aop;
import com.jfinal.kit.Kv;
import com.jfinal.kit.StrKit;

public class DepartmentService implements BaseService<Department>, ExcelService{
	
	public static Department dao = new Department().dao();
	private String error = "";
	
	/**
	 * 生成树形菜单
	 * @param pid long 上级ID
	 * @return List<Tree>
	 */
	public List<Tree> tree(long pid){
		List<Tree> tree = new ArrayList<>();
		List<Department> list = all(Kv.by("pid = ", pid), "list_sort ASC");
		if(list== null || list.size() == 0) return null;
		
		for(Department d:list) {
			Tree t = new  Tree();
			t.setId(d.getId()+"");
			t.setField(d.getType());
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
	public boolean insert(Department bean) {
		Department d = findFirst(Kv.by("code = ", bean.getCode()));
		if(d != null) {
			error = "代码已经存在："+bean.getCode();
			return false;
		}
		if(bean.getPid() == null) bean.setPid(0);
		
		return bean.save();
	}
	
	@Override
	public boolean update(Department bean) {
		if(bean.getPid() == null) bean.setPid(0);
		
		if(bean.getId() == null) {
			return bean.save();
		}else {
			return bean.update();
		}		
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public Department getModel() {
		return dao;
	}

	@Override
	public boolean importOne(String[] line, String repeat) {
		if(StrKit.isBlank(line[0]) || StrKit.isBlank(line[1]) || StrKit.isBlank(line[2])) {
			setError("必填 字段不能为空");
			return false;
		}
		DictService ds = Aop.get(DictService.class);
		Department d = findFirst(Kv.by("code = ", line[0]));
		if(d != null) {
			if("1".equals(repeat)) {
				setError("部门("+line[0]+")已经存在");
				return false;
			}else if("2".equals(repeat)) {
				return true;
			}else if("3".equals(repeat)) {
				d.setTitle(line[1]);
				d.setType(ds.getVal("depart_type", line[2]));
				if(line.length > 3 && StrKit.notBlank(line[3])) {
					Department p = findFirst(Kv.by("title = ", line[3]));
					if(p != null) d.setPid(p.getId());
				}else {
					d.setPid(0);
				}
				if(line.length > 4 && StrKit.notBlank(line[4])) {
					d.setStatus(ds.getVal("status", line[4]));
				}else {
					d.setStatus("1");
				}
				if(line.length > 5 && StrKit.notBlank(line[5])) {
					d.setListSort(Integer.valueOf(line[5]));
				}else {
					d.setListSort(9);
				}
				return d.update();
			}else {
				setError("未知错误");
				return false;
			}
		}else {
			d = new Department();
			d.setCode(line[0]);
			d.setTitle(line[1]);
			d.setType(ds.getVal("depart_type", line[2]));
			if(line.length > 3 && StrKit.notBlank(line[3])) {
				Department p = findFirst(Kv.by("title = ", line[3]));
				if(p != null) d.setPid(p.getId());
			}else {
				d.setPid(0);
			}
			if(line.length > 4 && StrKit.notBlank(line[4])) {
				d.setStatus(ds.getVal("status", line[4]));
			}else {
				d.setStatus("1");
			}
			if(line.length > 5 && StrKit.notBlank(line[5])) {
				d.setListSort(Integer.valueOf(line[5]));
			}else {
				d.setListSort(9);
			}
			return d.save();
		}
	}

	@Override
	public String[] exportOne(Object m) {
		DictService ds = Aop.get(DictService.class);
		String[] line = new String[6];
		Department d = (Department)m;
		line[0] = d.getCode();
		line[1] = d.getTitle();
		line[2] = ds.getText("depart_type", d.getType());
		if(d.getPid() != null && d.getPid().longValue() != 0l) {
			Department p = dao.findById(d.getPid());
			if(p != null) line[3] = p.getTitle();
		}
		line[4] = ds.getText("status", d.getStatus());
		line[5] = d.getListSort()+"";
		return line;
	}

	@Override
	public String[] header() {
		String[] titles = {"部门代码", "部门名称", "部门类型", "上级部门名称", "状态","排序"};
		return titles;
	}
	
	@Override
	public String title() {
		return "部门信息";
	}
}
