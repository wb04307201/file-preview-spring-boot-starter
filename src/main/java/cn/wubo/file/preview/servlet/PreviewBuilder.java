package cn.wubo.file.preview.servlet;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.common.Page;
import cn.wubo.file.preview.config.OnlyOfficeProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.dto.ConvertInfoDto;
import cn.wubo.file.preview.utils.FileUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PreviewBuilder {

    private static final String CONTEXT_PATH = "contextPath";
    private static final String DOCUMENT_TYPE = "documentType";

    private PreviewBuilder() {
    }

    public static void common(FilePreviewInfo filePreviewInfo, HttpServletRequest req, HttpServletResponse resp) {
        String fileType = FileUtils.fileType(filePreviewInfo.getFileName());
        if ("markdown".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            try (Stream<String> lines = Files.lines(Paths.get(convertInfoDto.getFilePath()))) {
                data.put("content", new String(Base64.getEncoder().encode(lines.collect(Collectors.joining("\n")).getBytes())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Page markdownPage = new Page("markdown.ftl", data, resp);
            markdownPage.write();
        } else if ("video".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", req.getContextPath() + "/file/preview/download?id=" + filePreviewInfo.getId());
            Page markdownPage = new Page("video.ftl", data, resp);
            markdownPage.write();
        } else if ("audio".equals(fileType)) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", req.getContextPath() + "/file/preview/download?id=" + filePreviewInfo.getId());
            Page markdownPage = new Page("audio.ftl", data, resp);
            markdownPage.write();
        } else if ("pdf".equals(fileType)) {
            try {
                resp.sendRedirect(String.format("%s/pdfjs/3.0.279/web/viewer.html?file=%s/file/preview/download?id=%s", req.getContextPath(), req.getContextPath(), filePreviewInfo.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            FileUtils.getMimeType(filePreviewInfo.getFileName());
            resp.setContentType("audio/mpeg;charset=UTF-8");
            CommonUtils.setContentType(resp, fileType, convertInfoDto.getExtName());
            File file = new File(convertInfoDto.getFilePath());
            try (OutputStream os = resp.getOutputStream()) {
                CommonUtils.writeToStream(file, os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void onlyOffice(ConvertInfoDto convertInfoDto, HttpServletRequest req, HttpServletResponse resp, OnlyOfficeProperties onlyOfficeProperties) {
        if ("word".equals(convertInfoDto.getType()) || "excel".equals(convertInfoDto.getType()) || "power point".equals(convertInfoDto.getType()) || "pdf".equals(convertInfoDto.getType()) || "txt".equals(convertInfoDto.getType())) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", onlyOfficeProperties.getApijs());
            if ("word".equals(convertInfoDto.getType()) || "pdf".equals(convertInfoDto.getType()) || "txt".equals(convertInfoDto.getType()))
                data.put(DOCUMENT_TYPE, "word");
            else if ("excel".equals(convertInfoDto.getType())) data.put(DOCUMENT_TYPE, "cell");
            else if ("power point".equals(convertInfoDto.getType())) data.put(DOCUMENT_TYPE, "slide");
            data.put("fileType", "txt".equals(convertInfoDto.getType()) ? "txt" : convertInfoDto.getExtName());
            data.put("key", convertInfoDto.getId());
            data.put("title", convertInfoDto.getSourceFileName());
            data.put("downloadUrl", onlyOfficeProperties.getDownload() + "?id=" + convertInfoDto.getId());
            data.put("callbackUrl", onlyOfficeProperties.getCallback() + "?id=" + convertInfoDto.getId());
            data.put("lang", "zh");
            data.put("userid", "file preview");
            data.put("username", "file preview");
            Page onlyofficePage = new Page("onlyoffice.ftl", data, resp);
            onlyofficePage.write();
        } else {
            common(convertInfoDto, req, resp);
        }
    }
}
