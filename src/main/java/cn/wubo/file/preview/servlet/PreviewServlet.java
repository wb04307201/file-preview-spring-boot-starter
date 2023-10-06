package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.page.PageFactory;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class PreviewServlet extends HttpServlet {

    FilePreviewService filePreviewService;
    FilePreviewProperties properties;

    public PreviewServlet(FilePreviewService filePreviewService, FilePreviewProperties properties) {
        this.filePreviewService = filePreviewService;
        this.properties = properties;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        log.debug("预览文件-----id:{}", id);
        FilePreviewInfo info = filePreviewService.findById(id);
        PageFactory.create(info, filePreviewService, properties, req, resp).build();
        log.debug("预览文件-----结束");
        //super.doGet(req, resp);
    }
}
