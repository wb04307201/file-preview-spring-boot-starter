package cn.wubo.file.preview.storage;

import cn.wubo.file.preview.core.FilePreviewInfo;

public interface IFileStorage {

    FilePreviewInfo save(byte[] bytes, String fileName);

    Boolean replace(byte[] bytes, FilePreviewInfo filePreviewInfo);

    byte[] get(FilePreviewInfo filePreviewInfo);
}
