package cn.wubo.file.preview.storage.impl;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.exception.StorageRuntimeException;
import cn.wubo.file.preview.storage.IFileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileStorageImpl implements IFileStorage {

    private String basePath = "temp";

    @Override
    public FilePreviewInfo save(byte[] bytes, String fileName) {
        FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
        filePreviewInfo.setFileName(fileName);

        Path filePath = Paths.get(basePath, fileName);
        filePreviewInfo.setFilePath(filePath.toString());
        try {
            Files.createDirectories(filePath.getParent());
            if (Files.exists(filePath)) Files.delete(filePath);
            Files.createFile(filePath);
            Files.write(filePath, bytes);
            return filePreviewInfo;
        } catch (IOException e) {
            throw new StorageRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean delete(FilePreviewInfo filePreviewInfo) {
        Path filePath = Paths.get(filePreviewInfo.getFilePath());
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new StorageRuntimeException(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public byte[] get(FilePreviewInfo filePreviewInfo) {
        try {
            return Files.readAllBytes(Paths.get(filePreviewInfo.getFilePath()));
        } catch (IOException e) {
            throw new StorageRuntimeException(e.getMessage(), e);
        }
    }
}
