package cn.wubo.file.preview.config;

import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.servlet.FileListServlet;
import cn.wubo.file.preview.servlet.DownloadServlet;
import cn.wubo.file.preview.storage.IFileStorage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

@EnableConfigurationProperties({FilePreviewProperties.class})
public class OfficeConfiguration {

    FilePreviewProperties properties;

    public OfficeConfiguration(FilePreviewProperties properties) {
        this.properties = properties;
    }

    @Bean
    public IFilePreviewRecord filePreviewRecord() {
        try {
            Class clazz = Class.forName(properties.getFilePreviewRecord());
            return (IFilePreviewRecord) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public IFileStorage fileStorage() {
        try {
            Class clazz = Class.forName(properties.getFileStorage());
            return (IFileStorage) clazz.newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public ServletRegistrationBean<FileListServlet> filePreviewListServlet(IFilePreviewRecord filePreviewRecord) {
        ServletRegistrationBean<FileListServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileListServlet(filePreviewRecord));
        registration.addUrlMappings("/file/preview/list");
        return registration;
    }
}
