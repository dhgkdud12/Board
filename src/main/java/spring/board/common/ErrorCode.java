package spring.board.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {
    INVALID_LOGIN(1000, "먼저 사용자 로그인을 해주세요."),
    INVALID_BOARD(1001, "해당 게시물이 존재하지 않습니다."),
    INVALID_COMMENT(1002, "해당 댓글이 존재하지 않습니다."),
    INVALID_FILE(1003, "해당 파일이 존재하지 않습니다."),
    INVALID_USER(1004, "본인이 작성한 게시물이나 댓글이 아닙니다."),



    DUPLICATE_ID(1005, "이미 해당 ID가 존재합니다."),
    MISMATCH_ID(1006, "사용자 ID가 올바르지 않습니다."),
    MISMATCH_PASSWORD(1007, "비밀번호가 일치하지 않습니다."),
    FAIL_SESSION(1008, "세션 생성에 실패하였습니다.");




    private final int code;
    private final String message;
}
