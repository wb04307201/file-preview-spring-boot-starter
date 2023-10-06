package cn.wubo.file.preview.page;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
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
    private HttpServletResponse resp;

    protected AbstractPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties, HttpServletResponse resp) {
        this.fileType = fileType;
        this.extName = extName;
        this.contextPath = contextPath;
        this.info = info;
        this.filePreviewService = filePreviewService;
        this.properties = properties;
        this.resp = resp;
    }

    protected void writePage(String templateName, Map<String, Object> data) {
        try (PrintWriter printWriter = resp.getWriter()) {
            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            Template template = cfg.getTemplate(templateName, "UTF-8");
            template.process(data, printWriter);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendRedirect(String url) throws IOException {
        resp.sendRedirect(url);
    }
}
