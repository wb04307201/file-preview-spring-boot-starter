package cn.wubo.file.preview.utils;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveInputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.springframework.util.StringUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {

    private FileUtils() {
    }

    /**
     * 特殊后缀
     */
    private static final CharSequence[] SPECIAL_SUFFIX = {"tar.bz2", "tar.Z", "tar.gz", "tar.xz"};

    /**
     * 路径分隔符
     */
    public static final CharSequence[] SEPARATOR = {"/", "\\"};

    public static final String DOT = ".";

    public static final String EMPTY = ".";

    /**
     * 获得文件的扩展名（后缀名），扩展名不带“.”
     *
     * @param fileName 文件名
     * @return 扩展名
     */
    public static String extName(String fileName) {
        if (fileName == null) {
            return null;
        }
        final int index = fileName.lastIndexOf(DOT);
        if (index == -1) {
            return EMPTY;
        } else {
            final int secondToLastIndex = fileName.substring(0, index).lastIndexOf(DOT);
            final String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
            if (containsAny(substr, SPECIAL_SUFFIX)) {
                return substr;
            }

            final String ext = fileName.substring(index + 1);
            // 扩展名中不能包含路径相关的符号
            return containsAny(ext, SEPARATOR) ? EMPTY : ext;
        }
    }

    /**
     * 查找指定字符串是否包含指定字符串列表中的任意一个字符串
     *
     * @param str      指定字符串
     * @param testStrs 需要检查的字符串数组
     * @return 是否包含任意一个字符串
     */
    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        if (!StringUtils.hasLength(str) || arrayIsEmpty(testStrs)) {
            return false;
        }
        for (CharSequence checkStr : testStrs) {
            if (str.toString().contains(checkStr)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 数组是否为空
     *
     * @param <T>   数组元素类型
     * @param array 数组
     * @return 是否为空
     */
    public static <T> boolean arrayIsEmpty(T[] array) {
        return array == null || array.length == 0;
    }

    /**
     * 根据后缀名对文件进行分类
     *
     * @param extName
     * @return
     */
    public static String fileType(String extName) {
        return switch (extName.toLowerCase()) {
            case "doc", "docm", "docx", "dot", "dotm" -> "word";
            case "xls", "xlt", "xlsx", "xlsm", "xltx", "xltm", "csv" -> "excel";
            case "ppt", "pot", "pps", "pptx", "pptm", "potx", "ppsx", "ppsm" -> "power point";
            case "gif", "jpg", "jpeg", "bmp", "tiff", "tif", "png", "svg" -> "image";
            case "ogg", "webm", "3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4" ->
                    "video";
            case "mp3", "wav", "m4a", "m3u", "m4b", "m4p", "mp2", "mpga", "rmvb", "wma", "wmv" -> "audio";
            case "zip", "rar", "7z", "gzip" -> "compressed file";
            case "pdf" -> "pdf";
            case "ofd" -> "ofd";
            case "htm", "html" -> "html";
            case "md" -> "markdown";
            case "sql" -> "sql";
            case "c", "cpp", "c++", "h" -> "cpp";
            case "java" -> "java";
            case "xml" -> "xml";
            case "js" -> "javascript";
            case "json" -> "json";
            case "css" -> "css";
            case "py", "py3" -> "python";
            case "epub" -> "epub";
            case "xmind" -> "xmind";
            case "bpmn" -> "bpmn";
            case "cmmn" -> "cmmn";
            case "dmn" -> "dmn";
            case "txt", "log", "conf", "prop", "rc", "sh", "yaml", "properties" -> "text";
            default -> "txt";
        };
    }

    /**
     * 根据文件扩展名获得MimeType
     *
     * @param filePath 文件路径或文件名
     * @return MimeType
     */
    public static String getMimeType(String filePath) throws IOException {
        return getMimeType(filePath, false);
    }

    /**
     * 根据文件扩展名获得MimeType
     *
     * @param filePath  文件路径或文件名
     * @param checkFile 是否检测本地文件
     * @return MimeType
     */
    public static String getMimeType(String filePath, Boolean checkFile) throws IOException {
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
        if (null == contentType) {
            // 补充一些常用的mimeType
            // 压缩文件
            if (StringUtils.endsWithIgnoreCase(filePath, ".rar")) {
                contentType = "application/x-rar-compressed";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".7z")) {
                contentType = "application/x-7z-compressed";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".gtar")) {
                contentType = "application/x-gtar";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".gz")) {
                contentType = "application/x-gzip";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".tar")) {
                contentType = "application/x-tar";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".tgz")) {
                contentType = "application/x-compressed";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".z")) {
                contentType = "application/x-compress";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".zip")) {
                contentType = "application/x-zip-compressed";
            }
            // office
            // word
            else if (StringUtils.endsWithIgnoreCase(filePath, ".doc")) {
                contentType = "application/msword";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".dot")) {
                contentType = "application/msword-template";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".dotm") || StringUtils.endsWithIgnoreCase(filePath, ".docm")) {
                contentType = "application/vnd.ms-word.template.macroenabled.12";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".dotx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.template";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".docx")) {
                contentType = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
            }
            // excel
            else if (StringUtils.endsWithIgnoreCase(filePath, ".xls") || StringUtils.endsWithIgnoreCase(filePath, ".xlt")) {
                contentType = "application/vnd.ms-excel";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".xlsx")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".xlsm")) {
                contentType = "application/vnd.ms-excel.sheet.macroenabled.12";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".xltx")) {
                contentType = "application/vnd.openxmlformats-officedocument.spreadsheetml.template";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".xltm")) {
                contentType = "application/vnd.ms-excel.template.macroenabled.12";
            }
            // ppt
            else if (StringUtils.endsWithIgnoreCase(filePath, ".ppt") || StringUtils.endsWithIgnoreCase(filePath, ".pps") || StringUtils.endsWithIgnoreCase(filePath, ".pot")) {
                contentType = "application/vnd.ms-powerpoint";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".pptx")) {
                contentType = "application/vnd.openxmlformats-officedocument.presentationml.presentation";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".pptm")) {
                contentType = "application/vnd.ms-powerpoint.presentation.macroenabled.12";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".potx")) {
                contentType = "application/vnd.openxmlformats-officedocument.presentationml.template";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".ppsx")) {
                contentType = "application/vnd.openxmlformats-officedocument.presentationml.slideshow";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".ppsm")) {
                contentType = "application/vnd.ms-powerpoint.slideshow.macroenabled.12";
            }
            // 视频
            else if (StringUtils.endsWithIgnoreCase(filePath, ".3gp")) {
                contentType = "video/3gpp";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".asf")) {
                contentType = "video/x-ms-asf";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".avi")) {
                contentType = "video/x-msvideo";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".m4u")) {
                contentType = "video/vnd.mpegurl";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".m4v")) {
                contentType = "video/x-m4v";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".mov")) {
                contentType = "video/quicktime";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".mp4") || StringUtils.endsWithIgnoreCase(filePath, ".mpg4")) {
                contentType = "video/mp4";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".mpe") || StringUtils.endsWithIgnoreCase(filePath, ".mpeg") || StringUtils.endsWithIgnoreCase(filePath, ".mpg")) {
                contentType = "video/mpeg";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".webm") || StringUtils.endsWithIgnoreCase(filePath, ".weba")) {
                contentType = "video/webm";
            }
            // 音频
            else if (StringUtils.endsWithIgnoreCase(filePath, ".m3u")) {
                contentType = "audio/x-mpegurl";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".m4a") || StringUtils.endsWithIgnoreCase(filePath, ".m4b") || StringUtils.endsWithIgnoreCase(filePath, ".m4p")) {
                contentType = "audio/mp4a-latm";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".mp2") || StringUtils.endsWithIgnoreCase(filePath, ".mp3")) {
                contentType = "audio/x-mpeg";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".wav")) {
                contentType = "audio/x-wav";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".wma")) {
                contentType = "audio/x-ms-wma";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".wmv")) {
                contentType = "audio/x-ms-wmv";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".ogg")) {
                contentType = "audio/ogg";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".rmvb")) {
                contentType = "audio/x-pn-realaudio";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".mpga")) {
                contentType = "audio/mpeg";
            }
            // 图片
            else if (StringUtils.endsWithIgnoreCase(filePath, ".bmp")) {
                contentType = "image/bmp";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".gif")) {
                contentType = "image/gif";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".jpeg") || StringUtils.endsWithIgnoreCase(filePath, ".jpg") || StringUtils.endsWithIgnoreCase(filePath, ".jpe") || StringUtils.endsWithIgnoreCase(filePath, ".jpz")) {
                contentType = "image/jpeg";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".png")) {
                contentType = "image/png";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".tiff") || StringUtils.endsWithIgnoreCase(filePath, ".tif")) {
                contentType = "image/tiff";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".svg") || StringUtils.endsWithIgnoreCase(filePath, ".svgz")) {
                contentType = "image/svg+xml";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".webp")) {
                contentType = "image/webp";
            }
            // 文本
            else if (StringUtils.endsWithIgnoreCase(filePath, ".txt") || StringUtils.endsWithIgnoreCase(filePath, ".text") || StringUtils.endsWithIgnoreCase(filePath, ".conf") || StringUtils.endsWithIgnoreCase(filePath, ".prop") || StringUtils.endsWithIgnoreCase(filePath, ".rc") || StringUtils.endsWithIgnoreCase(filePath, ".yaml") || StringUtils.endsWithIgnoreCase(filePath, ".properties")) {
                contentType = "text/plain";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".htm") || StringUtils.endsWithIgnoreCase(filePath, ".html")) {
                contentType = "text/html";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".java")) {
                contentType = "text/x-java";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".sh")) {
                contentType = "text/x-sh";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".c")) {
                contentType = "text/csrc";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".cpp") || StringUtils.endsWithIgnoreCase(filePath, ".c++")) {
                contentType = "text/x-c++src";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".h")) {
                contentType = "text/x-c++hdr";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".log")) {
                contentType = "text/x-log";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".xml")) {
                contentType = "text/xml";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".sql")) {
                contentType = "text/x-sql";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".css")) {
                contentType = "text/css";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".csv")) {
                contentType = "text/csv";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".py")) {
                contentType = "text/x-python";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".py3")) {
                contentType = "text/x-python3";
            }
            // 其他
            else if (StringUtils.endsWithIgnoreCase(filePath, ".jar")) {
                contentType = "application/java-archive";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".mpc")) {
                contentType = "application/vnd.mpohun.certificate";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".msg")) {
                contentType = "application/vnd.ms-outlook";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".pdf")) {
                contentType = "application/pdf";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".rtf")) {
                contentType = "application/rtf";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".wps")) {
                contentType = "application/vnd.ms-works";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".epub")) {
                contentType = "application/epub+zip";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".json")) {
                contentType = "application/json";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".js")) {
                contentType = "application/x-javascript";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".apk")) {
                contentType = "application/vnd.android.package-archive";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".bin") || StringUtils.endsWithIgnoreCase(filePath, ".class") || StringUtils.endsWithIgnoreCase(filePath, ".exe")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".xmind")) {
                contentType = "application/xmind";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".wgt")) {
                contentType = "application/widget";
            }
        }

        // 补充
        if (null == contentType && checkFile) {
            contentType = Files.probeContentType(Paths.get(filePath));
        }

        if (null == contentType) {
            contentType = "text/plain";
        }

        return contentType;
    }

    /**
     * 将指定的字节数组写入临时文件。
     *
     * 该方法通过结合当前系统时间和指定的tempFileName来生成唯一的临时文件名，
     * 将字节数组写入文件，然后返回创建的文件路径。使用Files.createTempFile确保了文件名的唯一性和临时文件位置的安全性。
     *
     * @param tempFileName 临时文件名的后缀。与当前系统时间组合以生成唯一的文件名。
     * @param bytes 要写入文件的字节数组。
     * @return 创建的临时文件的Path对象，表示文件路径。
     * @throws IOException 如果在文件创建或写入过程中发生I/O错误。
     */
    public static Path writeTempFile(String tempFileName, byte[] bytes) throws IOException {
        // 根据当前系统时间和指定的tempFileName生成唯一的临时文件路径。
        Path path = Files.createTempFile(String.valueOf(System.currentTimeMillis()), tempFileName);
        // 将指定的字节数组写入文件。
        Files.write(path, bytes);
        // 返回创建的文件路径。
        return path;
    }

    /**
     * 从压缩文件中提取指定的子文件，并返回其临时路径。
     *
     * 此方法创建一个临时文件，从压缩文件中读取指定的子文件内容，并写入该临时文件。
     * 主要用于处理压缩文件中单个文件的提取操作。
     *
     * @param compressFilePath 压缩文件的路径。
     * @param compressFileName 压缩文件中要提取的子文件名。
     * @return 提取的子文件的临时路径。
     * @throws IOException 如果读写文件时发生错误。
     * @throws ArchiveException 如果处理压缩文件时发生错误。
     */
    public static Path getSubCompressFile(Path compressFilePath, String compressFileName) throws IOException, ArchiveException {
        // 将字符串compressFileName转换为新的字符串对象subFileName
        String subFileName = new String(compressFileName);
        // 如果子文件名包含"/"，则提取最后一部分作为真正的子文件名
        if (subFileName.contains("/"))
            subFileName = subFileName.substring(subFileName.lastIndexOf("/") + 1);

        // 创建一个临时文件，名称基于当前时间戳和子文件名
        Path path = Files.createTempFile(String.valueOf(System.currentTimeMillis()), subFileName);

        // 使用try-with-resources语句确保资源正确关闭
        try (InputStream is = Files.newInputStream(compressFilePath);
             BufferedInputStream bis = new BufferedInputStream(is);
             ArchiveInputStream<ArchiveEntry> ais = new ArchiveStreamFactory().createArchiveInputStream(bis)) {
            ArchiveEntry entry;
            // 遍历压缩文件的每个条目，直到找到匹配的子文件
            while ((entry = ais.getNextEntry()) != null) {
                // 如果当前条目的名称与目标子文件名匹配
                if (compressFileName.equals(entry.getName())) {
                    // 读取条目的所有字节，并写入临时文件
                    Files.write(path, ais.readAllBytes());
                    // 找到匹配后跳出循环
                    break;
                }
            }
        }
        // 返回临时文件的路径
        return path;
    }

}
