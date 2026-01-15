package com.maple.generator.util;

import com.maple.common.config.exception.ErrorCode;
import com.maple.common.config.exception.MapleCommonException;
import com.maple.generator.constant.Constants;
import lombok.extern.slf4j.Slf4j;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.runtime.RuntimeConstants;

import java.util.Properties;

@Slf4j
public class VelocityInitializer {

    public static void initVelocity() {
        Properties p = new Properties();
        try {

            p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");

            p.setProperty(RuntimeConstants.INPUT_ENCODING, Constants.UTF8);

            Velocity.init(p);
        } catch (Exception e) {
            log.error("VelocityEngine工厂异常", e);
            throw new MapleCommonException(ErrorCode.COMMON_ERROR, e.getMessage());
        }
    }
}
