package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.API.dto.CarDto;
import spring.board.API.dto.RmcOutDto;

import java.util.List;

@Mapper
public interface RmcOutMapper {
    Integer insertRmcOut(RmcOutDto rmcOut);
    List<RmcOutDto> selectAllRmcOut();
}
