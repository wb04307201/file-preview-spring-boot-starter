package cn.wubo.file.online.common;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.util.ArrayUtil;
import freemarker.template.TemplateException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;

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
        if (!StringUtils.hasLength(str) || ArrayUtil.isEmpty(testStrs)) {
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
                return "compressed file";
            case "pdf":
                return "pdf";
            case "html":
                return "html";
            default:
                return "unkonow";
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

    public static OutputStream getOutputStream(File file) throws IOException {
        if (!file.exists()) {
            file.getCanonicalFile().getParentFile().mkdirs();
            if (!file.createNewFile())
                throw new RuntimeException("创建文件失败");
        }
        return Files.newOutputStream(file.toPath());
    }

    public static void writeToStream(File file, OutputStream os) throws IOException {
        try (FileInputStream is = new FileInputStream(file)) {
            byte[] bytes = new byte[DEFAULT_BUFFER_SIZE];
            int len;
            while ((len = is.read(bytes)) != -1) {
                os.write(bytes, 0, len);
            }
            os.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void writeFromByte(byte[] b, File file) throws IORuntimeException {
        try (OutputStream os = getOutputStream(file)) {
            os.write(b);
            os.flush();
        } catch (IOException e) {
            throw new IORuntimeException(e);
        }
    }

    public static void errorPage(String message, HttpServletResponse resp) {
        Map<String, Object> data = new HashMap<>();
        data.put("message", message);
        try {
            Page errorPage = new Page("error.ftl", data, resp.getWriter());
            errorPage.write();
        } catch (IOException | TemplateException e) {
            throw new RuntimeException(e);
        }
    }

}
