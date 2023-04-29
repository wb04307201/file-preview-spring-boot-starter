package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.exception.ConvertRuntimeException;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.io.OutputStream;

public class JodOfficeConverter implements IOfficeConverter {

    DocumentConverter converter;

    public JodOfficeConverter(DocumentConverter converter) {
        this.converter = converter;
    }

    public String convert(InputStream is, OutputStream os, String fileName) {
        String extName = FileUtils.extName(fileName);
        String fileType = FileUtils.fileType(extName);
        String newFileName = fileName;
        try {
            switch (fileType) {
                case "word":
                case "power point":
                    converter.convert(is).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
                    newFileName = newFileName.replace(extName, "pdf");
                    break;
                case "excel":
                    converter.convert(is).to(os).as(DefaultDocumentFormatRegistry.HTML).execute();
                    newFileName = newFileName.replace(extName, "html");
                    break;
                default:
                    IoUtils.copy(is, os);
                    break;
            }
        } catch (Exception e) {
            throw new ConvertRuntimeException(e.getMessage(), e);
        }
        return newFileName;
    }
}
