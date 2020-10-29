package org.pp.core;

import java.util.List;

import org.pp.consts.SqlConst;
import org.pp.utils.ModelUtil;

import com.jfinal.kit.Kv;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.SqlPara;
import com.jfinal.plugin.activerecord.TableMapping;

public interface BaseService <M extends Model<M>>{
	/**
	 * 获取服务对应的主模型
	 * @return
	 */
	public M getModel();
	
	/**
	 * 获取信息信息
	 * @return
	 */
	public String getError();
	
	/**
	 * 设置错误信息
	 * @param error
	 */
	public void setError(String error);
	
	/**
	 * 分页查询，使用ID降序
	 * @param where Kv 查询条件
	 * @param page int 当前页码
	 * @param size int 每页条数
	 * @return Page<M>
	 */
	default Page<M> page(Kv where, int page, int size) {
		return page(where, page, size, "id DESC");
	}
	
	/**
	 * 带排序的分页查询
	 * @param where Kv 查询条件
	 * @param page int 当前页码
	 * @param size int 每页条数
	 * @param order String 排序方式
	 * @return Page<M>
	 */
	default Page<M> page(Kv where, int page, int size, String order) {
		M dao = getModel();
		Kv cond = Kv.by("cond", where);
		cond.put("order", order);
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		SqlPara sql = Db.getSqlPara(table+"_page", cond);
		if(sql == null) {
			sql = Db.getSqlParaByString("SELECT * FROM " + table + SqlConst.TPL_WHERE + SqlConst.TPL_ORDER, cond);
		}
		return dao.paginate(page, size, sql);
	}
	
	/**
	 * 按条件查询全部数据，使用ID降序
	 * @param where  Kv 查询条件
	 * @return List<M>
	 */
	default List<M> all(Kv where) {
		return all(where,"id DESC");
	}

	/**
	 * 按条件查询全部数据，使用自定义排序
	 * @param where  Kv 查询条件
	 * @param order String 排序方式
	 * @return List<M>
	 */
	default List<M> all(Kv where, String order) {
		M dao = getModel();
		Kv cond = Kv.by("cond", where);
		cond.put("order", order);
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		SqlPara sql = Db.getSqlPara(table+"_all", cond);
		if(sql == null) {
			sql = Db.getSqlParaByString("SELECT * FROM " + table + SqlConst.TPL_WHERE + SqlConst.TPL_ORDER, cond);
		}
		return dao.find(sql);
	}
	
	/**
	 * 根据主键查询数据
	 * @param id long 主键ID
	 * @return M
	 */
	default M find(long id) {
		return getModel().findById(id);
	}
	
	/**
	 * 根据主键查询数据并缓存
	 * @param id long 主键ID
	 * @return M
	 */
	default M findCache(long id) {
		M dao = getModel();
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		return dao.findFirstByCache(table, id, "SELECT * FROM " + table + " WHERE id=?", id);
	}
	
	/**
	 * 根据条件查询第一条数据
	 * @param cond Kv 查询条件
	 * @return M
	 */
	default M findFirst(Kv cond) {
		M dao = getModel();
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		SqlPara sql = Db.getSqlParaByString("SELECT * FROM " + table + SqlConst.TPL_WHERE, Kv.by("cond", cond));
		return dao.findFirst(sql);
	}
	
	/**
	 * 根据条件查询指定条数数据
	 * @param where Kv 查询条件
	 * @param limit int 最多返回条数
	 * @return M
	 */
	default List<M> findTop(Kv where, int limit) {
		M dao = getModel();
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		Kv cond = Kv.by("cond", where);
		cond.put("limit", limit);
		SqlPara sql = Db.getSqlParaByString("SELECT * FROM " + table + SqlConst.TPL_WHERE + SqlConst.TPL_LIMIT, cond);
		return dao.find(sql);
	}
	
	/**
	 * 根据条件查询指定条数数据
	 * @param where Kv 查询条件
	 * @param limit int 最多返回条数
	 * @return M
	 */
	default List<M> findTop(Kv where, String order, int limit) {
		M dao = getModel();
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		Kv cond = Kv.by("cond", where);
		cond.put("limit", limit);
		cond.put("order", order);
		SqlPara sql = Db.getSqlParaByString("SELECT * FROM " + table + SqlConst.TPL_WHERE + SqlConst.TPL_LIMIT, cond);
		return dao.find(sql);
	}
		
	default boolean insert(M bean) {
		ModelUtil util = ModelUtil.get();
		boolean ret = util.save(bean);
		setError(util.getError());
		ModelUtil.release();
		return ret;
	}
	default boolean update(M bean) {
		ModelUtil util = ModelUtil.get();
		boolean ret = util.update(bean);
		setError(util.getError());
		ModelUtil.release();
		return ret;
	}
	default boolean delete(long id) {
		return getModel().deleteById(id);
	}	
	
	/**
	 * 根据条件删除数据
	 * @param Kv where 删除条件
	 * @return int
	 */
	default int delete(Kv where) {
		M dao = getModel();
		String table = TableMapping.me().getTable(dao.getClass()).getName();
		String sql = "DELETE FROM "+table+" " + SqlConst.TPL_WHERE;
		return Db.templateByString(sql, Kv.by("cond", where)).delete();
	}	
}
