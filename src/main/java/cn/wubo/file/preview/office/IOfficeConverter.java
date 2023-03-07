package cn.wubo.file.preview.office;

import java.io.InputStream;
import java.io.OutputStream;

public interface IOfficeConverter {

    String convert(InputStream is, OutputStream os, String fileName);
}
