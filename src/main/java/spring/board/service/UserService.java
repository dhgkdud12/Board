package spring.board.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import spring.board.common.response.exception.ErrorCode;
import spring.board.common.response.exception.TicketingException;
import spring.board.common.response.SuccessMessage;
import spring.board.dao.MyBatis.UserMapper;
import spring.board.dto.user.UserLoginRequest;
import spring.board.dto.user.UserRequest;
import spring.board.dto.user.UserSession;
import spring.board.domain.User;
import spring.board.util.SessionUtils;

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
            throw new TicketingException(ErrorCode.DUPLICATE_ID);
        } else {
            if (userMapper.selectUserEmail(userRequest.getEmail()) == 1) {
                throw new TicketingException(ErrorCode.DUPLICATE_EMAIL);
            }
        }

        String hashPassword = BCrypt.hashpw(userRequest.getPassword(), BCrypt.gensalt());
        userRequest.setPassword(hashPassword);

//        userDao.insertUser(userRequest);
        userMapper.insertUser(userRequest);
        return SuccessMessage.SUCCESS_REGISTER.getMessage();
    }

    public String login(UserLoginRequest userLoginRequest) {

        UserSession userSession = getLoginUserInfo();
        if (userSession != null) {
            throw new TicketingException(ErrorCode.DUPLICATE_LOGIN);
        }

        // 아이디 확인
        User loginUser = userMapper.selectUser(userLoginRequest.getId());
//                userDao.selectUser(userLoginRequest.getId());
        if (loginUser == null) {
            throw new TicketingException(ErrorCode.MISMATCH_ID);
        } else {
            if (BCrypt.checkpw(userLoginRequest.getPassword(), loginUser.getPassword())){
                if (createUserSession(loginUser) == null) {
                    throw new TicketingException(ErrorCode.FAIL_SESSION_CRATE);
                }
                return SuccessMessage.SUCCESS_LOGIN.getMessage();
            } else {
                throw new TicketingException(ErrorCode.MISMATCH_PASSWORD);
            }
        }
    }

//    public UserSession createUserSession(User loginUser, HttpServletRequest request) {
    public UserSession createUserSession(User loginUser) {
        UserSession userSession = new UserSession(loginUser.getIdx(), loginUser.getName());

//        HttpSession httpSession = request.getSession();
//        httpSession.setAttribute("USER", userSession);

        SessionUtils.setAttribute("USER", userSession);

        return userSession;
    }

    // 로그인한 유저 정보 가져오기
    public UserSession getLoginUserInfo() {
//        HttpSession session = request.getSession();
//        return (UserSession) session.getAttribute("USER");
        return (UserSession) SessionUtils.getAttribute("USER");
    }

    public String logout() {
//        HttpSession httpSession = request.getSession();
//        httpSession.removeAttribute("USER");
        SessionUtils.removeAttribute("USER");
        System.out.println("로그아웃");
        return SuccessMessage.SUCCESS_LOGOUT.getMessage();
    }

}
