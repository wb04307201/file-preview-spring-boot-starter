package cn.wubo.file.preview.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "file.preview")
public class FilePreviewProperties {
    private String filePreviewRecord = "";
    private String fileStorage = "";
    private OnlyOfficeProperties onlyOffice = new OnlyOfficeProperties();
}
