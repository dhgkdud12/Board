package spring.board.dao.MyBatis;

import org.apache.ibatis.annotations.Mapper;
import spring.board.API.dto.CarDto;

import java.util.List;

@Mapper
public interface CarMapper {
    void insertCar(CarDto car);
    List<CarDto> selectAllCar();
}
