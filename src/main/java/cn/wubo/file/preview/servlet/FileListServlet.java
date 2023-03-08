package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.utils.Page;
import cn.wubo.file.preview.record.IFilePreviewRecord;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileListServlet extends HttpServlet {

    IFilePreviewRecord filePreviewRecord;

    public FileListServlet(IFilePreviewRecord filePreviewRecord) {
        this.filePreviewRecord = filePreviewRecord;
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
        data.put("list", filePreviewRecord.list(filePreviewInfo));
        data.put("query", filePreviewInfo);

        Page listPage = new Page("list.ftl", data, resp);
        listPage.write();
        //super.doGet(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.doGet(req, resp);
    }
}
