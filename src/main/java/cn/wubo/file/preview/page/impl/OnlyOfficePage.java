package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.page.AbstractPage;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.Page;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class OnlyOfficePage extends AbstractPage {
    public OnlyOfficePage(String fileType, String extName, String contextPath, FilePreviewInfo info, IFileStorage fileStorage, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, fileStorage, properties, resp);
    }

    @Override
    public void build() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        data.put("url", String.format("%s/web-apps/apps/api/documents/api.js", getProperties().getOnlyOffice().getDomain()));
        switch (getFileType()) {
            case "word":
            case "txt":
                data.put(DOCUMENT_TYPE, "word");
                break;
            case "excel":
                data.put(DOCUMENT_TYPE, "cell");
                break;
            case "power point":
                data.put(DOCUMENT_TYPE, "slide");
                break;
        }
        data.put("fileType", "txt".equals(getFileType()) ? "txt" : getExtName());
        data.put("key", getInfo().getId());
        data.put("title", getInfo().getOriginalFilename());
        data.put("downloadUrl", getProperties().getOnlyOffice().getDownload() + "?id=" + getInfo().getId());
        data.put("callbackUrl", getProperties().getOnlyOffice().getCallback() + "?id=" + getInfo().getId());
        data.put("lang", "zh");
        data.put("userid", "file preview");
        data.put("username", "file preview");
        writePage("onlyoffice.ftl", data);
    }
}
