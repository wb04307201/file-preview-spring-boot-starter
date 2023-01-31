package cn.wubo.file.preview.storage;

import cn.wubo.file.preview.dto.ConvertInfoDto;

import java.util.List;

public interface IStorageService {
    ConvertInfoDto save(ConvertInfoDto convertInfoDto);
    List<ConvertInfoDto> list(ConvertInfoDto convertInfoDto);
    void check();
}
