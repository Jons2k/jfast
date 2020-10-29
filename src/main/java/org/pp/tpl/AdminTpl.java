package org.pp.tpl;

import org.pp.modules.sys.model.User;
import org.pp.utils.SecurityUtil;

/**
 * 后台管理模板扩展
 * @author yepanpan
 *
 */
public class AdminTpl {
		
	/**
	 * 权限判断 #if(ADMIN.access('sys/user/add')) <a href="">添加</a> #end
	 * @param access String
	 * @return
	 */
	public boolean access(String access, User user) {
		if(!access.startsWith("/")) access="/"+access;
		return SecurityUtil.checkAccess(access, user);
	}
}
