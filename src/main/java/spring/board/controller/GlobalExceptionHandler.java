package spring.board.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import spring.board.common.ErrorCode;
import spring.board.common.TicketingException;
import spring.board.domain.response.CommonResponse;


@ControllerAdvice // 모든 컨트롤러에서 발생하는 예외 잡음
@RestController
// Handler
public class GlobalExceptionHandler {
//    @ExceptionHandler(value = Exception.class) // 발생하는 예외의 종류 정의
//    public CommonResponse handlerException(Exception e) {
//        return new CommonResponse<>(ResponseStatus.FAILURE, e.getMessage(), null);
//    }

//    @ExceptionHandler(NullPointerException.class)
//    public CommonResponse nullPointerException() {
//        CommonResponse response = new CommonResponse()
//    }

    // nullexception, exception
    @ExceptionHandler(TicketingException.class) // 해당 예외 발생 시, 수행
    protected CommonResponse ticketingException(TicketingException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        CommonResponse response = new CommonResponse(errorCode.getCode(), errorCode.getMessage());
        return response;
    }


}
