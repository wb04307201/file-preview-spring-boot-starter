package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.page.AbstractPage;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.IoUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CodePage extends AbstractPage {
    public CodePage(String fileType, String extName, String contextPath, FilePreviewInfo info, IFileStorage fileStorage, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, fileStorage, properties, resp);
    }
    @Override
    public void build() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        data.put("language", getFileType());
        data.put("content", IoUtils.readByte(getFileStorage().get(getInfo()), getInfo().getFileName()));
        writePage("code.ftl", data);
    }
}
