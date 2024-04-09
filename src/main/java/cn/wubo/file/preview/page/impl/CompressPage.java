package cn.wubo.file.preview.page.impl;

import cn.wubo.file.preview.config.FilePreviewProperties;
import cn.wubo.file.preview.core.FilePreviewInfo;
import cn.wubo.file.preview.core.FilePreviewService;
import cn.wubo.file.preview.exception.PageRuntimeException;
import cn.wubo.file.preview.page.AbstractPage;
import cn.wubo.file.preview.page.PageFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.springframework.web.servlet.function.ServerResponse;

import java.io.BufferedInputStream;
import java.io.File;
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

    /**
     * 构建响应，根据压缩文件内文件名的不同执行不同的构建逻辑。
     * 该方法没有参数。
     *
     * @return ServerResponse 如果压缩文件名不为空，则返回构建压缩文件的响应；否则返回构建压缩列表的响应。
     */
    @Override
    public ServerResponse build() {
        // 判断压缩文件内文件名是否已设置，根据不同情况调用不同的构建方法
        if (getInfo().getCompressFileName() != null) return buildCompressFile();
        else return buildCompressList();
    }

    /**
     * 构建并返回一个压缩文件的服务器响应。
     * 该方法不接受参数，但依赖于外部方法获取信息、文件预览服务、属性以及上下文路径。
     * 它首先从压缩文件名中提取子文件名，然后设置该子文件名为当前文件名。
     * 最后，利用提取的信息和其他依赖项创建并构建一个服务器响应对象。
     *
     * @return ServerResponse 返回构建好的服务器响应对象，该对象包含了压缩文件的相关信息及预览等。
     */
    private ServerResponse buildCompressFile() {
        // 从压缩文件名中提取子文件名，直到最后一个“#”字符
        String subFileName = getInfo().getCompressFileName().substring(0, getInfo().getCompressFileName().lastIndexOf("#") + 1);
        // 如果子文件名中包含文件分隔符，则进一步提取最后一个文件分隔符后的部分作为子文件名
        if (subFileName.contains(File.separator))
            subFileName = subFileName.substring(subFileName.lastIndexOf(File.separator) + 1);
        // 设置提取的子文件名为当前文件名
        getInfo().setFileName(subFileName);
        // 利用当前信息、文件预览服务、属性以及上下文路径创建并构建服务器响应对象
        return PageFactory.create(getInfo(), getFilePreviewService(), getProperties(), getContextPath()).build();
    }

    /**
     * 构建压缩文件列表。
     * 该方法首先创建一个临时文件，将要预览的文件内容写入该临时文件，然后读取该临时文件的内容，
     * 解析文件为一个包含文件或目录信息的列表，并将这个列表以及相关上下文信息封装到服务器响应中返回。
     *
     * @return ServerResponse 包含压缩文件列表信息的服务器响应对象。
     * @throws PageRuntimeException 如果发生IO异常或归档异常，则抛出页面运行时异常。
     */
    private ServerResponse buildCompressList() {
        try {
            // 初始化数据容器，用于存放最终返回的数据
            Map<String, Object> data = new HashMap<>();
            data.put(CONTEXT_PATH, getContextPath());

            // 创建临时文件并写入文件预览内容
            Path path = Files.createTempFile(String.valueOf(System.currentTimeMillis()), getInfo().getFileName());
            Files.write(path, getFilePreviewService().getBytes(getInfo()));

            // 存储文件或目录信息的列表
            List<Map<String, Object>> list = new ArrayList<>();
            try (InputStream is = Files.newInputStream(path); BufferedInputStream bis = new BufferedInputStream(is); ArchiveInputStream<ArchiveEntry> ais = new ArchiveStreamFactory().createArchiveInputStream(bis)) {
                ArchiveEntry entry;
                AtomicInteger atomicInteger = new AtomicInteger(0);
                // 遍历归档文件中的所有条目，构建文件信息列表
                while ((entry = ais.getNextEntry()) != null) {
                    Map<String, Object> map = new HashMap<>();
                    // 区分条目是目录还是文件，并记录相关信息
                    if (entry.isDirectory()) {
                        log.debug("directory: " + entry.getName());
                        map.put("fileType", "directory");
                    } else {
                        log.debug("file: " + entry.getName());
                        map.put("fileType", "file");
                    }
                    map.put("fileName", entry.getName());
                    map.put("id", atomicInteger.addAndGet(1));
                    list.add(map);
                }
            }
            // 将文件列表和主要ID添加到数据容器
            data.put("list", list);
            data.put("mainid", this.getInfo().getId());
            // 使用数据容器构建并返回服务器响应
            return writePage("compress.ftl", data);
        } catch (IOException | ArchiveException e) {
            // 抛出页面运行时异常，以处理读写或归档过程中的异常
            throw new PageRuntimeException(e.getMessage(), e);
        }
    }
}
