package cn.wubo.file.preview.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ConvertInfoDto {
    private String id;
    private String sourceFileName;
    private String sourceExtName;
    private String sourceType;
    private String sourceFilePath;
    private String fileName;
    private String extName;
    private String type;
    private String filePath;
    /**
     * 00 暂不转换
     * 10 正在转换
     * 20 转换完成
     * 30 转换异常
     */
    private String convertStatus;
    private Timestamp convertStartTime;
    private Timestamp convertEndTime;
    private Timestamp prePreviewTime;
    private String errorMessage;
    private String converter;
}