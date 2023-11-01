package cn.wubo.file.preview.utils;

import cn.wubo.file.preview.exception.PageRuntimeException;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

public class PageUtils {

    private PageUtils() {
    }

    public static String write(String templateName, Map<String, Object> params) {
        try (StringWriter sw = new StringWriter()) {
            freemarker.template.Configuration cfg = new freemarker.template.Configuration(freemarker.template.Configuration.VERSION_2_3_23);
            cfg.setClassForTemplateLoading(this.getClass(), "/template");
            Template template = cfg.getTemplate(templateName, "UTF-8");
            template.process(params, sw);
            return sw.toString();
        } catch (TemplateException | IOException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }
}
