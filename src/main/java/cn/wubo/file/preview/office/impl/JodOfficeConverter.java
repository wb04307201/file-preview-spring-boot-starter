package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.office.IOfficeConverter;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.jodconverter.core.office.OfficeException;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.OutputStream;
import java.nio.file.Paths;

public class JodOfficeConverter implements IOfficeConverter {

    @Autowired
    DocumentConverter converter;

    @Override
    public void wordConvert(String sourcePath, OutputStream os) {
        try {
            converter.convert(Paths.get(sourcePath).toFile()).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void excelConvert(String sourcePath, OutputStream os) {
        try {
            converter.convert(Paths.get(sourcePath).toFile()).to(os).as(DefaultDocumentFormatRegistry.HTML).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void pptConvert(String sourcePath, OutputStream os) {
        try {
            converter.convert(Paths.get(sourcePath).toFile()).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
        } catch (OfficeException e) {
            throw new RuntimeException(e);
        }
    }
}
