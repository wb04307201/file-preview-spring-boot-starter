package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.AbstractPage;
import org.springframework.web.servlet.function.ServerResponse;

public class PdfPage extends AbstractPage {
    public PdfPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        super(fileType, extName, contextPath, info, filePreviewService, properties);
    }

    @Override
    public ServerResponse build() {
        return sendRedirect(String.format("%s/file/preview/static/pdfjs/4.0.379/web/viewer.html?file=%s/file/preview/download?id=%s", getContextPath(), getContextPath(), getInfo().getId()));
    }
}
