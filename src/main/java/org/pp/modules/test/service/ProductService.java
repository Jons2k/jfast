package org.pp.modules.test.service;

import org.pp.core.BaseService;
import org.pp.modules.test.model.Product;

public class ProductService implements BaseService<Product>{	
	public static Product dao = new Product().dao();
	private String error = "";

	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public Product getModel() {
		return dao;
	}
}
