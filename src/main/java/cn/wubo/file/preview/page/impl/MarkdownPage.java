package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.AbstractPage;
import cn.wubo.file.preview.utils.IoUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MarkdownPage extends AbstractPage {
    public MarkdownPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, filePreviewService, properties, resp);
    }

    @Override
    public void build() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        data.put("content", IoUtils.readByte(getFilePreviewService().getBytes(getInfo()), getInfo().getFileName()));
        writePage("markdown.ftl", data);
    }
}
