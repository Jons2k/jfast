package org.pp.modules.dev.model;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.pp.consts.SqlConst;
import org.pp.modules.dev.base.BaseValidation;
import org.pp.modules.dev.service.FieldService;
import org.pp.modules.dev.service.ModelsService;
import org.pp.utils.DateUtil;

import com.jfinal.kit.Kv;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Record;

/**
 * 数据验证配置，支持的验证类型和规则如下：
 * 1、正则表达式regex,内置规则为email,phone,url,number,ip,identity,date,datetime,可自定义规则
 * 2、长度length，如果规则只有一个数则值的长度不能超过该数值，如果有两个数，则值的长度必须在两个数字之间，如20或10，20
 * 3、数字范围between，如果规则只有一个数则值的大小不能超过该数值，如果有两个数，则值的大小必须在两个数字之间，如20或10，20
 * 3、枚举in，值必须是指定几个有效数据，如10，20，11，23等
 * 4、数据库判断sql，该功能基本是万能的，需要一个占位符用值进行填充，如果数据查询需要返回boolean判断验证是否通过，如SELECT count(id)>0 FROM sys_department WHERE code=?
 */
@SuppressWarnings("serial")
public class Validation extends BaseValidation<Validation> {
	private Models model;
	private Field field;


	/**
	 * 按当前规则对数据进行验证
	 * @param val String 值
	 * @return boolean
	 */
	public boolean valid(Map<String,String> data) {	
		boolean matched = true;
		String rule = getRule();	
		String[] va;
		String code = field.getCode();				
		String val = data.get(code);
		switch (getType().toLowerCase()) {
			case "regex":	//正则
				if(PropKit.containsKey("regex."+rule)) {
					rule = PropKit.get("regex."+rule);
				}
				Pattern p = Pattern.compile(rule);
				matched = p.matcher(val).matches();
				break;
			case "length":	//长度	
				va = rule.split(",");
				if(va.length == 1) {
					matched = val.length() <= Integer.valueOf(va[0]);
				}else {
					matched = (val.length() >= Integer.valueOf(va[0])) && (val.length() <= Integer.valueOf(va[1]));
				}
				break;
			case "between":	//数字范围
				va = rule.split(",");
				if(va.length == 1) {
					matched = Double.valueOf(val).compareTo(Double.valueOf(va[0])) <= 0;
				}else {
					matched = (Double.valueOf(val).compareTo(Double.valueOf(va[0])) >= 0) && (Double.valueOf(val).compareTo(Double.valueOf(va[1])) <= 0);
				}
				break;
			case "in":	//枚举
				matched = false;
				va = rule.split(",");
				for(String v:va) {
					if(val.equals(v)) {
						matched = true;
						break;
					}
				}
				break;
			case "unique":	//唯一
				matched = false;
				String sql = "SELECT * FROM "+ model.getTables() + SqlConst.TPL_WHERE;
				Kv cond = Kv.by(field.getCode()+" = ", data.get(field.getCode()));
				if(!StrKit.isBlank(rule)) {
					va = rule.split(",");
					for(String v:va) {
						cond.put(v+" = ", data.get(v));
					}
				}
				List<Record> list = Db.find(Db.getSqlParaByString(sql, Kv.by("cond", cond)));
				//不存在则为唯一
				if(list == null || list.size() == 0) {
					matched = true;
				}else if(list.size() == 1 && data.containsKey("id")){ //更新且只有一条记录
					Record r = list.get(0);
					//更新时是目标数据为唯一，不是目标数据则不为唯一
					if(r.getStr("id").equalsIgnoreCase(data.get("id"))) {
						matched = true;
					}else {
						matched = false;
					}
				}else {//多条记录或非更新时，不能有旧数据存在				
					matched = false;
				}
				break;
			case "sql":	//数据库查询
				matched = "1".equals(Db.queryStr(rule, val));
				break;
			default:
				break;
		}
		return matched;
	}

	
	public Field getField() {
		if(field == null) {
			field = FieldService.dao.findById(getFieldId());
		}
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}
	public Models getModel() {
		if(model == null) {
			model = ModelsService.dao.findById(getModelId());
		}
		return model;
	}

	public void setModel(Models model) {
		this.model = model;
	}
}
