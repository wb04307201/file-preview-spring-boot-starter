package cn.wubo.file.preview;

import cn.wubo.file.preview.dto.ConvertInfoDto;

public interface IFilePreviewService {

    ConvertInfoDto covert(String sourcePath);

    ConvertInfoDto recovert(ConvertInfoDto convertInfoDto);

    ConvertInfoDto record(String sourcePath);
}
