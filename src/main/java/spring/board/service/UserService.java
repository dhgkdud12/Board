package spring.board.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import spring.board.dao.MyBatis.UserMapper;
import spring.board.dto.user.UserLoginRequest;
import spring.board.dto.user.UserRequest;
import spring.board.dto.user.UserSession;
import spring.board.domain.User;
import spring.board.util.SessionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
//    private final JdbcUserDao userDao;

//    public UserService(JdbcUserDao userDao) {
//        this.userDao = userDao;
//    }
    private final UserMapper userMapper;

    public UserService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }


    public String register(UserRequest userRequest) {
        if (userMapper.selectUserId(userRequest.getId()) == 1) {
            return "이미 해당 ID가 존재";
        }
        String hashPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
        userRequest.setPassword(hashPassword);
//        userDao.insertUser(userRequest);
        userMapper.insertUser(userRequest);
        return "회원가입 완료";
    }

    public String login(UserLoginRequest userLoginRequest) throws Exception {

        if (SessionUtils.getAttribute("USER") != null) {
            return "이미 로그인중";
        }

        // 아이디 확인
        User loginUser = userMapper.selectUser(userLoginRequest.getId());
//                userDao.selectUser(userLoginRequest.getId());
        if (loginUser == null) {
            System.out.println("사용자 id 없음");
        } else {
            if (BCrypt.checkpw(userLoginRequest.getPassword(), loginUser.getPassword())){
//                if (createUserSession(loginUser, request) == null) {
//                    System.out.println("유저 세션 생성 실패");
//                }

                UserSession userSession = new UserSession(loginUser.getIdx(), loginUser.getName());
                SessionUtils.setAttribute("USER", userSession);

                return "로그인 완료";
            } else {
                System.out.println("비밀번호가 올바르지 않음.");
            }
        }
        return "로그인 실패";
    }

    // 세션 생성
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

    public String logout() throws Exception {
//        HttpSession httpSession = request.getSession();
//        httpSession.removeAttribute("USER"); // 특정 대상값을 삭제
//        httpSession.invalidate(); // 세션정보 초기화 - 세션의 모든 속성 제거하는 역할
        SessionUtils.removeAttribute("USER");
        System.out.println("로그아웃");
        return "로그아웃 완료";
    }

}
