package com.springcloud.framework.core.autoconfig;

import com.dangdang.ddframe.job.api.ElasticJob;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.listener.ElasticJobListener;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperConfiguration;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import com.springcloud.framework.core.elasticjob.Simple;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2018/3/28
 * @description
 */
@Configuration
@ConditionalOnClass(ElasticJob.class)
public class ElasticJobAutoConfig {

    @Autowired
    private ConfigurableApplicationContext configurableContext;

    //zookeeper 注册中心
    @Bean(initMethod = "init")
    public ZookeeperRegistryCenter zookeeperRegistryCenter(@Value("${job.reg.address}") final String serverList, @Value("${job.reg.namespace}") final String namespace) {
        return new ZookeeperRegistryCenter(new ZookeeperConfiguration(serverList, namespace));
    }

    //Simple注解解析，并注册job
    @PostConstruct
    public void registerJob() {
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory) configurableContext.getBeanFactory();
        String[] registryCenterBeanName = configurableContext.getBeanNamesForType(ZookeeperRegistryCenter.class);

        Map<String, Object> classMap = configurableContext.getBeansWithAnnotation(Simple.class);
        for (Map.Entry<String, Object> entry : classMap.entrySet()) {
            if (entry.getValue() instanceof SimpleJob) {
                SimpleJob simpleJob = (SimpleJob) entry.getValue();

                BeanDefinitionBuilder builder = BeanDefinitionBuilder.rootBeanDefinition(SpringJobScheduler.class);
                builder.setInitMethodName("init");

                builder.addConstructorArgReference(entry.getKey());
                builder.addConstructorArgReference(registryCenterBeanName[0]);
                builder.addConstructorArgValue(buildLiteJobConfiguration(entry.getKey(), simpleJob));
                builder.addConstructorArgValue(new ElasticJobListener[0]);

                String beanName = entry.getKey() + "SpringJobScheduler";
                beanFactory.registerBeanDefinition(beanName, builder.getBeanDefinition());
                // 主动get一次 强制实例化
                configurableContext.getBean(beanName);
            }
        }
    }


    private LiteJobConfiguration buildLiteJobConfiguration(String beanName, SimpleJob simpleJob) {
        Simple simple = simpleJob.getClass().getAnnotation(Simple.class);

        JobCoreConfiguration jobCoreConfiguration = JobCoreConfiguration.newBuilder(simpleJob.getClass().getName(), simple.cron(), simple.shardingTotalCount())
                .description(simple.description())
                .build();

        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(jobCoreConfiguration, simpleJob.getClass().getName()))
                .disabled(simple.disabled())
                .overwrite(simple.overwrite())
                .build();
    }
}
