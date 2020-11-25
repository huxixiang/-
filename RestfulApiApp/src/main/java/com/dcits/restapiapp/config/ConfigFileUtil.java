package com.dcits.restapiapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class ConfigFileUtil {
    private static Logger logger = LoggerFactory.getLogger(ConfigFileUtil.class);

    public static final Properties configProperties = new Properties();
    static{
        try {
            configProperties.load(ConfigFileUtil.class.getClassLoader().getResourceAsStream("application.properties"));
        }catch (Exception e){
            logger.error("【加载配置文件出错："+e.getMessage()+"】");
            e.printStackTrace();
        }
    }


}
