package cn.wubo.file.preview.exception;

public class StorageRuntimeException extends RuntimeException {
    public StorageRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public StorageRuntimeException(String message) {
        super(message);
    }
}
