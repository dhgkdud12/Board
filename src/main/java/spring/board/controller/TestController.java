package spring.board.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import spring.board.dto.ResponseDTO;
import spring.board.service.TestService;

@RestController
@RequestMapping(value = "/api/v1/app/")
public class TestController {
    private final TestService testService;

    public TestController(TestService testService) {
        this.testService = testService;
    }

    @RequestMapping(value = "findAll", method = RequestMethod.POST)
    public ResponseEntity<?> findAll() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("S0001");
        responseDTO.setRes(testService.findAll());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
