package com.waiterxiaoyy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 功能描述：
 *
 * @Author WaiterXiaoYY
 * @Date 2022/3/2 23:48
 * @Version 1.0
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Value("${waiterxiaoyy.file.LocalPath}")
    private String LocalPath;
    /**
     * springboot 无法直接访问静态资源，需要放开资源访问路径。
     * 添加静态资源文件，外部可以直接访问地址
     * @param registry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // 意思是我们通过项目访问资源路径为/localPath开头的将会被映射到D:/Temp下
        registry.addResourceHandler("/localPath/**").addResourceLocations("file:///" + LocalPath + "/");
    }

}
