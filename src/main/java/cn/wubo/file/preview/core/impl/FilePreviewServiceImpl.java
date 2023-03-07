package cn.wubo.file.preview.core.impl;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.IFilePreviewService;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.FileUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.InputStream;
import java.util.UUID;

@Slf4j
public class FilePreviewServiceImpl implements IFilePreviewService {

    IOfficeConverter officeConverter;
    IFileStorage fileStorage;
    IFilePreviewRecord filePreviewRecord;

    public FilePreviewServiceImpl(IOfficeConverter officeConverter, IFileStorage fileStorage, IFilePreviewRecord filePreviewRecord) {
        this.officeConverter = officeConverter;
        this.fileStorage = fileStorage;
        this.filePreviewRecord = filePreviewRecord;
    }

    @Override
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
        filePreviewInfo = filePreviewRecord.save(filePreviewInfo);
        return filePreviewInfo;
    }
}
