package spring.board.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor

public class CommonResponse<T> {
    private LocalDateTime timestamp = LocalDateTime.now();
    private ResponseStatus status;
    private int code;
    private String message;
    private T data;
    private T error;


    public CommonResponse(ResponseStatus status, int code, String message, T data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public CommonResponse(int errorCode, String errorMessage) {
        this.status = ResponseStatus.FAILURE;
        this.code = errorCode;
        this.message = errorMessage;
    }

    public CommonResponse(int errorCode, String errorMessage, T error) {
        this.status = ResponseStatus.FAILURE;
        this.code = errorCode;
        this.message = errorMessage;
        this.error = error;
    }
}
