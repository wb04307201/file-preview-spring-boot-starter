package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PreviewRuntimeException;
import cn.wubo.file.preview.exception.RecordRuntimeException;
import cn.wubo.file.preview.exception.StorageRuntimeException;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.JodOfficeConverter;
import cn.wubo.file.preview.office.impl.NoneConverter;
import cn.wubo.file.preview.office.impl.SpireOfficeConverter;
import cn.wubo.file.preview.page.PageFactory;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.record.impl.MemFilePreviewRecordImpl;
import cn.wubo.file.preview.servlet.*;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.storage.impl.LocalFileStorageImpl;
import cn.wubo.file.preview.utils.PageUtils;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.jodconverter.core.DocumentConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.servlet.function.*;
import org.springframework.web.util.HtmlUtils;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

@EnableConfigurationProperties({FilePreviewProperties.class})
public class OfficeConfiguration {

    FilePreviewProperties properties;

    public OfficeConfiguration(FilePreviewProperties properties) {
        this.properties = properties;
    }

    @Bean
    public IFilePreviewRecord filePreviewRecord() {
        return new MemFilePreviewRecordImpl();
    }

    @Bean
    public IFileStorage fileStorage() {
        return new LocalFileStorageImpl();
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
    public FilePreviewService filePreviewService(IOfficeConverter officeConverter, List<IFileStorage> fileStorageList, List<IFilePreviewRecord> filePreviewRecordList) {
        IFileStorage fileStorage = fileStorageList.stream().filter(obj -> obj.getClass().getName().equals(properties.getFileStorage())).findAny().orElseThrow(() -> new StorageRuntimeException(String.format("未找到%s对应的bean，无法加载IFileStorage！", properties.getFileStorage())));
        fileStorage.init();
        IFilePreviewRecord filePreviewRecord = filePreviewRecordList.stream().filter(obj -> obj.getClass().getName().equals(properties.getFilePreviewRecord())).findAny().orElseThrow(() -> new RecordRuntimeException(String.format("未找到%s对应的bean，无法加载IFilePreviewRecord！", properties.getFilePreviewRecord())));
        filePreviewRecord.init();
        return new FilePreviewService(officeConverter, fileStorage, filePreviewRecord);
    }

    @Bean("wb04307201_file_preview_router")
    public RouterFunction<ServerResponse> filePreviewRouter(FilePreviewService filePreviewService) {
        BiFunction<ServerRequest, FilePreviewService, ServerResponse> listFunction = (request, service) -> {
            String contextPath = request.requestPath().contextPath().value();
            FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
            if (HttpMethod.POST.equals(request.method())) {
                MultiValueMap<String, String> params = request.params();
                filePreviewInfo.setFileName(params.getFirst("fileName"));
                filePreviewInfo.setFilePath(params.getFirst("filePath"));
            }

            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", contextPath);
            data.put("list", service.list(filePreviewInfo));
            filePreviewInfo.setFileName(HtmlUtils.htmlEscape(filePreviewInfo.getFileName() == null ? "" : filePreviewInfo.getFileName()));
            filePreviewInfo.setFilePath(HtmlUtils.htmlEscape(filePreviewInfo.getFilePath() == null ? "" : filePreviewInfo.getFilePath()));
            data.put("query", filePreviewInfo);

            return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(PageUtils.write("list.ftl",data));
        };

        BiFunction<ServerRequest, FilePreviewService, ServerResponse> previewFunction = (request, service) -> {
            String id = request.param("id").orElseThrow(() -> new PreviewRuntimeException("请求参数id丢失!"));
            FilePreviewInfo info = filePreviewService.findById(id);
            String contextPath = request.requestPath().contextPath().value();
            return PageFactory.create(info, filePreviewService, properties, contextPath).build();
        };

        RouterFunctions.Builder builder = RouterFunctions.route()
                .GET("/file/preview/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> listFunction.apply(request, filePreviewService))
                .POST("/file/preview/list", RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED), request -> listFunction.apply(request, filePreviewService))
                .GET("/file/preview", request -> previewFunction.apply(request, filePreviewService));


        return builder.build();
    }

    /*@Bean
    public ServletRegistrationBean<FileListServlet> filePreviewListServlet(FilePreviewService filePreviewService) {
        ServletRegistrationBean<FileListServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new FileListServlet(filePreviewService));
        registration.addUrlMappings("/file/preview/list");
        return registration;
    }*/

   /* @Bean
    public ServletRegistrationBean<PreviewServlet> filePreviewServlet(FilePreviewService filePreviewService) {
        ServletRegistrationBean<PreviewServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new PreviewServlet(filePreviewService, properties));
        registration.addUrlMappings("/file/preview");
        return registration;
    }*/

    @Bean
    @ConditionalOnExpression("#{'only'.equals(environment['file.preview.officeConverter'])}")
    public ServletRegistrationBean<OnlyOfficeCallbackServlet> filePreviewCallbackServlet(FilePreviewService filePreviewService) {
        ServletRegistrationBean<OnlyOfficeCallbackServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new OnlyOfficeCallbackServlet(filePreviewService));
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
    public ServletRegistrationBean<DownloadServlet> filePreviewDownloadServlet(FilePreviewService filePreviewService) {
        ServletRegistrationBean<DownloadServlet> registration = new ServletRegistrationBean<>();
        registration.setServlet(new DownloadServlet(filePreviewService));
        registration.addUrlMappings("/file/preview/download");
        return registration;
    }
}
