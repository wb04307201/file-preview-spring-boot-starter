package cn.wubo.file.preview.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilePreviewInfo implements Serializable {
    private String id;
    /**
     * 原文件名
     */
    private String originalFilename;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 转换后文件定位
     */
    private String filePath;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 压缩文件内部文件名
     */
    private String compressFileName;
}
