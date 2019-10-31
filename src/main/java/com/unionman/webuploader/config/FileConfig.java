package com.unionman.webuploader.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

/**
 * 文件上传配置
 * @author Mr.J
 * @date 2019/10/31 21:59
 */
@Configuration
public class FileConfig {


    /**
     * 文件上传配置
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement(
            @Value("${multipart.maxFileSize}") String maxFileSize,
            @Value("${multipart.maxRequestSize}") String maxRequestSize,
            @Value("${multipart.uploadFolder}") String uploadFolder) {
        MultipartConfigFactory factory = new MultipartConfigFactory();

        // 单个文件最大
        factory.setMaxFileSize(DataSize.parse(maxFileSize));

        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));

        factory.setLocation(uploadFolder);

        return factory.createMultipartConfig();
    }


}
