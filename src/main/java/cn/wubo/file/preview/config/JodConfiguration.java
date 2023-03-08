package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.IFilePreviewService;
import cn.wubo.file.preview.core.impl.FilePreviewServiceImpl;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.JodOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.servlet.DeleteServlet;
import cn.wubo.file.preview.servlet.DownloadServlet;
import cn.wubo.file.preview.servlet.PreviewServlet;
import cn.wubo.file.preview.storage.IFileStorage;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;

public class JodConfiguration {

    @Bean
    public IOfficeConverter officeConverter() {
        return new JodOfficeConverter();
    }

    @Bean
    public IFilePreviewService filePreviewService(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        return new FilePreviewServiceImpl(officeConverter, fileStorage, filePreviewRecord);
    }

    @Bean
    public ServletRegistrationBean<PreviewServlet> previewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet(filePreviewRecord, fileStorage));
        registration.addUrlMappings("/file/preview");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DeleteServlet> deleteServlet(IFilePreviewService filePreviewService) {
        ServletRegistrationBean<DeleteServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DeleteServlet(filePreviewService));
        registration.addUrlMappings("/file/preview/delete");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<DownloadServlet> downloadServlet(IFilePreviewService filePreviewService) {
        ServletRegistrationBean<DownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DownloadServlet(filePreviewService));
        registration.addUrlMappings("/file/preview/download");
        return registration;
    }
}
