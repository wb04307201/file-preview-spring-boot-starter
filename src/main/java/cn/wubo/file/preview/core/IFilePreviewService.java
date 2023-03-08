package cn.wubo.file.preview.core;

import java.io.InputStream;

public interface IFilePreviewService {

    /**
     * 转换文件
     * @param is
     * @param fileName
     * @return
     */
    FilePreviewInfo covert(InputStream is, String fileName);

    /**
     * 删除文件
     * @param id
     * @return
     */
    Boolean delete(String id);

    /**
     * 下载文件bytes
     * @param id
     * @return
     */
    byte[] download(String id);
}
