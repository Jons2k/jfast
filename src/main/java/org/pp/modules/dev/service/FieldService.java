package org.pp.modules.dev.service;

import java.sql.SQLException;
import java.util.List;

import org.pp.consts.SqlConst;
import org.pp.core.BaseService;
import org.pp.modules.dev.model.Field;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.IAtom;

public class FieldService implements BaseService<Field>{
	
	public static Field dao = new Field().dao();
	private String error = "";
	
	@Override
	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}
	
	@Override
	public Field getModel() {
		return dao;
	}
	
	@Override
	public boolean delete(long id) {
		return Db.tx(new IAtom() {			
			@Override
			public boolean run() throws SQLException {
				Db.delete("DELETE FROM dev_field WHERE id=?", id);
				Db.delete("DELETE FROM dev_validation WHERE field_id=?", id);
				return true;
			}
		});
	}
	public List<Field> modelField(long moduleId){
		Kv cond = Kv.by("model_id = ", moduleId);
		return dao.find(Db.getSqlParaByString("SELECT * FROM dev_field "+ SqlConst.TPL_WHERE, Kv.by("cond", cond)));
	}

}
