package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.exception.ConvertRuntimeException;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import com.spire.doc.Document;
import com.spire.doc.FileFormat;
import com.spire.presentation.Presentation;
import com.spire.xls.Workbook;

import java.io.InputStream;
import java.io.OutputStream;

public class SpireOfficeConverter implements IOfficeConverter {

    public String convert(InputStream is, OutputStream os, String fileName) {
        String extName = FileUtils.extName(fileName);
        String fileType = FileUtils.fileType(extName);
        String newFileName = fileName;
        try {
            switch (fileType) {
                case "word":
                    Document document = new Document();
                    document.loadFromStream(is, FileFormat.Auto);
                    document.saveToStream(os, com.spire.doc.FileFormat.PDF);
                    newFileName = newFileName.replace(extName, "pdf");
                    break;
                case "excel":
                    //处理成html时，会产生多个文件，改成pdf
                    Workbook workbook = new Workbook();
                    workbook.loadFromStream(is);
                    workbook.saveToStream(os, com.spire.xls.FileFormat.PDF);
                    newFileName = newFileName.replace(extName, "pdf");
                    break;
                case "power point":
                    Presentation presentation = new Presentation();
                    presentation.loadFromStream(is, com.spire.presentation.FileFormat.AUTO);
                    presentation.saveToFile(os, com.spire.presentation.FileFormat.PDF);
                    newFileName = newFileName.replace(extName, "pdf");
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
