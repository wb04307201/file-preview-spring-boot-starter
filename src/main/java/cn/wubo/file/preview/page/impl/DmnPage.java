package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.AbstractPage;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.HashMap;
import java.util.Map;

public class DmnPage extends AbstractPage {
    public DmnPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        super(fileType, extName, contextPath, info, filePreviewService, properties);
    }

    @Override
    public ServerResponse build() {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        data.put("content", readLines());
        return writePage("dmn.ftl", data);
    }
}
