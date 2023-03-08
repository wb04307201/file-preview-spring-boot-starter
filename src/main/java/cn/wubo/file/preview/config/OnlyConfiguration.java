package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.OnlyOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.servlet.DeleteServlet;
import cn.wubo.file.preview.servlet.DownloadServlet;
import cn.wubo.file.preview.servlet.OnlyOfficeCallbackServlet;
import cn.wubo.file.preview.servlet.OnlyOfficePreviewServlet;
import cn.wubo.file.preview.storage.IFileStorage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class OnlyConfiguration {

    FilePreviewProperties properties;

    public OnlyConfiguration(FilePreviewProperties properties) {
        this.properties = properties;
    }

    @Bean
    public IOfficeConverter officeConverter() {
        return new OnlyOfficeConverter();
    }

    @Bean
    public FilePreviewService filePreviewService(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        return new FilePreviewService(officeConverter, fileStorage, filePreviewRecord);
    }

    @Bean
    public ServletRegistrationBean<OnlyOfficePreviewServlet> filePreviewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        ServletRegistrationBean<OnlyOfficePreviewServlet> registration = new ServletRegistrationBean<>();
        OnlyOfficePreviewServlet onlyOfficePreviewServlet = new OnlyOfficePreviewServlet(filePreviewRecord, fileStorage, properties.getOnlyOffice());
        registration.setServlet(onlyOfficePreviewServlet);
        registration.addUrlMappings("/file/preview");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<OnlyOfficeCallbackServlet> filePreviewCallbackServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        ServletRegistrationBean<OnlyOfficeCallbackServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficeCallbackServlet(filePreviewRecord, fileStorage));
        registration.addUrlMappings("/file/preview/onlyoffice/callback");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DeleteServlet> filePreviewDeleteServlet(FilePreviewService filePreviewService) {
        ServletRegistrationBean<DeleteServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DeleteServlet(filePreviewService));
        registration.addUrlMappings("/file/preview/delete");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DownloadServlet> filePreviewDownloadServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        ServletRegistrationBean<DownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DownloadServlet(filePreviewRecord, fileStorage));
        registration.addUrlMappings("/file/preview/download");
        return registration;
    }
}
