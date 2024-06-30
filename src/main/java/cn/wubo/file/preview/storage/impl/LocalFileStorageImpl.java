package cn.wubo.file.preview.storage.impl;

import cn.wubo.file.preview.exception.StorageRuntimeException;
import cn.wubo.file.preview.storage.IFileStorage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class LocalFileStorageImpl implements IFileStorage {

    private String basePath = "temp";

    @Override
    public String save(byte[] bytes, String fileName) {
        Path filePath = Paths.get(basePath, fileName);
        try {
            Files.createDirectories(filePath.getParent());
            if (Files.exists(filePath)) Files.delete(filePath);
            Files.createFile(filePath);
            Files.write(filePath, bytes);
            return filePath.toString();
        } catch (IOException e) {
            throw new StorageRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public Boolean delete(String path) {
        Path filePath = Paths.get(path);
        try {
            Files.delete(filePath);
        } catch (IOException e) {
            throw new StorageRuntimeException(e.getMessage(), e);
        }
        return true;
    }

    @Override
    public byte[] getBytes(String path) {
        try {
            return Files.readAllBytes(Paths.get(path));
        } catch (IOException e) {
            throw new StorageRuntimeException(e.getMessage(), e);
        }
    }

    @Override
    public void init() {

    }
}
