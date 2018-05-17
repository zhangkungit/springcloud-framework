package com.springcloud.framework.core.email.model;

import java.util.Map;

/**
 * EmailEntity邮件实体
 * */
public class EmailEntity {
	
	//发送人邮件账号
	private String emailAccount;
	
	//发送人邮件密码
	private String emailPassword;
	
	//smtp服务器地址
	private String host;
	
	//授权标识
	private String auth;
	
	//收件人列表
	private String receiverList;
	
	//发件人描述
	private String mailName;
	
	//主题
	private String subject;
	
	//模版参数
	private Map<String, Object> map;
	
	//模版类型
	private String typeModel;
	
	//邮件模版
	private String emailModel;

	public String getEmailAccount() {
		return emailAccount;
	}

	public void setEmailAccount(String emailAccount) {
		this.emailAccount = emailAccount;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public void setEmailPassword(String emailPassword) {
		this.emailPassword = emailPassword;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getAuth() {
		return auth;
	}

	public void setAuth(String auth) {
		this.auth = auth;
	}

	public String getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(String receiverList) {
		this.receiverList = receiverList;
	}

	public String getMailName() {
		return mailName;
	}

	public void setMailName(String mailName) {
		this.mailName = mailName;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Map<String, Object> getMap() {
		return map;
	}

	public void setMap(Map<String, Object> map) {
		this.map = map;
	}

	public String getTypeModel() {
		return typeModel;
	}

	public void setTypeModel(String typeModel) {
		this.typeModel = typeModel;
	}

	public String getEmailModel() {
		return emailModel;
	}

	public void setEmailModel(String emailModel) {
		this.emailModel = emailModel;
	}
	
}
