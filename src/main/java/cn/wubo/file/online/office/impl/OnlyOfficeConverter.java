package cn.wubo.file.online.office.impl;

import cn.hutool.core.io.FileUtil;
import cn.wubo.file.online.office.IOfficeConverter;

import java.io.File;
import java.io.OutputStream;

public class OnlyOfficeConverter implements IOfficeConverter {

    @Override
    public void wordConvert(File file, OutputStream os) {
        FileUtil.writeToStream(file,os);
    }

    @Override
    public void excelConvert(File file, OutputStream os) {
        FileUtil.writeToStream(file,os);
    }

    @Override
    public void pptConvert(File file, OutputStream os) {
        FileUtil.writeToStream(file,os);
    }
}
