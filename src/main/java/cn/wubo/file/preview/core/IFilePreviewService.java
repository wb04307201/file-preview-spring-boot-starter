package cn.wubo.file.preview.core;

import java.io.InputStream;

public interface IFilePreviewService {

    FilePreviewInfo covert(InputStream is, String fileName);
}
