package cn.wubo.file.preview.core;

import cn.wubo.file.preview.exception.PreviewRuntimeException;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.util.FastByteArrayOutputStream;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
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
     * 转换输入流中的文件内容，生成预览信息。
     * 该方法接收一个输入流和文件名，通过officeConverter将文件内容转换为新的格式，
     * 并保存转换后的文件，最后返回包含预览信息的对象。
     *
     * @param is       输入流，代表待转换的文件内容。
     * @param fileName 原文件名。
     * @return 返回包含转换后文件名、文件路径、原文件名和创建时间的文件预览信息对象。
     */
    public FilePreviewInfo covert(InputStream is, String fileName) {
        // 用于存储转换后的文件字节内容
        byte[] bytes;
        // 存储转换后的新文件名
        String newFileName;

        try (FastByteArrayOutputStream os = new FastByteArrayOutputStream()) {
            // 使用officeConverter进行文件转换，同时获取新文件名
            newFileName = officeConverter.convert(is, os, fileName);
            // 将转换后的文件内容保存到字节数组中
            bytes = os.toByteArray();
        }

        // 创建文件预览信息对象
        FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
        // 设置新文件名
        filePreviewInfo.setFileName(newFileName);
        // 保存转换后的文件到文件存储系统，并获取保存后的文件路径
        filePreviewInfo.setFilePath(fileStorage.save(bytes, newFileName));
        // 设置原文件名
        filePreviewInfo.setOriginalFilename(fileName);
        // 设置文件的创建时间
        filePreviewInfo.setCreateTime(new Date());
        // 将文件预览信息保存到数据库，并返回保存后的对象
        filePreviewInfo = filePreviewRecord.save(filePreviewInfo);
        return filePreviewInfo;
    }

    /**
     * 列出文件预览信息列表。
     *
     * @param filePreviewInfo 包含文件预览条件的信息对象，用于查询筛选。此对象中可以包含诸如文件类型、创建时间范围等条件，以便精确或模糊地查询文件预览的信息。
     * @return 返回一个文件预览信息的列表（List<FilePreviewInfo>）。这个列表包含了所有满足查询条件的文件预览信息。每个文件预览信息对象包含了文件的详细信息，如名称、路径、大小等，以便于在用户界面进行展示或进一步处理。
     */
    public List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo) {
        // 通过文件预览记录管理器，根据条件列出文件预览信息。此步骤是查询的核心，会根据传入的文件预览信息对象中的条件，从文件预览记录中筛选出符合条件的所有记录，并以列表的形式返回。
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
        return fileStorage.delete(filePreviewInfo.getFilePath()) && filePreviewRecord.delete(filePreviewInfo);
    }

    /**
     * 根据ID查找文件预览信息。
     * 如果ID包含"@"，则会将ID分割为两部分，一部分用于查找压缩文件记录，另一部分用于设置压缩文件名和原始文件名。
     * 如果ID不包含"@"，则直接查找对应的文件预览记录。
     *
     * @param id 文件预览信息的唯一标识符。
     * @return 返回找到的文件预览信息对象。如果未找到，则返回null。
     */
    public FilePreviewInfo findById(String id) {
        FilePreviewInfo filePreviewInfo;
        if (id.contains("@")) {
            // ID包含"@"，处理以获取压缩文件名和原始文件名
            int index = id.indexOf("@");
            filePreviewInfo = filePreviewRecord.findById(id.substring(0, index));
            String compressFileName = id.substring(index + 1);
            filePreviewInfo.setCompressFileName(compressFileName);

            // 设置原始文件名，如果压缩文件名中还包含"@"，则取到最后一个"@"为止的字符串，且去除路径部分
            String subFileName = compressFileName.contains("@") ? compressFileName.substring(0, compressFileName.lastIndexOf("@") + 1) : compressFileName;
            if (subFileName.contains("/")) subFileName = subFileName.substring(subFileName.lastIndexOf("/") + 1);
            filePreviewInfo.setOriginalFilename(subFileName);
            filePreviewInfo.setFileName(subFileName);
        } else {
            // ID不包含"@"，直接查找文件预览记录
            filePreviewInfo = filePreviewRecord.findById(id);
        }
        return filePreviewInfo;
    }

    /**
     * 获取文件预览信息对应的文件字节码。
     *
     * @param filePreviewInfo 文件预览信息对象，包含需要预览的文件的相关信息。
     *                        该对象应包含文件的唯一标识、文件路径等信息，以便于本方法通过这些信息找到对应的文件。
     * @return 返回文件的字节码数组，用于预览或处理。
     * 这些字节码可以被用于在前端展示文件内容，或者进行其他形式的文件处理。
     */
    public byte[] getBytes(FilePreviewInfo filePreviewInfo) {
        if (filePreviewInfo.getCompressFileName() != null) {
            // 如果文件预览信息中包含压缩文件名，则处理压缩文件
            String[] compressFileNames = filePreviewInfo.getCompressFileName().split("@");
            try {
                FilePreviewInfo compressFilePreviewInfo = findById(filePreviewInfo.getId());
                // 首先将文件从存储服务中读取出来，然后写入到临时文件
                Path path = FileUtils.writeTempFile(compressFilePreviewInfo.getFileName(), fileStorage.getBytes(compressFilePreviewInfo.getFilePath()));
                // 对临时文件进行解压，获取最终需要的文件
                for (String compressFileName : compressFileNames)
                    path = FileUtils.getSubCompressFile(path, compressFileName);
                // 将文件内容读取为字节码数组并返回
                return IoUtils.toByteArray(new BufferedInputStream(Files.newInputStream(path)));
            } catch (IOException | ArchiveException e) {
                // 如果处理过程中出现异常，则抛出运行时异常
                throw new PreviewRuntimeException(e.getMessage(), e);
            }
        } else {
            // 如果没有压缩文件名，则直接通过文件存储服务获取指定文件预览信息对应的字节码
            return fileStorage.getBytes(filePreviewInfo.getFilePath());
        }
    }
}
