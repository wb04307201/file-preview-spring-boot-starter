package cn.wubo.file.preview.page;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import cn.wubo.file.preview.utils.PageUtils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.function.ServerResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Data
public abstract class AbstractPage implements IPage {

    protected static final String CONTEXT_PATH = "contextPath";
    protected static final String DOCUMENT_TYPE = "documentType";
    private String fileType;
    private String extName;
    private String contextPath;
    private FilePreviewInfo info;
    private FilePreviewService filePreviewService;
    private FilePreviewProperties properties;

    protected AbstractPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        this.fileType = fileType;
        this.extName = extName;
        this.contextPath = contextPath;
        this.info = info;
        this.filePreviewService = filePreviewService;
        this.properties = properties;
    }

    protected ServerResponse writePage(String templateName, Map<String, Object> data) {
        return ServerResponse.ok().contentType(MediaType.TEXT_HTML).body(PageUtils.write(templateName, data));
    }

    protected ServerResponse sendRedirect(String url) {
        try {
            return ServerResponse.permanentRedirect(new URI(url)).contentType(MediaType.TEXT_HTML).build();
        } catch (URISyntaxException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }

    protected ServerResponse commonOutputStream() {
        try (InputStream is = new ByteArrayInputStream(filePreviewService.getBytes(info))) {
            return ServerResponse.ok().contentType(MediaType.parseMediaType(FileUtils.getMimeType(getInfo().getFileName()))).body(is);
        } catch (IOException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }

    protected String readLines() {
        try {
            return IoUtils.readLines(getFilePreviewService().getBytes(getInfo()), getInfo().getFileName());
        } catch (IOException e) {
            throw new PageRuntimeException(e.getMessage(),e);
        }
    }
}
