package cn.wubo.file.preview.core;

import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.InputStream;
import java.util.Date;

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

    public Boolean delete(String id) {
        //1 调用查询
        FilePreviewInfo filePreviewInfo = filePreviewRecord.findById(id);
        //2 删除文件
        //3 删除记录
        return fileStorage.delete(filePreviewInfo) && filePreviewRecord.deleteById(id);
    }

    public byte[] download(String id) {
        FilePreviewInfo info = filePreviewRecord.findById(id);
        return fileStorage.get(info);
    }
}
