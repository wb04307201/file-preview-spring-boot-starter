package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.office.IOfficeConverter;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;

import java.io.OutputStream;

public class SpireOfficeConverter implements IOfficeConverter {

    @Override
    public void wordConvert(String sourcePath, OutputStream os) {
        Document document = new Document();
        document.loadFromFile(sourcePath);
        document.saveToStream(os, com.spire.doc.FileFormat.PDF);
    }

    @Override
    public void excelConvert(String sourcePath, OutputStream os) {
        Workbook workbook = new Workbook();
        workbook.loadFromFile(sourcePath);
        workbook.saveToStream(os, com.spire.xls.FileFormat.HTML);
    }

    @Override
    public void pptConvert(String sourcePath, OutputStream os) {
        Presentation presentation = new Presentation();
        try {
            presentation.loadFromFile(sourcePath);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        presentation.saveToFile(os, com.spire.presentation.FileFormat.PDF);
    }
}
