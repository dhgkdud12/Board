package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.dto.UserRequest;
import spring.board.domain.User;

// 매핑 파일에 정의된 SQL을 호출하는 인터페이스
// MyBatis는 매퍼 인터페이스에 대한 구현 클래스를 자동으로 생성
@Mapper
public interface UserMapper {
    User selectUser(String id);
    Integer selectUserId(String id);
    void insertUser(UserRequest userRequest);
}
