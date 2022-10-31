package spring.board.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import spring.board.common.ErrorCode;
import spring.board.common.TicketingException;
import spring.board.dao.MyBatis.UserMapper;
import spring.board.dto.user.UserLoginRequest;
import spring.board.dto.user.UserRequest;
import spring.board.dto.user.UserSession;
import spring.board.domain.User;

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


    public String register(UserRequest userRequest)throws  Exception {

        if (userMapper.selectUserId(userRequest.getId()) == 1) {
            throw new TicketingException(ErrorCode.DUPLICATE_ID);
        }
        String hashPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
        userRequest.setPassword(hashPassword);
//        userDao.insertUser(userRequest);
        userMapper.insertUser(userRequest);
        return "회원가입 완료";
    }

    public String login(UserLoginRequest userLoginRequest, HttpServletRequest request) {

        if (request.getSession().getAttribute("USER") != null) {
            return "이미 로그인중";
        }

        // 아이디 확인
        User loginUser = userMapper.selectUser(userLoginRequest.getId());
//                userDao.selectUser(userLoginRequest.getId());
        if (loginUser == null) {
            throw new TicketingException(ErrorCode.MISMATCH_ID);
        } else {
            if (BCrypt.checkpw(userLoginRequest.getPassword(), loginUser.getPassword())){
                if (createUserSession(loginUser, request) == null) {
                    throw new TicketingException(ErrorCode.FAIL_SESSION);
                }
                return "로그인 완료";
            } else {
                throw new TicketingException(ErrorCode.MISMATCH_PASSWORD);
            }
        }
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
