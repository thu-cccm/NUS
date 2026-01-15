package com.maple.common.util;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataScope {

    String deptAlias() default "belong_dept_id";

    String userAlias() default "create_id";
}