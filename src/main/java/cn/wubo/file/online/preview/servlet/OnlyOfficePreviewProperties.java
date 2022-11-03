package cn.wubo.file.online.preview.servlet;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ToString
@Data
@ConfigurationProperties(prefix = "file.online.preview.onlyoffice")
public class OnlyOfficePreviewProperties {
    private String apijs;
    private String download;
    private String callback;
}
