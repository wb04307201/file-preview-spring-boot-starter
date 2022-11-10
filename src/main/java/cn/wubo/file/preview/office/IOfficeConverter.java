package cn.wubo.file.preview.office;

import cn.wubo.file.preview.dto.ConvertInfoDto;

import java.io.OutputStream;

public interface IOfficeConverter {

    void wordConvert(ConvertInfoDto convertInfoDto);

    void excelConvert(ConvertInfoDto convertInfoDto);

    void pptConvert(ConvertInfoDto convertInfoDto);
}
