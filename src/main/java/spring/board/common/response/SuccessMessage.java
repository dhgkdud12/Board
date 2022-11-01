package spring.board.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum SuccessMessage {


    SUCCESS_REGISTER("회원가입 완료"),
    SUCCESS_LOGIN("로그인 완료"),
    SUCCESS_LOGOUT("로그아웃 완료"),

    SUCCESS_CREATE("작성 완료"),
    SUCCESS_READ("조회 완료"),
    SUCCESS_UPDATE("수정 완료"),
    SUCCESS_DELETE("삭제 완료"),

    SUCCESS_FILE_UP("파일 업로드 완료"),
    SUCCESS_FILE_DOWN("파일 다운로드 완료");


    private final String message;
}
