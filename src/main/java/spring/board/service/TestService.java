package spring.board.service;

import org.springframework.stereotype.Service;
import spring.board.dao.UserMapper;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TestService {
    private final UserMapper userMapper;

    public TestService(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public ArrayList<HashMap<String, Object>> findAll() {
        return userMapper.findAll();
    }
}
