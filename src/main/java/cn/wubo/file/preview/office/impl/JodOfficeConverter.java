package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.office.IOfficeConverter;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Paths;

public class JodOfficeConverter implements IOfficeConverter {

    @Autowired
    DocumentConverter converter;

    @Override
    public void wordConvert(ConvertInfoDto convertInfoDto) {
        try(OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            converter.convert(Paths.get(convertInfoDto.getSourceFilePath()).toFile()).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException | IOException e) {
            throw new RuntimeException(e);
        }
        convert(convertInfoDto);
    }

    @Override
    public void excelConvert(ConvertInfoDto convertInfoDto) {
        try(OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            converter.convert(Paths.get(convertInfoDto.getSourceFilePath()).toFile()).to(os).as(DefaultDocumentFormatRegistry.HTML).execute();
        } catch (OfficeException | IOException e) {
            throw new RuntimeException(e);
        }
        convert(convertInfoDto);
    }

    @Override
    public void pptConvert(ConvertInfoDto convertInfoDto) {
        try(OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            converter.convert(Paths.get(convertInfoDto.getSourceFilePath()).toFile()).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException | IOException e) {
            throw new RuntimeException(e);
        }
        convert(convertInfoDto);
    }

    private void convert(ConvertInfoDto convertInfoDto){
        convertInfoDto.setConverter("jod");
    }
}
