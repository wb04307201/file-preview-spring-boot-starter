package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.IFilePreviewService;
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

    IFilePreviewService filePreviewService;

    public DownloadServlet(IFilePreviewService filePreviewService) {
        this.filePreviewService = filePreviewService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("下载文件-----开始");
        String id = req.getParameter("id");
        byte[] bytes = filePreviewService.download(id);
        try (OutputStream os = resp.getOutputStream()) {
            IoUtils.writeToStream(bytes, os);
        }
        log.debug("下载----结束");
    }
}
