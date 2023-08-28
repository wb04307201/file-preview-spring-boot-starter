package cn.wubo.file.preview.page;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

@Data
public abstract class AbstractPage<T> implements IPage<T> {

    protected void writePage(String templateName, Map<String, Object> data, HttpServletResponse resp) {
        try (PrintWriter printWriter = resp.getWriter()) {
            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            Template template = cfg.getTemplate(templateName, "UTF-8");
            template.process(data, printWriter);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

    protected void sendRedirect(String url, HttpServletResponse resp) throws IOException {
        resp.sendRedirect(url);
    }
}
