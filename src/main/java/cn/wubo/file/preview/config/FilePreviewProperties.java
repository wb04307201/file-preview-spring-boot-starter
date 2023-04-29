package cn.wubo.file.preview.config;

import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "file.preview")
public class FilePreviewProperties {
    private String officeConverter = "jod";
    private String filePreviewRecord = "cn.wubo.file.preview.record.impl.MemFilePreviewRecordImpl";
    private String fileStorage = "cn.wubo.file.preview.storage.impl.LocalFileStorageImpl";
    private OnlyOfficeProperties onlyOffice = new OnlyOfficeProperties();
}
