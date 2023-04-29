package cn.wubo.file.preview.office.impl;

import cn.wubo.file.preview.exception.ConvertRuntimeException;
import cn.wubo.file.preview.office.IOfficeConverter;
import cn.wubo.file.preview.utils.IoUtils;

import java.io.InputStream;
import java.io.OutputStream;

//无需转换
public class NoneConverter implements IOfficeConverter {

    public String convert(InputStream is, OutputStream os, String fileName) {
        try {
            IoUtils.copy(is, os);
        } catch (Exception e) {
            throw new ConvertRuntimeException(e.getMessage(), e);
        }
        return fileName;
    }
}
