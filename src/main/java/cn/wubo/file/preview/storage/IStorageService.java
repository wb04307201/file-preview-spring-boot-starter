package cn.wubo.file.preview.storage;

import cn.wubo.file.preview.dto.ConvertInfoDto;

import java.util.List;

public interface IStorageService {
    /**
     * 保存文件信息
     * @param convertInfoDto
     * @return
     */
    ConvertInfoDto save(ConvertInfoDto convertInfoDto);

    /**
     * 查询文件信息
     * @param convertInfoDto
     * @return
     */
    List<ConvertInfoDto> list(ConvertInfoDto convertInfoDto);

    /**
     * 初始化
     */
    void init();
}
