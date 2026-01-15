package com.maple.common.model;

import com.maple.common.enums.BusinessTypeEnum;
import com.maple.common.enums.OperateTypeEnum;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MapleLog {

    OperateTypeEnum operateType() default OperateTypeEnum.OTHER;

    BusinessTypeEnum businessType() default BusinessTypeEnum.SELECT;

    String value() default "";

    boolean saveResult() default true;
}