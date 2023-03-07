package cn.wubo.file.preview.storage;

import cn.wubo.file.preview.core.FilePreviewInfo;

public interface IFileStorage {

    FilePreviewInfo save(byte[] bytes, String fileName);

    byte[] get(FilePreviewInfo filePreviewInfo);
}
