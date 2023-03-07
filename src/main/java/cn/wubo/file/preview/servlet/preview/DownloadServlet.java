package cn.wubo.file.preview.servlet.preview;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.IoUtils;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class DownloadServlet extends HttpServlet {

    IFilePreviewRecord filePreviewRecord;
    IFileStorage fileStorage;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("下载文件-----开始");
        String id = req.getParameter("id");
        FilePreviewInfo query = new FilePreviewInfo();
        query.setId(id);
        FilePreviewInfo info = filePreviewRecord.list(query).get(0);

        byte[] bytes = fileStorage.get(info);

        try (OutputStream os = resp.getOutputStream()) {
            IoUtils.writeToStream(bytes, os);
        }
        log.debug("下载----结束");
    }
}
