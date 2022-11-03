package cn.wubo.file.online.file.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class ConvertInfoDto {
    private String id;
    private String orgFileName;
    private String orgExtName;
    private String orgType;
    private String orgFilePath;
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
}