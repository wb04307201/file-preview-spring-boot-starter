package cn.wubo.file.online.common;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

@Data
public class Page {

    private String templateName;
    private Map<String, Object> params;
    private HttpServletResponse resp;

    public Page(String templateName, Map<String, Object> params, HttpServletResponse resp) {
        this.templateName = templateName;
        this.params = params;
        this.resp = resp;
    }

    public void write() {
        try (PrintWriter printWriter = resp.getWriter()) {
            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            Template template = cfg.getTemplate(templateName, "UTF-8");
            template.process(params, printWriter);
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }
}
