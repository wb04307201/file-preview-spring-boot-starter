package cn.wubo.file.online.common;

import freemarker.template.Template;
import freemarker.template.TemplateException;
import lombok.Data;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;

@Data
public class Page {

    private String templateName;
    private Map<String, Object> params;
    private Writer out;

    public Page(String templateName, Map<String, Object> params, Writer out) {
        this.templateName = templateName;
        this.params = params;
        this.out = out;
    }

    public void write() throws IOException, TemplateException {
        freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
        cfg.setClassForTemplateLoading(this.getClass(), "/template");
        Template template = cfg.getTemplate(templateName, "UTF-8");
        template.process(params, out);
    }
}
