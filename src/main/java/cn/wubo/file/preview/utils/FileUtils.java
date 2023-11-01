package cn.wubo.file.preview.utils;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
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
        switch (extName.toLowerCase()) {
            case "doc":
            case "docm":
            case "docx":
            case "dot":
            case "dotm":
                return "word";
            case "xls":
            case "xlt":
            case "xlsx":
            case "xlsm":
            case "xltx":
            case "xltm":
            case "csv":
                return "excel";
            case "ppt":
            case "pot":
            case "pps":
            case "pptx":
            case "pptm":
            case "potx":
            case "ppsx":
            case "ppsm":
                return "power point";
            case "gif":
            case "jpg":
            case "jpeg":
            case "bmp":
            case "tiff":
            case "tif":
            case "png":
            case "svg":
                return "image";
            case "ogg":
            case "webm":
            case "3gp":
            case "asf":
            case "avi":
            case "m4u":
            case "m4v":
            case "mov":
            case "mp4":
            case "mpe":
            case "mpeg":
            case "mpg":
            case "mpg4":
                return "video";
            case "mp3":
            case "wav":
            case "m4a":
            case "m3u":
            case "m4b":
            case "m4p":
            case "mp2":
            case "mpga":
            case "rmvb":
            case "wma":
            case "wmv":
                return "audio";
            case "zip":
            case "rar":
            case "7z":
            case "gzip":
                return "compressed file";
            case "pdf":
                return "pdf";
            case "htm":
            case "html":
                return "html";
            case "md":
                return "markdown";
            case "sql":
                return "sql";
            case "c":
            case "cpp":
            case "c++":
            case "h":
                return "cpp";
            case "java":
                return "java";
            case "xml":
                return "xml";
            case "js":
                return "javascript";
            case "json":
                return "json";
            case "css":
                return "css";
            case "py":
            case "py3":
                return "python";
            case "epub":
                return "epub";
            case "xmind":
                return "xmind";
            case "txt":
            case "log":
            case "conf":
            case "prop":
            case "rc":
            case "sh":
            case "yaml":
            case "properties":
            default:
                return "txt";
        }
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
}
