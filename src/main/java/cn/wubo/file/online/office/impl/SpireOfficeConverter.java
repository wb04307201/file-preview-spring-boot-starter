package cn.wubo.file.online.office.impl;

import cn.wubo.file.online.office.IOfficeConverter;
import com.spire.doc.Document;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;

import java.io.File;
import java.io.OutputStream;

public class SpireOfficeConverter implements IOfficeConverter {

    @Override
    public void wordConvert(File file, OutputStream os) {
        Document document = new Document();
        document.loadFromFile(file.getPath());
        document.saveToStream(os, com.spire.doc.FileFormat.PDF);
    }

    @Override
    public void excelConvert(File file, OutputStream os) {
        Workbook workbook = new Workbook();
        workbook.loadFromFile(file.getPath());
        workbook.saveToStream(os, com.spire.xls.FileFormat.HTML);
    }

    @Override
    public void pptConvert(File file, OutputStream os) {
        Presentation presentation = new Presentation();
        try {
            presentation.loadFromFile(file.getPath());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        presentation.saveToFile(os, com.spire.presentation.FileFormat.PDF);
    }
}
