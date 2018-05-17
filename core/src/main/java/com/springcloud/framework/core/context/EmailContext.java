package com.springcloud.framework.core.context;

import java.io.IOException;
import java.util.Properties;

public class EmailContext {
	
	private static Properties properties = new Properties();
	
	static {
		if(properties.isEmpty()){
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			try {
	            properties.load(loader.getResourceAsStream("email.properties"));
	            
	            EmailContext.emailAccount = properties.getProperty("mail.exception.account");
	            EmailContext.emailPassword = properties.getProperty("mail.exception.password");
	            EmailContext.host = properties.getProperty("mail.exception.host");
	            EmailContext.auth = properties.getProperty("mail.exception.auth");
	            EmailContext.receiverList = properties.getProperty("mail.exception.receiverList");
	            EmailContext.mailName = properties.getProperty("mail.exception.mailname");
	            EmailContext.subject = properties.getProperty("mail.exception.subject");
	            
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
		}
	}
	
	private static String emailAccount;
	
	private static String emailPassword;
	
	private static String host;
	
	private static String auth;
	
	private static String receiverList;
	
	private static String mailName;
	
	private static String subject;

	public static String getEmailAccount() {
		return emailAccount;
	}

	public static String getEmailPassword() {
		return emailPassword;
	}

	public static String getHost() {
		return host;
	}

	public static String getAuth() {
		return auth;
	}

	public static String getReceiverList() {
		return receiverList;
	}

	public static String getMailName() {
		return mailName;
	}

	public static String getSubject() {
		return subject;
	}
	
}
