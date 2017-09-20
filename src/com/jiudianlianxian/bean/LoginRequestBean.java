package com.jiudianlianxian.bean;


/**
 * 
 * @Title: LoginRequestBean
 * @Description: 登录请求实体
 * @Company: 济宁九点连线信息技术有限公司
 * @ProjectName: socket
 * @author fupengpeng
 * @date 2017年9月20日 上午9:32:17
 */
public class LoginRequestBean {
	private String tag;
	private String username ;
	private String password ;
	public LoginRequestBean(){}
	
	public LoginRequestBean(String tag, String username, String password) {
		super();
		this.tag = tag;
		this.username = username;
		this.password = password;
	}

	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
