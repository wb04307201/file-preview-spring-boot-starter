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

    /**
     * 将输入流中的文件转换为预览信息。
     *
     * @param is 输入流，代表待转换的文件。
     * @param fileName 原文件名称。
     * @return FilePreviewInfo 文件预览信息对象，包含转换后的文件存储信息和记录。
     */
    public FilePreviewInfo covert(InputStream is, String fileName) {
        // 转换文件
        byte[] bytes;
        String newFileName;
        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            newFileName = officeConverter.convert(is, os, fileName);
            bytes = os.toByteArray();
        }

        // 存储转换后的文件
        FilePreviewInfo filePreviewInfo = fileStorage.save(bytes, newFileName);

        // 保存文件预览记录
        filePreviewInfo.setOriginalFilename(fileName);
        filePreviewInfo.setCreateTime(new Date());
        filePreviewInfo = filePreviewRecord.save(filePreviewInfo);
        return filePreviewInfo;
    }


    /**
     * 列出文件预览信息列表。
     *
     * @param filePreviewInfo 包含文件预览条件的信息对象，用于查询筛选。
     * @return 返回一个文件预览信息的列表，满足查询条件的预览信息将被包含在列表中。
     */
    public List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo) {
        // 通过文件预览记录管理器，根据条件列出文件预览信息
        return filePreviewRecord.list(filePreviewInfo);
    }

    /**
     * 根据提供的ID删除文件预览信息及其对应的文件。
     *
     * @param id 预览信息的唯一标识符。
     * @return 返回一个布尔值，表示是否成功删除。若文件预览信息和对应的文件都成功删除，则返回true；否则返回false。
     */
    public Boolean delete(String id) {
        // 根据ID查找文件预览信息
        FilePreviewInfo filePreviewInfo = filePreviewRecord.findById(id);
        // 尝试删除文件及其预览信息，并返回操作结果
        return fileStorage.delete(filePreviewInfo) && filePreviewRecord.delete(filePreviewInfo);
    }


    /**
     * 根据ID查找文件预览信息。
     * 如果ID中包含"#"，则将ID拆分为两部分，第一部分用于查找文件预览记录，第二部分设置为压缩文件名。
     * 如果ID中不包含"#"，则直接使用ID查找文件预览记录。
     *
     * @param id 文件预览信息的ID，可能包含"#"用于区分压缩文件名。
     * @return 返回匹配的文件预览信息对象，如果找不到则返回null。
     */
    public FilePreviewInfo findById(String id) {
        FilePreviewInfo filePreviewInfo;
        if (id.contains("#")) {
            // ID包含"#"，处理为分别获取真实ID和压缩文件名
            String[] ids = id.split("#");
            filePreviewInfo = filePreviewRecord.findById(ids[0]);
            filePreviewInfo.setCompressFileName(ids[1]);
        } else {
            // ID不包含"#"，直接查找文件预览信息
            filePreviewInfo = filePreviewRecord.findById(id);
        }
        return filePreviewInfo;
    }

    /**
     * 获取文件预览信息对应的文件字节码。
     *
     * @param filePreviewInfo 文件预览信息对象，包含需要预览的文件的相关信息。
     * @return 返回文件的字节码数组，用于预览或处理。
     */
    public byte[] getBytes(FilePreviewInfo filePreviewInfo) {
        // 通过文件存储服务获取指定文件预览信息对应的字节码
        return fileStorage.getBytes(filePreviewInfo);
    }
}
