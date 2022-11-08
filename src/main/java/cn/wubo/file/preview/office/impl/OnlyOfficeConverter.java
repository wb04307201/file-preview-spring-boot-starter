package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.office.IOfficeConverter;

import java.io.OutputStream;

public class OnlyOfficeConverter implements IOfficeConverter {

    @Override
    public void wordConvert(String sourcePath, OutputStream os) {
        CommonUtils.writeToStream(sourcePath,os);
    }

    @Override
    public void excelConvert(String sourcePath, OutputStream os) {
        CommonUtils.writeToStream(sourcePath,os);
    }

    @Override
    public void pptConvert(String sourcePath, OutputStream os) {
        CommonUtils.writeToStream(sourcePath,os);
    }
}
