package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.FilePreviewService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class DeleteServlet extends HttpServlet {

    private final FilePreviewService filePreviewService;

    public DeleteServlet(FilePreviewService filePreviewService) {
        this.filePreviewService = filePreviewService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("删除文件-----开始");
        String id = req.getParameter("id");
        filePreviewService.delete(id);
        log.debug("删除文件----结束");
        String contextPath = req.getContextPath();
        resp.sendRedirect(contextPath + "/file/preview/list");
    }
}
