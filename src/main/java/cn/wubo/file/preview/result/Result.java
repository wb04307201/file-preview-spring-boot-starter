package cn.wubo.file.preview.result;

import lombok.Data;

@Data
public class Result<T> {
    private Long code;
    private String message;
    private T data;

    public Result() {
    }

    public Result(T data) {
        this.code = 200L;
        this.message = "操作成功!";
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static <T> Result<T> fail(Exception e) {
        Result<T> result = new Result<>();
        result.setCode(500L);
        result.setMessage(e.getMessage());
        return result;
    }

}
