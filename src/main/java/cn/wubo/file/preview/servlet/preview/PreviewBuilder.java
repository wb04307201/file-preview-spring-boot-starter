package cn.wubo.file.preview.servlet.preview;

import cn.wubo.file.preview.common.CommonUtils;
import cn.wubo.file.preview.common.Page;
import cn.wubo.file.preview.dto.ConvertInfoDto;

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

    public static void common(ConvertInfoDto convertInfoDto, HttpServletRequest req, HttpServletResponse resp) {
        if ("markdown".equals(convertInfoDto.getType())) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            try (Stream<String> lines = Files.lines(Paths.get(convertInfoDto.getFilePath()))) {
                data.put("content", new String(Base64.getEncoder().encode(lines.collect(Collectors.joining("\n")).getBytes())));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Page markdownPage = new Page("markdown.ftl", data, resp);
            markdownPage.write();
        } else if ("video".equals(convertInfoDto.getType())) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", "/file/preview/download?id=" + convertInfoDto.getId());
            Page markdownPage = new Page("video.ftl", data, resp);
            markdownPage.write();
        } else if ("audio".equals(convertInfoDto.getType())) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", "/file/preview/download?id=" + convertInfoDto.getId());
            Page markdownPage = new Page("audio.ftl", data, resp);
            markdownPage.write();
        } else if ("pdf".equals(convertInfoDto.getType())) {
            try {
                resp.sendRedirect(String.format("/pdfjs/3.0.279/web/viewer.html?file=%s/file/preview/download?id=%s", req.getContextPath(), convertInfoDto.getId()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            CommonUtils.setContentType(resp, convertInfoDto.getType(), convertInfoDto.getExtName());
            File file = new File(convertInfoDto.getFilePath());
            try (OutputStream os = resp.getOutputStream()) {
                CommonUtils.writeToStream(file, os);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void onlyOffice(ConvertInfoDto convertInfoDto, HttpServletRequest req, HttpServletResponse resp, OnlyOfficePreviewProperties onlyOfficePreviewProperties) {
        if ("word".equals(convertInfoDto.getType()) || "excel".equals(convertInfoDto.getType()) || "power point".equals(convertInfoDto.getType()) || "pdf".equals(convertInfoDto.getType()) || "txt".equals(convertInfoDto.getType())) {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, req.getContextPath());
            data.put("url", onlyOfficePreviewProperties.getApijs());
            if ("word".equals(convertInfoDto.getType()) || "pdf".equals(convertInfoDto.getType()) || "txt".equals(convertInfoDto.getType()))
                data.put(DOCUMENT_TYPE, "word");
            else if ("excel".equals(convertInfoDto.getType())) data.put(DOCUMENT_TYPE, "cell");
            else if ("power point".equals(convertInfoDto.getType())) data.put(DOCUMENT_TYPE, "slide");
            data.put("fileType", "txt".equals(convertInfoDto.getType()) ? "txt" : convertInfoDto.getExtName());
            data.put("key", convertInfoDto.getId());
            data.put("title", convertInfoDto.getSourceFileName());
            data.put("downloadUrl", onlyOfficePreviewProperties.getDownload() + "?id=" + convertInfoDto.getId());
            data.put("callbackUrl", onlyOfficePreviewProperties.getCallback() + "?id=" + convertInfoDto.getId());
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
