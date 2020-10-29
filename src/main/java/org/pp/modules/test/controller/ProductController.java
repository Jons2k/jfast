package org.pp.modules.test.controller;

import com.jfinal.aop.Inject;

import org.pp.core.AdminController;
import org.pp.core.BaseService;
import org.pp.modules.test.model.Product;
import org.pp.modules.test.service.ProductService;

public class ProductController extends AdminController<Product> {

	@Inject
	ProductService service;


	@Override
	protected BaseService<Product> getService() {
		return service;
	}

}
