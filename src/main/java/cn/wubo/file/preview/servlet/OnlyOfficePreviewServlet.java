package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.utils.Page;
import cn.wubo.file.preview.config.OnlyOfficeProperties;
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
public class OnlyOfficePreviewServlet extends HttpServlet {

    IFilePreviewRecord filePreviewRecord;
    IFileStorage fileStorage;
    OnlyOfficeProperties onlyOfficeProperties;
    private static final String CONTEXT_PATH = "contextPath";
    private static final String DOCUMENT_TYPE = "documentType";

    public OnlyOfficePreviewServlet(IFilePreviewRecord filePreviewRecord, IFileStorage fileStorage, OnlyOfficeProperties onlyOfficeProperties) {
        this.filePreviewRecord = filePreviewRecord;
        this.fileStorage = fileStorage;
        this.onlyOfficeProperties = onlyOfficeProperties;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        log.debug("预览文件-----开始");
        String id = req.getParameter("id");
        log.debug("预览文件-----id:{}", id);
        FilePreviewInfo info = filePreviewRecord.findById(id);

        String contextPath = req.getContextPath();
        String extName = FileUtils.extName(info.getFileName());
        String fileType = FileUtils.fileType(extName);
        if ("word".equals(fileType) || "excel".equals(fileType) || "power point".equals(fileType) || "pdf".equals(fileType) || "txt".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", onlyOfficeProperties.getApijs());
            if ("word".equals(fileType) || "pdf".equals(fileType) || "txt".equals(fileType))
                data.put(DOCUMENT_TYPE, "word");
            else if ("excel".equals(fileType)) data.put(DOCUMENT_TYPE, "cell");
            else if ("power point".equals(fileType)) data.put(DOCUMENT_TYPE, "slide");
            data.put("fileType", "txt".equals(fileType) ? "txt" : extName);
            data.put("key", info.getId());
            data.put("title", info.getOriginalFilename());
            data.put("downloadUrl", onlyOfficeProperties.getDownload() + "?id=" + info.getId());
            data.put("callbackUrl", onlyOfficeProperties.getCallback() + "?id=" + info.getId());
            data.put("lang", "zh");
            data.put("userid", "file preview");
            data.put("username", "file preview");
            Page onlyofficePage = new Page("onlyoffice.ftl", data, resp);
            onlyofficePage.write();
        } else if ("markdown".equals(fileType)) {
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
