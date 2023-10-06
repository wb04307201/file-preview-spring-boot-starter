package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.page.AbstractPage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class CompressPage extends AbstractPage {

    protected CompressPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties, HttpServletResponse resp) {
        super(fileType, extName, contextPath, info, filePreviewService, properties, resp);
    }

    @Override
    public void build() throws IOException {
        Map<String, Object> data = new HashMap<>();
        data.put(CONTEXT_PATH, getContextPath());
        Path path = Files.createTempFile(String.valueOf(System.currentTimeMillis()), getInfo().getFileName());
        Files.write(path, getFilePreviewService().getBytes(getInfo()));
        List<Map<String, Object>> list = new ArrayList<>();
        try (ArchiveInputStream ais = new ArchiveStreamFactory().createArchiveInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            ArchiveEntry entry;
            while ((entry = ais.getNextEntry()) != null) {
                Map<String, Object> map = new HashMap<>();
                if (entry.isDirectory()) {
                    log.debug("directory: " + entry.getName());
                    map.put("fileType", "directory");
                } else {
                    log.debug("file: " + entry.getName());
                    map.put("fileType", "file");
                }
                map.put("fileName", entry.getName());
                list.add(map);
            }
        } catch (ArchiveException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
        data.put("list", list);
        writePage("compress.ftl", data);
    }
}
