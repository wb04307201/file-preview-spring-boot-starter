package cn.wubo.file.preview.utils;

import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class IoUtils {

    /**
     * 默认缓存大小 8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    /**
     * 拷贝bytes到输出流，拷贝后不关闭输出流
     *
     * @param bytes 内容bytes
     * @param os    输出流
     * @throws IOException-IO异常
     */
    public static void writeToStream(byte[] bytes, OutputStream os) throws IOException {
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            copy(is, os);
        }
    }

    /**
     * 拷贝流，拷贝后不关闭流
     * 文件流直接读取效率更高
     *
     * @param is 输入流
     * @param os 输出流
     * @throws IOException -IO异常
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
        if (is instanceof FileInputStream) {
            // 文件流的长度是可预见的，此时直接读取效率更高
            int available = is.available();
            byte[] result = new byte[available];
            int readLength = is.read(result);
            if (readLength != available) {
                throw new IOException(String.format("File length is [%s] but read [%s]!", available, readLength));
            }
            os.write(result);
        } else {
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        }
        os.flush();
    }

    public static void close(AutoCloseable autoCloseable) {
        if (autoCloseable != null) {
            try {
                autoCloseable.close();
            } catch (Exception e) {
                log.debug(e.getMessage(), e);
            }
        }
    }

    public static String readLines(byte[] bytes, String fileName) throws IOException {
        Path path = Files.createTempFile(String.valueOf(System.currentTimeMillis()), fileName);
        Files.write(path, bytes);
        try (Stream<String> lines = Files.lines(path)) {
            return new String(Base64.getEncoder().encode(lines.collect(Collectors.joining("\n")).getBytes()));
        } finally {
            Files.delete(path);
        }
    }
}
