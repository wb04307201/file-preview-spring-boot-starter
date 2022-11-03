package cn.wubo.file.online.file.storage;

import cn.wubo.file.online.file.dto.ConvertInfoDto;

import java.util.List;

public interface IStorageService {
    ConvertInfoDto save(ConvertInfoDto convertInfoDto);
    List<ConvertInfoDto> list(ConvertInfoDto convertInfoDto);
}
