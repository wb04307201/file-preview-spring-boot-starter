package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.page.AbstractPage;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class CommomPage extends AbstractPage {
    public CommomPage(String fileType, String extName, String contextPath, FilePreviewInfo info, IFileStorage fileStorage, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, fileStorage, properties, resp);
    }

    @Override
    public void build() throws IOException {
        getResp().setContentType(FileUtils.getMimeType(getInfo().getFileName()));
        try (OutputStream os = getResp().getOutputStream()) {
            IoUtils.writeToStream(getFileStorage().get(getInfo()), os);
        }
    }
}
