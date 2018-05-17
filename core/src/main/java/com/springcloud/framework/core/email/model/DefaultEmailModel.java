package com.springcloud.framework.core.email.model;


import com.springcloud.framework.core.email.common.TemplateFactory;
import com.springcloud.framework.core.email.modules.scan.CommonEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.internet.MimeMessage;
import java.util.Map;

/**
 * 默认的email邮件模版
 * */
public class DefaultEmailModel extends CommonEmail implements Runnable {
	
	// 日志记录对象
    private static Logger log = LoggerFactory.getLogger(DefaultEmailModel.class);
	
	@Override
	public void run() {
		try {
			super.sendMail();
		} catch (Exception e) {
			log.error("发送邮件失败", e);
		}
	}
	
	public void createContent(MimeMessage message, Map<String, Object> map){
		try {
			if(map != null){
				//模版类型
				String typeModel = String.valueOf(map.get("typeModel"));
				//邮件模版
				String emailModel = String.valueOf(map.get("emailModel"));
				//获取邮件模版
				if("html".equals(typeModel)){
					String content = TemplateFactory.generateHtmlFromFtl(emailModel, map);//exception.ftl
					//设置内容
					message.setContent(content, "text/html;charset=utf-8");
				}else if("text".equals(typeModel)){
					String content = String.valueOf(map.get("content"));
					//设置内容
					message.setContent(content, "text/html;charset=utf-8");
				}
			}
		} catch (Exception e) {
			log.error("获取邮件模版失败", e);
		}
	}

}
