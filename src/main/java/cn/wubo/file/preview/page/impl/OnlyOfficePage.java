package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.page.AbstractPage;
import cn.wubo.file.preview.storage.IFileStorage;
import io.fusionauth.jwt.Signer;
import io.fusionauth.jwt.domain.JWT;
import io.fusionauth.jwt.hmac.HMACSigner;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * config json示例
 * https://api.onlyoffice.com/editors/advanced
 * token示例
 * https://api.onlyoffice.com/editors/signature/#java
 */
public class OnlyOfficePage extends AbstractPage {
    public OnlyOfficePage(String fileType, String extName, String contextPath, FilePreviewInfo info, IFileStorage fileStorage, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, fileStorage, properties, resp);
    }

    @Override
    public void build() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        data.put("url", String.format("%s/web-apps/apps/api/documents/api.js", getProperties().getOnlyOffice().getDomain()));

        /*switch (getFileType()) {
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
        data.put("username", "file preview");*/

        Map<String, Object> config = new HashMap<>();
        config.put("type", "desktop");
        config.put("width", "100%");
        config.put("height", "100%");
        config.put(DOCUMENT_TYPE, "word".equals(getFileType()) || "txt".equals(getFileType()) ? "word" : ("excel".equals(getFileType()) ? "cell" : "slide"));

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
            for (String key : config.keySet()) {
                jwt.addClaim(key, config.get(key));
            }
            config.put("token", JWT.getEncoder().encode(jwt, signer));
        }

        data.put("config", config);

        writePage("onlyoffice.ftl", data);
    }
}
