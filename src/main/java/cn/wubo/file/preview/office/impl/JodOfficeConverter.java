package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.exception.ConvertRuntimeException;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import org.jodconverter.core.DocumentConverter;
import org.jodconverter.core.document.DefaultDocumentFormatRegistry;

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
        try {
            return switch (fileType) {
                case "word", "power point" -> {
                    converter.convert(is).to(os).as(DefaultDocumentFormatRegistry.PDF).execute();
                    yield fileName.replace(extName, "pdf");
                }
                case "excel" -> {
                    converter.convert(is).to(os).as(DefaultDocumentFormatRegistry.HTML).execute();
                    yield fileName.replace(extName, "html");

                }
                default -> {
                    IoUtils.copy(is, os);
                    yield fileName;
                }
            };
        } catch (Exception e) {
            throw new ConvertRuntimeException(e.getMessage(), e);
        }
    }
}
