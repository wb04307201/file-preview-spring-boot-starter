package cn.wubo.file.preview;

import cn.wubo.file.preview.dto.ConvertInfoDto;

public interface IFilePreviewService {

    ConvertInfoDto covert(String sourcePath);

    ConvertInfoDto covert(String sourcePath,ConvertInfoDto convertInfoDto);

    ConvertInfoDto record(String sourcePath);
}
