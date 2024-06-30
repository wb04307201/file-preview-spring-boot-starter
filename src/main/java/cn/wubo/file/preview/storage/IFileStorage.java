package cn.wubo.file.preview.storage;

public interface IFileStorage {

    /**
     * bytes保存文件
     * @param bytes
     * @param fileName
     * @return
     */
    String save(byte[] bytes, String fileName);

    /**
     * 删除文件
     * @param path
     * @return
     */
    Boolean delete(String path);

    /**
     * 获取文件bytes
     * @param path
     * @return
     */
    byte[] getBytes(String path);

    /**
     * 初始化
     */
    void init();
}
