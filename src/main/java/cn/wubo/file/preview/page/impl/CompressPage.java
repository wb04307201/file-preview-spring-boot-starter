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
import org.springframework.web.servlet.function.ServerResponse;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class CompressPage extends AbstractPage {
    public CompressPage(String fileType, String extName, String contextPath, FilePreviewInfo info, FilePreviewService filePreviewService, FilePreviewProperties properties) {
        super(fileType, extName, contextPath, info, filePreviewService, properties);
    }

    @Override
    public ServerResponse build() {
        try {
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, getContextPath());
            Path path = Files.createTempFile(String.valueOf(System.currentTimeMillis()), getInfo().getFileName());
            Files.write(path, getFilePreviewService().getBytes(getInfo()));
            List<Map<String, Object>> list = new ArrayList<>();
            try (InputStream is = Files.newInputStream(path)) {
                try (BufferedInputStream bis = new BufferedInputStream(is)) {
                    try (ArchiveInputStream<ArchiveEntry> ais = new ArchiveStreamFactory().createArchiveInputStream(bis)) {
                        ArchiveEntry entry;
                        AtomicInteger atomicInteger = new AtomicInteger(0);
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
                            map.put("id",atomicInteger.addAndGet(1));
                            list.add(map);
                        }
                    }
                }
            }
            data.put("list", list);
            return writePage("compress.ftl", data);
        } catch (IOException | ArchiveException e) {
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }
}
