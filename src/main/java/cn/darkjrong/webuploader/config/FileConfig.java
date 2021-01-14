package cn.darkjrong.webuploader.config;

import cn.darkjrong.webuploader.constants.CommonConstant;
import cn.darkjrong.webuploader.constants.FileConstant;
import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;
import java.io.File;

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

        //路径有可能限制
        String location = System.getProperty(FileConstant.USER_DIR);
        location = StrUtil.startWith(uploadFolder, CommonConstant.SLASH)
                ? location + uploadFolder : location +FileConstant.SEPARATOR + uploadFolder;

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
