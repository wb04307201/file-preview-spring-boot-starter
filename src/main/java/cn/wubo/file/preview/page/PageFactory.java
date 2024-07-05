package cn.wubo.file.preview.page;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.utils.FileUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PageFactory {

    private static final Set<String> OFFICE_FILE_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("word", "excel", "power point", "text")));

    private PageFactory() {
    }

    /**
     * 根据文件预览信息、文件预览服务、文件预览属性和上下文路径创建抽象页面。
     * 此方法根据文件类型和配置的办公文档转换器类型，动态决定创建哪种类型的页面。
     *
     * @param info 文件预览信息，包含文件名等信息。
     * @param filePreviewService 文件预览服务，用于提供文件预览的功能支持。
     * @param properties 文件预览属性，包含配置信息，如办公文档转换器类型。
     * @param contextPath 上下文路径，用于构造页面的URL。
     * @return 创建的抽象页面实例。
     * @throws PageRuntimeException 如果页面创建过程中发生异常。
     */
    public static AbstractPage create(FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties, String contextPath) {
        try {
            // 获取文件的扩展名
            String extName = FileUtils.extName(info.getFileName());
            // 根据扩展名确定文件类型
            String fileType = FileUtils.fileType(extName);

            Class<? extends AbstractPage> clazz;

            // 根据配置的办公文档转换器类型和文件类型，决定创建的页面类
            if ("only".equals(properties.getOfficeConverter()) && OFFICE_FILE_TYPES.contains(fileType)) {
                clazz = PageType.getClass("only");
            } else if ("lool".equals(properties.getOfficeConverter()) && OFFICE_FILE_TYPES.contains(fileType)) {
                clazz = PageType.getClass("lool");
            } else if ("cool".equals(properties.getOfficeConverter()) && OFFICE_FILE_TYPES.contains(fileType)) {
                clazz = PageType.getClass("cool");
            } else {
                clazz = PageType.getClass(fileType);
            }

            // 通过反射创建页面类的实例，并传递必要的参数
            return clazz.getConstructor(String.class, String.class, String.class, FilePreviewInfo.class, FilePreviewService.class, FilePreviewProperties.class)
                        .newInstance(fileType, extName, contextPath, info, filePreviewService, properties);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            // 如果在创建页面过程中发生异常，抛出自定义的运行时异常
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }
}
