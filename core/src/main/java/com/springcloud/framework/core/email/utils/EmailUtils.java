package com.springcloud.framework.core.email.utils;


import com.springcloud.framework.core.concurrent.TaskManager;
import com.springcloud.framework.core.context.EmailContext;
import com.springcloud.framework.core.email.model.DefaultEmailModel;
import com.springcloud.framework.core.email.model.EmailEntity;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * EmailUtils
 * 工具类
 * */
public class EmailUtils {
	
	// 日志记录对象
    private static Logger log = LoggerFactory.getLogger(EmailUtils.class);
	
	/**
	 * 发送邮件
	 * @param	邮件参数实体
	 * @return
	 * */
	public static String sendEmail(EmailEntity emailEntity) {
		if(emailEntity == null){
			return null;
		}
		
		DefaultEmailModel defaultModel = new DefaultEmailModel();
		defaultModel.setEmailAccount(emailEntity.getEmailAccount());
		defaultModel.setEmailPassword(emailEntity.getEmailPassword());
		defaultModel.setHost(emailEntity.getHost());
		defaultModel.setAuth(emailEntity.getAuth());
		defaultModel.setMailName(emailEntity.getMailName());
		defaultModel.setSubject(emailEntity.getSubject());
		defaultModel.setReceiverList(emailEntity.getReceiverList());
		Map<String, Object> map = emailEntity.getMap();
		if(map == null){
			map = new HashMap<>();
		}
		map.put("typeModel", emailEntity.getTypeModel());
		map.put("emailModel", emailEntity.getEmailModel());
		defaultModel.setMap(map);
		
		return EmailUtils.sendEmail(defaultModel);
	}
	
	/**
	 * 发送邮件
	 * @param
	 * @return
	 * */
	public static String sendEmail(DefaultEmailModel defaultModel) {
		if(defaultModel == null){
			return null;
		}
		//设置默认账号
		if(StringUtils.isBlank(defaultModel.getEmailAccount())){
			defaultModel.setEmailAccount(EmailContext.getEmailAccount());
		}
		//设置默认账号密码
		if(StringUtils.isBlank(defaultModel.getEmailPassword())){
			defaultModel.setEmailPassword(EmailContext.getEmailPassword());
		}
		//设置smtp邮件服务器
		if(StringUtils.isBlank(defaultModel.getHost())){
			defaultModel.setHost(EmailContext.getHost());
		}
		//设置授权标识
		if(StringUtils.isBlank(defaultModel.getAuth())){
			defaultModel.setAuth(EmailContext.getAuth());
		}
		//设置发件人名字
		if(StringUtils.isBlank(defaultModel.getMailName())){
			defaultModel.setMailName(EmailContext.getMailName());
		}
		//设置主题
		if(StringUtils.isBlank(defaultModel.getSubject())){
			defaultModel.setSubject(EmailContext.getSubject());
		}
		//设置收件人
		if(StringUtils.isBlank(defaultModel.getReceiverList())){
			defaultModel.setReceiverList(EmailContext.getReceiverList());
		}
		Map<String, Object> map = defaultModel.getMap();
		if(map != null){
			String typeModel = String.valueOf(map.get("typeModel"));
			//模版类型不能为空
			if(StringUtils.isBlank(typeModel)){
				log.error("typeModel is not empty");
				return "fail";
			}
			//html模版需要指定使用的ftl文件
			if("html".equals(typeModel)){
				String emailModel = String.valueOf(map.get("emailModel"));
				if(StringUtils.isBlank(emailModel)){
					log.error("emailModel is not empty");
					return "fail";
				}
			}
			
		}
		
		//将邮件放入线程池
		TaskManager.addTask(defaultModel);
		
		return "success";
	}


	public static boolean canEmail(String emailFlag){
		return StringUtils.isNotBlank(emailFlag) ? !"localhost".equals(emailFlag) : true;
	}
	
}
