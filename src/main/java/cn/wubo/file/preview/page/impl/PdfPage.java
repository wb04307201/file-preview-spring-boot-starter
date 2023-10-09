package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.AbstractPage;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class PdfPage extends AbstractPage {
    public PdfPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, filePreviewService, properties, resp);
    }

    @Override
    public void build() throws IOException {
        sendRedirect(String.format("%s/pdfjs/3.0.279/web/viewer.html?file=%s/file/preview/download?id=%s", getContextPath(), getContextPath(), getInfo().getId()));
    }
}
