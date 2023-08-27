package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.JodOfficeConverter;
import cn.wubo.file.preview.office.impl.NoneConverter;
import cn.wubo.file.preview.office.impl.SpireOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.servlet.*;
import cn.wubo.file.preview.storage.IFileStorage;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;
import org.jodconverter.core.DocumentConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
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
    @ConditionalOnExpression("'spire'.equals('${file.preview.officeConverter}')")
    @ConditionalOnClass(value = {Document.class, Workbook.class, Presentation.class})
    public IOfficeConverter spireOfficeConverter() {
        return new SpireOfficeConverter();
    }

    @Bean
    @ConditionalOnExpression("'only'.equals('${file.preview.officeConverter}') || 'lool'.equals('${file.preview.officeConverter}')")
    public IOfficeConverter noneConverter() {
        return new NoneConverter();
    }

    @Bean
    @ConditionalOnExpression("'jod'.equals('${file.preview.officeConverter:jod}')")
    @ConditionalOnBean(value = DocumentConverter.class)
    public IOfficeConverter jodOfficeConverter(DocumentConverter converter) {
        return new JodOfficeConverter(converter);
    }

    @Bean
    public IFilePreviewRecord filePreviewRecord() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(properties.getFilePreviewRecord());
        IFilePreviewRecord filePreviewRecord = (IFilePreviewRecord) clazz.newInstance();
        filePreviewRecord.init();
        return filePreviewRecord;
    }

    @Bean
    public IFileStorage fileStorage() throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class<?> clazz = Class.forName(properties.getFileStorage());
        return (IFileStorage) clazz.newInstance();
    }

    @Bean
    public FilePreviewService filePreviewService(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        return new FilePreviewService(officeConverter, fileStorage, filePreviewRecord);
    }

    @Bean
    public ServletRegistrationBean<FileListServlet> filePreviewListServlet(IFilePreviewRecord filePreviewRecord) {
        ServletRegistrationBean<FileListServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileListServlet(filePreviewRecord));
        registration.addUrlMappings("/file/preview/list");
        return registration;
    }

    @Bean
    public ServletRegistrationBean<PreviewServlet> filePreviewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet(filePreviewRecord, fileStorage, properties));
        registration.addUrlMappings("/file/preview");
        return registration;
    }

    @Bean
    @ConditionalOnExpression("#{'only'.equals(environment['file.preview.officeConverter'])}")
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
