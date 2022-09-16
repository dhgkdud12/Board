package spring.board.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Service;
import spring.board.dao.JdbcUserDao;
import spring.board.dto.LoginDto;
import spring.board.dto.UserDto;
import spring.board.entity.User;

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

    public User login(LoginDto loginDto) {
        // 아이디 확인
        User loginUser = userDao.selectUser(loginDto.getId());
        if (loginUser == null) {
            System.out.println("사용자 id 없음");
            return null;
        } else {
            if (BCrypt.checkpw(loginDto.getPassword(), loginUser.getPassword())){
                return loginUser;
            } else {
                System.out.println("비밀번호가 올바르지 않음.");
                return null;
            }
        }
    }

}
