package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.API.domain.CountList;
import spring.board.API.dto.CarDto;
import spring.board.API.dto.CountDto;

import java.util.List;

@Mapper
public interface CntMapper {
    Integer insertCnt(CountDto count);
    List<CountDto> selectAllCnt();
}
