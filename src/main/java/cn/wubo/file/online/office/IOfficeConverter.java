package cn.wubo.file.online.office;

import java.io.File;
import java.io.OutputStream;

public interface IOfficeConverter {

    void wordConvert(File file, OutputStream os);

    void excelConvert(File file, OutputStream os);

    void pptConvert(File file, OutputStream os);
}
