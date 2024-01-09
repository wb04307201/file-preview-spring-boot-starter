package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.page.AbstractPage;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LoolPage extends AbstractPage {
    public LoolPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        super(fileType, extName, contextPath, info, filePreviewService, properties);
    }

    @Override
    public ServerResponse build() {
        try {
            Path path = Paths.get(getProperties().getLibreOffice().getStorage() + File.separator + getInfo().getFileName());
            Files.deleteIfExists(path);
            Files.write(path, getFilePreviewService().getBytes(getInfo()));
            return sendRedirect(String.format("%s/loleaflet/dist/loleaflet.html?file_path=file:///srv/data/%s&permission=readonly", getProperties().getLibreOffice().getDomain(), URLEncoder.encode(getInfo().getFileName(), "UTF-8")));
        } catch (IOException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }
}
