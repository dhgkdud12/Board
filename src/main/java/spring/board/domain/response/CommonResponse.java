package spring.board.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
    private final ResponseStatus status;
    private final String message;
    private final T data;
    // 에러
}
