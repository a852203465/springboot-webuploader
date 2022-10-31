package cn.darkjrong.webuploader.common.config;

import cn.darkjrong.webuploader.common.constants.FileConstant;
import cn.hutool.core.util.StrUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.File;

/**
 * 文件上传配置
 *
 * @author Rong.Jia
 * @date 2022/10/31
 */
@Data
@Component
@ConfigurationProperties(prefix = "multipart")
public class FileUploadConfig {

    private String maxFileSize;
    private String maxRequestSize;
    private String uploadFolder;
    private Long chunkSize;

    /**
     * 文件上传配置
     * @return MultipartConfigElement
     */
    @Bean
    public MultipartConfigElement multipartConfigElement() {

        MultipartConfigFactory factory = new MultipartConfigFactory();

        //路径有可能限制
        String location = StrUtil.startWith(uploadFolder, StrUtil.SLASH) ? uploadFolder : FileConstant.SEPARATOR + uploadFolder;

        File tmpFile = new File(location);
        if (!tmpFile.exists()) {
            tmpFile.mkdirs();
        }

        // 当上传文件达到10MB的时候进行磁盘写入
        //factory.setFileSizeThreshold(DataSize.ofMegabytes(10));

        // 单个文件最大
        factory.setMaxFileSize(DataSize.parse(maxFileSize));

        // 设置总上传数据总大小
        factory.setMaxRequestSize(DataSize.parse(maxRequestSize));

        factory.setLocation(uploadFolder);

        return factory.createMultipartConfig();
    }


}
