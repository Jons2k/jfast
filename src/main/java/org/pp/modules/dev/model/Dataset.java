package org.pp.modules.dev.model;

import org.pp.modules.dev.base.BaseDataset;
import org.pp.modules.sys.model.Category;
import org.pp.modules.sys.service.CategoryService;

import com.jfinal.aop.Aop;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Dataset extends BaseDataset<Dataset> {
	private Category cate;

	public Category getCate() {
		if(cate == null && getCateId() != null) {
			cate = Aop.get(CategoryService.class).findCache(getCateId());
		}
		return cate;
	}

	public void setCate(Category cate) {
		this.cate = cate;
	}
}
