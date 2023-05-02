package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
public class DownloadServlet extends HttpServlet {
    IFilePreviewRecord filePreviewRecord;
    IFileStorage fileStorage;

    public DownloadServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        this.filePreviewRecord = filePreviewRecord;
        this.fileStorage = fileStorage;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("下载文件-----开始");
        String id = req.getParameter("id");
        FilePreviewInfo info = filePreviewRecord.findById(id);
        byte[] bytes = fileStorage.get(info);
        resp.setContentType(FileUtils.getMimeType(info.getFileName()));
        resp.addHeader("Content-Length", String.valueOf(bytes.length));
        resp.addHeader("Content-Disposition", "attachment;filename=" + new String(Objects.requireNonNull(info.getFileName()).getBytes(), StandardCharsets.ISO_8859_1));
        try (OutputStream os = resp.getOutputStream()) {
            IoUtils.writeToStream(bytes, os);
        }
        log.debug("下载----结束");
    }
}
