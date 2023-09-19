package cn.wubo.file.preview.storage;

import cn.wubo.file.preview.core.FilePreviewInfo;

public interface IFileStorage {

    /**
     * bytes保存文件
     * @param bytes
     * @param fileName
     * @return
     */
    FilePreviewInfo save(byte[] bytes, String fileName);

    /**
     * 删除文件
     * @param filePreviewInfo
     * @return
     */
    Boolean delete(FilePreviewInfo filePreviewInfo);

    /**
     * 获取文件bytes
     * @param filePreviewInfo
     * @return
     */
    byte[] get(FilePreviewInfo filePreviewInfo);

    /**
     * 初始化
     */
    void init();
}
