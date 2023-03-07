package cn.wubo.file.preview.record;

import cn.wubo.file.preview.core.FilePreviewInfo;

import java.util.List;

public interface IFilePreviewRecord {
    /**
     * 保存文件信息
     * @param filePreviewInfo
     * @return
     */
    FilePreviewInfo save(FilePreviewInfo filePreviewInfo);

    /**
     * 查询文件信息
     * @param filePreviewInfo
     * @return
     */
    List<FilePreviewInfo> list(FilePreviewInfo filePreviewInfo);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    FilePreviewInfo findById(String id);

    /**
     * 初始化
     */
    void init();
}
