package spring.board.dao;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;


/*
SQL 코드와 매핑되는 껍데기 메소드를 인터페이스로 생성
인터페이스에 Mapper라는 어노테이션 지정
 */
@Mapper
@Repository
public interface UserMapper {
    ArrayList<HashMap<String, Object>> findAll();
}
