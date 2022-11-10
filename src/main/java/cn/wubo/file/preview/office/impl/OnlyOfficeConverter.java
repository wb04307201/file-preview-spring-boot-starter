package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.office.IOfficeConverter;

import java.io.IOException;
import java.io.OutputStream;

public class OnlyOfficeConverter implements IOfficeConverter {

    @Override
    public void wordConvert(ConvertInfoDto convertInfoDto) {
        convert(convertInfoDto);
    }

    @Override
    public void excelConvert(ConvertInfoDto convertInfoDto) {
        convert(convertInfoDto);
    }

    @Override
    public void pptConvert(ConvertInfoDto convertInfoDto) {
        convert(convertInfoDto);
    }

    private void convert(ConvertInfoDto convertInfoDto){
        try (OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            CommonUtils.writeToStream(convertInfoDto.getSourceFilePath(), os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        convertInfoDto.setConverter("OnlyOffice");
    }
}
