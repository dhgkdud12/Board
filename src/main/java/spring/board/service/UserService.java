package spring.board.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import spring.board.dao.JdbcUserDao;
import spring.board.dto.UserLoginRequest;
import spring.board.dto.UserRequest;
import spring.board.dto.UserSession;
import spring.board.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    private final JdbcUserDao userDao;

    public UserService(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    public String register(UserRequest userRequest) {
        String hashPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
        userRequest.setPassword(hashPassword);
        userDao.insertUser(userRequest);
        return "회원가입 완료";
    }

    public String login(UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (request.getSession().getAttribute("USER") != null) {
            return "이미 로그인중";
        }

        // 아이디 확인
        User loginUser = userDao.selectUser(userLoginRequest.getId());
        if (loginUser == null) {
            System.out.println("사용자 id 없음");
        } else {
            if (BCrypt.checkpw(userLoginRequest.getPassword(), loginUser.getPassword())){
                if (createUserSession(loginUser, request) == null) {
                    System.out.println("유저 세션 생성 실패");
                }
                return "로그인 완료";
            } else {
                System.out.println("비밀번호가 올바르지 않음.");
            }
        }
        return "로그인 실패";
    }

    public UserSession createUserSession(User loginUser, HttpServletRequest request) {
        UserSession userSession = new UserSession(loginUser.getIdx(), loginUser.getName());

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("USER", userSession);

        return userSession;
    }

    // 로그인 확인
    public UserSession getLoginUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (UserSession) session.getAttribute("USER");
    }

    public String logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute("USER");
        System.out.println("로그아웃");
        return "로그아웃 완료";
    }

}
