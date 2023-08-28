package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.page.PageFactory;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class PreviewServlet extends HttpServlet {

    private static final Set<String> OFFICE_FILE_TYPES = Collections.unmodifiableSet(new HashSet<>(Arrays.asList("word", "excel", "power point", "txt")));
    IFilePreviewRecord filePreviewRecord;
    IFileStorage fileStorage;
    FilePreviewProperties properties;
    private static final String CONTEXT_PATH = "contextPath";
    private static final String DOCUMENT_TYPE = "documentType";

    public PreviewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage, FilePreviewProperties properties) {
        this.filePreviewRecord = filePreviewRecord;
        this.fileStorage = fileStorage;
        this.properties = properties;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        log.debug("预览文件-----id:{}", id);
        FilePreviewInfo info = filePreviewRecord.findById(id);
        PageFactory.create(info, fileStorage, properties, req, resp).build();
        log.debug("预览文件-----结束");
        //super.doGet(req, resp);
    }
}
