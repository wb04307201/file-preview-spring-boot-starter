package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.utils.PageUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileListServlet extends HttpServlet {

    private final FilePreviewService filePreviewService;

    public FileListServlet(FilePreviewService filePreviewService) {
        this.filePreviewService = filePreviewService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        FilePreviewInfo filePreviewInfo = new FilePreviewInfo();
        if (req.getMethod().equalsIgnoreCase("post")) {
            Map<String, String[]> map = req.getParameterMap();
            filePreviewInfo.setFileName(map.get("fileName")[0]);
            filePreviewInfo.setFilePath(map.get("filePath")[0]);
        }

        Map<String, Object> data = new HashMap<>();
        data.put("contextPath", req.getContextPath());
        data.put("list", filePreviewService.list(filePreviewInfo));
        data.put("query", filePreviewInfo);

        PageUtils listPage = new PageUtils("previewlist.ftl", data, resp);
        listPage.write();
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
