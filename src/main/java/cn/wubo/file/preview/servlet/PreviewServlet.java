package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.record.IFilePreviewRecord;
import cn.wubo.file.preview.storage.IFileStorage;
import cn.wubo.file.preview.utils.FileUtils;
import cn.wubo.file.preview.utils.IoUtils;
import cn.wubo.file.preview.utils.Page;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class PreviewServlet extends HttpServlet {

    IFilePreviewRecord filePreviewRecord;
    IFileStorage fileStorage;

    private static final String CONTEXT_PATH = "contextPath";

    public PreviewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage) {
        this.filePreviewRecord = filePreviewRecord;
        this.fileStorage = fileStorage;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        log.debug("预览文件-----id:{}", id);
        FilePreviewInfo info = filePreviewRecord.findById(id);

        Map<String, Object> data = new HashMap<>();

        String contextPath = req.getContextPath();
        String extName = FileUtils.extName(info.getFileName());
        String fileType = FileUtils.fileType(extName);
        switch (fileType) {
            case "markdown": {
                data.put(CONTEXT_PATH, contextPath);
                data.put("content", IoUtils.readByte(fileStorage.get(info), info.getFileName()));
                Page markdownPage = new Page("markdown.ftl", data, resp);
                markdownPage.write();
                break;
            }
            case "sql":
            case "cpp":
            case "java":
            case "xml":
            case "javascript":
            case "json":
            case "css":
            case "python":
                data.put(CONTEXT_PATH, contextPath);
                data.put("language", fileType);
                data.put("content", IoUtils.readByte(fileStorage.get(info), info.getFileName()));
                Page viewPage = new Page("code.ftl", data, resp);
                viewPage.write();
                break;
            case "video": {
                data.put(CONTEXT_PATH, contextPath);
                data.put("url", contextPath + "/file/preview/download?id=" + info.getId());
                Page markdownPage = new Page("video.ftl", data, resp);
                markdownPage.write();
                break;
            }
            case "audio": {
                data.put(CONTEXT_PATH, contextPath);
                data.put("url", contextPath + "/file/preview/download?id=" + info.getId());
                Page markdownPage = new Page("audio.ftl", data, resp);
                markdownPage.write();
                break;
            }
            case "pdf":
                resp.sendRedirect(String.format("%s/pdfjs/3.0.279/web/viewer.html?file=%s/file/preview/download?id=%s", contextPath, contextPath, info.getId()));
                break;
            default:
                resp.setContentType(FileUtils.getMimeType(info.getFileName()));
                try (OutputStream os = resp.getOutputStream()) {
                    IoUtils.writeToStream(fileStorage.get(info), os);
                }
                break;
        }

        log.debug("预览文件-----结束");
        //super.doGet(req, resp);
    }
}
