package spring.board.API.service;

import org.springframework.stereotype.Service;
import spring.board.API.domain.AiResults;
import spring.board.API.domain.CountList;
import spring.board.API.domain.DataList;
import spring.board.API.dto.CarDto;
import spring.board.API.dto.CountDto;
import spring.board.API.dto.RmcOutDto;
import spring.board.dao.MyBatis.CarMapper;
import spring.board.dao.MyBatis.CntMapper;
import spring.board.dao.MyBatis.RmcOutMapper;

import java.util.List;

@Service
public class ApiService {

    private final CarMapper carMapper;
    private final RmcOutMapper rmcOutMapper;
    private final CntMapper cntMapper;

    public ApiService(CarMapper carMapper, RmcOutMapper rmcOutMapper, CntMapper cntMapper) {
        this.carMapper = carMapper;
        this.rmcOutMapper = rmcOutMapper;
        this.cntMapper = cntMapper;
    }

    public void saveResult(AiResults results) {
        System.out.println("carInDate: " + results.getCarInDate());
        System.out.println("carOutDate: " + results.getCarOutDate());

        CarDto car = new CarDto(results.getCarInDate(), results.getCarOutDate());
        carMapper.insertCar(car);

        List<DataList> dataList = results.getDataList();

        for (DataList data : dataList) {
            System.out.println("rmcOutStDate: " + data.getRmcOutStDate());
            System.out.println("rmcOutEnDate: " + data.getRmcOutEnDate());
            System.out.println("splitTime: " + data.getSplitTime());

            RmcOutDto rmcOut = new RmcOutDto(data.getRmcOutStDate(), data.getRmcOutEnDate(), results.getCarInDate(), results.getCarOutDate());
            rmcOutMapper.insertRmcOut(rmcOut);

            List<CountList> countLists = data.getCountList();

            for(CountList countList : countLists) {
                System.out.println("normalCount: " + countList.getNormalCount());
                System.out.println("abnormalCount: " + countList.getAbnormalCount());

                CountDto cnt = new CountDto(null, data.getSplitTime(), countList.getNormalCount(), countList.getAbnormalCount(), results.getCarInDate(), results.getCarOutDate(), data.getRmcOutStDate(), data.getRmcOutEnDate());
                cntMapper.insertCnt(cnt);

            }
        }
    }

    public void findResult() {
        List<CarDto> cars = carMapper.selectAllCar();
        List<RmcOutDto> rmcOuts = rmcOutMapper.selectAllRmcOut();
        List<CountDto> counts = cntMapper.selectAllCnt();

        System.out.println(cars.toString());
        System.out.println(rmcOuts.toString());
        System.out.println(counts.toString());

    }
}
