package cn.wubo.file.preview.office;

import java.io.OutputStream;

public interface IOfficeConverter {

    void wordConvert(String sourcePath, OutputStream os);

    void excelConvert(String sourcePath, OutputStream os);

    void pptConvert(String sourcePath, OutputStream os);
}
