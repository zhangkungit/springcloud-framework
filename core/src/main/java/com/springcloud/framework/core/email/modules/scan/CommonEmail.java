package com.springcloud.framework.core.email.modules.scan;

import com.google.common.base.Splitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 公共的email发送类
 * */
public class CommonEmail {
	
	// 日志记录对象
    private static Logger log = LoggerFactory.getLogger(CommonEmail.class);
	
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
	
    private static final String DEFAULT_CHARSET = "UTF-8";
    
    private static final Splitter SPLITTER = Splitter.on(",").omitEmptyStrings();
    
	/**
	 * 发送邮件
	 * @throws	Exception
	 * */
	public void sendMail() throws Exception {
		// 1. 创建参数配置, 用于连接邮件服务器的参数配置
		Properties props = new Properties();
		props.setProperty("mail.transport.protocol", "smtp");
		props.setProperty("mail.smtp.host", host);
		props.setProperty("mail.smtp.auth", auth);
		props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.setProperty("mail.smtp.socketFactory.fallback", "false");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		
		// 2. 根据配置创建会话对象, 用于和邮件服务器交互
		Session session = Session.getDefaultInstance(props);
        session.setDebug(true);// 设置为debug模式, 可以查看详细的发送 log
        
        // 3. 创建一封邮件
        MimeMessage message = createMimeMessage(session, emailAccount, emailPassword);
        
        // 4. 根据 Session 获取邮件传输对象
        Transport transport = session.getTransport();
        // 5. 使用 邮件账号
        transport.connect(emailAccount, emailPassword);
        // 6. 发送邮件, 发到所有的收件地址
        transport.sendMessage(message, message.getAllRecipients());
        //关闭连接
        transport.close();
        log.info("sendMail success");
	}
	
	/**
	 * 创建邮件模版
	 * @param	session		和服务器交互的会话
	 * @param	sendMail	发件人邮箱
	 * @param	receiveMail	收件人邮箱
	 * @return
	 * @throws	Exception
	 * */
	public MimeMessage createMimeMessage(Session session, String sendMail, String receiveMail) throws Exception {
		// 1. 创建一封邮件
        MimeMessage message = new MimeMessage(session);
        
        //2. From: 发件人
        message.setFrom(new InternetAddress(sendMail, mailName, DEFAULT_CHARSET));
        
        //3. To: 收件人
        List<String> receivers = SPLITTER.splitToList(receiverList);
        if(receivers != null && !receivers.isEmpty()){
        	InternetAddress[] addresses = new InternetAddress[receivers.size()];
        	for(int i = 0; i<receivers.size(); i++){
        		addresses[i] = new InternetAddress(receivers.get(i), "", DEFAULT_CHARSET);
        	}
        	message.setRecipients(MimeMessage.RecipientType.TO, addresses);
        }
        
        // 4. Subject: 邮件主题
        message.setSubject(subject, DEFAULT_CHARSET);
        
        //5. Content: 邮件正文
        createContent(message, map);
        
        // 6. 设置发件时间
        message.setSentDate(new Date());
        
        // 7. 保存设置
        message.saveChanges();
        
        return message;
	}
	
	/**
	 * 设置邮件正文
	 * */
	public void createContent(MimeMessage message, Map<String, Object> map){}

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
	
}
