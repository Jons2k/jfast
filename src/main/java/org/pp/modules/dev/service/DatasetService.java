package org.pp.modules.dev.service;


import org.pp.core.BaseService;
import org.pp.modules.dev.model.Dataset;


public class DatasetService implements BaseService<Dataset>{
	
	public static Dataset dao = new Dataset().dao();
	private String error = "";
	
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Dataset getModel() {
		return dao;
	}
}
