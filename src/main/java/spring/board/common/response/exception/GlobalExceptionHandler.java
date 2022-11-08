package spring.board.common.response.exception;

import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

import spring.board.common.response.CommonResponse;

import java.util.HashMap;
import java.util.Map;


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

    @ExceptionHandler(TicketingException.class) // 해당 예외 발생 시, 수행
    protected CommonResponse ticketingException(TicketingException ex) {
        ErrorCode errorCode = ex.getErrorCode();
        CommonResponse response = new CommonResponse(errorCode.getCode(), errorCode.getMessage());
        return response;
    }

    // 유효성 검사 예외처리
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public CommonResponse errorValid(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        Map<String, String> errors = new HashMap<>();
        for (ObjectError result : bindingResult.getAllErrors()) {
            errors.put(
                    ((FieldError)result).getField(), // id, password 등 객체
                    result.getDefaultMessage());
        }
        ErrorCode error = ErrorCode.FAIL_VALIDATION;
        return new CommonResponse(error.getCode(), error.getMessage(), errors);
    }

    @ExceptionHandler(BindException.class)
    public CommonResponse errorArgumentValid(BindException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        Map<String, String> errors = new HashMap<>();
        for (ObjectError result : bindingResult.getAllErrors()) {
            errors.put(
                    ((FieldError)result).getField(),
                    result.getDefaultMessage());
        }
        ErrorCode error = ErrorCode.FAIL_VALIDATION;
        return new CommonResponse(error.getCode(), error.getMessage(), errors);
    }
}
