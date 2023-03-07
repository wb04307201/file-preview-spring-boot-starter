package cn.wubo.file.preview.utils;

import org.springframework.util.StringUtils;

import java.io.IOException;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class FileUtils {

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
     * @param extName
     * @return
     */
    public static String fileType(String extName) {
        switch (extName) {
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
            case "mp4":
            case "ogg":
            case "webm":
                return "video";
            case "mp3":
            case "wav":
            case "m4a":
                return "audio";
            case "zip":
            case "rar":
            case "7z":
            case "gzip":
                return "compressed file";
            case "pdf":
                return "pdf";
            case "html":
                return "html";
            case "txt":
            case "sql":
            case "log":
                return "txt";
            case "md":
                return "markdown";
            default:
                return "unknow";
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
            if (StringUtils.endsWithIgnoreCase(filePath, ".css")) {
                contentType = "text/css";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".js")) {
                contentType = "application/x-javascript";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".rar")) {
                contentType = "application/x-rar-compressed";
            } else if (StringUtils.endsWithIgnoreCase(filePath, ".7z")) {
                contentType = "application/x-7z-compressed";
            }
        }

        // 补充
        if (null == contentType && checkFile) {
            contentType = Files.probeContentType(Paths.get(filePath));
        }

        return contentType;
    }
}
