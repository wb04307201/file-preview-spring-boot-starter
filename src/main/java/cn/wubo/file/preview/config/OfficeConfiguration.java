package cn.wubo.file.preview.config;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.exception.PreviewRuntimeException;
import cn.wubo.file.preview.exception.RecordRuntimeException;
import cn.wubo.file.preview.exception.StorageRuntimeException;
import cn.wubo.file.preview.function.PreviewFunction;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.office.impl.JodOfficeConverter;
import cn.wubo.file.preview.office.impl.NoneConverter;
import cn.wubo.file.preview.office.impl.SpireOfficeConverter;
import cn.wubo.file.preview.page.PageFactory;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.record.impl.MemFilePreviewRecordImpl;
import cn.wubo.file.preview.render.IRenderPage;
import cn.wubo.file.preview.result.Result;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.storage.impl.LocalFileStorageImpl;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import cn.wubo.file.preview.utils.PageUtils;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;
import jakarta.servlet.http.Part;
import org.jodconverter.core.DocumentConverter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.util.CollectionUtils;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.function.*;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    @ConditionalOnExpression("'only'.equals('${file.preview.officeConverter}') || 'lool'.equals('${file.preview.officeConverter}') || 'cool'.equals('${file.preview.officeConverter}')")
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

    private static final String LOST_ID = "请求参数id丢失!";

    private final PreviewFunction<ServerRequest, FilePreviewService, List<IRenderPage>, ServerResponse> previewFunction = (request, service, rps) -> {
        String id = request.param("id").orElseThrow(() -> new PreviewRuntimeException(LOST_ID));
        FilePreviewInfo info = service.findById(id);
        if (!CollectionUtils.isEmpty(rps)) {
            Optional<IRenderPage> optionalIRenderPage = rps.stream().filter(rp -> rp.support(service, info)).findAny();
            if (optionalIRenderPage.isPresent()) return optionalIRenderPage.get().render(service, info);
        }
        rps.stream().filter(rp -> rp.support(service, info)).findAny().ifPresent(rp -> rp.render(service, info));

        String contextPath = request.requestPath().contextPath().value();
        return PageFactory.create(info, service, properties, contextPath).build();
    };

    private final Function<String, byte[]> downloadFunction = url -> {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);
        return response.getBody();
    };

    /**
     * 添加onlyoffice回调路由函数
     * @param builder 路由函数构建器
     */
    private void addOnlyOfficeCallback(RouterFunctions.Builder builder) {
        // 添加onlyoffice回调路由函数
        builder.POST("/file/preview/onlyoffice/callback", request -> {
            // 获取请求参数
            MultiValueMap<String, String> params = request.params();
            Integer status = 0;
            if (params.containsKey("status"))
                // 解析状态参数
                status = Integer.parseInt(Objects.requireNonNull(params.getFirst("status")));
            if (status == 2 || status == 3) {
                // 获取文件id
                String id = params.getFirst("id");
                // 取回修改后文件
                byte[] fileBytes = downloadFunction.apply(params.getFirst("url"));
                // 变更记录下载
                byte[] changeBytes = downloadFunction.apply(params.getFirst("changesurl"));
            }
            // 返回响应
            return ServerResponse.ok().body("{\"error\":0}");
        });
    }

    /**
     * 添加WOPi路由函数
     * @param builder 路由函数构建器
     * @param filePreviewService 文件预览服务
     */
    private void addWopi(RouterFunctions.Builder builder, FilePreviewService filePreviewService) {
        // 获取文件信息
        builder.GET("/wopi/files/{id}", request -> {
            String id = request.pathVariable("id"); // 获取文件id
            FilePreviewInfo info = filePreviewService.findById(id); // 根据id获取文件预览信息
            byte[] bytes = filePreviewService.getBytes(info); // 根据文件信息获取文件内容
            Map<String, Object> map = new HashMap<>(); // 创建一个HashMap用于存储文件信息
            map.put("BaseFileName", info.getFileName()); // 添加文件名到map中
            map.put("Size", bytes.length); // 添加文件大小到map中
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(map); // 返回文件信息
        }).GET("/wopi/files/{id}/contents", request -> {
            String id = request.pathVariable("id"); // 获取文件id
            FilePreviewInfo info = filePreviewService.findById(id); // 根据id获取文件预览信息
            byte[] bytes = filePreviewService.getBytes(info); // 根据文件信息获取文件内容
            return ServerResponse.ok().contentType(MediaType.parseMediaType(FileUtils.getMimeType(info.getFileName()))) // 设置响应内容类型
                    .contentLength(bytes.length) // 设置响应内容长度
                    .header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(info.getFileName()).getBytes(), StandardCharsets.ISO_8859_1)) // 设置响应头的Content-Disposition字段，指定下载文件名
                    .build((res, req) -> {
                        try (OutputStream os = req.getOutputStream()) {
                            IoUtils.writeToStream(bytes, os); // 将文件内容写入响应体
                        } catch (IOException e) {
                            throw new PageRuntimeException(e.getMessage(), e); // 抛出异常
                        }
                        return new ModelAndView(); // 返回空的结果
                    });
        });
    }

    /**
     * 文件预览路由
     *
     * @param filePreviewService 文件预览服务
     * @param renderPages        渲染页面列表
     * @return 路由器功能
     */
    @Bean("wb04307201FilePreviewRouter")
    public RouterFunction<ServerResponse> filePreviewRouter(FilePreviewService filePreviewService, List<IRenderPage> renderPages) {
        RouterFunctions.Builder builder = RouterFunctions.route().GET("/file/preview/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> {
            Map<String, Object> data = new HashMap<>();
            data.put("contextPath", request.requestPath().contextPath().value());
            return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(PageUtils.write("list1.ftl", data));
        }).POST("/file/preview/list", request -> {
            FilePreviewInfo filePreviewInfo = request.body(FilePreviewInfo.class);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(filePreviewService.list(filePreviewInfo)));
        }).POST("/file/preview/upload", request -> {
            Part part = request.multipartData().getFirst("file");
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(filePreviewService.covert(part.getInputStream(), part.getSubmittedFileName()));
        }).GET("/file/preview/delete", request -> {
            String id = request.param("id").orElseThrow(() -> new IllegalArgumentException(LOST_ID));
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(filePreviewService.delete(id)));
        }).GET("/file/preview/download", request -> {
            String id = request.param("id").orElseThrow(() -> new IllegalArgumentException(LOST_ID));
            FilePreviewInfo info = filePreviewService.findById(id);
            byte[] bytes = filePreviewService.getBytes(info);
            return ServerResponse.ok().contentType(MediaType.parseMediaType(FileUtils.getMimeType(info.getFileName()))).contentLength(bytes.length).header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(info.getFileName()).getBytes(), StandardCharsets.ISO_8859_1)).build((res, req) -> {
                try (OutputStream os = req.getOutputStream()) {
                    IoUtils.writeToStream(bytes, os);
                } catch (IOException e) {
                    throw new PageRuntimeException(e.getMessage(), e);
                }
                return null;
            });
        }).GET("/file/preview", request -> previewFunction.apply(request, filePreviewService, renderPages));

        if ("only".equals(properties.getOfficeConverter())) addOnlyOfficeCallback(builder);
        else if ("cool".equals(properties.getOfficeConverter())) addWopi(builder, filePreviewService);

        return builder.build();
    }

}
