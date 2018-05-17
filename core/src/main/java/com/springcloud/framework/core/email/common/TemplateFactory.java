package com.springcloud.framework.core.email.common;

import freemarker.template.Configuration;
import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * ftl模版类
 */
public class TemplateFactory {

    // 日志记录对象
    private static Logger log = LoggerFactory.getLogger(TemplateFactory.class);
    // 模板文件路径
    private static String templatePath = "/template";
    // 模板文件内容编码
    private static final String ENCODING = "utf-8";
    // 模板生成配置
    private static Configuration conf = new Configuration();
    // 邮件模板缓存池
    private static Map<String, Template> tempMap = new HashMap<String, Template>();

    static {
        conf.setDefaultEncoding(ENCODING);
        conf.setClassForTemplateLoading(TemplateFactory.class, templatePath);
    }

    public static String generateHtmlFromFtl(String name,
                                             Map<String, Object> map) throws Exception {
        Writer out = new StringWriter(2048);
        try {
            Template temp = getTemplateByName(name);
            temp.setEncoding(ENCODING);
            temp.process(map, out);
            return out.toString();
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                log.error("Writer close fail======", e);
            }
        }

    }

    private static Template getTemplateByName(String name) throws IOException {
        if (tempMap.containsKey(name)) {
            log.debug("the template is already exist in the map,template name :"
                    + name);
            // 缓存中有该模板直接返回
            return tempMap.get(name);
        }
        // 缓存中没有该模板时，生成新模板并放入缓存中
        Template temp = conf.getTemplate(name);
        tempMap.put(name, temp);
        log.debug("the template is not found  in the map,template name :"
                + name);
        return temp;
    }


}
