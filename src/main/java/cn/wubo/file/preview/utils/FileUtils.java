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
     * 获取文件名的扩展名（后缀名）。
     * 扩展名是指文件名中最后一个“.”之后的部分。如果文件名中没有“.”，或者“.”后面紧跟着路径分隔符，
     * 则视为该文件没有扩展名。
     * 特殊情况下，如果文件名以特殊后缀结尾（如".zip.gz"），会返回整个特殊后缀。
     *
     * @param fileName 文件名或路径
     * @return 扩展名，如果没有扩展名则返回空字符串，如果文件名是null则返回null。
     */
    public static String extName(String fileName) {
        // 检查文件名是否为空
        if (fileName == null) {
            return null;
        }
        // 查找最后一个“.”
        final int index = fileName.lastIndexOf(DOT);
        // 如果没有“.”，则没有扩展名
        if (index == -1) {
            return EMPTY;
        } else {
            // 查找倒数第二个“.”，以处理如".zip.gz"这样的特殊后缀
            final int secondToLastIndex = fileName.substring(0, index).lastIndexOf(DOT);
            // 提取从倒数第二个“.”之后到最后一个“.”之前的字符串
            final String substr = fileName.substring(secondToLastIndex == -1 ? index : secondToLastIndex + 1);
            // 如果该字符串包含在特殊后缀列表中，则返回整个特殊后缀
            if (containsAny(substr, SPECIAL_SUFFIX)) {
                return substr;
            }

            // 提取从最后一个“.”之后的字符串作为扩展名
            final String ext = fileName.substring(index + 1);
            // 如果扩展名中包含路径分隔符，则视为没有扩展名
            // 扩展名中不能包含路径相关的符号
            return containsAny(ext, SEPARATOR) ? EMPTY : ext;
        }
    }

    /**
     * 检查指定字符串是否包含在给定的字符串数组中。
     * <p>
     * 此方法用于验证一个主字符串是否含有一个或多个子字符串。这在某些验证场景中非常有用，比如检查输入文本是否包含敏感词。
     *
     * @param str      要检查的主字符串。
     * @param testStrs 一个字符串数组，包含要检查的子字符串。
     * @return 如果主字符串包含任何子字符串，则返回true；否则返回false。
     */
    public static boolean containsAny(CharSequence str, CharSequence... testStrs) {
        // 首先检查主字符串和子字符串数组是否为空，如果任意一个为空，则直接返回false。
        if (!StringUtils.hasLength(str) || arrayIsEmpty(testStrs)) {
            return false;
        }
        // 遍历子字符串数组，检查主字符串是否包含每个子字符串。
        for (CharSequence checkStr : testStrs) {
            // 如果主字符串包含当前检查的子字符串，则返回true。
            if (str.toString().contains(checkStr)) {
                return true;
            }
        }
        // 如果遍历完所有子字符串后都没有找到匹配的，则返回false。
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
     * 根据文件扩展名确定文件类型。
     *
     * 此方法通过文件扩展名来识别各种文件类型，并返回相应的类型名称。
     * 支持的文件类型包括文档、电子表格、演示文稿、图片、视频、音频、压缩文件、
     * PDF、OFD、HTML、Markdown、SQL、C/C++、Java、XML、JavaScript、JSON、CSS、Python、
     * EPUB、Xmind、BPMN、CMMN、DMN、文本和其他3D模型文件等。
     * 对于不识别的文件扩展名，将默认返回"txt"作为文件类型。
     *
     * @param extName 文件的扩展名，不包含点号（.）。
     * @return 文件的类型名称。
     */
    public static String fileType(String extName) {
        // 使用switch表达式根据文件扩展名的小写形式判断文件类型
        return switch (extName.toLowerCase()) {
            case "doc", "docm", "docx", "dot", "dotm" -> "word"; // 文档类型：Microsoft Word
            case "xls", "xlt", "xlsx", "xlsm", "xltx", "xltm", "csv" -> "excel"; // 电子表格类型：Microsoft Excel
            case "ppt", "pot", "pps", "pptx", "pptm", "potx", "ppsx", "ppsm" -> "power point"; // 演示文稿类型：Microsoft PowerPoint
            case "gif", "jpg", "jpeg", "bmp", "tiff", "tif", "png", "svg" -> "image"; // 图片类型
            case "ogg", "webm", "3gp", "asf", "avi", "m4u", "m4v", "mov", "mp4", "mpe", "mpeg", "mpg", "mpg4" ->
                    "video"; // 视频类型
            case "mp3", "wav", "m4a", "m3u", "m4b", "m4p", "mp2", "mpga", "rmvb", "wma", "wmv" -> "audio"; // 音频类型
            case "zip", "rar", "7z", "gzip" -> "compressed file"; // 压缩文件类型
            case "pdf" -> "pdf"; // PDF类型
            case "ofd" -> "ofd"; // OFD类型
            case "htm", "html" -> "html"; // HTML类型
            case "md" -> "markdown"; // Markdown类型
            case "sql" -> "sql"; // SQL类型
            case "c", "cpp", "c++", "h" -> "cpp"; // C/C++源代码类型
            case "java" -> "java"; // Java源代码类型
            case "xml" -> "xml"; // XML类型
            case "js" -> "javascript"; // JavaScript类型
            case "json" -> "json"; // JSON类型
            case "css" -> "css"; // CSS类型
            case "py", "py3" -> "python"; // Python类型
            case "epub" -> "epub"; // EPUB电子书类型
            case "xmind" -> "xmind"; // Xmind思维导图类型
            case "bpmn" -> "bpmn"; // BPMN业务流程类型
            case "cmmn" -> "cmmn"; // CMMN案例管理类型
            case "dmn" -> "dmn"; // DMN决策模型类型
            case "txt", "log", "conf", "prop", "rc", "sh", "yaml", "properties" -> "text"; // 文本类型
            case "3dm", "3ds", "3mf", "amf", "bim", "brep", "dae", "fbx", "fcstd", "gltf", "ifc", "iges", "step", "stl",
                 "obj", "off", "ply", "wrl" -> "o3dv"; // 3D模型类型
            default -> "txt"; // 默认为文本类型
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
     * 根据文件路径获取文件的MIME类型。
     *
     * @param filePath 文件路径。
     * @param checkFile 是否检查文件的实际内容以确定MIME类型。
     * @return 文件的MIME类型。
     * @throws IOException 如果发生I/O错误。
     */
    public static String getMimeType(String filePath, Boolean checkFile) throws IOException {
        // 使用URLConnection.getFileNameMap().getContentTypeFor()尝试获取文件的MIME类型
        String contentType = URLConnection.getFileNameMap().getContentTypeFor(filePath);
        if (null == contentType) {
            // 如果获取失败，则根据文件后缀手动匹配MIME类型
            // 下面是各种文件类型及其对应的MIME类型的匹配逻辑
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
            // office文档
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
            // 视频文件
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
            // 音频文件
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
            // 图片文件
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
            // 文本文件
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
            // 3D文件
            // application/octet-stream
            else if (StringUtils.endsWithIgnoreCase(filePath, ".3dm")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".3ds")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".3mf")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".amf")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".bim")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".brep")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".dae")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".fbx")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".fcstd")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".gltf")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".ifc")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".iges")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".step")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".stl")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".off")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".ply")) {
                contentType = "application/octet-stream";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".wrl")) {
                contentType = "application/octet-stream";
            }
            // 其他文件类型
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

        // 如果通过文件后缀无法确定contentType，且checkFile为true，则通过检查文件的实际内容来尝试确定contentType
        // 补充
        if (null == contentType && checkFile) {
            contentType = Files.probeContentType(Paths.get(filePath));
        }

        // 如果仍然无法确定contentType，则默认为"text/plain"
        if (null == contentType) {
            contentType = "text/plain";
        }

        // 返回最终确定的contentType
        return contentType;
    }

    /**
     * 将指定的字节数组写入临时文件。
     * <p>
     * 该方法通过结合当前系统时间和指定的tempFileName来生成唯一的临时文件名，
     * 将字节数组写入文件，然后返回创建的文件路径。使用Files.createTempFile确保了文件名的唯一性和临时文件位置的安全性。
     *
     * @param tempFileName 临时文件名的后缀。与当前系统时间组合以生成唯一的文件名。
     * @param bytes        要写入文件的字节数组。
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
     * <p>
     * 此方法创建一个临时文件，从压缩文件中读取指定的子文件内容，并写入该临时文件。
     * 主要用于处理压缩文件中单个文件的提取操作。
     *
     * @param compressFilePath 压缩文件的路径。
     * @param compressFileName 压缩文件中要提取的子文件名。
     * @return 提取的子文件的临时路径。
     * @throws IOException      如果读写文件时发生错误。
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
