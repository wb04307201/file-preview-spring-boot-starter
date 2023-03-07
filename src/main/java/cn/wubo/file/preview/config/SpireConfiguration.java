package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.IFilePreviewService;
import cn.wubo.file.preview.core.impl.FilePreviewServiceImpl;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.SpireOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.servlet.list.FileListServlet;
import cn.wubo.file.preview.servlet.preview.DownloadServlet;
import cn.wubo.file.preview.servlet.preview.PreviewServlet;
import cn.wubo.file.preview.storage.IFileStorage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class SpireConfiguration {

    FilePreviewProperties properties;

    public SpireConfiguration(FilePreviewProperties properties) {
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
    public IOfficeConverter officeConverter() {
        return new SpireOfficeConverter();
    }

    @Bean
    public IFilePreviewService filePreviewService(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        return new FilePreviewServiceImpl(officeConverter, fileStorage, filePreviewRecord);
    }

    @Bean
    public ServletRegistrationBean<PreviewServlet> previewServlet() {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet());
        registration.addUrlMappings("/file/preview");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DownloadServlet> onlyOfficeDownloadServlet() {
        ServletRegistrationBean<DownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DownloadServlet());
        registration.addUrlMappings("/file/preview/download");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<FileListServlet> listServlet(IFilePreviewRecord filePreviewRecord) {
        ServletRegistrationBean<FileListServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileListServlet(filePreviewRecord));
        registration.addUrlMappings("/file/preview/list");
        return registration;
    }
}
