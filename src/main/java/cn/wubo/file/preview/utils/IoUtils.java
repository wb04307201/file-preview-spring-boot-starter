package cn.wubo.file.preview.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
public class IoUtils {

    private IoUtils() {
    }

    /**
     * 默认缓存大小 8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

    /**
     * 将字节数据写入指定的输出流中，完成后不关闭输出流。
     * 主要用于将字节数组内容复制到目标输出流。
     *
     * @param bytes 需要写入的字节数据。
     * @param os    目标输出流，用于接收字节数据。
     * @throws IOException 如果在写入过程中发生IO异常。
     */
    public static void writeToStream(byte[] bytes, OutputStream os) throws IOException {
        // 使用字节数组输入流包装字节数据，以便于复制到目标输出流
        try (InputStream is = new ByteArrayInputStream(bytes)) {
            copy(is, os);  // 执行实际的数据复制操作
        }
    }


    /**
     * 拷贝流，拷贝后不关闭流。该方法首先尝试将输入流直接读取到输出流，特别适用于文件流的高效拷贝。
     * 对于非文件输入流，则采用标准的分块读写方式。
     *
     * @param is 输入流，可以是任意类型的输入流。
     * @param os 输出流，可以是任意类型的输出流。
     * @throws IOException 如果在读取或写入过程中发生IO错误。
     */
    public static void copy(InputStream is, OutputStream os) throws IOException {
        if (is instanceof FileInputStream) {
            // 对于文件输入流，尝试直接读取全部内容以提高效率
            int available = is.available();
            byte[] result = new byte[available];
            int readLength = is.read(result);
            // 校验读取长度是否符合预期
            if (readLength != available) {
                throw new IOException(String.format("File length is [%s] but read [%s]!", available, readLength));
            }
            os.write(result);
        } else {
            // 对于非文件输入流，采用标准的分块读写方式
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            // 循环读取直到没有更多数据
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
        }
        // 确保数据被刷新到输出流
        os.flush();
    }

    /**
     * 安全关闭自动关闭资源。
     *
     * @param autoCloseable 自动关闭资源，如果为null，则不执行关闭操作。
     *                      必须实现AutoCloseable接口，以提供close()方法进行资源释放。
     * @see AutoCloseable 接口定义了能够自动关闭的资源，例如InputStream、OutputStream等。
     */
    public static void close(AutoCloseable autoCloseable) {
        // 检查资源是否为null，避免空指针异常
        if (autoCloseable != null) {
            try {
                // 尝试关闭资源，并捕获可能抛出的异常
                autoCloseable.close();
            } catch (Exception e) {
                // 记录异常信息，此处使用log.debug()进行日志记录，日志级别为DEBUG
                // 在实际环境中，可根据需要调整日志级别或处理方式
                log.debug(e.getMessage(), e);
            }
        }
    }

    /**
     * 将字节数据转换为字符串，该字符串是通过将字节数据写入临时文件，然后读取该文件的每行内容，
     * 将这些行内容合并并进行Base64编码得到的。
     *
     * @param bytes 需要转换的字节数据。
     * @param fileName 用于临时文件的文件名后缀。
     * @return 经过Base64编码的字符串，该字符串包含临时文件中所有行的内容。
     * @throws IOException 如果在读取文件或写入文件过程中发生I/O错误。
     */
    public static String readLines(byte[] bytes, String fileName) throws IOException {
        // 创建一个临时文件，并将字节数据写入临时文件
        Path path = FileUtils.writeTempFile(fileName, bytes);
        try (Stream<String> lines = Files.lines(path)) {
            // 读取临时文件的每行内容，合并为一个字符串，并对其进行Base64编码
            return new String(Base64.getEncoder().encode(lines.collect(Collectors.joining("\n")).getBytes()));
        } finally {
            // 最后删除临时文件
            Files.delete(path);
        }
    }

    /**
     * 将InputStream转换为byte数组。
     * 此方法会读取输入流中的所有数据，将其保存到一个byte数组中并返回。
     * 注意：如果输入流中的数据超过Integer.MAX_VALUE字节，将抛出IllegalArgumentException异常。
     *
     * @param inputStream 需要转换的输入流。
     * @return 从输入流中读取的所有数据的byte数组。
     * @throws IOException 如果读取输入流时发生错误。
     * @throws IllegalArgumentException 如果输入流中的数据超过Integer.MAX_VALUE字节。
     */
    public static byte[] toByteArray(final InputStream inputStream) throws IOException {
        // 使用UnsynchronizedByteArrayOutputStream和ThresholdingOutputStream来限制读取的数据量并捕获异常情况
        try (UnsynchronizedByteArrayOutputStream ubaOutput = UnsynchronizedByteArrayOutputStream.builder().get();
             ThresholdingOutputStream thresholdOutput = new ThresholdingOutputStream(Integer.MAX_VALUE, os -> {
                // 当数据量超过Integer.MAX_VALUE时，抛出异常
                throw new IllegalArgumentException(String.format("Cannot read more than %,d into a byte array", Integer.MAX_VALUE));
            }, os -> ubaOutput)) {
            // 将输入流中的数据复制到thresholdOutput中，实际上会被复制到ubaOutput中
            copy(inputStream, thresholdOutput);
            // 返回存储了输入流所有数据的byte数组
            return ubaOutput.toByteArray();
        }
    }
}
