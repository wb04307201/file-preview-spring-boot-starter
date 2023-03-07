package cn.wubo.file.preview.servlet.preview;

import cn.wubo.file.preview.common.Page;
import cn.wubo.file.preview.core.FilePreviewInfo;
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
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class PreviewServlet extends HttpServlet {

    IFilePreviewRecord filePreviewRecord;
    IFileStorage fileStorage;

    private static final String CONTEXT_PATH = "contextPath";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");

        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        log.debug("预览文件-----id:{}", id);
        FilePreviewInfo query = new FilePreviewInfo();
        query.setId(id);
        FilePreviewInfo info = filePreviewRecord.list(query).get(0);

        String contextPath = req.getContextPath();

        String fileType = FileUtils.fileType(info.getFileName());
        if ("markdown".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, contextPath);

            byte[] bytes = fileStorage.get(info);
            Path path = Files.createTempFile("markdown", info.getFileName());
            Files.write(path, bytes);

            try (Stream<String> lines = Files.lines(path)) {
                data.put("content", new String(Base64.getEncoder().encode(lines.collect(Collectors.joining("\n")).getBytes())));
            }
            Page markdownPage = new Page("markdown.ftl", data, resp);
            markdownPage.write();
        } else if ("video".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, contextPath);
            data.put("url", contextPath + "/file/preview/download?id=" + info.getId());
            Page markdownPage = new Page("video.ftl", data, resp);
            markdownPage.write();
        } else if ("audio".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, contextPath);
            data.put("url", contextPath + "/file/preview/download?id=" + info.getId());
            Page markdownPage = new Page("audio.ftl", data, resp);
            markdownPage.write();
        } else if ("pdf".equals(fileType)) {
            resp.sendRedirect(String.format("%s/pdfjs/3.0.279/web/viewer.html?file=%s/file/preview/download?id=%s", contextPath, contextPath, info.getId()));
        } else {
            resp.setContentType(FileUtils.getMimeType(info.getFileName()));
            try (OutputStream os = resp.getOutputStream()) {
                IoUtils.writeToStream(fileStorage.get(info), os);
            }
        }

        log.debug("预览文件-----结束");
        //super.doGet(req, resp);
    }
}
