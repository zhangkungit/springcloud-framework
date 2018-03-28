package com.springcloud.framework.core.elasticjob;

import java.lang.annotation.*;

/**
 * @author zhangkun
 * @version 1.0
 * @date 2018/3/28
 * @description
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Simple {

    String cron();

    String description() default "";

    int shardingTotalCount() default 1;

    boolean disabled() default false;

    boolean overwrite() default false;

}
