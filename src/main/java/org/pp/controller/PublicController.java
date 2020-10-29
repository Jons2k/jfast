package org.pp.controller;

import org.pp.consts.RestConst;
import org.pp.core.BaseController;
import org.pp.core.RestObject;
import org.pp.modules.sys.model.Token;
import org.pp.modules.sys.model.User;
import org.pp.modules.sys.service.TokenService;
import org.pp.modules.sys.service.UserLoginService;
import org.pp.modules.sys.service.UserService;
import org.pp.modules.sys.vo.UserInfo;
import org.pp.utils.HttpUtil;
import org.pp.utils.SecurityUtil;

import com.jfinal.aop.Aop;
import com.jfinal.aop.Inject;
import com.jfinal.core.ActionKey;
import com.jfinal.kit.PropKit;

public class PublicController extends BaseController {
	@Inject
	UserService service;
	@Inject
	UserLoginService login;
		
	@ActionKey("/")
	public void index() {
		Object o = this.getSession().getAttribute("user");
		if(o == null) {
			this.login();
		}else {
			this.redirect(SecurityUtil.homeUrl());
		}		
	}
	
	@ActionKey("/login")
	public void login() {
		if(HttpUtil.isGet(getRequest())) {
			setAttr("_title", "登录");
			this.render("/view/public/login.html");
			return;
		}
		
		
		if(!this.validateCaptcha("captcha")) {
			renderJson(RestObject.error(RestConst.CAPTCHA, "验证码错误",""));
			return;
		}
		
		String account = this.getPara("account");
		User user = service.login(account, getPara("password"));
		if(user == null) {
			renderJson(RestObject.error(service.getError()));
			login.record(account, getRequest().getRemoteAddr());
		}else {
			login.record(user, getRequest().getRemoteAddr(), getSession().getId());

			setSessionAttr("user", user);
			
			UserInfo info = new UserInfo();
			info.setAccount(account);
			info.setId(user.getId());
			info.setNickname(user.getNickname());
			String redirect = getSessionAttr("redirect");
			
			if(redirect == null ||SecurityUtil.loginUrl().equalsIgnoreCase(redirect)) {
				redirect = getRequest().getContextPath()+SecurityUtil.homeUrl();
			}
			renderJson(RestObject.success("登录成功",info, getRequest().getContextPath()+SecurityUtil.homeUrl()));
			//renderJson(RestObject.success("登录成功",info, redirect));
			removeSessionAttr("redirect");
		}
	}
	
	
	@ActionKey("/logout")
	public void logout() {
		Object user = getSessionAttr("user");
		if(user == null) {
			login.logout(getRequest().getRemoteAddr(), getSession().getId());
		}else {
			login.logout((User)user, getSession().getId());
		}
		removeSessionAttr("user");
		getSession().invalidate();
		this.redirect(PropKit.get("security.context", "/"));
	}

	@ActionKey("/captcha")
	public void captcha() {
		this.renderCaptcha();
	}
	
	@ActionKey("/token")
	public void token() {
		String account = this.getPara("account");
		String ip = getRequest().getRemoteAddr();
		User user = service.login(account, getPara("password"));
		if(user == null) {
			renderJson(RestObject.error(service.getError()));
			login.record(account, ip);
		}else {
			login.record(user, ip);

			UserInfo info = new UserInfo();
			info.setAccount(account);
			info.setId(user.getId());
			info.setNickname(user.getNickname());
			Token t = Aop.get(TokenService.class).grant(user, ip);
			if(t == null) {
				renderJson(RestObject.error("生成令牌失败"));
				return;
			}
				
			info.setToken(t.getCode());
			renderJson(RestObject.success("认证成功",info));
		}
	}
}
