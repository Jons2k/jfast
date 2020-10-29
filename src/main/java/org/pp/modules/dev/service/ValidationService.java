package org.pp.modules.dev.service;


import org.pp.core.BaseService;
import org.pp.modules.dev.model.Validation;

public class ValidationService implements BaseService<Validation>{
	
	public static Validation dao = new Validation().dao();
	private String error = "";
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Validation getModel() {
		return dao;
	}	
}
