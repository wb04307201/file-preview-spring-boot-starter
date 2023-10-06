package cn.wubo.file.preview.core;

import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

/**
 * 预览文件服务
 */
@Slf4j
public class FilePreviewService {

    IOfficeConverter officeConverter;
    IFileStorage fileStorage;
    IFilePreviewRecord filePreviewRecord;

    public FilePreviewService(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        this.officeConverter = officeConverter;
        this.fileStorage = fileStorage;
        this.filePreviewRecord = filePreviewRecord;
    }

    public FilePreviewInfo covert(InputStream is, String fileName) {
        //1 调用转换
        byte[] bytes;
        String newFileName;
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            newFileName = officeConverter.convert(is, os, fileName);
            bytes = os.toByteArray();
        }
        //2 存储文件
        FilePreviewInfo filePreviewInfo = fileStorage.save(bytes, newFileName);

        //3 保存记录
        filePreviewInfo.setOriginalFilename(fileName);
        filePreviewInfo.setCreateTime(new Date());
        filePreviewInfo = filePreviewRecord.save(filePreviewInfo);
        return filePreviewInfo;
    }

    public List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo) {
        return filePreviewRecord.list(filePreviewInfo);
    }

    public Boolean delete(String id) {
        FilePreviewInfo filePreviewInfo = filePreviewRecord.findById(id);
        return fileStorage.delete(filePreviewInfo) && filePreviewRecord.delete(filePreviewInfo);
    }

    public FilePreviewInfo findById(String id) {
        return filePreviewRecord.findById(id);
    }

    public byte[] getBytes(FilePreviewInfo filePreviewInfo) {
        return fileStorage.getBytes(filePreviewInfo);
    }
}
