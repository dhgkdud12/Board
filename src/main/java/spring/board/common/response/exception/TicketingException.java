package spring.board.common.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor

/*
공통 Custom Exception
ErrorCode를 품은 Exception

Exception을 던질 때 TicketingException을 공통으로 사용하고
상황에 맞게 ErrorCode를 정의해주는 형식
 */

public class TicketingException extends RuntimeException{
    private final ErrorCode errorCode;
}
