package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.AbstractPage;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * config json示例
 * https://api.onlyoffice.com/editors/advanced
 * token示例
 * https://api.onlyoffice.com/editors/signature/#java
 */
public class OnlyOfficePage extends AbstractPage {
    public OnlyOfficePage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        super(fileType, extName, contextPath, info, filePreviewService, properties);
    }

    @Override
    public ServerResponse build() {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        data.put("url", String.format("%s/web-apps/apps/api/documents/api.js", getProperties().getOnlyOffice().getDomain()));

        Map<String, Object> config = new HashMap<>();
        config.put("type", "desktop");
        config.put("width", "100%");
        config.put("height", "100%");
        config.put(DOCUMENT_TYPE, getDoucmentType(getFileType()));

        Map<String, Object> document = new HashMap<>();
        document.put("fileType", getExtName());
        document.put("key", getInfo().getId());
        document.put("title", getInfo().getOriginalFilename());
        document.put("url", getProperties().getOnlyOffice().getDownload() + "?id=" + getInfo().getId());
        config.put("document", document);

        Map<String, Object> editorConfig = new HashMap<>();
        editorConfig.put("mode", "view");
        editorConfig.put("callbackUrl", getProperties().getOnlyOffice().getCallback() + "?id=" + getInfo().getId());
        editorConfig.put("lang", "zh");

        Map<String, Object> user = new HashMap<>();
        user.put("id", "file preview");
        user.put("name", "file preview");
        editorConfig.put("user", user);
        config.put("editorConfig", editorConfig);

        String secret = getProperties().getOnlyOffice().getSecret();
        if (StringUtils.hasText(secret)) {
            Signer signer = HMACSigner.newSHA256Signer(secret);
            JWT jwt = new JWT();
            for (Map.Entry<String, Object> entry : config.entrySet())
                jwt.addClaim(entry.getKey(), entry.getValue());
            config.put("token", JWT.getEncoder().encode(jwt, signer));
        }

        data.put("config", config);

        return writePage("onlyoffice.ftl", data);
    }

    private String getDoucmentType(String fileType) {
        if ("word".equals(fileType) || "text".equals(fileType)) return "word";
        return "excel".equals(fileType) ? "cell" : "slide";
    }
}
