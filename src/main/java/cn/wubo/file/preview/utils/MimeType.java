package cn.wubo.file.preview.utils;

import javax.activation.MimetypesFileTypeMap;
import java.io.IOException;
import java.io.InputStream;

public class MimeType {
    private static MimetypesFileTypeMap map = createMap();

    public static MimetypesFileTypeMap createMap() {
        try (InputStream is = MimeType.class.getClassLoader().getResourceAsStream("mimetype/mimetype.txt")) {
            return new MimetypesFileTypeMap(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String filename) {
        return get(filename, null);
    }

    public static String get(String filename, String charset) {
        String mimeType = map.getContentType(filename.toLowerCase());
        if (charset != null && (mimeType.startsWith("text/") || mimeType.contains("javascript"))) {
            mimeType += ";charset=" + charset.toLowerCase();
        }
        return mimeType;
    }
}
