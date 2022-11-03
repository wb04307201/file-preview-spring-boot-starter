package cn.wubo.file.online.office.impl;

import cn.wubo.file.online.office.IOfficeConverter;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.io.OutputStream;

public class JodOfficeConverter implements IOfficeConverter {

    @Autowired
    DocumentConverter converter;

    @Override
    public void wordConvert(File file, OutputStream os) {
        try {
            converter.convert(file).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void excelConvert(File file, OutputStream os) {
        try {
            converter.convert(file).to(os).as(DefaultDocumentFormatRegistry.HTML).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pptConvert(File file, OutputStream os) {
        try {
            converter.convert(file).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }
}
