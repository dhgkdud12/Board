package spring.board.util;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import spring.board.dto.user.UserSession;

public class SessionUtils {

    // 세션 속성 값 가져오기
    public static Object getAttribute(String name) throws Exception{
        return (Object) RequestContextHolder.getRequestAttributes().getAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    // 세션 정보 설정
    public static void setAttribute(String name, Object object) throws Exception {
        RequestContextHolder.getRequestAttributes().setAttribute(name, object, RequestAttributes.SCOPE_SESSION);
    }

    // 세션 속성 값 제거
    public static void removeAttribute(String name) throws Exception {
        RequestContextHolder.getRequestAttributes().removeAttribute(name, RequestAttributes.SCOPE_SESSION);
    }

    // 세션 Id 가져오기
    public static String getSessionId() throws Exception {
        return RequestContextHolder.getRequestAttributes().getSessionId();
    }
}
