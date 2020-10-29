package org.pp.modules.dev.service;

import org.pp.core.BaseService;
import org.pp.modules.dev.model.Chart;
import org.pp.utils.CodeUtil;

public class ChartService implements BaseService<Chart>{
	
	public static Chart dao = new Chart().dao();
	private String error = "";
	
	public boolean genCode(Chart chart) {
		if(!CodeUtil.chart(chart)){
			return false;
		}	
		return true;
	}
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	@Override
	public Chart getModel() {
		return dao;
	}
}
