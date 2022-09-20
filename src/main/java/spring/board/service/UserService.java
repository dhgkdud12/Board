package spring.board.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import spring.board.dao.JdbcUserDao;
import spring.board.dto.LoginDto;
import spring.board.dto.UserDto;
import spring.board.dto.UserSessionDto;
import spring.board.entity.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Service
public class UserService {
    private final JdbcUserDao userDao;

    public UserService(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    public String register(UserDto userDto) {
        String hashPassword = BCrypt.hashpw(userDto.getPassword(), BCrypt.gensalt());
        userDto.setPassword(hashPassword);
        userDao.insertUser(userDto);
        return "회원가입 완료";
    }

    public String login(LoginDto loginDto, HttpServletRequest request) {

        if (request.getSession().getAttribute("USER") != null) {
            return "이미 로그인중";
        }

        // 아이디 확인
        User loginUser = userDao.selectUser(loginDto.getId());
        if (loginUser == null) {
            System.out.println("사용자 id 없음");
        } else {
            if (BCrypt.checkpw(loginDto.getPassword(), loginUser.getPassword())){
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

    public UserSessionDto createUserSession(User loginUser, HttpServletRequest request) {
        UserSessionDto userSessionDto = new UserSessionDto(loginUser.getIdx(), loginUser.getName());

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("USER", userSessionDto);

        return userSessionDto;
    }

    // 로그인 확인
    public UserSessionDto getLoginUserInfo(HttpServletRequest request) {
        HttpSession session = request.getSession();
        return (UserSessionDto) session.getAttribute("USER");
    }

    public String logout(HttpServletRequest request) {
        HttpSession httpSession = request.getSession();
        httpSession.removeAttribute("USER");
        System.out.println("로그아웃");
        return "로그아웃 완료";
    }

}
