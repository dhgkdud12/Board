package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.JdbcUserDao;
import spring.board.dto.UserDto;

@Service
public class UserService {
    private final JdbcUserDao userDao;

    public UserService(JdbcUserDao userDao) {
        this.userDao = userDao;
    }

    public String register(UserDto userDto) {
        userDao.insertUser(userDto);
        return "회원가입 완료";
    }


}
