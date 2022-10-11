package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.UserRequest;
import spring.board.entity.User;

@Mapper
public interface UserMapper {
    User selectUser(String id);
    void insertUser(UserRequest userRequest);
}
