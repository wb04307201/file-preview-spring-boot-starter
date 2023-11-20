package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.AbstractPage;
import org.springframework.web.servlet.function.ServerResponse;

public class CoolPage extends AbstractPage {
    public CoolPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        super(fileType, extName, contextPath, info, filePreviewService, properties);
    }

    @Override
    public ServerResponse build() {
        return sendRedirect(String.format("%s/browser/dist/cool.html?WOPISrc=%s/wopi/files/%s", getProperties().getCollabora().getDomain(), getProperties().getCollabora().getStorageDomain(), getInfo().getId()));
    }
}
