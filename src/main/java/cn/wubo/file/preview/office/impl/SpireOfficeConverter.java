package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.office.IOfficeConverter;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;

import java.io.IOException;
import java.io.OutputStream;

public class SpireOfficeConverter implements IOfficeConverter {

    @Override
    public void wordConvert(ConvertInfoDto convertInfoDto) {
        try (OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            Document document = new Document();
            document.loadFromFile(convertInfoDto.getSourceFilePath());
            document.saveToStream(os, com.spire.doc.FileFormat.PDF);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        convert(convertInfoDto);
    }

    @Override
    public void excelConvert(ConvertInfoDto convertInfoDto) {
        try (OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            Workbook workbook = new Workbook();
            workbook.loadFromFile(convertInfoDto.getSourceFilePath());
            workbook.saveToStream(os, com.spire.xls.FileFormat.HTML);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        convert(convertInfoDto);
    }

    @Override
    public void pptConvert(ConvertInfoDto convertInfoDto) {
        try (OutputStream os = CommonUtils.getOutputStream(convertInfoDto.getFilePath())) {
            Presentation presentation = new Presentation();
            presentation.loadFromFile(convertInfoDto.getSourceFilePath());
            presentation.saveToFile(os, com.spire.presentation.FileFormat.PDF);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        convert(convertInfoDto);
    }

    private void convert(ConvertInfoDto convertInfoDto){
        convertInfoDto.setConverter("spire");
    }
}
