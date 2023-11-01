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
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.storage.impl.LocalFileStorageImpl;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.PageUtils;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;
import org.jodconverter.core.DocumentConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.function.*;
import org.springframework.web.util.HtmlUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

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

            return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(PageUtils.write("list.ftl", data));
        };

        BiFunction<ServerRequest, FilePreviewService, ServerResponse> previewFunction = (request, service) -> {
            String id = request.param("id").orElseThrow(() -> new PreviewRuntimeException("请求参数id丢失!"));
            FilePreviewInfo info = filePreviewService.findById(id);
            String contextPath = request.requestPath().contextPath().value();
            return PageFactory.create(info, filePreviewService, properties, contextPath).build();
        };

        RouterFunctions.Builder builder = RouterFunctions.route().GET("/file/preview/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> listFunction.apply(request, filePreviewService)).POST("/file/preview/list", RequestPredicates.accept(MediaType.APPLICATION_FORM_URLENCODED), request -> listFunction.apply(request, filePreviewService)).GET("/file/preview", request -> previewFunction.apply(request, filePreviewService));

        if ("only".equals(properties.getOfficeConverter())) {
            Function<String, byte[]> downloadFunction = url -> {
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
                ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
                return response.getBody();
            };

            builder.POST("/file/preview/onlyoffice/callback", request -> {
                MultiValueMap<String, String> params = request.params();
                Integer status = 0;
                if (params.containsKey("status"))
                    status = Integer.parseInt(Objects.requireNonNull(params.getFirst("status")));
                if (status == 2 || status == 3) {
                    String id = params.getFirst("id");
                    // 取回修改后文件
                    byte[] fileBytes = downloadFunction.apply(params.getFirst("url"));
                    // 变更记录下载
                    byte[] changeBytes = downloadFunction.apply(params.getFirst("changesurl"));
                }
                return ServerResponse.ok().body("{\"error\":0}");
            });
        }

        builder.GET("/file/preview/delete", request -> {
            String id = request.param("id").orElseThrow(() -> new PreviewRuntimeException("请求参数id丢失!"));
            filePreviewService.delete(id);
            return ServerResponse.permanentRedirect(new URI(request.requestPath().contextPath().value() + "/file/preview/list")).build();
        }).GET("/file/preview/download", request -> {
            String id = request.param("id").orElseThrow(() -> new PreviewRuntimeException("请求参数id丢失!"));
            FilePreviewInfo info = filePreviewService.findById(id);
            try (InputStream is = new ByteArrayInputStream(filePreviewService.getBytes(info))) {
                return ServerResponse.ok().contentType(MediaType.parseMediaType(FileUtils.getMimeType(info.getFileName()))).header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(info.getFileName()).getBytes(), StandardCharsets.ISO_8859_1)).body(is);
            }
        });

        return builder.build();
    }
}
