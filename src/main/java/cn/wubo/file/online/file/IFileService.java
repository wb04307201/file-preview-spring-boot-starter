package cn.wubo.file.online.file;

import cn.wubo.file.online.file.dto.ConvertInfoDto;

import java.io.File;

public interface IFileService {

    ConvertInfoDto covert(File file);

    ConvertInfoDto covert(File file,ConvertInfoDto convertInfoDto);

    ConvertInfoDto record(File file);
}
