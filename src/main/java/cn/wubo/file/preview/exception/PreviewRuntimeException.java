package cn.wubo.file.preview.exception;

public class PreviewRuntimeException extends RuntimeException {
    public PreviewRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PreviewRuntimeException(String message) {
        super(message);
    }
}
