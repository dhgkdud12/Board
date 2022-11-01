package spring.board.common.response.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorCode {

    // 1000 INVALID
    INVALID_LOGIN(1000, "먼저 사용자 로그인을 해주세요."),
    INVALID_BOARD(1001, "해당 게시물이 존재하지 않습니다."),
    INVALID_COMMENT(1002, "해당 댓글이 존재하지 않습니다."),
    INVALID_FILE(1003, "파일이 존재하지 않습니다."),
    INVALID_USER(1004, "본인이 작성한 게시물이나 댓글이 아닙니다."),



    // 2000 DUPLICATE
    DUPLICATE_ID(2000, "이미 해당 ID가 존재합니다."),
    DUPLICATE_EMAIL(2000, "이미 해당 이메일이 존재합니다."),
    DUPLICATE_LOGIN(2001, "이미 로그인중입니다."),


    // 3000 MISMATCH
    MISMATCH_ID(3000, "사용자 ID가 올바르지 않습니다."),
    MISMATCH_PASSWORD(3001, "비밀번호가 일치하지 않습니다."),


    // 4000 FAIL
    FAIL_SESSION_CRATE(4000, "세션 생성에 실패하였습니다."),
    FAIL_FILE_UP(4001, "파일 업로드에 실패하였습니다."),
    FAIL_FILE_DOWN(4002, "파일 다운로드에 실패하였습니다.");


    private final int code;
    private final String message;
}
