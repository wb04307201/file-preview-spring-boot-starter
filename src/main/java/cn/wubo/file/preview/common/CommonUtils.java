package cn.wubo.file.preview.common;

import net.sf.sevenzipjbinding.*;
import net.sf.sevenzipjbinding.impl.RandomAccessFileInStream;
import net.sf.sevenzipjbinding.simple.ISimpleInArchive;
import net.sf.sevenzipjbinding.simple.ISimpleInArchiveItem;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class CommonUtils {

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
     * 默认缓存大小 8192
     */
    public static final int DEFAULT_BUFFER_SIZE = 2 << 12;

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

    public static void setContentType(HttpServletResponse resp, String type, String extName) {
        if ("audio".equals(type) && ("m4a".equals(extName) || "mp3".equals(extName)))
            resp.setContentType("audio/mpeg;charset=UTF-8");
        else if ("audio".equals(type) && "wav".equals(extName))
            resp.setContentType("audio/wav;charset=UTF-8");

        else if ("video".equals(type) && "mp4".equals(extName))
            resp.setContentType("video/mp4;charset=UTF-8");
        else if ("video".equals(type) && "webm".equals(extName))
            resp.setContentType("video/webm;charset=UTF-8");
        else if ("video".equals(type) && "ogg".equals(extName))
            resp.setContentType("video/ogg;charset=UTF-8");
    }

    public static OutputStream getOutputStream(String targetPath) {
        Path path = Paths.get(targetPath);
        try {
            if (Files.notExists(path)) {
                Files.createDirectories(path.getParent());
                Files.createFile(path);
            }
            return Files.newOutputStream(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToStream(String sourcePath, OutputStream os) {
        Path path = Paths.get(sourcePath);
        try {
            Files.copy(path, os);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeToStream(File file, OutputStream os) {
        try (FileInputStream is = new FileInputStream(file)) {
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void writeFromByte(byte[] b, String orgPath) {
        try (OutputStream os = getOutputStream(orgPath)) {
            os.write(b);
            os.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void errorPage(String message, HttpServletResponse resp) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        Page errorPage = new Page("error.ftl", data, resp);
        errorPage.write();
    }

    public static List<String> compress(String filePath, String direct) {
        List<String> exacts = new ArrayList<>();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(filePath, "r");
             IInArchive inArchive = SevenZip.openInArchive(null, // autodetect archive type
                     new RandomAccessFileInStream(randomAccessFile));) {
            ISimpleInArchive simpleInArchive = inArchive.getSimpleInterface();
            for (ISimpleInArchiveItem item : simpleInArchive.getArchiveItems()) {
                if (!item.isFolder()) {
                    ExtractOperationResult result = item.extractSlow(data -> {
                        String exactPath = direct + File.separator + item.getPath();
                        exacts.add(exactPath);
                        writeFromByte(data, exactPath);
                        return data.length; // Return amount of consumed data
                    });

                    if (result != ExtractOperationResult.OK) {
                        throw new RuntimeException("Error extracting item: " + result.toString());
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return exacts;
    }

}
