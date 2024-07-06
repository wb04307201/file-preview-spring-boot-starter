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

    /**
     * 创建并初始化文件预览服务。
     * 该方法根据配置的属性选择对应的文件存储和文件预览记录实现，并基于提供的office转换器创建文件预览服务实例。
     *
     * @param officeConverter 用于文件格式转换的IOfficeConverter接口实例。
     * @param fileStorageList 文件存储实现的列表，用于根据配置选择合适的文件存储服务。
     * @param filePreviewRecordList 文件预览记录实现的列表，用于根据配置选择合适的文件预览记录服务。
     * @return 初始化后的FilePreviewService实例，提供文件预览功能。
     * @throws StorageRuntimeException 如果根据配置无法找到对应的IFileStorage实现。
     * @throws RecordRuntimeException 如果根据配置无法找到对应的IFilePreviewRecord实现。
     */
    @Bean
    public FilePreviewService filePreviewService(IOfficeConverter officeConverter, List<IFileStorage> fileStorageList, List<IFilePreviewRecord> filePreviewRecordList) {
        // @formatter:off
        // 根据配置选择并初始化文件存储服务
        IFileStorage fileStorage = fileStorageList.stream()
                .filter(obj -> obj.getClass().getName().equals(properties.getFileStorage()))
                .findAny()
                .orElseThrow(() -> new StorageRuntimeException(String.format("未找到%s对应的bean，无法加载IFileStorage！", properties.getFileStorage())));
        fileStorage.init();

        // 根据配置选择并初始化文件预览记录服务
        IFilePreviewRecord filePreviewRecord = filePreviewRecordList.stream()
                .filter(obj -> obj.getClass().getName().equals(properties.getFilePreviewRecord()))
                .findAny()
                .orElseThrow(() -> new RecordRuntimeException(String.format("未找到%s对应的bean，无法加载IFilePreviewRecord！", properties.getFilePreviewRecord())));
        filePreviewRecord.init();
        // @formatter:on

        // 创建并返回文件预览服务实例
        return new FilePreviewService(officeConverter, fileStorage, filePreviewRecord);
    }

    private static final String LOST_ID = "请求参数id丢失!";

    /**
     * 预览功能的lambda表达式。该函数接收一个服务器请求，使用给定的文件预览服务和渲染页面列表来生成并返回一个服务器响应。
     *
     * @param request 服务器请求，用于获取请求参数和路径信息。
     * @param service 文件预览服务，用于根据请求参数查找文件预览信息。
     * @param rps 渲染页面列表，是一组支持不同文件类型的渲染页面实现。
     * @return 服务器响应，包含根据请求渲染的预览页面。
     */
    private final PreviewFunction<ServerRequest, FilePreviewService, List<IRenderPage>, ServerResponse> previewFunction = (request, service, rps) -> {
        // 从请求中获取预览的文件ID，如果不存在则抛出异常
        String id = request.param("id").orElseThrow(() -> new PreviewRuntimeException(LOST_ID));
        // 根据ID查找文件预览信息
        FilePreviewInfo info = service.findById(id);

        // 检查是否有支持当前文件类型的渲染页面，如果有，则使用该渲染页面生成并返回服务器响应
        if (!CollectionUtils.isEmpty(rps)) {
            Optional<IRenderPage> optionalIRenderPage = rps.stream().filter(rp -> rp.support(service, info)).findAny();
            if (optionalIRenderPage.isPresent()) return optionalIRenderPage.get().render(service, info);
        }

        // 如果没有找到支持的渲染页面，则使用默认的渲染机制生成并返回服务器响应
        rps.stream().filter(rp -> rp.support(service, info)).findAny().ifPresent(rp -> rp.render(service, info));

        // 构建并返回服务器响应，包含根据请求渲染的预览页面
        String contextPath = request.requestPath().contextPath().value();
        return PageFactory.create(info, service, properties, contextPath).build();
    };

    /**
     * 下载指定URL的内容并返回其字节码数组。
     * 该函数使用RestTemplate通过GET方法请求指定的URL，并将返回的内容作为字节码数组返回。
     *
     * @param url 要下载内容的URL字符串
     * @return 从指定URL下载的内容的字节码数组
     */
    private final Function<String, byte[]> downloadFunction = url -> {
        // 创建RestTemplate实例用于执行HTTP请求
        RestTemplate restTemplate = new RestTemplate();

        // 初始化HTTP请求头
        HttpHeaders headers = new HttpHeaders();
        // 构建HTTP请求实体
        HttpEntity<Resource> httpEntity = new HttpEntity<>(headers);

        // 执行HTTP GET请求，并将响应内容转换为字节码数组
        ResponseEntity<byte[]> response = restTemplate.exchange(url, HttpMethod.GET, httpEntity, byte[].class);

        // 返回获取到的字节码数组
        return response.getBody();
    };

    /**
     * 添加onlyoffice回调路由函数
     *
     * 该函数用于通过路由函数构建器构建一个处理OnlyOffice预览回调的路由。
     * 当OnlyOffice编辑文件后需要回调应用时，会发送请求到此路由进行处理。
     *
     * @param builder 路由函数构建器，用于构建路由函数。
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
                // 处理文件编辑完成后的逻辑
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
     *
     * 该方法用于通过RouterFunctions.Builder构建两个GET请求的路由：
     * 1. 用于获取文件信息；
     * 2. 用于获取文件内容。
     *
     * @param builder            路由函数构建器，用于构建和注册路由函数。
     * @param filePreviewService 文件预览服务，提供文件信息查询和文件内容获取的功能。
     */
    private void addWopi(RouterFunctions.Builder builder, FilePreviewService filePreviewService) {
        // 获取文件信息的路由处理
        builder.GET("/wopi/files/{id}", request -> {
            String id = request.pathVariable("id"); // 从请求路径中获取文件id
            FilePreviewInfo info = filePreviewService.findById(id); // 根据id查询文件预览信息
            byte[] bytes = filePreviewService.getBytes(info); // 根据文件信息获取文件内容
            Map<String, Object> map = new HashMap<>(); // 创建HashMap用于封装文件信息
            map.put("BaseFileName", info.getFileName()); // 将文件名添加到map中
            map.put("Size", bytes.length); // 将文件大小添加到map中
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(map); // 以JSON格式返回文件信息
        }).GET("/wopi/files/{id}/contents", request -> {
            String id = request.pathVariable("id"); // 从请求路径中获取文件id
            FilePreviewInfo info = filePreviewService.findById(id); // 根据id查询文件预览信息
            byte[] bytes = filePreviewService.getBytes(info); // 根据文件信息获取文件内容
            // @formatter:off
            // 构建返回文件内容的响应
            return ServerResponse.ok().contentType(MediaType.parseMediaType(FileUtils.getMimeType(info.getFileName()))) // 根据文件名设置响应的内容类型
                    .contentLength(bytes.length) // 设置响应内容的长度
                    .header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(info.getFileName()).getBytes(), StandardCharsets.ISO_8859_1)) // 设置响应头，指定文件下载名称
                    .build((res, req) -> {
                        try (OutputStream os = req.getOutputStream()) {
                            IoUtils.writeToStream(bytes, os); // 将文件内容写入响应输出流
                        } catch (IOException e) {
                            throw new PageRuntimeException(e.getMessage(), e); // 文件写入发生异常时，抛出异常
                        }
                        return new ModelAndView(); // 返回空的ModelAndView对象
                    });
            // @formatter:on
        });
    }

    /**
     * 构建文件预览的路由功能。
     *
     * @param filePreviewService 文件预览服务，用于处理文件预览相关的业务逻辑。
     * @param renderPages 渲染页面列表，提供不同的页面渲染能力。
     * @return RouterFunction<ServerResponse> 路由器功能，用于处理HTTP请求并返回响应。
     */
    @Bean("wb04307201FilePreviewRouter")
    public RouterFunction<ServerResponse> filePreviewRouter(FilePreviewService filePreviewService, List<IRenderPage> renderPages) {
        RouterFunctions.Builder builder = RouterFunctions.route();

        // 如果启用了Web和REST功能，添加文件预览列表的HTML页面渲染。
        if (properties.getEnableWeb() && properties.getEnableRest()) {
            builder.GET("/file/preview/list", RequestPredicates.accept(MediaType.TEXT_HTML), request -> {
                Map<String, Object> data = new HashMap<>();
                data.put("contextPath", request.requestPath().contextPath().value());
                // 渲染并返回文件预览列表页面。
                return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(PageUtils.write("list.ftl", data));
            });
        }

        // 如果启用了REST功能，添加文件预览的RESTful API处理。
        if (properties.getEnableRest()) {
            // 处理文件预览信息列表的请求。
            builder.POST("/file/preview/list", request -> {
                FilePreviewInfo filePreviewInfo = request.body(FilePreviewInfo.class);
                // 返回文件预览信息列表。
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(filePreviewService.list(filePreviewInfo)));
            }).POST("/file/preview/upload", request -> {
                Part part = request.multipartData().getFirst("file");
                // 处理文件上传请求，返回上传结果。
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(filePreviewService.covert(part.getInputStream(), part.getSubmittedFileName())));
            }).GET("/file/preview/delete", request -> {
                String id = request.param("id").orElseThrow(() -> new IllegalArgumentException(LOST_ID));
                // 处理文件预览信息删除请求。
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Result.success(filePreviewService.delete(id)));
            }).GET("/file/preview/download", request -> {
                String id = request.param("id").orElseThrow(() -> new IllegalArgumentException(LOST_ID));
                FilePreviewInfo info = filePreviewService.findById(id);
                byte[] bytes = filePreviewService.getBytes(info);
                // @formatter:off
                // 处理文件下载请求，返回文件内容。
                return ServerResponse.ok().contentType(MediaType.parseMediaType(FileUtils.getMimeType(info.getFileName())))
                        .contentLength(bytes.length)
                        .header("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(info.getFileName()).getBytes(), StandardCharsets.ISO_8859_1))
                        .build((res, req) -> {
                    try (OutputStream os = req.getOutputStream()) {
                        IoUtils.writeToStream(bytes, os);
                    } catch (IOException e) {
                        throw new PageRuntimeException(e.getMessage(), e);
                    }
                    return null;
                });
                // @formatter:on
            });
        }

        // 添加文件预览功能路由。
        builder.GET("/file/preview", request -> previewFunction.apply(request, filePreviewService, renderPages));

        // 根据配置的文档转换器类型，添加相应的回调路由。
        if ("only".equals(properties.getOfficeConverter())) addOnlyOfficeCallback(builder);
        else if ("cool".equals(properties.getOfficeConverter())) addWopi(builder, filePreviewService);

        // 构建并返回最终的路由功能。
        return builder.build();
    }
}
