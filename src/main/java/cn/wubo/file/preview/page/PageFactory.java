package cn.wubo.file.preview.page;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PageFactory {

    private static final Set<String> OFFICE_FILE_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("word", "excel", "power point", "txt")));

    public static AbstractPage create(FilePreviewInfo info, IFileStorage fileStorage, FilePreviewProperties properties, HttpServletRequest req, HttpServletResponse resp) {
        try {
            String contextPath = req.getContextPath();
            String extName = FileUtils.extName(info.getFileName());
            String fileType = FileUtils.fileType(extName);

            Class<? extends AbstractPage> clazz;
            if ("only".equals(properties.getOfficeConverter()) && OFFICE_FILE_TYPES.contains(fileType)) {
                clazz = PageType.getClass("only");
            } else if ("lool".equals(properties.getOfficeConverter()) && OFFICE_FILE_TYPES.contains(fileType)) {
                clazz = PageType.getClass("lool");
            } else {
                clazz = PageType.getClass(fileType);
            }
            return clazz.getConstructor(String.class, String.class, String.class, FilePreviewInfo.class, IFileStorage.class, FilePreviewProperties.class, HttpServletResponse.class).newInstance(fileType, extName, contextPath, info, fileStorage, properties, resp);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException |
                 InvocationTargetException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }
}
