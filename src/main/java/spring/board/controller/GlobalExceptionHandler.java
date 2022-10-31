package spring.board.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import spring.board.domain.response.CommonResponse;
import spring.board.domain.response.ResponseStatus;

@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 잡음
@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(value = Exception.class) // 발생하는 예외의 종류 정의
    public CommonResponse handlerException(Exception e) {
        return new CommonResponse<>(ResponseStatus.FAILURE, e.getMessage(), null);
    }
}
