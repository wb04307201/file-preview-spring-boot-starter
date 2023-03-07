package cn.wubo.file.preview.core;

import lombok.Data;

import java.util.Date;

@Data
public class FilePreviewInfo {
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
}
