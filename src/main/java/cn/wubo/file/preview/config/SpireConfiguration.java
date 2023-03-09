package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.SpireOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.servlet.DeleteServlet;
import cn.wubo.file.preview.servlet.DownloadServlet;
import cn.wubo.file.preview.servlet.PreviewServlet;
import cn.wubo.file.preview.storage.IFileStorage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class SpireConfiguration {

    @Bean
    public IOfficeConverter officeConverter() {
        return new SpireOfficeConverter();
    }

    @Bean
    public FilePreviewService filePreviewService(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        return new FilePreviewService(officeConverter, fileStorage, filePreviewRecord);
    }

    @Bean
    public ServletRegistrationBean<PreviewServlet> filePreviewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet(filePreviewRecord, fileStorage));
        registration.addUrlMappings("/file/preview");
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